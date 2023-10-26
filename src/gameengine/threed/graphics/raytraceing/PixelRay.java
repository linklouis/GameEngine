package gameengine.threed.graphics.raytraceing;

import gameengine.threed.prebuilt.objectmovement.collisions.Collider3D;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public class PixelRay {
    private static final Vector3D BLACK = new Vector3D(0);

    private final Ray startRay;
    private final int MAX_BOUNCES;
    private final int NUM_ITERATIONS;

    private final Collider3DList objectsInField;


    /*
     * Construction:
     */

    public PixelRay(Ray startRay, int maxBounces,
                    int numIterations,
                    Collider3DList objectsInField) {
        this.startRay = startRay;
        this.MAX_BOUNCES = maxBounces;
        NUM_ITERATIONS = numIterations;
        this.objectsInField = objectsInField;
    }


    /*
     * Functionality:
     */

    public Color getFinalColor() {
        Collider3D<?> firstCollision = startRay.firstCollision(objectsInField);

        if (firstCollision == null) {
            return Color.BLACK;
        }
        if (firstCollision.getTexture().isLightSource()) {
            return firstCollision.getAppearance().getColor();
        }

        return getAllCollisions(firstCollision);
    }

    private Color getAllCollisions(Collider3D<?> firstCollision) {
        Vector3D startColor = new Vector3D(
                firstCollision.getAppearance().getColor());

        Vector3D averageColor = new Vector3D(0);

        for (int i = 0; i < NUM_ITERATIONS; i++) {
//            averageColor = averageColor.add(
            averageColor.addMutable(
                    getColorFromBounces(
                            new Ray(startRay.getPosition(),
                                    firstCollision.reflection(startRay)),
                            startColor));
        }

        return averageColor.scalarDivide(NUM_ITERATIONS).toColor();
    }

    public Vector3D getColorFromBounces(Ray currentRay, Vector3D color) {
        Collider3D<?> collision;
        color = new Vector3D(color);

        for (double bounces = 2; bounces <= MAX_BOUNCES; bounces++) {
            collision = currentRay.firstCollision(objectsInField);

            if (collision == null) {
                return BLACK;
            }

            color.addMutable(new Vector3D(collision.getAppearance().getColor())
                    .scalarDivide(bounces / 2));
//            color = color.add(
//                    new Vector3D(collision.getAppearance().getColor())
//                            .scalarDivide(/*2 / */bounces / 2/* + 0.5*/));

            if (collision.getTexture().isLightSource()) {
                return color/*.scalarDivide(bounces*//* / 2*//*)*/;
            }

            currentRay = new Ray(currentRay.getPosition(),
                    collision.reflection(currentRay));
        }

        return BLACK;
    }
}
