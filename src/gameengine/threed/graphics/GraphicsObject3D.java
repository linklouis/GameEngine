package gameengine.threed.graphics;

import gameengine.skeletons.GraphicsObject;
import gameengine.skeletons.Modifier;
import gameengine.vectormath.Vector3D;

public abstract class GraphicsObject3D extends GraphicsObject {
    public GraphicsObject3D() {
        super();
    }

    public GraphicsObject3D(Modifier... modifiers) {
        super(modifiers);
    }

    public abstract Vector3D getCenter();
}
