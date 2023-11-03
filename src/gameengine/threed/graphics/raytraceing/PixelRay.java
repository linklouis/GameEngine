package gameengine.threed.graphics.raytraceing;

import gameengine.threed.graphics.raytraceing.objectgraphics.RayIntersectableList;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

/**
 * A manager for all {@link LightRay}s tested for a single pixel in a
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
     * The initial {@link LightRay} shot from the {@link RayTracedCamera}.
     */
    private final LightRay startLightRay;
    /**
     * The maximum number of times each {@code LightRay} can bounce before declaring
     * that they never hit a light.
     */
    private final int maxBounces;
    /**
     * The number of rays averaged to find the pixel's color.
     */
    private final int numIterations;

    /**
     * A {@link RayIntersectableList} of the {@link RayTraceable}s to consider for the
     * scene.
     */
    private final RayIntersectableList objectsInField;


    /*
     * Construction:
     */

    public PixelRay(final LightRay startLightRay, final int maxBounces,
                    final int numIterations,
                    final RayIntersectableList objectsInField) {
        this.startLightRay = startLightRay;
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
        RayTraceable firstCollision = startLightRay.firstCollision(objectsInField);

        if (firstCollision == null) {
            return Color.BLACK;
        }
        if (firstCollision.getTexture().isLightSource()) {
            return firstCollision.getColor();
        }

        Vector3D averageColor = new Vector3D(0);
        for (int i = 0; i < numIterations; i++) {
            averageColor.addMutable(
                    getColorFromBounces(
                            startLightRay.getReflected(firstCollision, 1),
                            firstCollision.getColor()));
        }

        return averageColor.scalarDivide(numIterations).toColor();
    }

    /**
     * Finds the color of a single {@link LightRay} over the course of all it's
     * reflections.
     *
     * @param currentLightRay The initial {@link LightRay} who's path to trace.
     * @param col The color of any {@link LightRay}s that came before
     *              {@code currentLightRay}.
     * @return The color of {@code currentLightRay} after all of its reflections.
     */
    public Vector3D getColorFromBounces(final LightRay currentLightRay,
                                        final Color col) {
        RayTraceable collision;
        Vector3D color = new Vector3D(col);

        for (double bounces = 2; bounces <= maxBounces; bounces++) {
            collision = currentLightRay.firstCollision(objectsInField);

            if (collision == null) {
                return BLACK;
            }

            color.addMutable(new Vector3D(collision.getColor())
                    .scalarDivide(bounces / 2));

            if (collision.getTexture().isLightSource()) {
                return color;
            }

            currentLightRay.reflect(collision, (int) bounces);
        }

        return BLACK;
    }

    public static double generatePseudoRandomDouble(double x, double y, double z) {
//        long xBits = Double.doubleToLongBits(x);
//        long yBits = Double.doubleToLongBits(y);
//        long zBits = Double.doubleToLongBits(z);
//
//        long resultBits = (xBits ^ yBits ^ zBits);
//
//        return Double.longBitsToDouble(resultBits);
        // Combine the three input values into a single long seed
        long combinedSeed = Double.doubleToLongBits(x) ^
                Double.doubleToLongBits(y) ^
                Double.doubleToLongBits(z);

        // Apply LCG to generate a random long value
//        combinedSeed = (combinedSeed ^ (combinedSeed >> 32)) * 6364136223846793005L + 1L;

        // Convert the long value to a double in the range [0, 1)
        return (double) ((combinedSeed ^ (combinedSeed >> 32)) * 6364136223846793005L + 1L >>> 11) / (1L << 53);
    }
}
