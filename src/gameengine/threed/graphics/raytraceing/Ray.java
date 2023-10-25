package gameengine.threed.graphics.raytraceing;

import gameengine.threed.prebuilt.objectmovement.collisions.Collider3D;
import gameengine.vectormath.Vector3D;

public class Ray {
    private final Vector3D direction;
    private final Vector3D start;
    private Vector3D position;

    public Ray(Vector3D start, Vector3D direction) {
        this.direction = direction;
        this.start = start;
        this.position = start;
    }

    public Collider3D<?> firstCollision(SinglyLinkedListAttribute objectsInFieldList) {
        double closestDist = Double.MAX_VALUE;
        Collider3D<?> closest = null;

        SinglyLinkedListAttribute.Element element = objectsInFieldList.getHead();
        while (element != null) {
            double newDistance = element.getValue().distanceToCollide(this);
            if (newDistance >= 0) {
                if (newDistance < closestDist) {
                    closestDist = newDistance;
                    closest = element.getValue();
                }
            }
            element = element.getNext();
        }

        if (closestDist != Double.MAX_VALUE) {
            position = start.add(direction.unitVector().scalarMultiply(closestDist));
        }

        return closest;
    }

    public Vector3D getDirection() {
        return direction;
    }

    public Vector3D getStart() {
        return start;
    }

    public Vector3D getPosition() {
        return position;
    }
}
