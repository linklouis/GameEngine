package gameengine.threed.graphics.raytraceing;

import gameengine.threed.graphics.raytraceing.objectgraphics.RayIntersectableList;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public final class PixelRayStatic {
    private static final Vector3D BLACK = new Vector3D(0);

    private PixelRayStatic() {

    }

//    public Color getFinalColor(final LightRay startRay,
//                               final int maxBounces,
//                               final int numIterations,
//                               final RayIntersectableList objectsInField) {
//        RayTraceable firstCollision = startRay.firstCollision(objectsInField);
//
//        if (firstCollision == null) {
//            return Color.BLACK;
//        }
//        if (firstCollision.getTexture().isLightSource()) {
//            return firstCollision.getColor();
//        }
//
//        return getAllCollisions(startRay, firstCollision, maxBounces,
//                numIterations, objectsInField);
//    }
//
//    private Color getAllCollisions(final LightRay startRay,
//                                   final RayTraceable firstCollision,
//                                   final int maxBounces,
//                                   final int numIterations,
//                                   final RayIntersectableList objectsInField) {
//        Vector3D startColor = new Vector3D(
//                firstCollision.getColor());
//
//        Vector3D averageColor = new Vector3D(0);
//
//        for (int i = 0; i < numIterations; i++) {
//            averageColor.addMutable(
//                    getColorFromBounces(
//                            new LightRay(startRay.getPosition(),
//                                    firstCollision.reflection(startRay)),
//                            maxBounces,
//                            objectsInField,
//                            startColor));
//        }
//
//        return averageColor.scalarDivide(numIterations).toColor();
//    }

    public static Color getFinalColor(final LightRay startLightRay,
                                      final int maxBounces,
                                      final int numIterations,
                                      final RayIntersectableList objectsInField) {
        RayTraceable firstCollision = startLightRay.firstCollision(objectsInField);

        if (firstCollision == null) {
            return Color.BLACK;
        }
        if (firstCollision.getTexture().isLightSource()) {
            return firstCollision.getColor();
        }

        Vector3D averageColor = new Vector3D(0);
        for (int i = 0; i < numIterations; i++) {
            averageColor.addMutable(getColorFromBounces(
                    new LightRay(startLightRay.getPosition(),
                            firstCollision.reflection(startLightRay).direction()),
                    maxBounces,
                    objectsInField,
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
    public static Vector3D getColorFromBounces(final LightRay currentLightRay,
                                               final int maxBounces,
                                               final RayIntersectableList objectsInField,
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
}
