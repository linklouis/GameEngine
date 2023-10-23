package gameengine.threed.graphics.raytraceing;

import gameengine.threed.graphics.Visual3D;
import gameengine.threed.prebuilt.objectmovement.collisions.Collider3D;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public class LightRay {
    private static Vector3D BLACK = new Vector3D(0);

    private final Ray startRay;

    private final double PRECISION_SCALE;
    private final double MAX_DISTANCE;
    private final int MAX_BOUNCES;
    private final int NUM_ITERATIONS;
    private final double STEP_SIZE;

    private final SinglyLinkedListAttribute objectsInFieldList;


    /*
     * Construction:
     */

    public LightRay(Ray startRay, double precisionScale, double maxDistance, int maxBounces, int numIterations, double stepSize, Collider3D<?>[] objectsInFieldList) {
        this.startRay = startRay;
        PRECISION_SCALE = precisionScale;
        this.MAX_DISTANCE = maxDistance;
        this.MAX_BOUNCES = maxBounces;
        NUM_ITERATIONS = numIterations;
        STEP_SIZE = stepSize;
        this.objectsInFieldList = new SinglyLinkedListAttribute(objectsInFieldList);
    }


    /*
     * Functionality:
     */

    public Color getFinalColor() {
        Collider3D<?> collision = startRay.firstCollision(objectsInFieldList, PRECISION_SCALE, MAX_DISTANCE);
        if (collision == null) {
            return Color.BLACK;
        }
        if (collision.getTexture().isLightSource()) {
            return collision.getFromParent(Visual3D.class)
                    .getAppearance().getColor();
        }

        Vector3D startColor = new Vector3D(
                collision.getFromParent(Visual3D.class)
                        .getAppearance()
                        .getColor());

        Vector3D averageColor = new Vector3D(0);

//        int num = 0;


        for (int i = 0; i < NUM_ITERATIONS; i++) {
            averageColor = averageColor.add(getColorFromBounces(new Ray(startRay.getPosition(), collision.reflection(startRay).atMagnitude(STEP_SIZE)), startColor));
//            Vector3D newColor = getColorFromBounces(new Ray(startRay.getPosition(), collision.reflection(startRay).atMagnitude(STEP_SIZE)), startColor);
//            if (newColor.magnitude() != 0) {
//                average = average.add(newColor);
//                num++;
//            } else {
//                num += 2;
//            }
//            if (newColor.magnitude() != 0) {
//                averageColor = averageColor.add(newColor.scalarMultiply(2));
//                num += 2;
//            } else {
//                averageColor = averageColor.add(newColor);
//                num++;
//            }
        }

        return averageColor.scalarDivide(NUM_ITERATIONS/*num*/).toColor();
    }

//    public Color getFinalColor() {
//        return getColorFromBounces();
//    }

//    public Vector3D getColorFromBounces() {
//        Collider3D<?> collision = startRay.firstCollision(objectsInFieldList, PRECISION_SCALE, MAX_DISTANCE);
//        Vector3D color = BLACK;
//
//        if (collision == null) {
//            return BLACK;
//        }
//
//        if (collision.getTexture().isLightSource()) {
//            return new Vector3D(collision.getFromParent(Visual3D.class)
//                    .getAppearance().getColor());
//        }
//
//        color = color.add(new Vector3D(
//                collision.getFromParent(Visual3D.class)
//                        .getAppearance()
//                        .getColor()));
//
//        Ray currentRay = new Ray(collision.reflection(startRay), startRay.getPosition());
//        double bounces = 2;
//
//        while (bounces <= MAX_BOUNCES) {
//            collision = currentRay.firstCollision(objectsInFieldList, PRECISION_SCALE, MAX_DISTANCE);
//
//            if (collision == null) {
//                return BLACK;
//            }
//
//            color = color.add(
//                    new Vector3D(collision.getFromParent(Visual3D.class)
//                            .getAppearance().getColor())
//                            .scalarDivide(bounces / 2 + 0.5)
//                            /*.scalarDivide(
//                                    collision.getTexture().getReflectivity())*/);
//
//            if (collision.getTexture().isLightSource()) {
//                return color;
//            }
//
//            // Create a new LightRay with the reflection direction
//            currentRay = new Ray(collision.reflection(currentRay), currentRay.getPosition());
//
//            bounces++;
//        }
//
//        return BLACK;
//    }

    public Vector3D getColorFromBounces(Ray currentRay, Vector3D color) {
        Collider3D<?> collision;

        for (double bounces = 2; bounces <= MAX_BOUNCES; bounces++) {
            collision = currentRay.firstCollision(
                    objectsInFieldList, PRECISION_SCALE, MAX_DISTANCE);

            if (collision == null) {
                return BLACK;
            }

            color = color.add(getColor(collision, bounces));

            if (collision.getTexture().isLightSource()) {
                return color/*.scalarDivide(bounces*//* / 2*//*)*/;
            }

            currentRay = new Ray(currentRay.getPosition(),
                    collision.reflection(currentRay).atMagnitude(STEP_SIZE));
        }

        return BLACK;
    }

    public Vector3D getColorFromBounces1(Ray currentRay, Vector3D color) {
        double bounces = 2;
        Collider3D<?> collision;// = startRay.firstCollision(objectsInFieldList, PRECISION_SCALE, MAX_DISTANCE);
//
//        if (collision == null) {
//            return BLACK;
//        }
//
//        color = color.add(getColor(collision, bounces));
//
//        if (collision.getTexture().isLightSource()) {
//            return color;
//        }

//        Ray currentRay =
//                new Ray(startRay.getPosition(), collision.reflection(startRay).atMagnitude(STEP_SIZE));
//        bounces++;

        while (bounces <= MAX_BOUNCES) {
            collision =
                    currentRay.firstCollision(objectsInFieldList, PRECISION_SCALE, MAX_DISTANCE);

            if (collision == null) {
                return BLACK;
            }

            color = color.add(getColor(collision, bounces));

            if (collision.getTexture().isLightSource()) {
                return color;
            }

            // Create a new Ray with the reflection direction
            currentRay = new Ray(currentRay.getPosition(), collision.reflection(currentRay).atMagnitude(STEP_SIZE));
//            currentRay = new Ray(startRay.getPosition(), collision.reflection(startRay).atMagnitude(STEP_SIZE));

            bounces++;
        }

        return BLACK;
    }

    public Vector3D getColor(Collider3D<?> collision, double bounces) {
        return new Vector3D(collision.getFromParent(Visual3D.class)
                .getAppearance().getColor())
                .scalarDivide(/*2 / */bounces / 2/* + 0.5*/);
    }

    public Vector3D getColorFromBounces() {
        Collider3D<?> collision = startRay.firstCollision(objectsInFieldList, PRECISION_SCALE, MAX_DISTANCE);

        if (collision == null) {
            return BLACK;
        }

        Vector3D color = new Vector3D(collision.getFromParent(Visual3D.class)
                        .getAppearance().getColor());

        if (collision.getTexture().isLightSource()) {
            return color;
        }

        Ray currentRay =
                new Ray(startRay.getPosition(), collision.reflection(startRay).atMagnitude(STEP_SIZE));

        double bounces = 2;

        while (bounces <= MAX_BOUNCES) {
            collision =
                    currentRay.firstCollision(objectsInFieldList, PRECISION_SCALE, MAX_DISTANCE);

            if (collision == null) {
                return BLACK;
            }

            color = color.add(
                    new Vector3D(collision.getFromParent(Visual3D.class)
                            .getAppearance().getColor())
                            .scalarDivide(bounces/* / 2 + 0.5*/));

            if (collision.getTexture().isLightSource()) {
                return color;
            }

            // Create a new LightRay with the reflection direction
            currentRay = new Ray(startRay.getPosition(), collision.reflection(startRay).atMagnitude(STEP_SIZE));

            bounces++;
        }

        return BLACK;
    }



//    public Color getColorFromBounces(Vector3D firstColor) {
//        color = firstColor;
//        double bounces = 2;
//        Collider3D<?> collision = startRay.firstCollision(objectsInFieldList, PRECISION_SCALE, MAX_DISTANCE);
//
//        if (collision == null) {
//            return Color.BLACK;
//        }
//        color = color.add(
//                new Vector3D(collision.getFromParent(Visual3D.class)
//                        .getAppearance().getColor())
//                        .scalarDivide(bounces / 2 + 0.5));
//        if (collision.getTexture().isLightSource()) {
//            return color.toColor();
//        }
//
//        LightRay currentRay = new LightRay(collision.reflection(this), precisionScale, this,/*, objectsInFieldList*/MAX_BOUNCES, objectsInFieldList);
//        bounces++;
//
//        while (bounces <= MAX_BOUNCES) {
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
//            currentRay = new LightRay(collision.reflection(currentRay), precisionScale, currentRay,/*, objectsInFieldList*/MAX_BOUNCES, objectsInFieldList);
//
//            bounces++;
//        }
//
//        return Color.BLACK;
//    }
//
//    private Collider3D<?> updateCollisions(SinglyLinkedListAttribute colliders) {
//        if (colliders.isEmpty()) {
//            position = position.add(actualMove);
//        } else {
//            SinglyLinkedListAttribute.Element element = colliders.getPointer();
//            while (element.hasNext()) {
//                Collider3D<?> collider = element.getNext().getValue();
//                if (collider.inRange(position)) {
//                    if (collider.contains(position)) {
//                        while(collider.contains(position)) {
//                            position = position.subtract(checkMove);
//                        }
//                        return collider;
//                    }
//                    element = element.getNext();
//                } else {
//                    element.removeNext();
//                }
//            }
//            position = position.add(checkMove);
//        }
//
//        return null;
//    }

    public static double clamp(double num, double min, double max) {
        return Math.min(max, Math.max(min, num));
    }
}
