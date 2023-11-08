package gameengine.threed.graphics.objectgraphics.textures;

import gameengine.threed.graphics.raytraceing.Ray;
import gameengine.threed.graphics.raytraceing.Reflection;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public abstract class RayTracingTexture {
    private Color color;
    private boolean lightSource;

    public RayTracingTexture(Color color, boolean isLightSource) {
        this.color = color;
        this.lightSource = isLightSource;
    }

    public abstract Reflection reflection(final Ray rayDirection,
                                          final Vector3D surfaceNormal);

    protected Vector3D scatterRay(Vector3D surfaceNormal) {
        Vector3D reflection = Vector3D.random(-1, 1);
        if (reflection.dotProduct(surfaceNormal) < 0) {
            return reflection.scalarMultiply(-1);
        }
        return reflection;
    }

    protected Vector3D reflectRay(final Vector3D rayDirection,
                                  final Vector3D surfaceNormal) {
        return rayDirection.subtract(
                surfaceNormal.scalarMultiply(
                        2 * rayDirection.dotProduct(surfaceNormal)
                                / Math.pow(surfaceNormal.magnitude(), 2)));
    }


    /*
     * Utilities:
     */

    public Color getColor() {
        return color;
    }

    public Vector3D colorVector() {
        return new Vector3D(color);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isLightSource() {
        return lightSource;
    }

    public void setLightSource(boolean lightSource) {
        this.lightSource = lightSource;
    }

    @Override
    public String toString() {
        return "RayTracingTexture: " + color + ", " + isLightSource();
    }
}
