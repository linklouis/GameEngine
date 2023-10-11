import java.util.ArrayList;
import java.util.List;

public final class Collision implements Comparable<Collision> {
    private static final List<Collision> COLLISIONS = new ArrayList<>();
    private final PhysicsObject obj1;
    private final PhysicsObject obj2;

    private Collision(final PhysicsObject physObj1, final PhysicsObject physObj2) {
        if (physObj1 == physObj2) {
            throw new IllegalArgumentException("Collision objects must be unique.");
        }
        if (!physObj1.getCollider().isColliding(physObj2.getCollider())) {
            throw new IllegalArgumentException("Collision objects must be colliding.");
        }
        this.obj1 = physObj1;
        this.obj2 = physObj2;
    }

    public boolean newCollision(final PhysicsObject physObj1, final PhysicsObject physObj2) {
        if (physObj1 == physObj2 || !physObj1.getCollider().isColliding(physObj2.getCollider())) {
            return false;
        }
        Collision collision = new Collision(physObj1, physObj2);
        for (Collision coll : COLLISIONS) {
            if (collision.compareTo(coll) == 0) {
                return false;
            }
        }
        COLLISIONS.add(collision);
        return true;
    }

    public boolean occurring() {
        return obj1.getCollider().isColliding(obj2) && obj2.getCollider().isColliding(obj1);
    }

    public void handle() {
        if (occurring()) {
            obj1.setVelocity(obj1.getVelocity().scalarMultiply(-1));
            obj2.setVelocity(obj2.getVelocity().scalarMultiply(-1));
            while (occurring()) {
                obj1.move(obj1.getVelocity().unitVector());
                obj2.move(obj2.getVelocity().unitVector());
            }
        }
    }

    public static void handleCollisions() {
        COLLISIONS.forEach(Collision::handle);
        COLLISIONS.clear();
    }

    public static boolean exists(final PhysicsObject physObj1, final PhysicsObject physObj2) {
        Collision toCheck = new Collision(physObj1, physObj2);
        for (Collision collision : COLLISIONS) {
            if (toCheck.compareTo(collision) == 0) {
                return true;
            }
        }
        return false;
    }

    public PhysicsObject getObj1() {
        return obj1;
    }

    public PhysicsObject getObj2() {
        return obj2;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure {@link Integer#signum
     * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
     * all {@code x} and {@code y}.  (This implies that {@code
     * x.compareTo(y)} must throw an exception if and only if {@code
     * y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code
     * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
     * == signum(y.compareTo(z))}, for all {@code z}.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     * @apiNote It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     */
    @Override
    public int compareTo(final  Collision o) {
        if ((this.getObj1() == o.getObj1() && this.getObj2() == o.getObj2())
                || (this.getObj1() == o.getObj2() && this.getObj2() == o.getObj1())) {
            return 0;
        }
        return 1;
    }
}
