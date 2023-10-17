package gameengine.prebuilt.objectmovement.collisions;

public class PhysicsCollision extends Collision {

    protected PhysicsCollision(final LayerCollider physObj1, final LayerCollider physObj2) {
        super(physObj1, physObj2);
    }

    public static <T extends Collision> T newCollision(final LayerCollider physObj1, final LayerCollider physObj2) {
        return (T) new Collision(physObj1, physObj2);
    }

    @Override
    public String toString() {
        return "(" + this.getObj1() + ", " + this.getObj2() + ")";
    }
}

