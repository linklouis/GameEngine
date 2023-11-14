package gameengine.threed.geometry;

import gameengine.threed.graphics.raytraceing.LightRay;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayIntersectableList;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.threed.graphics.raytraceing.objectgraphics.SphereGraphics;
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
    public RayIntersectable firstCollision(final RayIntersectableList objectsInField) {
        double newDistance;
        double amountInDirec;
        double closestDist = Double.MAX_VALUE;
        RayIntersectable closest;
        RayIntersectableList.Element element = objectsInField.getHead();

        while (true) {
            amountInDirec = getDirection().dotWithSubtracted(position, element.value().getCenter());

//            if (amountInDirec < 0) {
                newDistance = element.value().distanceToCollide(this, closestDist, amountInDirec);
                if (newDistance > 0) {
                    closestDist = newDistance;
                    closest = element.value();
                    break;
                }
//            }

            if (element.next() == null) {
                return null;
            }
            element = element.next();
        }

        Vector3D toCenter;

        while (element != null) {
            toCenter = position.subtract(element.value().getCenter());
            amountInDirec = getDirection().dotProduct(toCenter);

            if (/*amountInDirec < 0 &&*/ toCenter.magnitude() - element.value().getRange() < closestDist) {
                newDistance = element.value().distanceToCollide(this, closestDist, amountInDirec);
                if (newDistance >= 0 && newDistance < closestDist) {
                    closestDist = newDistance;
                    closest = element.value();
                }
            }

            element = element.next();
        }

        toDistance(closestDist - 0.01);
        return closest;
    }

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
     * Finds whether {@code collider} is possible for the {@code LightRay} ray to
     * hit.
     *
     * @param collider The {@link RayTraceable} to check if the {@code LightRay}'s
     *                 direction lines up with.
     * @return Whether {@code collider} is within 90 degrees of the direction of
     * the {@code LightRay} from the {@code LightRay}'s initial position.
     */
    public boolean objectIsInDirection(final VectorLineIntersectable collider) {
        return getDirection().dotWithSubtracted(collider.getCenter(), position) > 0;

//        Vector3D toCenter = position.subtract(collider.getCenter());
//        return !(getDirection().dotProduct(toCenter) > 0/* && toCenter.magnitudeSquared() < 0.5*/);

//        return !(getDirection().dotWithSubtracted(position, collider.getCenter()) > 0
//                && Math.abs(position.distance(collider.getCenter())/*toCenter.magnitude()*/) < 1);
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
