package gameengine.threed.graphics;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.skeletons.Visual;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;

import java.util.ArrayList;
import java.util.List;

public class Visual3D extends Visual<GraphicsObject3D> {
    private GraphicsObject3D appearance;

    /*
     * Construction:
     */

    public Visual3D() {
        super();
    }

    public Visual3D(Modifier... modifiers) {
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
                add(InPlane3D.class);
            }
        };
    }

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
                        new ModifierInstantiateParameter<>(
                                "appearance", GraphicsObject3D.class,
                                this::setAppearance)
                )
        };
    }


    /*
     * Utilities:
     */

    public GraphicsObject3D getAppearance() {
        return appearance;
    }

    public void setAppearance(GraphicsObject3D appearance) {
        this.appearance = appearance;
    }
}
