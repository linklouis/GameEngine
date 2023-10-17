package gameengine.prebuilt.objectmovement.physics;

import gameengine.objects.GameObject;
import gameengine.objects.Modifier;
import gameengine.prebuilt.objectmovement.collisions.LayerCollider;
import gameengine.prebuilt.objectmovement.collisions.PhysicsCollisionHandler;

import java.util.ArrayList;
import java.util.function.Supplier;

public class PhysicsEngine extends ArrayList<PhysicsObject> {
    private int iterations;
    private Supplier<Double> frameRateSupplier;
    private final PhysicsCollisionHandler collisionHandler;


    /*
     * Construction:
     */

    public PhysicsEngine(int iterations, Supplier<Double> frameRateSupplier) {
        this.iterations = iterations;
        this.frameRateSupplier = frameRateSupplier;
        collisionHandler = new PhysicsCollisionHandler(0.9);
    }

    public PhysicsEngine(int iterations, Supplier<Double> frameRateSupplier,
                         PhysicsCollisionHandler handler) {
        this.iterations = iterations;
        this.frameRateSupplier = frameRateSupplier;
        collisionHandler = handler;
    }


    /*
     * Functionality:
     */

    public void updateObjects() {
        updateGravityForces();
        evaluateForces();
        updatePositionsAndHandleCollisions();
        handleCollisions();
    }

//    public void updateObjects(boolean updateGraphics) {
//        LayerCollider[] colliders = getColliders();
//
//        forEach(object -> object.updateGravityForces(this.toArray(new PhysicsObject[0])));
//        forEach(object -> object.evaluateForces(getFrameRate()));
//        for (int i = 0; i < getIterations(); i++) {
//            forEach(object -> {
//                object.updatePosition(getFrameRate() * getIterations()); // toArray(new PhysicsObject[0]),
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

    private void updateGravityForces() {
        forEach(object -> object.updateGravityForces(this.toArray(new PhysicsObject[0])));
    }

    private void evaluateForces() {
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
        e.get(LayerCollider.class).setHandler(collisionHandler);
        return super.add(e.get(PhysicsObject.class));
    }

    public GameObject[] getGameObjects() {
        return stream()
                .map(Modifier::getParent)
                .toList()
                .toArray(new GameObject[0]);
    }

    public LayerCollider[] getColliders() {
        return stream()
                .map(PhysicsObject::getCollider)
                .toList()
                .toArray(new LayerCollider[0]);
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public double getFrameRate() {
        return Math.round(frameRateSupplier.get());
    }

    public void setFrameRateSupplier(Supplier<Double> newFrameRateSupplier) {
        this.frameRateSupplier = newFrameRateSupplier;
    }

    public PhysicsCollisionHandler getCollisionHandler() {
        return collisionHandler;
    }
}
