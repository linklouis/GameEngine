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

    public Collider3D<?> firstCollision(SinglyLinkedListAttribute objectsInFieldList, double precisionScale) {
//        SinglyLinkedListAttribute inRangeOf = new SinglyLinkedListAttribute();

//        double closestDist = getFirstCollisionDistance(objectsInFieldList, inRangeOf);
//
//        if (closestDist == Double.MAX_VALUE){
//            return null;
//        }
//        position = position.add(direction.unitVector().scalarMultiply(closestDist /*- *//*0.01*//*checkMove.magnitude()*/));

        return getFirstCollision(objectsInFieldList);//getCollision(inRangeOf, precisionScale);
    }

    public double getFirstCollisionDistance(SinglyLinkedListAttribute objectsInFieldList, SinglyLinkedListAttribute inRangeOf) {
        double closestDist = Double.MAX_VALUE;

        SinglyLinkedListAttribute.Element element = objectsInFieldList.getHead();
        while (element != null) {
            double newDistance = element.getValue().distanceToCollide(this);
            if (newDistance > 0) {
                inRangeOf.add(element.getValue());
                if (newDistance < closestDist) {
                    closestDist = newDistance;
                }
            }
            element = element.getNext();
        }

        return closestDist;
    }

    public Collider3D<?> getFirstCollision(SinglyLinkedListAttribute objectsInFieldList) {
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

    private Collider3D<?> getCollision(SinglyLinkedListAttribute colliders, double precisionScale) {
        Vector3D checkMove = direction.scalarMultiply(precisionScale);
        boolean inARange;

        while (!colliders.isEmpty()) {
            inARange = false;

            SinglyLinkedListAttribute.Element currentElement = colliders.getPointer();
            while (currentElement.hasNext()) {
                SinglyLinkedListAttribute.Element element = currentElement.getNext();
                if (element.getValue().inRange(position)) {
                    if (!element.isInRange()) {
                        element.setInRange(true);
                    }
                    if (!inARange) {
                        inARange = true;
                    }

                    if (element.getValue().contains(position)) {
                        escapeObject(element.getValue(), checkMove);
                        return element.getValue();
                    }

                    currentElement = element;
                } else {
                    if (element.isInRange()) {
                        currentElement.removeNext();
                    } else {
                        currentElement = element;
                    }
                }
            }

            if (inARange) {
                position = position.add(checkMove);
            } else {
                position = position.add(direction);
            }
        }

        return null;
    }

//    private Collider3D<?> getCollision(SinglyLinkedListAttribute colliders, double precisionScale, double maxDistance) {
////        double distanceMoved = 0;
//        Vector3D checkMove = direction.scalarMultiply(precisionScale);
////        double checkDistance = checkMove.magnitude();
////        double moveDistance = direction.magnitude();
//        boolean inARange;
////        System.out.println("\n" + colliders);
////        System.out.println(colliders.isEmpty());
//
//        while (!colliders.isEmpty()/*distanceMoved < maxDistance*/) {
//            inARange = false;
//
//            SinglyLinkedListAttribute.Element currentElement = colliders.getPointer();
//            while (currentElement.hasNext()) {
//                SinglyLinkedListAttribute.Element element = currentElement.getNext();
//                if (element.getValue().inRange(position)) {
////                    System.out.print("a");
//                    if (!element.isInRange()) {
//                        element.setInRange(true);
//                    }
//                    if (!inARange) {
//                        inARange = true;
//                    }
//
//                    if (element.getValue().contains(position)) {
//                        escapeObject(element.getValue(), checkMove);
//                        return element.getValue();
//                    }
//
//                    currentElement = element;
//                } else {
////                    System.out.print("(" + position + ", " + element.getValue().getCenter() + ")");
//                    if (element.isInRange()) {
//                        currentElement.removeNext();
//                    } else {
//                        currentElement = element;
//                    }
//                }
//            }
//
//            if (inARange) {
//                position = position.add(checkMove);
////                distanceMoved += checkDistance;
//            } else {
//                position = position.add(direction);
////                distanceMoved += moveDistance;
//            }
//        }
//
//        return null;
//    }

    private void escapeObject(Collider3D<?> collision, Vector3D checkMove) {
        while (collision.contains(position)) {
            position = position.subtract(checkMove.scalarDivide(10));
        }
    }

    private Collider3D<?> getCollision1(SinglyLinkedListAttribute colliders, double precisionScale, double maxDistance) {
        double distanceMoved = 0;
        Vector3D checkMove = direction.scalarMultiply(precisionScale);
        double checkDistance = checkMove.magnitude();
        double moveDistance = direction.magnitude();
        boolean inARange;
        while (distanceMoved < maxDistance) {
            inARange = false;

            SinglyLinkedListAttribute.Element element = colliders.getHead();
            while (element != null) {
                if (element.getValue().inRange(position)) {
                    if (!inARange) {
                        inARange = true;
                    }
                    if (element.getValue().contains(position)) {
                        while (element.getValue().contains(position)) {
                            position = position.subtract(checkMove.scalarDivide(14));
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
