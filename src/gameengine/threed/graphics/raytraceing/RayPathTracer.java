package gameengine.threed.graphics.raytraceing;

import gameengine.threed.graphics.objectgraphics.skeletons.RayTraceable;
import gameengine.vectormath.Vector3D;

import java.util.LinkedHashMap;

public final class RayPathTracer {
    /**
     * A precalculated {@link Vector3D} representing the color black in rgb.
     */
    private static final Vector3D BLACK = new Vector3D(0);

    private RayPathTracer() {
    }

    /**
     * Finds the color of a single {@link Ray} over the course of all it's
     * reflections.
     *
     * @param currentRay The initial {@link Ray} who's path to trace.
     * @return The color of {@code currentRay} after all of its reflections.
     */
    public static Vector3D getColor(final Ray currentRay,
                                    final int maxBounces,
                                    final RayIntersectableList objectsInField) {
        RayTraceable collision;

        for (int bounces = 2; bounces <= maxBounces; bounces++) {
            collision = (RayTraceable) currentRay.firstCollision(objectsInField);

            if (collision == null) {
                return BLACK;
            }

            currentRay.reflect(collision, bounces);

            if (collision.getTexture().isLightSource()) {
                return currentRay.getColor();
            }
        }

        return BLACK;
    }

    public static LinkedHashMap<RayTraceable, Ray> getCollisions(final Ray currentRay,
                                                                 final int maxBounces,
                                                                 final RayIntersectableList objectsInField) {
        LinkedHashMap<RayTraceable, Ray> map = new LinkedHashMap<>(maxBounces);
        RayTraceable collision;

        for (int bounces = 2; bounces <= maxBounces; bounces++) {
            collision = (RayTraceable) currentRay.firstCollision(objectsInField);

            if (collision == null) {
                return null;
            }

            map.put(collision, new Ray(currentRay));
            currentRay.reflect(collision, bounces);

            if (collision.getTexture().isLightSource()) {
                return map;
            }
        }

        return map;
    }
}
