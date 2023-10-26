package gameengine.threed.prebuilt.objectmovement.collisions;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.GraphicsObject3D;
import gameengine.threed.graphics.Visual3D;
import gameengine.threed.graphics.raytraceing.Ray;
import gameengine.threed.prebuilt.objectmovement.physics.PhysicsObject3D;
import gameengine.twod.prebuilt.objectmovement.collisions.CollisionHandler;
import gameengine.twod.prebuilt.objectmovement.collisions.PhysicsCollisionHandler;
import gameengine.twod.prebuilt.objectmovement.physics.PhysicsObject2D;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public abstract class Collider3D<ColliderType extends Collider3D<ColliderType>>  extends GraphicsObject3D {
    private CollisionHandler<?> handler = null;


    /*
     * Construction:
     */

    public Collider3D() {
        super();
    }

    public Collider3D(Modifier... modifiers) {
        super(modifiers);
    }

    @Override
    public void instantiate(GameObject parent, Object... args) {
        try {
            super.instantiate(parent, args);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
                        new ModifierInstantiateParameter<>(
                                "collisionHandler", CollisionHandler.class,
                                this::setHandler),
                        new ModifierInstantiateParameter<>(
                                "color", Color.class,
                                this::setColor)
//                        new ModifierInstantiateParameter<>(
//                                "range", Double.class,
//                                this::setRange)
                )
        };
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        List<Class<? extends Modifier>> modifiers = new ArrayList<>();
        if (handler instanceof PhysicsCollisionHandler) {
            modifiers.add(PhysicsObject3D.class);
        }
        return modifiers;
    }


    /*
     * Functionality:
     */

    public boolean isColliding(GameObject gObj) {
        assert gObj.containsModifier(getColliderClass());
        return isColliding(gObj.get(getColliderClass()));
    }

    public boolean isColliding(PhysicsObject2D pObj) {
        return isColliding(
                pObj.getFromParent(getColliderClass()));
    }

    public boolean inRange(Collider3D<?> collider) {
        return
                (
                        (maxY() > collider.minY() && minY() < collider.maxY()) ||
                                (collider.maxY() > minY() && collider.minY() < maxY())
                ) && (
                        (maxX() > collider.minX() && minX() < collider.maxX()) ||
                                (collider.maxX() > minX() && collider.minX() < maxX())
                ) && (
                        (maxZ() > collider.minZ() && minZ() < collider.maxZ()) ||
                                (collider.maxZ() > minZ() && collider.minZ() < maxZ())
                );
    }


    /*
     * Abstract Methods:
     */

    /**
     * Finds the first intersection a ray will have with the
     * {@code Collider3D}.
     *
     * @param ray The ray to find a collision with.
     * @param curSmallestDist The largest distance the output is looking for.
     *                        Can be used for optimization by counting out a
     *                        {@code Collider3D} early.
     * @return -1 if never enters range or if collision is behind start.
     * Otherwise, the distance to first hit
     */
    public abstract double distanceToCollide(Ray ray, double curSmallestDist);

    public abstract boolean isColliding(ColliderType coll);

    public abstract boolean contains(Vector3D point);

    public abstract double minX();

    public abstract double minY();

    public abstract double minZ();

    public abstract double maxX();

    public abstract double maxY();

    public abstract double maxZ();

    public abstract Vector3D getCenter();

    public abstract Class<ColliderType> getColliderClass();

    public abstract void paint(GraphicsContext gc, Color color);


    /*
     * Utilities:
     */

    public double centerX() {
        return getCenter().getX();
    }

    public double centerY() {
        return getCenter().getY();
    }

    public double centerZ() {
        return getCenter().getZ();
    }

    public double getWidth() {
        return maxX() - minX();
    }

    public double getHeight() {
        return maxY() - minY();
    }

    public double getLength() {
        return maxZ() - minZ();
    }

    public CollisionHandler<?> getHandler() {
        return handler;
    }

    public void setHandler(CollisionHandler<?> handler) {
        if (getHandler() != null) {
            this.handler = handler;
        }
    }

    public GraphicsObject3D getAppearance() {
        return getFromParent(Visual3D.class)
                .getAppearance();
    }
}
