package gameengine.threed.prebuilt.objectmovement.collisions;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.GraphicsObject3D;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
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
    //TODO extends Collider2D & make Collider2D class
    private CollisionHandler<?> handler = null; // TODO make a CollisionHandler interface and just pass in the handler type + have collection of default handlers?
    private double range;


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
                                this::setColor),
                        new ModifierInstantiateParameter<>(
                                "range", Double.class,
                                this::setRange)
                )
        };
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        List<Class<? extends Modifier>> modifiers = new ArrayList<>();
        if (handler instanceof PhysicsCollisionHandler) {
            modifiers.add(PhysicsObject2D.class);
        }
        return modifiers;
    }


    /*
     * Functionality:
     */

    public boolean isColliding(GameObject gObj) {
        assert gObj.containsModifier(getColliderClass());
        ColliderType coll = gObj.get(getColliderClass());
        return isColliding(coll);
    }

    public boolean isColliding(PhysicsObject2D pObj) {
        ColliderType coll = pObj.getFromParent(getColliderClass());
        return isColliding(coll);
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

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public boolean inRange(Vector3D position) {
        return getCenter()
                .subtract(position)
                .magnitude() <= getRange();
    }

    public boolean inRange(Vector3D position, double offset) {
        return getCenter()
                .subtract(position)
                .magnitude() <= getRange() + offset;
    }
}
