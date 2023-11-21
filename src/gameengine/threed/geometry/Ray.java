package gameengine.threed.geometry;

import gameengine.threed.graphics.raytraceing.LightRay;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.vectormath.Vector3D;
import org.jocl.*;
import org.jocl.struct.Buffers;
import org.jocl.struct.CLTypes.cl_float4;
import org.jocl.struct.SizeofStruct;
import org.jocl.struct.Struct;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.jocl.CL.*;

public class Ray extends VectorLine3D {
    // CL state
    private static cl_context context;
    private static cl_command_queue commandQueue;
    private static cl_kernel kernel;
    private static cl_program firstCollisionProgram;

    private static String firstCollisionCode = "firstCollisionKernel";

    public Ray(final Vector3D startPosition, final Vector3D direction) {
        super(startPosition, direction);
    }

    public Ray(Ray parent) {
        super(parent.getPosition(), parent.getDirection());
    }

    static {
        defaultInitialization();
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
        commandQueue = clCreateCommandQueue(context, devices[0], 0, null);

        // Create the program from the source code
        firstCollisionProgram = clCreateProgramWithSource(context,
                1, new String[]{readKernelSources("VectorUtilities", "Ray", "Object", firstCollisionCode)}, null, null);

        // Build the program
        clBuildProgram(firstCollisionProgram, 0, null, null, null, null);

        // Create the kernel
        kernel = clCreateKernel(firstCollisionProgram, "getCollisionDistances", null);
    }

    private static String readKernelSource(String filePath) {
        StringBuilder source = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("oclcode/" + filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                source.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return source.toString();
    }

    /**
     * Reads OpenCL kernel code from several files into a single program
     * in the order they appear in the input.
     * <p></p>
     * By default, all file paths not containing the indented OCL code
     * directory ("oclcode") will have the directory appended to the front,
     * and all file paths not containing a '.' indicating a file type, will
     * have ".txt" appended to the end.
     * <p>
     * If the omission of the oclcode directory or file type is intentional,
     * include a '*' at the beginning or the end of the path, respectively.
     * <p></p>
     *
     * @param filePaths The paths to the OpenCL kernel files.
     * @return A single {@code String} containing the contents of all the files
     * in {@code filePaths}.
     */
    private static String readKernelSources(String... filePaths) {
        if (filePaths.length == 0) {
            return "";
        }

        filePaths = Arrays.stream(filePaths)
                .filter(path -> path != null && !path.isEmpty())
                .map(path -> {
                    if (!path.contains("oclcode/")) {
                        if (path.indexOf("*") == 0) {
                            path = path.substring(1);
                        } else {
                            path = "oclcode/" + path;
                        }
                    }

                    if (!path.contains(".")) {
                        if (path.indexOf("*") == path.length() - 1) {
                            path = path.substring(0, path.length() - 1);
                        } else {
                            path += ".txt";
                        }
                    }

                    return path;
                }).toArray(String[]::new);

        StringBuilder source = new StringBuilder();
        int i = 0;
        int lineNumber;
        for (String filePath : filePaths) {
            lineNumber = 0;
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    source.append(line).append("\n");
                    lineNumber++;
                }
            } catch (IOException e) {
                System.err.println("An IOException occurred while reading OpenCL kernels from files."
                        + "\nException: " + e.getClass().toString().substring(6)
                        + "\nRelevant file(index=" + i + ", line=" + lineNumber + "): " + filePath
                        + (source.isEmpty() ? "" : ("\n\nProcessed code:\n" + source))
                        + "\nAll source files: " + Arrays.toString(filePaths)
                        + (e.getCause() == null ? "" : ("\nCause: " + e.getCause()))
                        + "\n\nOccurred at: " + extractLocation(e)
                );
                System.exit(0);
            }
            i++;
        }
        return source.toString();
    }

    private static String extractLocation(IOException e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder locationInfo = new StringBuilder();

        for (StackTraceElement element : stackTrace) {
            if (!element.toString().contains("java.io")
                && !element.toString().contains("QuantumToolkit")
                    && !element.toString().contains("com.sun.")) {
                locationInfo
                        .append("\n")
                        .append(element.getClassName())
                        .append(".")
                        .append(element.getMethodName())
                        .append("(")
                        .append(element.getFileName())
                        .append(":")
                        .append(element.getLineNumber())
                        .append(")");
            }
        }
        return locationInfo.isEmpty() ? "unknown location" : locationInfo.toString();
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
            return "RayStruct[position=" + position + ", " + "direction=" + direction + "]";
        }
    }

    public RayStruct toStruct() {
        return new RayStruct(position.toStruct(), getDirection().toStruct());
    }

    public static void sendObjects(RayTraceable.RayTraceableStruct[] objectsInField) {
        Arrays.stream(objectsInField).forEach(object -> System.out.println(object.normal));
        System.out.println();
        Arrays.stream(objectsInField)
                .filter(object -> object.type == 2)
                .forEach(obj -> System.out.println("normal: " + obj.normal + ", vertex1: " + obj.vertexOrCenter + ", side1: " + obj.side1 + ", side2: " + obj.side2 + ", max: " + obj.max + ", min: " + obj.min));
        ByteBuffer objectsBuffer = Buffers.allocateBuffer(objectsInField);
        Buffers.writeToBuffer(objectsBuffer, objectsInField);
        cl_mem objectMem = clCreateBuffer(context,
                /*CL_MEM_READ_WRITE*/CL_MEM_READ_ONLY | CL_MEM_USE_HOST_PTR,
                RayTraceable.STRUCT_SIZE * objectsInField.length, Pointer.to(objectsBuffer), null);
        clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(objectMem));
    }

    public static void cleanUp() {
        clReleaseMemObject(distanceMem);
        clReleaseMemObject(rayMem);
    }

    public float[] executeKernel(ByteBuffer rayBuffer, int n) {
//        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
//        System.out.println(toStruct());
        float[] distances = new float[n];
//        System.out.println("a");

//        rayBuffer.clear();
//        Buffers.writeToBuffer(rayBuffer, toStruct());

        rayMem = clCreateBuffer(context,
                CL_MEM_READ_ONLY | CL_MEM_USE_HOST_PTR,
                SizeofStruct.sizeof(RayStruct.class), Pointer.to(rayBuffer), null);

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
