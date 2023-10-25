package gameengine.threed.graphics;

import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public class BaseTexture extends Texture {

    public BaseTexture(Color color, boolean isLightSource, double reflectivity) {
        super(color, isLightSource, reflectivity);
    }

    @Override
    public Vector3D reflection(Vector3D rayDirection, Vector3D surfaceNormal) {
        Vector3D reflection;
        surfaceNormal = surfaceNormal.unitVector();

        if (Math.random() > getReflectivity()) {
            reflection = Vector3D.random(-1, 1);
            if (reflection.dotProduct(surfaceNormal) < 0) {
                reflection = reflection.scalarMultiply(-1);
            }
            return reflection;
        } else {
            Vector3D incident = surfaceNormal.scalarMultiply(rayDirection.dotProduct(surfaceNormal));
            return rayDirection.subtract(incident.scalarMultiply(2));
        }
    }
}
