package gameengine.prebuilt.physics;

import gameengine.objects.Modifier;

import java.util.ArrayList;
import java.util.List;

public abstract class CollisionHandler extends Modifier {

    public CollisionHandler() {
        super();
    }

    public CollisionHandler(Modifier[] modifiers) {
        super(modifiers);
    }

    public abstract void handle(Collision collision, Collidable[] otherColliders);

//    @Override
//    public List<Class<? extends Modifier>> getDependencies() {
//        return new ArrayList<>();
//    }
}
