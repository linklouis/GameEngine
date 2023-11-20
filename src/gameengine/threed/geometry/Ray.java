package gameengine.threed.geometry;

import gameengine.threed.graphics.raytraceing.LightRay;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;
import org.jocl.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import static org.jocl.CL.*;
import static org.jocl.CL.clCreateKernel;
import org.jocl.struct.CLTypes.cl_float4;
import org.jocl.struct.CLTypes.cl_float2;

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
    private static void defaultInitialization()
    {
        // Obtain the platform IDs and initialize the context properties
        cl_platform_id platforms[] = new cl_platform_id[1];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platforms[0]);

        // Create an OpenCL context on a GPU device
        context = clCreateContextFromType(
                contextProperties, CL_DEVICE_TYPE_GPU, null, null, null);
        if (context == null)
        {
            // If no context for a GPU device could be created,
            // try to create one for a CPU device.
            context = clCreateContextFromType(
                    contextProperties, CL_DEVICE_TYPE_CPU, null, null, null);

            if (context == null)
            {
                System.out.println("Unable to create a context");
                return;
            }
        }

        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Get the list of GPU devices associated with the context
        long numBytes[] = new long[1];
        clGetContextInfo(context, CL_CONTEXT_DEVICES, 0, null, numBytes);

        // Obtain the cl_device_id for the first device
        int numDevices = (int) numBytes[0] / Sizeof.cl_device_id;
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetContextInfo(context, CL_CONTEXT_DEVICES, numBytes[0],
                Pointer.to(devices), null);

        // Create a command-queue
        commandQueue =
                clCreateCommandQueue(context, devices[0], 0, null);

        // Create the program from the source code
        firstCollisionProgram = clCreateProgramWithSource(context,
                1, new String[]{ readKernelSource(firstCollisionCode) }, null, null);

        // Build the program
        clBuildProgram(firstCollisionProgram, 0, null, null, null, null);

        // Create the kernel
        kernel = clCreateKernel(firstCollisionProgram, "dotProduct", null);
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

    private static cl_float4 add(cl_float4 a, cl_float4 b) {
        return Vector3D.newFloat4(a.get(0) + b.get(0), a.get(1) + b.get(1), a.get(2) + b.get(2));
    }

    private static cl_float4 subtract(cl_float4 a, cl_float4 b) {
        return Vector3D.newFloat4(a.get(0) - b.get(0), a.get(1) - b.get(1), a.get(2) - b.get(2));
    }

    private static cl_float4 scalarMultiply(cl_float4 vector, float multiplier) {
        return Vector3D.newFloat4(vector.get(0) * multiplier, vector.get(1) * multiplier, vector.get(2) * multiplier);
    }

    private static float dotProduct(cl_float4 a, cl_float4 b) {
        return a.get(0) * b.get(0) + a.get(1) * b.get(1) + a.get(2) * b.get(2);
    }

    private static float distanceSquared(cl_float4 a, cl_float4 b) {
        return    (a.get(0) - b.get(0)) * (a.get(0) - b.get(0))
                + (a.get(1) - b.get(1)) * (a.get(1) - b.get(1))
                + (a.get(2) - b.get(2)) * (a.get(2) - b.get(2));
    }

    public static RayTraceable.RayTraceableStruct[] toStructs(RayTraceable[] objects, Ray ray) {
        RayTraceable.RayTraceableStruct[] structs = new RayTraceable.RayTraceableStruct[objects.length];

        for (int gid = 0; gid < objects.length; gid++) {
            structs[gid] = objects[gid].toStruct(ray);
        }

        return structs;
    }

    public RayTraceable firstCollision(RayTraceable.RayTraceableStruct[] objectsInField, RayTraceable[] objs) {
        float[] distances = new float[objs.length];
        getCollisionDistances(objectsInField, distances);

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

    public void getCollisionDistances(RayTraceable.RayTraceableStruct[] objectsInField, float[] distances) {
        for (int gid = 0; gid < objectsInField.length; gid++) {
            if (objectsInField[gid].type == 2) {
                // Quads
                distances[gid] = distanceToCollideRect(
                        getDirection().toStruct(), getPosition().toStruct(),
                        objectsInField[gid]);
            } else if (objectsInField[gid].type == 1) {
                // Tris
                distances[gid] = distanceToCollideTri(
                        getDirection().toStruct(), getPosition().toStruct(),
                        objectsInField[gid]);
            } else /*if (objectsInField[gid].type == 0)*/{
                // Spheres
                distances[gid] = distanceToCollideSphere(
                        getDirection().toStruct(), getPosition().toStruct(),
                        objectsInField[gid]);
            }
//            else {
//                distances[gid] = 0/0f;
//            }
        }
    }

    public static cl_float2 projectToPlane(cl_float4 rayPos,
                                            cl_float4 planeX, cl_float4 planeY,
                                            cl_float4 direction, float distance) {
        return Vector2D.newFloat2(
                (rayPos.get(0) + direction.get(0) * distance) * planeX.get(0)
                        + (rayPos.get(1) + direction.get(1) * distance) * planeX.get(1)
                        + (rayPos.get(2) + direction.get(2) * distance) * planeX.get(2),
                (rayPos.get(0) + direction.get(0) * distance) * planeY.get(0)
                        + (rayPos.get(1) + direction.get(1) * distance) * planeY.get(1)
                        + (rayPos.get(2) + direction.get(2) * distance) * planeY.get(2)
        );
    }

    public static float distToCollidePlane(cl_float4 normal,
                                           cl_float4 vertexOrCenter,
                                           cl_float4 rayPos, cl_float4 rayDir) {
        return (  normal.get(0) * (vertexOrCenter.get(0) - rayPos.get(0))
                + normal.get(1) * (vertexOrCenter.get(1) - rayPos.get(1))
                + normal.get(2) * (vertexOrCenter.get(2) - rayPos.get(2)))
                /
                (  normal.get(0) * rayDir.get(0)
                 + normal.get(1) * rayDir.get(1)
                 + normal.get(2) * rayDir.get(2));
    }

    private static float distanceToCollideRect(cl_float4 rayDir, cl_float4 rayPos,
                                               RayTraceable.RayTraceableStruct obj
//                                                cl_float4 normal, cl_float4 center,
//                                                cl_float4 planeXaxis, cl_float4 planeYaxis,
//                                                cl_float2 max, cl_float2 min
    ) {
        float distance = distToCollidePlane(obj.normal, obj.vertexOrCenter, rayPos, rayDir);
        cl_float2 pointOnPlane = projectToPlane(rayPos, obj.side1, obj.side2, rayDir, distance);
        if (obj.min.get(0) < pointOnPlane.get(0) && obj.max.get(0) > pointOnPlane.get(0)
                && obj.min.get(1) < pointOnPlane.get(1) && obj.max.get(1) > pointOnPlane.get(1)) {
            return distance;
        }
        return Float.NaN;
    }

    private static float distanceToCollideSphere(cl_float4 rayDir, cl_float4 rayPos,
                                                  RayTraceable.RayTraceableStruct obj) {
        float amountInDirection = dotProduct(rayDir, subtract(rayPos, obj.vertexOrCenter));
        if (amountInDirection <= 0) {
            return (float) (-amountInDirection - Math.sqrt(amountInDirection * amountInDirection + obj.dot00 * obj.dot00 - distanceSquared(rayPos, obj.vertexOrCenter)));
        }
        return Float.NaN;
    }

    private static float distanceToCollideTri(cl_float4 rayDir, cl_float4 rayPos,
                                               RayTraceable.RayTraceableStruct obj) {
        float distance = distToCollidePlane(obj.normal, obj.vertexOrCenter, rayPos, rayDir);

        if (distance <= 0) {
            return Float.NaN;
        }

        cl_float4 point = add(rayPos, scalarMultiply(rayDir, distance));
        float dot02 = dotProduct(obj.side1, subtract(point, obj.vertexOrCenter));
        float dot12 = dotProduct(obj.side2, subtract(point, obj.vertexOrCenter));
        // Compute barycentric coordinates
        float u = (obj.dot11 * dot02 - obj.dot01 * dot12);
        float v = (obj.dot00 * dot12 - obj.dot01 * dot02);
        // Check if the point is inside the triangle
        if ((u >= 0) && (v >= 0) && ((u + v) <= obj.invDenom)) {
            return distance;
        }
        return Float.NaN;
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
