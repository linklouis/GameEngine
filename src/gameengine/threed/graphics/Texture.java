package gameengine.threed.graphics;

import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public abstract class Texture {
    private Color color;
    private boolean lightSource = false;
    private final double reflectivity;

    public Texture(Color color, boolean isLightSource, double reflectivity) {
        this.color = color;
        this.lightSource = isLightSource;
        this.reflectivity = reflectivity;
    }

    public abstract Vector3D reflection(Vector3D rayDirection, Vector3D surfaceNormal);

    public Color getColor() {
        return color;
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

    public double getReflectivity() {
        return reflectivity;
    }
}
