package gameengine.threed.geometry;

import gameengine.threed.graphics.raytraceing.LightRay;
import gameengine.threed.graphics.raytraceing.objectgraphics.*;
import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;

import java.util.LinkedHashMap;

public class Ray extends VectorLine3D {


    public Ray(final Vector3D startPosition, final Vector3D direction) {
        super(startPosition, direction);
    }

    public Ray(Ray parent) {
        super(parent.getPosition(), parent.getDirection());
    }

    /**
     * Finds the first {@link RayTraceable} in {@code objectsInField} that the
     * {@code LightRay} will hit.
     *
     * @param objectsInField The {@link RayTraceable}s that the {@code LightRay} can
     *                       potentially collide with.
     * @return The closest {@link RayTraceable} the {@code LightRay} can collide with.
     */
//    public RayIntersectable firstCollision1(final RayIntersectableList objectsInField) {
//        RayIntersectableList.Element element = objectsInField.getHead();
//
//        if (element == null) {
//            return null;
//        }
//        // Set the closest distance to the distance of the first object.
//        double closestDist = element.value().distanceToCollide(this,
//                getDirection().dotWithSubtracted(position, element.value().getCenter()));
//
//        // While the distance of the closest collision either doesn't exist or is behind the ray,
//        // keep finding the next distance.
//        while (Double.isNaN(closestDist) || closestDist < 0) {
//            if (element.next() == null) {
//                return null;
//            }
//            element = element.next();
//            closestDist = element.value().distanceToCollide(this,
//                    getDirection().dotWithSubtracted(position, element.value().getCenter()));
//        }
//
//        // If the current element is the last one, early exit.
//        if (element.isLast()) {
//            toDistance(closestDist - 0.01);
//            return element.value();
//        }
//        RayIntersectable closest = element.value();
//        element = element.next();
//        double newDistance;
//        Vector3D toCenter;
//
//        // While there are more objects in the scene,
//        // if that object is closer than the current closest distance, check the distance it collides at,
//        // and if that's also closer, take this object instead.
//        while (true) {
//            toCenter = position.subtract(element.value().getCenter());
//
//            if (toCenter.magnitude() - element.value().getRange() < closestDist) {
//                newDistance = element.value().distanceToCollide(this, closestDist,
//                        getDirection().dotProduct(toCenter));
//                if (newDistance >= 0 && newDistance < closestDist) {
//                    closestDist = newDistance;
//                    closest = element.value();
//                }
//            }
//
//            if (element.isLast()) {
//                toDistance(closestDist - 0.01);
//                return closest;
//            }
//            element = element.next();
//        }
//    }

    /*
    Problems with old version:
        Unnecessary value setting (i.e. closestDist = Double.MAX_VALUE)
        Unnecessary checks (i.e. is first collision distance less than Double.MAX_VALUE, etc.)
     */
//    public RayIntersectable firstCollision(final RayIntersectableList objectsInField) {
//        double closestDist = Double.MAX_VALUE;
//        RayIntersectable closest = null;
//        double newDistance;
//        double amountInDirec;
//        Vector3D toCenter;
//
//        for (RayIntersectableList.Element element = objectsInField.getHead();
//             element != null; element = element.next()) {
//
//            toCenter = position.subtract(element.value().getCenter());
//            amountInDirec = getDirection().dotProduct(toCenter);
////            if (amountInDirec < 0.5 && toCenter.magnitude() - element.value().getRange() < closestDist) {
//                newDistance = element.value().distanceToCollide(this, closestDist, amountInDirec);
//                if (newDistance >= 0 && newDistance < closestDist) {
//                    closestDist = newDistance;
//                    closest = element.value();
//                }
////            }
//        }
//
//        if (closest != null) {
//            position = position.addMultiplied(getDirection(),closestDist - 0.01);
//        }
//
//        return closest;
//    }

