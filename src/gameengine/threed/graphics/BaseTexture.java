package gameengine.threed.graphics;

import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public class BaseTexture extends Texture {
    private final double reflectivity;

    public BaseTexture(Color color, boolean isLightSource, double reflectivity) {
        super(color, isLightSource);
        this.reflectivity = reflectivity;
    }

    @Override
    public Vector3D reflection(Vector3D rayDirection, Vector3D surfaceNormal) {
        Vector3D reflection;

        if (Math.random() > reflectivity) {
            reflection = Vector3D.random(0, 1);
            if (reflection.dotProduct(surfaceNormal) < 0) {
                reflection = reflection.scalarMultiply(-1);
            }
            return reflection;
        } else {
            Vector3D incident = surfaceNormal.scalarMultiply(rayDirection.dotProduct(surfaceNormal));
            return rayDirection.subtract(incident.scalarMultiply(2));
        }
    }

    public double getReflectivity() {
        return reflectivity;
    }
}
