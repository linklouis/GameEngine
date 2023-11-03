package gameengine.threed.graphics.raytraceing;

import gameengine.threed.graphics.raytraceing.objectgraphics.RayIntersectableList;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.threed.utilities.VectorLine3D;
import gameengine.threed.utilities.VectorLineIntersectable;
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
    public RayTraceable firstCollision(final RayIntersectableList objectsInField) {
        double closestDist = Double.MAX_VALUE;
        VectorLineIntersectable closest = null;
        double newDistance;

        for (RayIntersectableList.Element element = objectsInField.getHead();
             element != null; element = element.next()) {

            if (objectIsInDirection(element.value())) {
                newDistance = element.value()
                        .distanceToCollide(this, closestDist);

                if (newDistance >= 0 && newDistance < closestDist) {
                    closestDist = newDistance;
                    closest = element.value();
                }
            }
        }

        if (closest != null) {
            position = position.add(direction.atMagnitude(closestDist - 0.01));
        }

        return (RayTraceable) closest;
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
//        Vector3D toCenter = position.subtract(collider.getCenter());

        return !(getDirection().dotWithSubtracted(position, collider.getCenter()) > 0
                && Math.abs(position.distance(collider.getCenter())/*toCenter.magnitude()*/) < 1);
    }

    /**
     * Updates the {@code Ray}'s direction based on its reflection of off the
     * given {@link RayTraceable}.
     *
     * @param collider The {@link RayTraceable} to reflect off of.
     */
    public void reflect(RayTraceable collider, int numBounces) {
        direction = collider.reflection((LightRay) this).direction();
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
