package gameengine.threed.graphics;

import gameengine.skeletons.GraphicsObject;
import gameengine.skeletons.Modifier;
import javafx.scene.paint.Color;

public abstract class GraphicsObject3D extends GraphicsObject {
    private Color color;

    /*
     * Construction:
     */

    public GraphicsObject3D() {
        super();
    }

    public GraphicsObject3D(Modifier... modifiers) {
        super(modifiers);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
