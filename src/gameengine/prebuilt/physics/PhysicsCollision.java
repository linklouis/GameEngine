package gameengine.prebuilt.physics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PhysicsCollision extends Collision {
    private static final List<Collision> COLLISIONS = new ArrayList<>();

    private PhysicsCollision(final Collidable physObj1, final Collidable physObj2) {
        super(physObj1, physObj2);
    }

    public static boolean newCollision(final Collidable physObj1, final Collidable physObj2) {
        if (physObj1 == physObj2 || !physObj1.isColliding(physObj2)) {
            return false;
        }
        if (!(physObj1.getHandler() instanceof PhysicsCollisionHandler
                && physObj2.getHandler() instanceof PhysicsCollisionHandler)) {
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

    @Override
    public void handle(Collidable[] colliders) {
        if (!isHandled() && getObj1().getHandler().getClass().equals(getObj2().getHandler().getClass())) {
            getObj1().getHandler().handle(this, colliders);
            setHandled(true);
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

    // TODO move to collision handler
    public static void getAndHandleCollisions(Collidable[] colliders,
                                              boolean recheck,
                                              Collidable collider,
                                              boolean active,
                                              double velocityScaleFactor) {
        if (findCollisions(collider, colliders) && active) {
            PhysicsObject pObj = collider.getParent().get(PhysicsObject.class);
            pObj.move(pObj.getVelocity().scalarDivide(-velocityScaleFactor * 1.2));
        }
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

    public static boolean findCollisions(Collidable collider, Collidable[] objects) {
        boolean found = false;
        Iterator<Collidable> iter = Arrays.stream(objects).iterator();
        while (iter.hasNext()) {
            Collidable current = iter.next();
            for (Collidable checking : objects) {
                if (current != collider && current.isColliding(checking)) {
                    found = newCollision(current, checking);
                }
            }
        }
        return found;
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

    public static Collision[] getCollisions() {
        return COLLISIONS.toArray(new Collision[0]);
    }

    public static void clearCollisions() {
        COLLISIONS.clear();
    }

    @Override
    public String toString() {
        return "(" + this.getObj1() + ", " + this.getObj2() + ")";
    }
}

