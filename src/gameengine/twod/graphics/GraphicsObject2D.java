package gameengine.twod.graphics;

import gameengine.skeletons.GraphicsObject;
import gameengine.skeletons.Modifier;
import javafx.scene.canvas.GraphicsContext;

public abstract class GraphicsObject2D extends GraphicsObject {
    public GraphicsObject2D() {
        super();
    }

    public GraphicsObject2D(final Modifier[] modifiers) {
        super(modifiers);
    }

    public abstract void paint(GraphicsContext gc);
}
