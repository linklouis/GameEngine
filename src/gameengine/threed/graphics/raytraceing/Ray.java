package gameengine.threed.graphics.raytraceing;

import gameengine.threed.graphics.raytraceing.objectgraphics.RayIntersectableList;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.threed.utilities.RayIntersectable;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

/**
 * A single ray of light which starts at a point and moves in a direction until
 * it hits a {@link RayTraceable}.
 *
 * @author Louis Link
 * @since 1.0
 */
public class Ray {
    /**
     * The direction the {@code Line} moves.
     */
    protected Vector3D direction;
    /**
     * The current position of the {@code Line}.
     */
    protected Vector3D position;
    private final Vector3D color;

    /**
     * Creates a new {@code Ray}.
     *
     * @param startPosition The initial position of the {@code Ray}.
     * @param direction The direction the {@code Ray} will move in.
     */
    public Ray(final Vector3D startPosition, final Vector3D direction) {
        this.direction = direction.unitVector();
        this.position = startPosition;
        color = new Vector3D(0);
    }

    public Ray(final Vector3D startPosition, final Vector3D direction, final Vector3D color) {
        this.direction = direction.unitVector();
        this.position = startPosition;
        this.color = color;
    }

    public Ray(final Vector3D startPosition, final Vector3D direction, final Color color) {
        this.direction = direction.unitVector();
        this.position = startPosition;
        this.color = new Vector3D(color);
    }

    public Ray(final Vector3D startPosition, final Reflection reflection) {
        this.direction = reflection.direction();
        this.position = startPosition;
        this.color = new Vector3D(reflection.color());
    }

    public Ray(final Ray parent) {
        direction = parent.getDirection();
        position = parent.getPosition();
        color = parent.getColor();
    }


    /*
     * Functionality:
     */

    /**
     * Finds the first {@link RayTraceable} in {@code objectsInField} that the
     * {@code Ray} will hit.
     *
     * @param objectsInField The {@link RayTraceable}s that the {@code Ray} can
     *                       potentially collide with.
     * @return The closest {@link RayTraceable} the {@code Ray} can collide with.
     */
    public RayIntersectable firstCollision(final RayIntersectableList objectsInField) {
        double closestDist = Double.MAX_VALUE;
        RayIntersectable closest = null;
        double newDistance;

        for (RayIntersectableList.Element element = objectsInField.getHead();
             element != null; element = element.next()) {
            if (objectIsInDirection(element.value())) {
                newDistance = element.value().distanceToCollide(this, closestDist);
                if (newDistance >= 0 && newDistance < closestDist) {
                    closestDist = newDistance;
                    closest = element.value();
                }
            }
        }

        if (closest != null) {
            position = position.addMultiplied(direction,closestDist - 0.01);
        }

        return closest;
    }

    /**
     * Finds whether {@code collider} is possible for the {@code Ray} ray to
     * hit.
     *
     * @param collider The {@link RayTraceable} to check if the {@code Ray}'s
     *                 direction lines up with.
     * @return Whether {@code collider} is within 90 degrees of the direction of
     * the {@code Ray} from the {@code Ray}'s initial position.
     */
    public boolean objectIsInDirection(final RayIntersectable collider) {
        Vector3D toCenter = position.subtract(collider.getCenter());
        return !(getDirection().dotProduct(toCenter) > 0 && toCenter.magnitudeSquared() < 1);

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
        Reflection reflectionDetails = collider.reflection(this);
        direction = reflectionDetails.direction();
        color.addMutable(reflectionDetails.color().scalarDivide(numBounces));
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
        Reflection reflectionDetails = collider.reflection(this);
        return new Ray(position, reflectionDetails.direction(), color.add(reflectionDetails.color().scalarDivide(numBounces)));
    }

    public Vector3D pointAtDistance(double distance) {
        return position.add(direction.scalarMultiply(distance));
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

    public Vector3D getColor() {
        return color;
    }
}
