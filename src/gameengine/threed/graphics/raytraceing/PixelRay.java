package gameengine.threed.graphics.raytraceing;

import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

/**
 * A manager for all {@link Ray}s tested for a single pixel in a
 * {@link RayTracedCamera}'s image.
 *
 * @author Louis Link
 * @since 1.0
 */
public class PixelRay {
    /**
     * A precalculated {@link Vector3D} representing the color black in rgb.
     */
    private static final Vector3D BLACK = new Vector3D(0);

    /**
     * The initial {@link Ray} shot from the {@link RayTracedCamera}.
     */
    private final Ray startRay;
    /**
     * The maximum number of times each {@code Ray} can bounce before declaring
     * that they never hit a light.
     */
    private final int maxBounces;
    /**
     * The number of rays averaged to find the pixel's color.
     */
    private final int numIterations;

    /**
     * A {@link RayTraceableList} of the {@link Collider3D}s to consider for the
     * scene.
     */
    private final RayTraceableList objectsInField;


    /*
     * Construction:
     */

    public PixelRay(final Ray startRay, final int maxBounces,
                    final int numIterations,
                    final RayTraceableList objectsInField) {
        this.startRay = startRay;
        this.maxBounces = maxBounces;
        this.numIterations = numIterations;
        this.objectsInField = objectsInField;
    }


    /*
     * Functionality:
     */

    /**
     * @return The average color of each ray to measure based on
     * {@link #numIterations}.
     */
    public Color getFinalColor() {
        RayTraceable firstCollision = startRay.firstCollision(objectsInField);

        if (firstCollision == null) {
            return Color.BLACK;
        }
        if (firstCollision.getTexture().isLightSource()) {
            return firstCollision.getColor();
        }

        return getAllCollisions(firstCollision);
    }

    private Color getAllCollisions(final RayTraceable firstCollision) {
        Vector3D startColor = new Vector3D(
                firstCollision.getColor());

        Vector3D averageColor = new Vector3D(0);

        for (int i = 0; i < numIterations; i++) {
            averageColor.addMutable(
                    getColorFromBounces(
                            new Ray(startRay.getPosition(),
                                    firstCollision.reflection(startRay)),
                            startColor));
        }

        return averageColor.scalarDivide(numIterations).toColor();
    }

    /**
     * Finds the color of a single {@link Ray} over the course of all it's
     * reflections.
     *
     * @param currentRay The initial {@link Ray} who's path to trace.
     * @param color The color of any {@link Ray}s that came before
     *              {@code currentRay}.
     * @return The color of {@code currentRay} after all of its reflections.
     */
    public Vector3D getColorFromBounces(final Ray currentRay, Vector3D color) {
        RayTraceable collision;
        color = new Vector3D(color);

        for (double bounces = 2; bounces <= maxBounces; bounces++) {
            collision = currentRay.firstCollision(objectsInField);

            if (collision == null) {
                return BLACK;
            }

            color.addMutable(new Vector3D(collision.getColor())
                    .scalarDivide(bounces / 2));

            if (collision.getTexture().isLightSource()) {
                return color;
            }

            currentRay.reflect(collision);
        }

        return BLACK;
    }
}
