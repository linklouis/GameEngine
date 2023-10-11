import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public abstract class Visual extends Modifier {
    // TODO MAKE VISUAL AND COLLIDABLE NOT EXTEND INPLAIN. USE THE INPLAIN FROM THE PARENT. ALSO FIX VISUAL'S CONSTRUCTOR
    // TODO make a interface?
    private Color color;

    public Visual(GameObject parent) {
        super(parent);
    }

    public Visual(GameObject parent, Modifier[] modifiers) {
        super(parent, modifiers);
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlain.class);
            }
        };
    }

    @Override
    public void instantiate(Object... args) {
        if (args[0] instanceof Color) {
            color = (Color) args[0];
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public abstract void paint(GraphicsContext gc);
}
