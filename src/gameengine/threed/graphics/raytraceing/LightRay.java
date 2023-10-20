package gameengine.threed.graphics.raytraceing;

import gameengine.threed.graphics.Visual3D;
import gameengine.threed.prebuilt.objectmovement.collisions.Collider3D;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public class LightRay extends Vector3D {
    private final Vector3D start;
    private Vector3D position;
    private final double maxDistance;
    private final int numBounces;
    private Vector3D color = new Vector3D(0);

//    private final Collider3D[] objectsInField;
    private final SinglyLinkedList<Collider3D<?>> objectsInFieldList;
    private final SinglyLinkedList<Collider3D<?>> objectsInFieldParent;

    private Vector3D actualMove;
//    private double actualMoveSize;
    private Vector3D checkMove;

    /*
     * Construction:
     */

    public LightRay(Vector3D start, double x, double y, double z, double marchDistance,
                    double maxDistance, int numBounces, Collider3D<?>[] objectsInField) {
        super(new Vector3D(x, y, z)
                .unitVector()
                .scalarMultiply(marchDistance)
                .getComponents());
        this.start = start;
        position = start;
        this.maxDistance = maxDistance;
        this.numBounces = numBounces;
//        this.objectsInField = objectsInField;
        this.objectsInFieldList = new SinglyLinkedList<>(objectsInField);
        this.objectsInFieldParent = new SinglyLinkedList<>(objectsInField);
        init();
    }

    public LightRay(Vector3D start, Vector3D vector, double marchDistance,
                    double maxDistance, int numBounces, Collider3D<?>[] objectsInField) {
        super(vector
                .unitVector()
                .scalarMultiply(marchDistance)
                .getComponents());
        this.start = start;
        position = start;
        this.maxDistance = maxDistance;
        this.numBounces = numBounces;
//        this.objectsInField = objectsInField;
        this.objectsInFieldList = new SinglyLinkedList<>(objectsInField);
        this.objectsInFieldParent = new SinglyLinkedList<>(objectsInField);
        init();
    }

    public LightRay(Vector3D direction, LightRay ray, SinglyLinkedList<Collider3D<?>> objectsInFieldList) {
        super(direction
                .unitVector()
                .scalarMultiply(ray.magnitude())
                .getComponents());
        this.start = ray.position;
        position = start;
        this.maxDistance = ray.maxDistance;
        this.numBounces = ray.numBounces;
//        this.objectsInField = new Collider3D[0];//ray.objectsInField;
        this.objectsInFieldList = objectsInFieldList;
        objectsInFieldParent = null;
        init();
    }

    private void init() {
        actualMove = scalarMultiply(3);
//        actualMoveSize = actualMove.magnitude();
        if (/*actualMoveSize*/actualMove.magnitude() > 0.5) {
            actualMove = unitVector().scalarDivide(2);
        }
        checkMove = scalarMultiply(0.1);
//        actualMove = scalarMultiply(1);
//        checkMove = scalarMultiply(1);
//        actualMoveSize = actualMove.magnitude();
    }


    /*
     * Functionality:
     */

//    public Collider3D<?> firstCollision(Collider3D<?> previousCollider) {
//        Collider3D<?>[] potentialColliders = potentialColliders();
//
//        while (position.subtract(start).magnitude() < maxDistance) {
//            for (Collider3D<?> collider : potentialColliders) {
//                if (collider.inRange(position)) {
//                    if (distanceTraveled() > collider.getRange()) {
//                        while (collider.inRange(position)) {
//                            position = position.subtract(checkMove);
//                        }
//                        position = position.add(checkMove);
//                    }
//                    while(collider.inRange(position)) {
//                        position = position.add(checkMove);
//
//                        if (collider.contains(position)) {
////                            int numMoves = 0;
////                            while (!collider.contains(position) && numMoves < 100) {
////                                position = position.add(checkMove);
////                                numMoves++;
////                            }
////                            if (numMoves >= 100) {
////                                System.out.println("a");
////                            }
//                            position = position.subtract(checkMove);
//                            return collider;
//                        }
//                    }
//                }
//            }
//
//            position = position.add(actualMove);
//        }
//
//        return null;
//    }

//    public Collider3D<?> firstCollision(Collider3D<?> previousCollider) {
//        Collider3D<?>[] potentialColliders = potentialColliders();
//
//        while (position.subtract(start).magnitude() < maxDistance) {
//            for (Collider3D<?> collider : potentialColliders) {
//                if (collider.inRange(position)) {
//                    if (collider != previousCollider) {
//                        escapeRange(collider);
//                    }
//                    while (collider.inRange(position)) {
//                        for (Collider3D<?> colliderA : potentialColliders) {
//                            if (colliderA.contains(position)) {
//                                while(collider.contains(position)) {
//                                    position = position.subtract(checkMove.scalarDivide(2));
//                                }
//                                position = position.add(checkMove.scalarDivide(2));
//                                return colliderA;
//                            }
//                        }
//
//                        position = position.add(checkMove);
//                    }
//                }
//            }
//
//            position = position.add(actualMove);
//        }
//
//        return null;
//    }

//    public Collider3D<?> firstCollision1(Collider3D<?> previousCollider) {
//        List<Collider3D<?>> potentialColliders = new ArrayList(List.of(objectsInField));
//        SinglyLinkedList<Collider3D<?>> inRangeOf = new SinglyLinkedList<>();
//
//        if (previousCollider != null) {
//            inRangeOf.add(previousCollider);
//            potentialColliders.remove(previousCollider);
//        }
//
//        while (position.subtract(start).magnitude() < maxDistance) {
//            potentialColliders.removeIf(collider -> {
//                if (collider.inRange(position)) {
//                    inRangeOf.add(collider);
//                    return true;
//                }
//                return false;
//            });
//
////            Iterator<Collider3D<?>> iter = potentialColliders.iterator();
////            while (iter.hasNext()) {
////                Collider3D<?> collider = iter.next();
////                if (collider.inRange(position)) {
////                    inRangeOf.add(collider);
////                    iter.remove();
////                }
////            }
//
//            Collider3D<?> collision = updateCollisions(inRangeOf);
//
//            if (collision != null) {
//                return collision;
//            }
//        }
//
//        return null;
//    }

    //    private void escapeRange(Collider3D<?> collider) {
//        while (collider.inRange(position)) {
//            position = position.subtract(checkMove);
//        }
//        position = position.add(checkMove);
//    }
//
//    private double distanceTraveled() {
//        return Math.abs(position.subtract(start).magnitude());
//    }

//    public Color getColorFromBounces() {
//        Collider3D<?> collision = firstCollision();
//
//        if (collision == null) {
//            return Color.BLACK;
//        }
//        if (collision.getTexture().isLightSource()) {
//            return collision.getFromParent(Visual3D.class)
//                    .getAppearance().getColor();
//        }
//        color = color.add(new Vector3D(
//                collision.getFromParent(Visual3D.class)
//                        .getAppearance()
//                        .getColor()));
//
//        LightRay currentRay = new LightRay(collision.reflection(this), this, objectsInFieldParent);
//        double bounces = 2;
//
//        while (bounces < numBounces + 1) {
//            collision = currentRay.firstCollision();
//
//            if (collision == null) {
//                return Color.BLACK;
//            }
//
//            color = color.add(
//                    new Vector3D(collision.getFromParent(Visual3D.class)
//                            .getAppearance().getColor())
//                            .scalarDivide(bounces / 2 + 0.5));
//
//            if (collision.getTexture().isLightSource()) {
//                return color.toColor();
//            }
//
//            // Create a new LightRay with the reflection direction
//            currentRay = new LightRay(collision.reflection(currentRay), currentRay, objectsInFieldParent);
//
//            bounces++;
//        }
//
//        return Color.BLACK;
//    }

    public Color getColorFromBounces() {
        Collider3D<?> collision = firstCollision();

        if (collision == null) {
            return Color.BLACK;
        }
        if (collision.getTexture().isLightSource()) {
            return collision.getFromParent(Visual3D.class)
                    .getAppearance().getColor();
        }
        color = color.add(new Vector3D(
                collision.getFromParent(Visual3D.class)
                        .getAppearance()
                        .getColor()));

        LightRay currentRay = new LightRay(collision.reflection(this), this, objectsInFieldParent);
        double bounces = 2;

        while (bounces <= numBounces) {
            // First collision always the same, so just save it?
            collision = currentRay.firstCollision();

            if (collision == null) {
                return Color.BLACK;
            }

            color = color.add(
                    new Vector3D(collision.getFromParent(Visual3D.class)
                            .getAppearance().getColor())
                            .scalarDivide(bounces / 2 + 0.5)
                            /*.scalarDivide(
                                    collision.getTexture().getReflectivity())*/);

            if (collision.getTexture().isLightSource()) {
                return color.toColor();
            }

            // Create a new LightRay with the reflection direction
            currentRay = new LightRay(collision.reflection(currentRay), currentRay, objectsInFieldParent);

            bounces++;
        }

        return Color.BLACK;
    }

    public Collider3D<?> firstCollision() {
        SinglyLinkedList<Collider3D<?>> inRangeOf = new SinglyLinkedList<>();

        while (position.subtract(start).magnitude() < maxDistance) {
            SinglyLinkedList<Collider3D<?>>.Element element = objectsInFieldList.getPointer();
            while (element.hasNext()) {
                Collider3D<?> collider = element.getNext().getValue();
                if (collider.inRange(position)) {
                    inRangeOf.add(collider);
                    element.removeNext();
                } else {
                    element = element.getNext();
                }
            }

            Collider3D<?> collision = updateCollisions(inRangeOf);

            if (collision != null) {
                return collision;
            }
        }

        return null;
    }

    private Collider3D<?> updateCollisions(SinglyLinkedList<Collider3D<?>> colliders) {
        if (colliders.isEmpty()) {
            position = position.add(actualMove);
        } else {
            SinglyLinkedList<Collider3D<?>>.Element element = colliders.getPointer();
            while (element.hasNext()) {
                Collider3D<?> collider = element.getNext().getValue();
                if (collider.inRange(position)) {
                    if (collider.contains(position)) {
                        while(collider.contains(position)) {
                            position = position.subtract(checkMove);
                        }
                        return collider;
                    }
                    element = element.getNext();
                } else {
                    element.removeNext();
                }
            }
            position = position.add(checkMove);
        }

        return null;
    }

    public static double clamp(double num, double min, double max) {
        return Math.min(max, Math.max(min, num));
    }


    /*
     * Utilities:
     */

    public Color getColor() {
        return color.toColor();
    }

    public void setColor(Vector3D color) {
        this.color = color;
    }

    public Vector3D getPosition() {
        return position;
    }

}
