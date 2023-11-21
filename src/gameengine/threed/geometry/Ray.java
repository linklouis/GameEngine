package gameengine.threed.geometry;

import gameengine.threed.graphics.raytraceing.LightRay;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;
import org.jocl.*;
import org.jocl.struct.Buffers;
import org.jocl.struct.CLTypes.cl_float2;
import org.jocl.struct.CLTypes.cl_float4;
import org.jocl.struct.SizeofStruct;
import org.jocl.struct.Struct;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.jocl.CL.*;

public class Ray extends VectorLine3D {
    // CL state
    private static cl_context context;
    private static cl_command_queue commandQueue;
    private static cl_kernel kernel;
    private static cl_program firstCollisionProgram;

    private static String firstCollisionCode = "firstCollisionKernel.txt";

    public Ray(final Vector3D startPosition, final Vector3D direction) {
        super(startPosition, direction);
    }

    public Ray(Ray parent) {
        super(parent.getPosition(), parent.getDirection());
    }

    /**
     * Default initialization of the context, command queue, kernel
     * and program
     */
    public static void defaultInitialization() {
        // Obtain the platform IDs and initialize the context properties
        cl_platform_id[] platforms = new cl_platform_id[1];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platforms[0]);

        // Create an OpenCL context on a GPU device
        context = clCreateContextFromType(
                contextProperties, CL_DEVICE_TYPE_GPU, null, null, null);
        if (context == null) {
            // If no context for a GPU device could be created,
            // try to create one for a CPU device.
            context = clCreateContextFromType(
                    contextProperties, CL_DEVICE_TYPE_ALL, null, null, null);

            if (context == null) {
                System.out.println("Unable to create a context");
                return;
            }
        }

        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Get the list of GPU devices associated with the context
        long[] numBytes = new long[1];
        clGetContextInfo(context, CL_CONTEXT_DEVICES, 0, null, numBytes);

        // Obtain the cl_device_id for the first device
        int numDevices = (int) numBytes[0] / Sizeof.cl_device_id;
        cl_device_id[] devices = new cl_device_id[numDevices];
        clGetContextInfo(context, CL_CONTEXT_DEVICES, numBytes[0],
                Pointer.to(devices), null);

        // Create a command-queue
        commandQueue =
                clCreateCommandQueue(context, devices[0], 0, null);

        // Create the program from the source code
        firstCollisionProgram = clCreateProgramWithSource(context,
                1, new String[]{readKernelSource(firstCollisionCode)}, null, null);

        // Build the program
        clBuildProgram(firstCollisionProgram, 0, null, null, null, null);

        // Create the kernel
        kernel = clCreateKernel(firstCollisionProgram, "getCollisionDistances", null);
    }

    private static String readKernelSource(String filePath) {
        StringBuilder source = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                source.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return source.toString();
    }

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
            return "RayStruct[direction=" + direction + "," + "position=" + position + "]";
        }
    }

    public RayStruct toStruct() {
        return new RayStruct(position.toStruct(), getDirection().toStruct());
    }

    public static void sendObjects(RayTraceable.RayTraceableStruct[] objectsInField) {
        ByteBuffer objectsBuffer = Buffers.allocateBuffer(objectsInField);
        cl_mem objectMem = clCreateBuffer(context,
                CL_MEM_READ_WRITE/*CL_MEM_READ_ONLY*/ | CL_MEM_USE_HOST_PTR,
                RayTraceable.STRUCT_SIZE * objectsInField.length, Pointer.to(objectsBuffer), null);
        clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(objectMem));
    }

    public static void cleanUp() {
        clReleaseMemObject(distanceMem);
        clReleaseMemObject(rayMem);
    }

    public float[] executeKernel(ByteBuffer rayBuffer, int n) {
        float[] distances = new float[n];
        System.out.println("a");

//        rayBuffer.clear();
//        Buffers.writeToBuffer(rayBuffer, toStruct());
//
//        rayMem = clCreateBuffer(context,
//                CL_MEM_READ_ONLY | CL_MEM_USE_HOST_PTR,
//                SizeofStruct.sizeof(RayStruct.class), Pointer.to(rayBuffer), null);

        // Set the arguments for the kernel
//        clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(distanceMem));
        clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(rayMem));

        // Execute the kernel
        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
                new long[]{n}, null, 0, null, null);


        // Read back the data from to memory object to the particle buffer
        clEnqueueReadBuffer(commandQueue, distanceMem, true, 0,
                (long) Sizeof.cl_float * distances.length, Pointer.to(distances),
                0, null, null);

        // Clean up
//        clReleaseMemObject(distanceMem);
//        clReleaseMemObject(rayMem);

        return distances;
    }

    private static cl_mem distanceMem;
    private static cl_mem rayMem;
    // Only called on the first ray of each pixel to set up the memory.
    public float[] executeKernel(int n) {
        RayStruct ray = toStruct();
        ByteBuffer rayBuffer = Buffers.allocateBuffer(ray);
        Buffers.writeToBuffer(rayBuffer, ray);
//        System.out.println("Correct Values: " + ray);

        float[] distances = new float[n];
        distanceMem = clCreateBuffer(context,
                CL_MEM_READ_WRITE | CL_MEM_USE_HOST_PTR,
                (long) Sizeof.cl_float * n, Pointer.to(distances), null);
        rayMem = clCreateBuffer(context,
                /*CL_MEM_READ_WRITE*/CL_MEM_READ_ONLY | CL_MEM_USE_HOST_PTR,
                SizeofStruct.sizeof(RayStruct.class), Pointer.to(rayBuffer), null);

        // Set the arguments for the kernel
        clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(distanceMem));
        clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(rayMem));

        // Execute the kernel
        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
                new long[]{n}, null, 0, null, null);


        // Read back the data from to memory object to the particle buffer
        clEnqueueReadBuffer(commandQueue, distanceMem, true, 0,
                (long) Sizeof.cl_float * distances.length, Pointer.to(distances),
                0, null, null);

//        System.exit(0);
        return distances;
    }

    public static RayTraceable.RayTraceableStruct[] toStructs(RayTraceable[] objects) {
        RayTraceable.RayTraceableStruct[] structs = new RayTraceable.RayTraceableStruct[objects.length];

        for (int gid = 0; gid < objects.length; gid++) {
            structs[gid] = objects[gid].toStruct();
        }

        return structs;
    }

    public RayTraceable firstCollision(RayTraceable.RayTraceableStruct[] objectsInField, RayTraceable[] objs,
                                       ByteBuffer rayBuffer) {
        float[] distances = executeKernel(rayBuffer, objectsInField.length);

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

    public RayTraceable firstCollision(RayTraceable.RayTraceableStruct[] objectsInField, RayTraceable[] objs) {
        float[] distances = executeKernel(objectsInField.length);

//        System.out.println(Arrays.toString(distances));
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
