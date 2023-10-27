package gameengine.threed.graphics.raytraceing;

import gameengine.threed.prebuilt.objectmovement.collisions.Collider3D;
import gameengine.vectormath.Vector3D;

/**
 * A single ray of light which starts at a point and moves in a direction until
 * it hits a {@link Collider3D}.
 *
 * @author Louis Link
 * @since 1.0
 */
public class Ray {
    /**
     * The direction the {@code Ray} moves.
     */
    private final Vector3D direction;
    /**
     * The current position of the {@code Ray}. Will only ever have two values:
     * <p>On creation: {@code Ray}'s start
     * <p>After {@link #firstCollision} is called: If it collides, then the
     * first place it will collide, otherwise the start.
     */
    private Vector3D position;

    /**
     * Creates a new {@code Ray}.
     *
     * @param start The initial position of the {@code Ray}.
     * @param direction The direction the {@code Ray} will move in.
     */
    public Ray(final Vector3D start, final Vector3D direction) {
        this.direction = direction;
        this.position = start;
    }

    /**
     * Finds the first {@link Collider3D} in {@code objectsInField} that the
     * {@code Ray} will hit.
     *
     * @param objectsInField The {@link Collider3D}s that the {@code Ray} can
     *                       potentially collide with.
     * @return The closest {@link Collider3D} the {@code Ray} can collide with.
     */
    public Collider3D<?> firstCollision(final Collider3DList objectsInField) {
        double closestDist = Double.MAX_VALUE;
        Collider3D<?> closest = null;
        double newDistance;

        Collider3DList.Element element = objectsInField.getHead();
        while (element != null) {
            if (objectIsInDirection(element.value())) {
                newDistance = element.value()
                        .distanceToCollide(this, closestDist);

                if (newDistance >= 0 && newDistance < closestDist) {
                    closestDist = newDistance;
                    closest = element.value();
                }
            }
            element = element.next();
        }

        if (closest != null) {
            position = position.add(direction.atMagnitude(closestDist - 0.01));
        }

        return closest;
    }

    /**
     * Finds whether {@code collider} is possible for the {@code Ray} ray to
     * hit.
     *
     * @param collider The {@link Collider3D} to check if the {@code Ray}'s
     *                 direction lines up with.
     * @return Whether {@code collider} is within 90 degrees of the direction of
     * the {@code Ray} from the {@code Ray}'s initial position.
     */
    public boolean objectIsInDirection(final Collider3D<?> collider) {
        Vector3D toCenter = position.subtract(collider.getCenter());

        return !(toCenter.dotProduct(direction) > 0
                && Math.abs(toCenter.magnitude()) < 1);
    }


    /*
     * Utilities:
     */

    public Vector3D getDirection() {
        return direction;
    }

    public Vector3D getPosition() {
        return position;
    }
}
