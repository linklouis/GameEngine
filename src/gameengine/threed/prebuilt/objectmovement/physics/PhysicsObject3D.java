package gameengine.threed.prebuilt.objectmovement.physics;

import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.GraphicsObject3D;
import gameengine.threed.graphics.raytraceing.Ray;
import gameengine.threed.prebuilt.objectmovement.collisions.Collider3D;
import gameengine.utilities.ArgumentContext;
import gameengine.vectormath.Vector3D;

import java.util.List;

public class PhysicsObject3D extends Modifier {
    //TODO
    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return null;
    }

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[0];
    }
}
