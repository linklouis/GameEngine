package gameengine.drivers;

import gameengine.skeletons.GameDriver;
import gameengine.skeletons.GameObject;
import gameengine.threed.graphics.GraphicsDriver3D;
import gameengine.threed.graphics.Visual3D;

public abstract class GameDriver3D extends GameDriver<GraphicsDriver3D> {
    /*
     * Construction:
     */

    public GameDriver3D(GraphicsDriver3D graphicsDriver) {
        super("New 3D Game", graphicsDriver);
    }

    public GameDriver3D(String name, GraphicsDriver3D graphicsDriver) {
        super(name, graphicsDriver);
    }

    /*
     * Utilities:
     */

    public void newObject(GameObject object) {
        if (object.containsModifier(Visual3D.class)) {
            getGraphicsDriver().add(object.get(Visual3D.class));
        }

        getObjects().add(object);
    }
}
