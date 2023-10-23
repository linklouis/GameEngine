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

    public Collider3D<?> firstCollision(SinglyLinkedList<Collider3D<?>> objectsInFieldList, double precisionScale, double maxDistance) {
        SinglyLinkedList<Collider3D<?>> inRangeOf = new SinglyLinkedList<>();

        double closestDist = getFirstCollisionDistance(objectsInFieldList, inRangeOf);

        if (closestDist == Double.MAX_VALUE){
            return null;
        }
        position = position.add(direction.unitVector().scalarMultiply(closestDist /*- *//*0.01*//*checkMove.magnitude()*/));

        return getCollision(inRangeOf, precisionScale, maxDistance);
    }

    public double getFirstCollisionDistance(SinglyLinkedList<Collider3D<?>> objectsInFieldList, SinglyLinkedList<Collider3D<?>> inRangeOf) {
        double closestDist = Double.MAX_VALUE;

        SinglyLinkedList<Collider3D<?>>.Element element = objectsInFieldList.getHead();
        while (element != null) {
            double newDistance = element.getValue().distanceToEnterRange(start, direction);
            if (newDistance >= 0) {
                inRangeOf.add(element.getValue());
                if (newDistance < closestDist) {
                    closestDist = newDistance;
                }
            }
            element = element.getNext();
        }

        return closestDist;
    }

    private Collider3D<?> getCollision(SinglyLinkedList<Collider3D<?>> colliders, double precisionScale, double maxDistance) {
        // TODO Have list of all currently in range of.If an object in that
        //  list is no longer colliding, remove it.
        double distanceMoved = 0;
        Vector3D checkMove = direction.scalarMultiply(precisionScale);
        double checkDistance = checkMove.magnitude();
        double moveDistance = direction.magnitude();
        boolean inARange;
        while (distanceMoved < maxDistance) {
            inARange = false;

            SinglyLinkedList<Collider3D<?>>.Element element = colliders.getHead();
            while (element != null) {
                if (element.getValue().inRange(position)) {
                    if (!inARange) {
                        inARange = true;
                    }
                    if (element.getValue().contains(position)) {
                        while (element.getValue().contains(position)) {
                            position = position.subtract(checkMove.scalarDivide(10));
                        }
                        return element.getValue();
                    }
                }

                element = element.getNext();
            }

            if (inARange) {
                position = position.add(checkMove);
                distanceMoved += checkDistance;
            } else {
                position = position.add(direction);
                distanceMoved += moveDistance;
            }
        }

        return null;
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
