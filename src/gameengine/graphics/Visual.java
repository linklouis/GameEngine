package gameengine.graphics;

import gameengine.objects.GameObject;
import gameengine.objects.Modifier;
import gameengine.prebuilt.objectmovement.InPlane;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class Visual extends Modifier {
    private GraphicsObject appearance; // TODO name better

    /*
     * Construction:
     */

    public Visual() {
        super();
    }

    public Visual(Modifier... modifiers) {
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
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane.class);
            }
        };
    }

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
                    new ModifierInstantiateParameter<>(
                        "appearance", GraphicsObject.class,
                            this::setAppearance)
                )
        };
    }

    public void paint(GraphicsContext gc) {
        getAppearance().paint(gc);
    }

    /*
     * Utilities:
     */

    public GraphicsObject getAppearance() {
        return appearance;
    }

    public void setAppearance(GraphicsObject appearance) {
        this.appearance = appearance;
    }
}
