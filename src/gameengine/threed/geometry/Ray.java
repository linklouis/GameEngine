package gameengine.threed.geometry;

import gameengine.threed.graphics.raytraceing.LightRay;
import gameengine.threed.graphics.raytraceing.objectgraphics.QuadGraphics;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayIntersectableList;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.threed.graphics.raytraceing.objectgraphics.SphereGraphics;
import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

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
    public RayIntersectable firstCollision1(final RayIntersectableList objectsInField) {
        RayIntersectableList.Element element = objectsInField.getHead();

        if (element == null) {
            return null;
        }
        // Set the closest distance to the distance of the first object.
        double closestDist = element.value().distanceToCollide(this,
                getDirection().dotWithSubtracted(position, element.value().getCenter()));

        // While the distance of the closest collision either doesn't exist or is behind the ray,
        // keep finding the next distance.
        while (Double.isNaN(closestDist) || closestDist < 0) {
            if (element.next() == null) {
                return null;
            }
            element = element.next();
            closestDist = element.value().distanceToCollide(this,
                    getDirection().dotWithSubtracted(position, element.value().getCenter()));
        }

        // If the current element is the last one, early exit.
        if (element.isLast()) {
            toDistance(closestDist - 0.01);
            return element.value();
        }
        RayIntersectable closest = element.value();
        element = element.next();
        double newDistance;
        Vector3D toCenter;

        // While there are more objects in the scene,
        // if that object is closer than the current closest distance, check the distance it collides at,
        // and if that's also closer, take this object instead.
        while (true) {
            toCenter = position.subtract(element.value().getCenter());

            if (toCenter.magnitude() - element.value().getRange() < closestDist) {
                newDistance = element.value().distanceToCollide(this, closestDist,
                        getDirection().dotProduct(toCenter));
                if (newDistance >= 0 && newDistance < closestDist) {
                    closestDist = newDistance;
                    closest = element.value();
                }
            }

            if (element.isLast()) {
                toDistance(closestDist - 0.01);
                return closest;
            }
            element = element.next();
        }
    }

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

    public RayIntersectable firstCollision(final RayIntersectableList objectsInField) {
        double closestDist = Double.MAX_VALUE;
        RayIntersectable closest = null;
        double newDistance;
        double amountInDirec;
        Vector3D toCenter;

        for (RayIntersectableList.Element element = objectsInField.getHead();
             element != null; element = element.next()) {

            toCenter = position.subtract(element.value().getCenter());
            if (element.value() instanceof SphereGraphics) {
               newDistance = distanceToCollideSphere(this, element.value().getCenter(),
                       ((SphereGraphics) element.value()).getRadius());
            } else {
                newDistance = distanceToCollideRect(this, element.value().surfaceNormal(this), ((QuadGraphics) element.value()).getVertex1(),
                        ((QuadGraphics) element.value()).getPlaneXAxis(), ((QuadGraphics) element.value()).getPlaneYAxis());
            }
            if (newDistance >= 0 && newDistance < closestDist) {
                closestDist = newDistance;
                closest = element.value();
            }
        }

        if (closest != null) {
            position = position.addMultiplied(getDirection(),closestDist - 0.01);
        }

        return closest;
    }

    private static double distanceToCollideRect(Ray ray, Vector3D normal, Vector3D center, Vector3D planeXaxis, Vector3D planeYaxis, Vector2D max, Vector2D min) {
        double distance = normal.distToCollidePlane(center, ray.getPosition(), ray.getDirection());
        Vector2D pointOnPlane = ray.getPosition().projectToPlane(planeXaxis, planeYaxis, ray.getDirection(), distance);
        if (min.getX() < pointOnPlane.getX() && max.getX() > pointOnPlane.getX()
                && min.getY() < pointOnPlane.getY() && max.getY() > pointOnPlane.getY()) {
            return distance;
        }
        return Double.NaN;
    }

    private static double distanceToCollideSphere(Ray lightRay, Vector3D center, double radius) {
        Double amountInDirection = lightRay.getDirection().dotProduct(lightRay.getPosition().subtract(center));
        if (amountInDirection <= 0) {
            return -amountInDirection - Math.sqrt(amountInDirection * amountInDirection + radius * radius - lightRay.getPosition().distanceSquared(center));
        }
        return Double.NaN;
    }


    public LinkedHashMap<RayTraceable, Ray> getCollisions(final int maxBounces,
                                                          final RayIntersectableList objectsInField) {
        LinkedHashMap<RayTraceable, Ray> map = new LinkedHashMap<>(maxBounces);
        RayTraceable collision;

        for (int bounces = 2; bounces <= maxBounces; bounces++) {
            collision = (RayTraceable) firstCollision(objectsInField);

            if (collision == null) {
                return null;
            }

            map.put(collision, new Ray(this));
            reflect(collision);
        }

        return map;
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
