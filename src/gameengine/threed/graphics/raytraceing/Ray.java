package gameengine.threed.graphics.raytraceing;

import gameengine.threed.prebuilt.objectmovement.collisions.Collider3D;
import gameengine.vectormath.Vector3D;

public class Ray {
    private final Vector3D direction;
    private Vector3D position;

    public Ray(Vector3D start, Vector3D direction) {
        this.direction = direction;
        this.position = start;
    }

    public Collider3D<?> firstCollision(Collider3DList objectsInFieldList) {
        double closestDist = Double.MAX_VALUE;
        Collider3D<?> closest = null;

        Collider3DList.Element element = objectsInFieldList.getHead();
        while (element != null) {
            if (objectIsInDirection(element.value())) {
                double newDistance = element.value().distanceToCollide(this, closestDist);
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

    public boolean objectIsInDirection(Collider3D<?> collider) {
        Vector3D toCenter = position.subtract(collider.getCenter());
        return !(toCenter.dotProduct(direction) > 0 && Math.abs(toCenter.magnitude()) < 1);
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
