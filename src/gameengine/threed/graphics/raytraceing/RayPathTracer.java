package gameengine.threed.graphics.raytraceing;

import gameengine.threed.geometry.Ray;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayIntersectableList;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

import java.util.LinkedHashMap;

public final class RayPathTracer {
    /**
     * A precalculated {@link Vector3D} representing the color black in rgb.
     */
    private static final Vector3D BLACK = new Vector3D(0);

    private RayPathTracer() {
    }

    /**
     * Finds the color of a single {@link LightRay} over the course of all it's
     * reflections.
     *
     * @param currentLightRay The initial {@link LightRay} who's path to trace.
     * @param startingBounce
     * @return The color of {@code currentLightRay} after all of its reflections.
     */
    public static Vector3D getColor(final LightRay currentLightRay,
                                    final int maxBounces,
                                    final RayIntersectableList objectsInField, int startingBounce) {
        RayTraceable collision;

        for (int bounces = startingBounce; bounces <= maxBounces; bounces++) {
            collision = (RayTraceable) currentLightRay.firstCollision(objectsInField);

            if (collision == null) {
                return currentLightRay.getIncomingLight();//.add(new Vector3D(Color.SKYBLUE));
            }

            currentLightRay.reflect(collision);

            if (collision.getTexture().getColor().equals(Color.BLACK)) {
                return currentLightRay.getIncomingLight();
            }

//            if (collision.getTexture().isLightSource()) {
//                return currentLightRay.getColor();
//            }
        }

        return currentLightRay.getIncomingLight();
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
            currentRay.reflect(collision);

//            if (collision.getTexture().isLightSource()) {
//                return map;
//            }
        }

        return map;
    }
}
