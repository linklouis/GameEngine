package gameengine.twod.graphics;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.skeletons.Visual;
import gameengine.twod.prebuilt.objectmovement.InPlane;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class Visual2D extends Visual<GraphicsObject2D> {

    /*
     * Construction:
     */

    public Visual2D() {
        super();
    }

    public Visual2D(Modifier... modifiers) {
        super(modifiers);
    }

    public void instantiate(GameObject parent, Object... args) {
        try {
            super.instantiate(parent, args);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
                        new ModifierInstantiateParameter<>(
                                "appearance", GraphicsObject2D.class,
                                this::setAppearance)
                )
        };
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane.class);
            }
        };
    }

    public void paint(GraphicsContext gc) {
        getAppearance().paint(gc);
    }

}
