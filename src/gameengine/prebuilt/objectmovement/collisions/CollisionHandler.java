package gameengine.prebuilt.objectmovement.collisions;

import java.util.*;

public abstract class CollisionHandler<CollisionType extends Collision> {
    private final List<CollisionType> COLLISIONS = new ArrayList<>();

    public CollisionHandler() {
    }

    public CollisionHandler(Collection<CollisionType> collisions) {
        this.COLLISIONS.addAll(collisions);
    }

    protected abstract void handle(CollisionType collision, Collidable[] otherColliders);

    public boolean newCollision(final Collidable physObj1, final Collidable physObj2) {
        if (physObj1 == physObj2 || !physObj1.isColliding(physObj2)) {
            return false;
        }

        CollisionType collision = CollisionType.newCollision(physObj1, physObj2);
        for (CollisionType coll : COLLISIONS) {
            if (collision.compareTo(coll) == 0) {
                return false;
            }
        }
        COLLISIONS.add(collision);
        return true;
    }

    public void handleCollision(CollisionType collision, Collidable[] otherColliders) {
        if (!collision.isHandled()
                && collision.getObj1().getHandler() == this
                && collision.getObj2().getHandler() == this) { // TODO figure out way to not need Collisions to hold a handler
            handle(collision, otherColliders);
            collision.setHandled(true);
        }
    }

    public void handleAllCollisions(Collidable[] colliders) {
        COLLISIONS.forEach(collision -> handleCollision(collision, colliders));
//        COLLISIONS.removeIf(collision -> !collision.occurring());
        COLLISIONS.clear();
    }

    public void getAndHandleAllCollisions(Collidable[] colliders) {
        findCollisions(colliders);
        while (!getCollisions().isEmpty()) {
            handleAllCollisions(colliders);
            findCollisions(colliders);
        }
    }

    // TODO move to collision handler
    public void getAndHandleAllCollisions(Collidable[] colliders, boolean recheck) {
        findCollisions(colliders);
        if (recheck) {
            while (!getCollisions().isEmpty()) {
                handleAllCollisions(colliders);
                findCollisions(colliders);
            }
        } else {
            handleAllCollisions(colliders);
        }
    }

    public void getAndHandleAllCollisions(Collidable[] colliders, boolean recheck, Collidable collider) {
        findCollisions(collider, colliders);
        if (recheck) {
            while (!getCollisions().isEmpty()) {
                handleAllCollisions(colliders);
                findCollisions(collider, colliders);
            }
        } else {
            handleAllCollisions(colliders);
        }
    }

    public void findCollisions(Collidable[] objects) {
        Iterator<Collidable> iter = Arrays.stream(objects).iterator();
        while (iter.hasNext()) {
            Collidable current = iter.next();
            for (Collidable checking : objects) {
                if (current.isColliding(checking)) {
                    newCollision(current, checking);
                }
            }
        }
    }

    public boolean findCollisions(Collidable collider, Collidable[] objects) {
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

    public boolean alreadyExists(final Collidable physObj1, final Collidable physObj2) {
        Collision toCheck = new Collision(physObj1, physObj2);
        for (Collision collision : COLLISIONS) {
            if (toCheck.compareTo(collision) == 0) {
                return true;
            }
        }
        return false;
    }

    public CollisionType[] getCollisions(CollisionType[] collisionArray) {
        return COLLISIONS.toArray(collisionArray);
    }

    public List<CollisionType> getCollisions() {
        return COLLISIONS;
    }

    public void clearCollisions() {
        COLLISIONS.clear();
    }
}
