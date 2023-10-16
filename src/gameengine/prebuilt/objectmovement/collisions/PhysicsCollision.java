package gameengine.prebuilt.objectmovement.collisions;

import gameengine.prebuilt.objectmovement.collisions.Collidable;
import gameengine.prebuilt.objectmovement.collisions.Collision;

public class PhysicsCollision extends Collision {

    protected PhysicsCollision(final Collidable physObj1, final Collidable physObj2) {
        super(physObj1, physObj2);
    }

    public static <T extends Collision> T newCollision(final Collidable physObj1, final Collidable physObj2) {
        return (T) new Collision(physObj1, physObj2);
    }

    @Override
    public String toString() {
        return "(" + this.getObj1() + ", " + this.getObj2() + ")";
    }
}

