package gameengine.utilities;

import org.jocl.*;

import static gameengine.threed.graphics.raytraceing.KernelReader.readKernelSources;
import static org.jocl.CL.*;
import static org.jocl.CL.clCreateKernel;

public class JOCLHelper {
    public static cl_context defaultContext() {
        // Obtain the platform IDs and initialize the context properties
        cl_platform_id[] platforms = new cl_platform_id[1];
        clGetPlatformIDs(platforms.length, platforms, null);

        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platforms[0]);

        // Create an OpenCL context on a GPU device
        cl_context context = clCreateContextFromType(
                contextProperties, CL_DEVICE_TYPE_GPU, null, null, null);
        if (context == null) {
            // If no context for a GPU device could be created,
            // try to create one for a CPU device.
            context = clCreateContextFromType(
                    contextProperties, CL_DEVICE_TYPE_ALL, null, null, null);

            if (context == null) {
                System.out.println("Unable to create a context");
                return null;
            }
        }
        return context;
    }

    public static cl_command_queue defaultCommandQueue(cl_context context) {
        // Get the list of GPU devices associated with the context
        long[] numBytes = new long[1];
        clGetContextInfo(context, CL_CONTEXT_DEVICES, 0, null, numBytes);

        // Obtain the cl_device_id for the first device
        int numDevices = (int) numBytes[0] / Sizeof.cl_device_id;
        cl_device_id[] devices = new cl_device_id[numDevices];
        clGetContextInfo(context, CL_CONTEXT_DEVICES, numBytes[0],
                Pointer.to(devices), null);

        return clCreateCommandQueue(context, devices[0], 0, null);
    }

    public static cl_program defaultProgram(cl_context context, String... sources) {
        cl_program program = clCreateProgramWithSource(context,
                1, new String[]{readKernelSources(sources)}, null, null);

        // Build the program
        clBuildProgram(program, 0, null, null, null, null);

        return program;
    }

    public static cl_kernel defaultKernel(cl_program program, String name) {
        return clCreateKernel(program, name, null);
    }

    public static cl_kernel defaultKernel(cl_context context, String name, String... sources) {
        return clCreateKernel(defaultProgram(context, sources), name, null);
    }

    public static void defaultInitialization() {
        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);
    }

    /**
     * Default initialization of the context, command queue, kernel
     * and program
     */
    public static void defaultInitialization(String... sources) {
        cl_context context = defaultContext();

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
        cl_command_queue commandQueue = clCreateCommandQueue(context, devices[0], 0, null);

        // Create the program from the source code
        cl_program firstCollisionProgram = clCreateProgramWithSource(context,
                1, new String[]{readKernelSources(sources)}, null, null);

        // Build the program
        clBuildProgram(firstCollisionProgram, 0, null, null, null, null);

        // Create the kernel
        cl_kernel kernel = clCreateKernel(firstCollisionProgram, "getCollisionDistances", null);
    }
}