    private static Vector3D.V3Struct add(Vector3D.V3Struct a, Vector3D.V3Struct b) {
        return new Vector3D.V3Struct(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    private static Vector3D.V3Struct subtract(Vector3D.V3Struct a, Vector3D.V3Struct b) {
        return new Vector3D.V3Struct(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    private static Vector3D.V3Struct scalarMultiply(Vector3D.V3Struct vector, float multiplier) {
        return new Vector3D.V3Struct(vector.x * multiplier, vector.y * multiplier, vector.z * multiplier);
    }

    private static float dotProduct(Vector3D.V3Struct a, Vector3D.V3Struct b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    private static float distanceSquared(Vector3D.V3Struct a, Vector3D.V3Struct b) {
        return    (a.x - b.x) * (a.x - b.x)
                + (a.y - b.y) * (a.y - b.y)
                + (a.z - b.z) * (a.z - b.z);
    }

    public static RayTraceable.RayTraceableStruct[] toStructs(RayTraceable[] objects, Ray ray) {
        RayTraceable.RayTraceableStruct[] structs = new RayTraceable.RayTraceableStruct[objects.length];

        for (int gid = 0; gid < objects.length; gid++) {
            structs[gid] = objects[gid].toStruct(ray);
        }

        return structs;
    }

    public RayTraceable firstCollision(RayTraceable.RayTraceableStruct[] objectsInField, RayTraceable[] objs) {
        double closestDist = Double.MAX_VALUE;
        RayTraceable closest = null;
        double newDistance;

        for (int gid = 0; gid < objectsInField.length; gid++) {
            if (objectsInField[gid].type == 2) {
                // Quads
                newDistance = distanceToCollideRect(getDirection().toStruct(), getPosition().toStruct(),
                        objectsInField[gid]
//                        quad.surfaceNormal(this), quad.getVertex1(),
//                        quad.getPlaneXAxis(), quad.getPlaneYAxis(),
//                        quad.getOnPlaneMax(), quad.getOnPlaneMin()
                );
            } else if (objectsInField[gid].type == 1) {
                // Tris
                newDistance = distanceToCollideTri(
                        getDirection().toStruct(), getPosition().toStruct(),
                        closestDist,
                        objectsInField[gid]
//                        tri.surfaceNormal(this), closestDist, tri.v0(),
//                        tri.v1(), tri.getVertex1(), tri.dot00(), tri.dot01(), tri.dot11(), tri.invDenom()
                        );
            } else {
                // Spheres
                newDistance = distanceToCollideSphere(
                        getDirection().toStruct(), getPosition().toStruct(),
                        objectsInField[gid]
//                        ((RayTraceable) element.value()).toStruct(this)

//                        element.value().getCenter(),
//                        ((SphereGraphics) element.value()).getRadius()
                );
            }

            if (newDistance >= 0 && newDistance < closestDist) {
                closestDist = newDistance;
                closest = objs[gid];
            }
        }

        if (closest != null) {
            position = position.addMultiplied(getDirection(),closestDist - 0.01);
        }

        return closest;
    }

    public static Vector2D.V2Struct projectToPlane(Vector3D.V3Struct rayPos,
                                            Vector3D.V3Struct planeX, Vector3D.V3Struct planeY,
                                            Vector3D.V3Struct direction, float distance) {
        return new Vector2D.V2Struct(
                (rayPos.x + direction.x * distance) * planeX.x
                        + (rayPos.y + direction.y * distance) * planeX.y
                        + (rayPos.z + direction.z * distance) * planeX.z,
                (rayPos.x + direction.x * distance) * planeY.x
                        + (rayPos.y + direction.y * distance) * planeY.y
                        + (rayPos.z + direction.z * distance) * planeY.z
        );
    }

    public static float distToCollidePlane(Vector3D.V3Struct normal,
                                           Vector3D.V3Struct vertexOrCenter,
                                           Vector3D.V3Struct rayPos, Vector3D.V3Struct rayDir) {
        return (  normal.x * (vertexOrCenter.x - rayPos.x)
                + normal.y * (vertexOrCenter.y - rayPos.y)
                + normal.z * (vertexOrCenter.z - rayPos.z))
                /
                (  normal.x * rayDir.x
                 + normal.y * rayDir.y
                 + normal.z * rayDir.z);
    }

    private static float distanceToCollideRect(Vector3D.V3Struct rayDir, Vector3D.V3Struct rayPos,
                                               RayTraceable.RayTraceableStruct obj
//                                                Vector3D.V3Struct normal, Vector3D.V3Struct center,
//                                                Vector3D.V3Struct planeXaxis, Vector3D.V3Struct planeYaxis,
//                                                Vector2D.V2Struct max, Vector2D.V2Struct min
    ) {
        float distance = distToCollidePlane(obj.normal, obj.vertexOrCenter, rayPos, rayDir);
        Vector2D.V2Struct pointOnPlane = projectToPlane(rayPos, obj.side1, obj.side2, rayDir, distance);
        if (obj.min.x < pointOnPlane.x && obj.max.x > pointOnPlane.x
                && obj.min.y < pointOnPlane.y && obj.max.y > pointOnPlane.y) {
            return distance;
        }
        return Float.NaN;
    }

    private static double distanceToCollideSphere(Vector3D.V3Struct rayDir, Vector3D.V3Struct rayPos,
                                                  RayTraceable.RayTraceableStruct obj) {
        float amountInDirection = dotProduct(rayDir, subtract(rayPos, obj.vertexOrCenter));
        if (amountInDirection <= 0) {
            return -amountInDirection - Math.sqrt(amountInDirection * amountInDirection + obj.dot00 * obj.dot00 - distanceSquared(rayPos, obj.vertexOrCenter));
        }
        return Double.NaN;
    }

    private static double distanceToCollideTri(Vector3D.V3Struct rayDir, Vector3D.V3Struct rayPos,
                                               double curSmallestDist,
                                               RayTraceable.RayTraceableStruct obj) {
        float distance = //obj.normal.distToCollidePlane(obj.vertexOrCenter, rayPos, rayDir);
                (  obj.normal.x * (obj.vertexOrCenter.x - rayPos.x)
                        + obj.normal.y * (obj.vertexOrCenter.y - rayPos.y)
                        + obj.normal.z * (obj.vertexOrCenter.z - rayPos.z))
                        /
                        (  obj.normal.x * rayDir.x
                                + obj.normal.y * rayDir.y
                                + obj.normal.z * rayDir.z);

        if (distance <= 0 || distance >= curSmallestDist) {
            return Double.NaN;
        }

        Vector3D.V3Struct point = add(rayPos, scalarMultiply(rayDir, distance));
        double dot02 = dotProduct(obj.side1, subtract(point, obj.vertexOrCenter));
        double dot12 = dotProduct(obj.side2, subtract(point, obj.vertexOrCenter));
        // Compute barycentric coordinates
        double u = (obj.dot11 * dot02 - obj.dot01 * dot12);
        double v = (obj.dot00 * dot12 - obj.dot01 * dot02);
        // Check if the point is inside the triangle
        if ((u >= 0) && (v >= 0) && ((u + v) <= obj.invDenom)) {
            return distance;
        }
        return Double.NaN;
    }


//    private static double distanceToCollideTri(Ray ray, double curSmallestDist, Vector3D normal,
//                                               Vector3D v0, Vector3D v1, Vector3D vertex1,
//                                               double dot00, double dot01, double dot11, double invDenom) {
//        double distance = normal.distToCollidePlane(vertex1, ray.getPosition(), ray.getDirection());
//
//        if (distance <= 0 || distance >= curSmallestDist || !inRange(ray.position.add(ray.getDirection().scalarMultiply(distance)), v0, v1, vertex1, dot00, dot01, dot11, invDenom)) {
//            return Double.NaN;
//        }
//
//        return distance;
//    }
//
//    private static boolean inRange(Vector3D point, Vector3D v0, Vector3D v1, Vector3D vertex1, double dot00, double dot01, double dot11, double invDenom) {
//        // ChatGPT
//        double dot02 = v0.dotWithSubtracted(point, vertex1);
//        double dot12 = v1.dotWithSubtracted(point, vertex1);
//
//        // Compute barycentric coordinates
//        double u = (dot11 * dot02 - dot01 * dot12);
//        double v = (dot00 * dot12 - dot01 * dot02);
//
//        // Check if the point is inside the triangle
//        return (u >= 0) && (v >= 0) && ((u + v) <= invDenom);
//    }


//    public LinkedHashMap<RayTraceable, Ray> getCollisions(final int maxBounces,
//                                                          final RayIntersectableList objectsInField) {
//        LinkedHashMap<RayTraceable, Ray> map = new LinkedHashMap<>(maxBounces);
//        RayTraceable collision;
//
//        for (int bounces = 2; bounces <= maxBounces; bounces++) {
//            collision = (RayTraceable) firstCollision(objectsInField);
//
//            if (collision == null) {
//                return null;
//            }
//
//            map.put(collision, new Ray(this));
//            reflect(collision);
//        }
//
//        return map;
//    }

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
