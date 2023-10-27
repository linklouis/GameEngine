package gameengine.twod;

import gameengine.skeletons.GameObject;
import gameengine.twod.prebuilt.objectmovement.InPlane;
import gameengine.twod.prebuilt.objectmovement.collisions.Collider2D;
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

    public Collider2D<?> firstCollision() {
        Point2D position = (Point2D) start.clone();
        Collider2D<?>[] potentialCollider2DS = potentialColliders();

        while (position.distance(start) < maxDistance) {
            for (Collider2D<?> collider2D : potentialCollider2DS) {
                if (collider2D.contains(position)) {
                    return collider2D;
                }
            }

            position = pointAtDistanceFrom(position);
        }

        return null;
    }

    private Point2D pointAtDistanceFrom(Point2D position) {
        return new Point2D.Double(
                getX() + position.getX(),
                getY() + position.getY());
    }

    private Collider2D<?>[] potentialColliders() {
        return Arrays.stream(objectsInField)
                .filter(gameObject ->
                        gameObject.containsModifier(Collider2D.class))
                .filter(gameObject ->
                        this.dotProduct(Vector2D.displacement(
                                gameObject.get(InPlane.class).getLocation(),
                                start
                                )) > 0)
                .map(gameObject -> gameObject.get(Collider2D.class))
                .toArray(Collider2D<?>[]::new);
    }
}
