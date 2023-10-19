package gameengine.twod.prebuilt.objectmovement.physics;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.skeletons.PhysicsEngine;
import gameengine.twod.prebuilt.objectmovement.collisions.LayerCollider2D;
import gameengine.twod.prebuilt.objectmovement.collisions.PhysicsCollisionHandler;

public class PhysicsEngine2D extends PhysicsEngine<PhysicsObject2D> {
    private int iterations;
    private final PhysicsCollisionHandler collisionHandler;


    /*
     * Construction:
     */

    public PhysicsEngine2D(int iterations/*, Supplier<Double> frameRateSupplier*/) {
        this.iterations = iterations;
//        this.frameRateSupplier = frameRateSupplier;
        collisionHandler = new PhysicsCollisionHandler(0.9);
    }

    public PhysicsEngine2D(int iterations/*, Supplier<Double> frameRateSupplier*/,
                           PhysicsCollisionHandler handler) {
        this.iterations = iterations;
//        this.frameRateSupplier = frameRateSupplier;
        collisionHandler = handler;
    }


    /*
     * Functionality:
     */

    @Override
    public void updateObjects() {
        updateGravityForces();
        evaluateForces();
        updatePositionsAndHandleCollisions();
        handleCollisions();
    }

//    public void updateObjects(boolean updateGraphics) {
//        LayerCollider2D[] colliders = getColliders();
//
//        forEach(object -> object.updateGravityForces(this.toArray(new PhysicsObject2D[0])));
//        forEach(object -> object.evaluateForces(getFrameRate()));
//        for (int i = 0; i < getIterations(); i++) {
//            forEach(object -> {
//                object.updatePosition(getFrameRate() * getIterations()); // toArray(new PhysicsObject2D[0]),
//                collisionHandler.getAndHandleAllCollisions(
//                        colliders,
//                        false,
//                        object.getCollider(),
//                        true,
//                        getFrameRate() * getIterations()
//                );
//            });
//        }
//
//        collisionHandler.getAndHandleAllCollisions(colliders, false);
//        collisionHandler.clearCollisions();
//    }

    @Override
    public void updateGravityForces() {
        forEach(object -> object.updateGravityForces(this.toArray(new PhysicsObject2D[0])));
    }

    @Override
    protected void evaluateForces() {
        forEach(object -> object.evaluateForces(getFrameRate()));
    }

    private void updatePositionsAndHandleCollisions() {
        for (int i = 0; i < getIterations(); i++) {
            forEach(object -> {
                object.updatePosition(getFrameRate() * getIterations());
                collisionHandler.getAndHandleAllCollisions(
                        getColliders(),
                        false,
                        object.getCollider(),
                        true,
                        getFrameRate() * getIterations()
                );
            });
        }
    }

    private void handleCollisions() {
        collisionHandler.getAndHandleAllCollisions(getColliders(), false);
        collisionHandler.clearCollisions();
    }


    /*
     * Utilities:
     */

    public boolean add(GameObject e) {
        // TODO add exception here it GO does not have PO?
        e.get(LayerCollider2D.class).setHandler(collisionHandler);
        return super.add(e.get(PhysicsObject2D.class));
    }

    public GameObject[] getGameObjects() {
        return stream()
                .map(Modifier::getParent)
                .toList()
                .toArray(new GameObject[0]);
    }

    public LayerCollider2D[] getColliders() {
        return stream()
                .map(PhysicsObject2D::getCollider)
                .toList()
                .toArray(new LayerCollider2D[0]);
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public PhysicsCollisionHandler getCollisionHandler() {
        return collisionHandler;
    }
}
