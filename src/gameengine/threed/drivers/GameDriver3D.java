package gameengine.threed.drivers;

import gameengine.skeletons.GameDriver;
import gameengine.skeletons.GameObject;
import gameengine.threed.graphics.Visual3D;
import gameengine.threed.prebuilt.objectmovement.physics.PhysicsEngine3D;
import gameengine.threed.prebuilt.objectmovement.physics.PhysicsObject3D;
import gameengine.utilities.Random;

public abstract class GameDriver3D extends GameDriver<GraphicsDriver3D, PhysicsEngine3D> {
    /*
     * Construction:
     */

    public GameDriver3D(GraphicsDriver3D graphicsDriver, PhysicsEngine3D physicsEngine) {
        super("New 3D Game", graphicsDriver, physicsEngine);
    }

    public GameDriver3D(String name, GraphicsDriver3D graphicsDriver, PhysicsEngine3D physicsEngine) {
        super(name, graphicsDriver, physicsEngine);
    }

    /*
     * Utilities:
     */

    public void newObject(GameObject object) {
        if (object.containsModifier(PhysicsObject3D.class)) {
            getPhysicsEngine().add(object.get(PhysicsObject3D.class));
        }
        if (object.containsModifier(Visual3D.class)) {
            getGraphicsDriver().add(object.get(Visual3D.class));
        }

        getObjects().add(object);
    }
}
