package gameengine.prebuilt.physics;

import gameengine.objects.GameObject;
import gameengine.objects.Modifier;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class PhysicsEngine extends ArrayList<PhysicsObject> {
    private int iterations;
    private int framerate;
    private GraphicsContext graphicsContext = null;

    public PhysicsEngine(int iterations, int framerate) {
        this.iterations = iterations;
        this.framerate = framerate;
    }

    public PhysicsEngine(int iterations, int framerate, GraphicsContext gc) {
        this.iterations = iterations;
        this.framerate = framerate;
        graphicsContext = gc;
    }

    public boolean add(GameObject e) {
        // TODO add exception here it GO does not have PO?
        return super.add(e.get(PhysicsObject.class));
    }

    public GameObject[] getGameObjects() {
        return stream()
                .map(Modifier::getParent)
                .toList()
                .toArray(new GameObject[0]);
    }

    public Collidable[] getColliders() {
        return stream()
                .map(PhysicsObject::getCollider)
                .toList()
                .toArray(new Collidable[0]);
    }

    public void updateObjects() {
        updateObjects(false);
    }

    public void updateObjects(boolean updateGraphics) {
        Collidable[] colliders = getColliders();

        forEach(object -> object.updateForces(this.toArray(new PhysicsObject[0]), getFramerate()));
        for (int i = 0; i < getIterations(); i++) {
            forEach(object -> {
                object.updatePosition(toArray(new PhysicsObject[0]), getFramerate() * getIterations());
                gameengine.prebuilt.physics.PhysicsCollision
                        .getAndHandleCollisions(
                                colliders,
                                false,
                                object.getCollider(),
                                true,
                                getFramerate() * getIterations()
                        );
            });
//            forEach(
//            );
        }

        gameengine.prebuilt.physics.PhysicsCollision.getAndHandleCollisions(colliders, false);
        Collision.clearCollisions();

        if (updateGraphics) {
            paintObjects();
        }
    }

    public void paintObjects() {
        forEach(object -> object.paint(getGraphicsContext()));
    }

    public void paintObjects(GraphicsContext gc) {
        forEach(object -> object.paint(gc));
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getFramerate() {
        return framerate;
    }

    public void setFramerate(int framerate) {
        this.framerate = framerate;
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }
}
