package gameengine.threed.graphics.raytraceing;

import gameengine.threed.graphics.Visual3D;
import gameengine.threed.prebuilt.objectmovement.collisions.Collider3D;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public class PixelRay {
    private static final Vector3D BLACK = new Vector3D(0);

    private final Ray startRay;

    private final double PRECISION_SCALE;

    private final double STEP_SIZE;
    private final int MAX_BOUNCES;
    private final int NUM_ITERATIONS;

    private final SinglyLinkedListAttribute objectsInFieldList;


    /*
     * Construction:
     */

    public PixelRay(Ray startRay, double precisionScale, int maxBounces,
                    int numIterations, double stepSize,
                    Collider3D<?>[] objectsInFieldList) {
        this.startRay = startRay;
        PRECISION_SCALE = precisionScale;
        this.MAX_BOUNCES = maxBounces;
        NUM_ITERATIONS = numIterations;
        STEP_SIZE = stepSize;
        this.objectsInFieldList =
                new SinglyLinkedListAttribute(objectsInFieldList);
    }


    /*
     * Functionality:
     */

    public Color getFinalColor() {
        Collider3D<?> collision = startRay
                .firstCollision(
                        objectsInFieldList,
                        PRECISION_SCALE);

        if (collision == null) {
            return Color.BLACK;
        }
        if (collision.getTexture().isLightSource()) {
            return collision.getAppearance().getColor();
        }

        Vector3D startColor = new Vector3D(
                collision.getFromParent(Visual3D.class)
                        .getAppearance()
                        .getColor());

        Vector3D averageColor = new Vector3D(0);

        for (int i = 0; i < NUM_ITERATIONS; i++) {
            averageColor = averageColor.add(
                    getColorFromBounces(
                            new Ray(
                                    startRay.getPosition(),
                                    collision.reflection(startRay)
                                            .atMagnitude(STEP_SIZE)
                            ),
                            startColor));
        }

        return averageColor.scalarDivide(NUM_ITERATIONS).toColor();
    }

    public Vector3D getColorFromBounces(Ray currentRay, Vector3D color) {
        Collider3D<?> collision;

        for (double bounces = 2; bounces <= MAX_BOUNCES; bounces++) {
            collision = currentRay.firstCollision(
                    objectsInFieldList, PRECISION_SCALE);

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

    public Vector3D getColor(Collider3D<?> collision, double bounces) {
        return new Vector3D(collision.getAppearance().getColor())
                .scalarDivide(/*2 / */bounces / 2/* + 0.5*/);
    }

    public static double clamp(double num, double min, double max) {
        return Math.min(max, Math.max(min, num));
    }
}
