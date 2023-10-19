package gameengine.twod.prebuilt.objectmovement.collisions;

public class PhysicsCollision extends Collision {

    protected PhysicsCollision(final LayerCollider2D physObj1, final LayerCollider2D physObj2) {
        super(physObj1, physObj2);
    }

    public static <T extends Collision> T newCollision(final LayerCollider2D physObj1, final LayerCollider2D physObj2) {
        return (T) new Collision(physObj1, physObj2);
    }

    @Override
    public String toString() {
        return "(" + this.getObj1() + ", " + this.getObj2() + ")";
    }
}

