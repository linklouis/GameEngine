package gameengine.threed.prebuilt.objectmovement.physics;

import gameengine.skeletons.Modifier;
import gameengine.utilities.ArgumentContext;

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
