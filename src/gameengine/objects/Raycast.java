package gameengine.objects;

import gameengine.prebuilt.objectmovement.InPlane;
import gameengine.prebuilt.objectmovement.collisions.Collider;
import gameengine.vectormath.Vector2D;

import java.awt.geom.Point2D;
import java.util.Arrays;

public class Raycast extends Vector2D {
    private final Point2D start;
    private final double maxDistance;

    private final GameObject[] objectsInField;

    /*
     * Construction:
     */

    public Raycast(Point2D start, double x, double y, double marchDistance,
                   double maxDistance, GameObject[] objectsInField) {
        super(new Vector2D(x, y)
                .unitVector()
                .scalarMultiply(marchDistance)
                .getComponents());
        this.start = start;
        this.maxDistance = maxDistance;
        this.objectsInField = objectsInField;
    }

    public Raycast(Point2D start, Vector2D vector, double marchDistance,
                   double maxDistance, GameObject[] objectsInField) {
        super(vector
                .unitVector()
                .scalarMultiply(marchDistance)
                .getComponents());
        this.start = start;
        this.maxDistance = maxDistance;
        this.objectsInField = objectsInField;
    }


    /*
     * Functionality:
     */

    public Collider<?> firstCollision() {
        Point2D position = (Point2D) start.clone();
        Collider<?>[] potentialColliders = potentialColliders();

        while (position.distance(start) < maxDistance) {
            for (Collider<?> collider : potentialColliders) {
                if (collider.contains(position)) {
                    return collider;
                }
            }

            position = pointAtDistanceFrom(position);
        }

        return null;
    }

    private Collider<?>[] potentialColliders() {
        return Arrays.stream(objectsInField)
                .filter(gameObject ->
                        gameObject.containsModifier(Collider.class))
                .filter(gameObject ->
                        this.dotProduct(Vector2D.displacement(
                                gameObject.get(InPlane.class).getLocation(),
                                start
                                )) > 0)
                .map(gameObject -> gameObject.get(Collider.class))
                .toArray(Collider<?>[]::new);
    }
}
