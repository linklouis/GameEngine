package gameengine.threed.graphics;

import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public abstract class Texture {
    private Color color;
    private boolean lightSource = false;

    public Texture(Color color, boolean isLightSource) {
        this.color = color;
        this.lightSource = isLightSource;
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
}
