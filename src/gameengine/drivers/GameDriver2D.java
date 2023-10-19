package gameengine.drivers;

import gameengine.skeletons.GameDriver;
import gameengine.skeletons.GameObject;
import gameengine.twod.graphics.GraphicsDriver2D;
import gameengine.twod.graphics.Visual2D;
import gameengine.twod.prebuilt.objectmovement.physics.PhysicsEngine2D;
import gameengine.twod.prebuilt.objectmovement.physics.PhysicsObject2D;

public abstract class GameDriver2D extends GameDriver<GraphicsDriver2D, PhysicsEngine2D> {
    /*
     * Construction:
     */

    public GameDriver2D(GraphicsDriver2D graphicsDriver, PhysicsEngine2D physicsEngine) {
        super("New 2D Game", graphicsDriver, physicsEngine);
    }

    public GameDriver2D(String name, GraphicsDriver2D graphicsDriver, PhysicsEngine2D physicsEngine) {
        super(name, graphicsDriver, physicsEngine);
    }

    /*
     * Utilities:
     */

    public void newObject(GameObject object) {
        if (object.containsModifier(PhysicsObject2D.class)) {
            getPhysicsEngine().add(object.get(PhysicsObject2D.class));
        }
        if (object.containsModifier(Visual2D.class)) {
            getGraphicsDriver().add(object.get(Visual2D.class));
        }

        getObjects().add(object);
    }
}
