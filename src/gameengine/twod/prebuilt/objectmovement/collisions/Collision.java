package gameengine.twod.prebuilt.objectmovement.collisions;

public class Collision implements Comparable<Collision> {
    private final LayerCollider2D obj1;
    private final LayerCollider2D obj2;
    private boolean handled = false;

    protected Collision(final LayerCollider2D physObj1, final LayerCollider2D physObj2) {
        if (physObj1 == physObj2) {
            throw new IllegalArgumentException("gameengine.twod.prebuilt.objectmovement.physics.collisions.Collision objects must be unique.");
        }
        if (!physObj1.isColliding(physObj2)) {
            throw new IllegalArgumentException("gameengine.twod.prebuilt.objectmovement.physics.collisions.Collision objects must be colliding.");
        }
        this.obj1 = physObj1;
        this.obj2 = physObj2;
    }

    public static <T extends Collision> T newCollision(final LayerCollider2D physObj1, final LayerCollider2D physObj2) {
        return (T) new Collision(physObj1, physObj2);
    }

    public boolean occurring() {
        return obj1.isColliding(obj2) && obj2.isColliding(obj1);
    }

//    public void handle(LayerCollider2D[] colliders) { // TODO make a handler instance variable?
//        if (!handled && getObj1().getHandler().getClass().equals(getObj2().getHandler().getClass())) {
//            getObj1().getHandler().handle(this, colliders);
//            handled = true;
//        }
//    }

    public LayerCollider2D getObj1() {
        return obj1;
    }

    public LayerCollider2D getObj2() {
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
//        System.out.println(this + ", " + o);
        return 1;
    }

    @Override
    public String toString() {
        return "(" + this.getObj1() + ", " + this.getObj2() + ")";
    }

    public boolean isHandled() {
        return handled;
    }

    protected void setHandled(boolean handled) {
        this.handled = handled;
    }
}
