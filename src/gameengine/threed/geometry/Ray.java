package gameengine.threed.geometry;

import gameengine.threed.graphics.raytraceing.LightRay;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayIntersectableList;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.threed.graphics.raytraceing.objectgraphics.SphereGraphics;
import gameengine.vectormath.Vector3D;

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
//    public RayIntersectable firstCollision(final RayIntersectableList objectsInField) {
//        double newDistance;
//        double amountInDirec;
//        double closestDist = Double.MAX_VALUE;
//        RayIntersectable closest = null;
//        RayIntersectableList.Element element = objectsInField.getHead();
//
//        boolean a = false;
//
//        Vector3D toCenter;
//
//        while (element != null) {
//            toCenter = position.subtract(element.value().getCenter());
//            amountInDirec = getDirection().dotProduct(toCenter);
//            if (((RayTraceable) element.value()).getTexture().isLightSource()) {
//                a = true;
////                System.out.println(element.value().distanceToCollide(this, closestDist, amountInDirec));
//            }
//
//            newDistance = element.value().distanceToCollide(this, closestDist, amountInDirec);
//            if (newDistance >= 0 && newDistance < closestDist) {
//                closestDist = newDistance;
//                closest = element.value();
//            }
//
//            element = element.next();
//        }
//
////        if (((RayTraceable) closest).getTexture().isLightSource() || a) {
////            System.out.println(closest.getClass() + ": " + ((RayTraceable) closest).getTexture().getColor() + ", " + closestDist);
////        }
////        System.out.println();
//
//        toDistance(closestDist - 0.01);
//        return closest;
//    }
    public RayIntersectable firstCollision(final RayIntersectableList objectsInField) {
        double newDistance;
        double amountInDirec;
        double closestDist = Double.MAX_VALUE;
        RayIntersectable closest;
        RayIntersectableList.Element element = objectsInField.getHead();

        while (true) {
            amountInDirec = getDirection().dotWithSubtracted(position, element.value().getCenter());

            if (amountInDirec < 0.5) {
                newDistance = element.value().distanceToCollide(this, closestDist, amountInDirec);
                if (newDistance > 0) {
                    closestDist = newDistance;
                    closest = element.value();
                    break;
                }
            }

            if (element.next() == null) {
                return null;
            }
            element = element.next();
        }

        Vector3D toCenter;

        while (element != null) {
            toCenter = position.subtract(element.value().getCenter());
            amountInDirec = getDirection().dotProduct(toCenter);

            if (amountInDirec < 0.5 && toCenter.magnitude() - element.value().getRange() < closestDist) {
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
//            if (amountInDirec < 0.5 && toCenter.magnitude() - element.value().getRange() < closestDist) {
//                newDistance = element.value().distanceToCollide(this, closestDist, amountInDirec);
//                if (newDistance >= 0 && newDistance < closestDist) {
//                    closestDist = newDistance;
//                    closest = element.value();
//                }
//            }
//        }
//
//        if (closest != null) {
//            position = position.addMultiplied(direction,closestDist - 0.01);
//        }
//
//        return closest;
//    }


//    public RayIntersectable firstCollision(final RayIntersectableList objectsInField) {
//        double closestDist = Double.MAX_VALUE;//objectsInField.getHead().value().distanceToCollide(this, Double.MAX_VALUE);
//        RayIntersectable closest = null;//objectsInField.getHead().value();
//        double newDistance;
//        double amountInDirec;
//        Vector3D toCenter;
//
////        System.out.println();
////        String output = "";
//        for (RayIntersectableList.Element element = objectsInField.getHead();
//             element != null; element = element.next()) {
//
//            amountInDirec = getDirection().dotWithSubtracted(position, element.value().getCenter());
//            if (amountInDirec < 0.5 && element.value().closestDistTo(position) < closestDist) {
//                newDistance = element.value().distanceToCollide(this, closestDist, amountInDirec);
////                System.out.println(element.value().getClass() + ": " + element.dist() + ", " + newDistance);
////                output += "\n" + element.value().getClass() + ": " + element.dist() + ", " + newDistance;
//                if (newDistance >= 0 && newDistance < closestDist) {
//                    closestDist = newDistance;
//                    closest = element.value();
////                        break;
////                    if (element.next() != null && newDistance <= element.next().dist()) {
////                        element = element.next();
////                        newDistance = element.value().distanceToCollide(this, closestDist, amountInDirec);
////                        output += "\n" + element.value().getClass() + ": " + element.dist() + ", " + newDistance;
//////                            System.out.println(output);
////                        break;
////                    }
//                }
//            }
//        }
////        System.exit(0);
//
//        if (closest != null/*objectsInField.getHead().value()*/) {
//            position = position.addMultiplied(direction,closestDist - 0.01);
//        }
//
//        return closest;
//    }

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
    public void reflect(RayTraceable collider, int numBounces) {
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
    public Ray getReflected(RayTraceable collider, int numBounces) {
        return new Ray(position, collider.reflection((LightRay) this).direction());
    }
}
