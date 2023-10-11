import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public abstract class Visual extends Modifier {
    // TODO MAKE VISUAL AND COLLIDABLE NOT EXTEND INPLAIN. USE THE INPLAIN FROM THE PARENT. ALSO FIX VISUAL'S CONSTRUCTOR
    // TODO make a interface?

    public Visual() {
        super();
    }

    public Visual(Modifier[] modifiers) {
        super(modifiers);
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane.class);
            }
        };
    }

    public abstract void paint(GraphicsContext gc);
}
