package gameengine.threed.geometry;

import gameengine.threed.graphics.raytraceing.LightRay;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.utilities.JOCLHelper;
import gameengine.vectormath.Vector3D;
import org.jocl.*;
import org.jocl.struct.Buffers;
import org.jocl.struct.CLTypes.cl_float4;
import org.jocl.struct.SizeofStruct;
import org.jocl.struct.Struct;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.jocl.CL.*;

public class Ray extends VectorLine3D {
    // CL state
    private static final cl_context context;
    private static final cl_command_queue commandQueue;
    private static final cl_kernel kernel;

    private static String firstCollisionCode = "firstCollisionKernel";

    private static ByteBuffer rayBuffer = Buffers.allocateBuffer(new Ray(new Vector3D(), new Vector3D()).toStruct());
    private static cl_mem objectMem;
    private static cl_mem distanceMem;
    private static cl_mem rayMem;
    private static float[] distances;

    public Ray(final Vector3D startPosition, final Vector3D direction) {
        super(startPosition, direction);
    }

    public Ray(Ray parent) {
        super(parent.getPosition(), parent.getDirection());
    }

    static {
        CL.setExceptionsEnabled(true);
        context = JOCLHelper.defaultContext();
        commandQueue = JOCLHelper.defaultCommandQueue(context);
        kernel = JOCLHelper.defaultKernel(context,  "getCollisionDistances", "VectorUtilities", "Ray", "Object", firstCollisionCode);
    }


    /*
     * Kernel Definition:
     */

    public static class RayStruct extends Struct {
        public cl_float4 direction;
        public cl_float4 position;

        public RayStruct() {
            this.position = new cl_float4();
            this.direction = new cl_float4();
        }

        public RayStruct(cl_float4 position, cl_float4 direction) {
            this.position = position;
            this.direction = direction;
        }

        public String toString() {
            return "RayStruct[position=" + position + ", " + "direction=" + direction + "]";
        }
    }

    public RayStruct toStruct() {
        return new RayStruct(position.toStruct(), getDirection().toStruct());
    }


    /*
     * Kernel Setup:
     */

    public static void setupForObjects(RayTraceable.RayTraceableStruct[] objectsInField) {
        sendObjects(objectsInField);

        distances = new float[objectsInField.length];
        distanceMem = clCreateBuffer(context,
                CL_MEM_READ_WRITE | CL_MEM_USE_HOST_PTR,
                (long) Sizeof.cl_float * objectsInField.length, Pointer.to(distances), null);
        rayMem = clCreateBuffer(context,
                CL_MEM_READ_ONLY | CL_MEM_USE_HOST_PTR,
                SizeofStruct.sizeof(RayStruct.class), Pointer.to(rayBuffer), null);

        clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(distanceMem));
    }

    private static void sendObjects(RayTraceable.RayTraceableStruct[] objectsInField) {
        ByteBuffer objectsBuffer = Buffers.allocateBuffer(objectsInField);
        Buffers.writeToBuffer(objectsBuffer, objectsInField);

        objectMem = clCreateBuffer(context,
                CL_MEM_READ_ONLY | CL_MEM_USE_HOST_PTR,
                RayTraceable.STRUCT_SIZE * objectsInField.length, Pointer.to(objectsBuffer), null);
        clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(objectMem));
    }

    public static RayTraceable.RayTraceableStruct[] toStructs(RayTraceable[] objects) {
        return Arrays.stream(objects)
                .map(RayTraceable::toStruct)
                .toArray(RayTraceable.RayTraceableStruct[]::new);
    }

    public static void cleanUp() {
        clReleaseMemObject(distanceMem);
        clReleaseMemObject(rayMem);
        clReleaseMemObject(objectMem);
    }


    /*
     * Kernel Execution:
     */

    public void claimBuffer() {
        RayStruct ray = toStruct();
        rayBuffer.clear();
        Buffers.writeToBuffer(rayBuffer, ray);
    }

    public void executeKernel() {
        rayMem = clCreateBuffer(context,
                CL_MEM_READ_ONLY | CL_MEM_USE_HOST_PTR,
                SizeofStruct.sizeof(RayStruct.class), Pointer.to(rayBuffer), null);

        // Set the arguments for the kernel
        clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(rayMem));

        // Execute the kernel
        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
                new long[]{distances.length}, null, 0, null, null);

        clEnqueueReadBuffer(commandQueue, distanceMem, true, 0,
                (long) Sizeof.cl_float * distances.length, Pointer.to(distances),
                0, null, null);
    }

    public RayTraceable firstCollision(RayTraceable[] objs) {
        executeKernel();

        int i = 0;
        while (distances[i] < 0 || Float.isNaN(distances[i])) {
            i++;
            if (i == distances.length) {
                return null;
            }
        }

        int closest = i;
        for (; i < distances.length; i++) {
            if (distances[i] >= 0 && distances[i] < distances[closest]) {
                closest = i;
            }
        }
        position = position.addMultiplied(getDirection(), distances[closest] - 0.01);

        return objs[closest];
    }


    /*
     * Basic Ray Functionality:
     */

    /**
     * Updates the {@code Ray}'s direction based on its reflection of off the
     * given {@link RayTraceable}.
     *
     * @param collider The {@link RayTraceable} to reflect off of.
     */
    public void reflect(RayTraceable collider) {
        setDirection(collider.reflection((LightRay) this).direction());
    }

    /**
     * Returns a new {@code Ray} representing the current {@code Ray} after
     * reflecting off of {@code collider}. Assumes the current {@code Ray} is
     * colliding with {@code collider}.
     *
     * @param collider The {@link RayTraceable} to reflect off of.
     * @return A new {@code Ray} representing the current {@code Ray} after
     * reflecting off of {@code collider}.
     */
    public Ray getReflected(RayTraceable collider) {
        return new Ray(position, collider.reflection((LightRay) this).direction());
    }
}
