package gameengine.prebuilt.physics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class Collision implements Comparable<Collision> {
    private static final List<Collision> COLLISIONS = new ArrayList<>();
    private final Collidable obj1;
    private final Collidable obj2;
    private boolean handled = false;

    private Collision(final Collidable physObj1, final Collidable physObj2) {
        if (physObj1 == physObj2) {
            throw new IllegalArgumentException("gameengine.prebuilt.physics.Collision objects must be unique.");
        }
        if (!physObj1.isColliding(physObj2)) {
            throw new IllegalArgumentException("gameengine.prebuilt.physics.Collision objects must be colliding.");
        }
        this.obj1 = physObj1;
        this.obj2 = physObj2;
    }

    public static boolean newCollision(final Collidable physObj1, final Collidable physObj2) {
        if (physObj1 == physObj2 || !physObj1.isColliding(physObj2)) {
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
        return obj1.isColliding(obj2) && obj2.isColliding(obj1);
    }

    public void handle(Collidable[] colliders) {
        if (!handled && getObj1().getHandler().getClass().equals(getObj2().getHandler().getClass())) {
            getObj1().getHandler().handle(this, colliders);
            handled = true;
        }
    }

    public static void handleCollisions(Collidable[] colliders) {
        COLLISIONS.forEach(collision -> collision.handle(colliders));
        COLLISIONS.clear();
    }

    public static void getAndHandleCollisions(Collidable[] colliders) {
        findCollisions(colliders);
        while (getCollisions().length > 0) {
            COLLISIONS.forEach(collision -> collision.handle(colliders));
            COLLISIONS.clear();
            findCollisions(colliders);
        }
    }

    public static void getAndHandleCollisions(Collidable[] colliders, boolean recheck) {
        findCollisions(colliders);
        if (recheck) {
            while (getCollisions().length > 0) {
                COLLISIONS.forEach(collision -> collision.handle(colliders));
                COLLISIONS.clear();
                findCollisions(colliders);
            }
        } else {
            COLLISIONS.forEach(collision -> collision.handle(colliders));
            COLLISIONS.clear();
        }
    }

    public static void getAndHandleCollisions(Collidable[] colliders, boolean recheck, Collidable collider) {
        findCollisions(collider, colliders);
        if (recheck) {
            while (getCollisions().length > 0) {
                COLLISIONS.forEach(collision -> collision.handle(colliders));
                COLLISIONS.clear();
                findCollisions(collider, colliders);
            }
        } else {
            COLLISIONS.forEach(collision -> collision.handle(colliders));
            COLLISIONS.removeIf(collision -> !collision.occurring());
//            COLLISIONS.clear();
        }
    }

    public static void findCollisions(Collidable[] objects) {
        Iterator<Collidable> iter = Arrays.stream(objects).iterator();
        while (iter.hasNext()) {
            Collidable current = iter.next();
//            boolean collided = false;
            for (Collidable checking : objects) {
                if (current.isColliding(checking)) {
                    newCollision(current, checking);
//                    collided = true;
                }
            }
//            if (collided) {
//                iter.remove();
//            }
        }
    }

    public static void findCollisions(Collidable collider, Collidable[] objects) {
        Iterator<Collidable> iter = Arrays.stream(objects).iterator();
        while (iter.hasNext()) {
            Collidable current = iter.next();
            for (Collidable checking : objects) {
                if (current != collider && current.isColliding(checking)) {
                    newCollision(current, checking);
                }
            }
        }
    }

    public static boolean exists(final Collidable physObj1, final Collidable physObj2) {
        Collision toCheck = new Collision(physObj1, physObj2);
        for (Collision collision : COLLISIONS) {
            if (toCheck.compareTo(collision) == 0) {
                return true;
            }
        }
        return false;
    }

    public Collidable getObj1() {
        return obj1;
    }

    public Collidable getObj2() {
        return obj2;
    }

    public static Collision[] getCollisions() {
        return COLLISIONS.toArray(new Collision[0]);
    }

    public static void clearCollisions() {
        COLLISIONS.clear();
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
}
