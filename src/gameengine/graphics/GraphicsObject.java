package gameengine.graphics;

import gameengine.objects.Modifier;
import javafx.scene.canvas.GraphicsContext;

public abstract class GraphicsObject extends Modifier {
    public GraphicsObject() {
        super();
    }

    public GraphicsObject(final Modifier[] modifiers) {
        super(modifiers);
    }

    public abstract void paint(GraphicsContext gc);
}
