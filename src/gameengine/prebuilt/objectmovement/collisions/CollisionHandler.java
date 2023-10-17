package gameengine.prebuilt.objectmovement.collisions;

import java.util.*;

public abstract class CollisionHandler<CollisionType extends Collision> {
    private final List<CollisionType> COLLISIONS = new ArrayList<>();

    public CollisionHandler() {
    }

    public CollisionHandler(Collection<CollisionType> collisions) {
        this.COLLISIONS.addAll(collisions);
    }

    protected abstract void handle(CollisionType collision, LayerCollider[] otherColliders);

    public boolean newCollision(final LayerCollider physObj1, final LayerCollider physObj2) {
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

    public void handleCollision(CollisionType collision, LayerCollider[] otherColliders) {
        if (!collision.isHandled()
                && collision.getObj1().getHandler() == this
                && collision.getObj2().getHandler() == this) { // TODO figure out way to not need Collisions to hold a handler
            handle(collision, otherColliders);
            collision.setHandled(true);
        }
    }

    public void handleAllCollisions(LayerCollider[] colliders) {
        COLLISIONS.forEach(collision -> handleCollision(collision, colliders));
//        COLLISIONS.removeIf(collision -> !collision.occurring());
        COLLISIONS.clear();
    }

    public void getAndHandleAllCollisions(LayerCollider[] colliders) {
        findCollisions(colliders);
        while (!getCollisions().isEmpty()) {
            handleAllCollisions(colliders);
            findCollisions(colliders);
        }
    }

    // TODO move to collision handler
    public void getAndHandleAllCollisions(LayerCollider[] colliders, boolean recheck) {
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

    public void getAndHandleAllCollisions(LayerCollider[] colliders, boolean recheck, LayerCollider collider) {
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

    public void findCollisions(LayerCollider[] objects) {
        Iterator<LayerCollider> iter = Arrays.stream(objects).iterator();
        while (iter.hasNext()) {
            LayerCollider current = iter.next();
            for (LayerCollider checking : objects) {
                if (current.isColliding(checking)) {
                    newCollision(current, checking);
                }
            }
        }
    }

    public boolean findCollisions(LayerCollider collider, LayerCollider[] objects) {
        boolean found = false;
//        Iterator<LayerCollider> iter = Arrays.stream(objects).iterator();
//        while (iter.hasNext()) {
//            LayerCollider current = iter.next();
            for (LayerCollider checking : objects) {
                if (checking != collider && collider.isColliding(checking)) {
                    found = newCollision(collider, checking);
                }
//                if (current != collider && current.isColliding(checking)) {
//                    found = newCollision(current, checking);
//                }
            }
//        }
        return found;
    }

    public List<CollisionType> getCollisions(LayerCollider collider, LayerCollider[] objects) {
        List<CollisionType> newCollisions = new ArrayList<>();
        for (LayerCollider checking : objects) {
            if (checking != collider && collider.isColliding(checking)) {
                newCollisions.add(CollisionType.newCollision(collider, checking));
            }
        }
        return newCollisions;
    }

    public boolean alreadyExists(final LayerCollider physObj1, final LayerCollider physObj2) {
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
