package gameengine.threed.graphics.raytraceing;

import gameengine.threed.prebuilt.objectmovement.collisions.Collider3D;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public final class PixelRayStatic {
    private static final Vector3D BLACK = new Vector3D(0);

    private PixelRayStatic() {

    }

    public static Color getFinalColor(Ray startRay, int maxBounces,
                                      int numIterations,
                                      Collider3DList objectsInField) {
        Collider3D<?> firstCollision = startRay.firstCollision(objectsInField);

        if (firstCollision == null) {
            return Color.BLACK;
        }
        if (firstCollision.getTexture().isLightSource()) {
            return firstCollision.getAppearance().getColor();
        }

        Vector3D startColor = new Vector3D(
                firstCollision.getAppearance().getColor());

        Vector3D averageColor = new Vector3D(0);

        for (int i = 0; i < numIterations; i++) {
            averageColor.addMutable(
                    getColorFromBounces(
                            new Ray(startRay.getPosition(),
                                    firstCollision.reflection(startRay)),
                            startColor, objectsInField, maxBounces));
        }

        return averageColor.scalarDivide(numIterations).toColor();
    }

//    private static Color getAllCollisions(Collider3D<?> firstCollision,
//                                          Ray startRay,
//                                          Collider3DList objectsInField,
//                                          int numIterations,
//                                          int maxBounces) {
//        Vector3D startColor = new Vector3D(
//                firstCollision.getAppearance().getColor());
//
//        Vector3D averageColor = new Vector3D(0);
//
//        for (int i = 0; i < numIterations; i++) {
//            averageColor.addMutable(
//                    getColorFromBounces(
//                            new Ray(startRay.getPosition(),
//                                    firstCollision.reflection(startRay)),
//                            startColor, objectsInField, maxBounces));
//        }
//
//        return averageColor.scalarDivide(numIterations).toColor();
//    }

    public static Vector3D getColorFromBounces(Ray currentRay, Vector3D color, Collider3DList objectsInField, int maxBounces) {
        Collider3D<?> collision;
        color = new Vector3D(color);

        for (double bounces = 2; bounces <= maxBounces; bounces++) {
            collision = currentRay.firstCollision(objectsInField);

            if (collision == null) {
                return BLACK;
            }

            color.addMutable(new Vector3D(collision.getAppearance().getColor())
                    .scalarDivide(bounces / 2));

            if (collision.getTexture().isLightSource()) {
                return color;
            }

            currentRay = new Ray(currentRay.getPosition(),
                    collision.reflection(currentRay));
        }

        return BLACK;
    }
}
