package gameengine.twod.prebuilt.objectmovement.collisions;

import gameengine.twod.graphics.GraphicsObject2D;
import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.twod.prebuilt.objectmovement.InPlane;
import gameengine.twod.prebuilt.objectmovement.physics.PhysicsObject2D;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public abstract class Collider2D<ColliderType extends Collider2D<ColliderType>>  extends GraphicsObject2D {
    private CollisionHandler<?> handler = null; // TODO make a CollisionHandler interface and just pass in the handler type + have collection of default handlers?

    private Color color;


    /*
     * Construction:
     */

    public Collider2D() {
        super();
    }

    public Collider2D(Modifier... modifiers) {
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
                                this::setHandler)
                )
        };
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        List<Class<? extends Modifier>> modifiers = new ArrayList<>();
        modifiers.add(InPlane.class);
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

    public boolean inRange(Collider2D<?> collider2D) {
        return
                (
                        (maxY() > collider2D.minY() && minY() < collider2D.maxY()) ||
                        (collider2D.maxY() > minY() && collider2D.minY() < maxY())
                ) && (
                        (maxX() > collider2D.minX() && minX() < collider2D.maxX()) ||
                        (collider2D.maxX() > minX() && collider2D.minX() < maxX())
                );
    }

    @Override
    public void paint(GraphicsContext gc) {
        paint(gc, getColor());
    }


    /*
     * Abstract Methods:
     */

    public abstract boolean isColliding(ColliderType coll);

    public abstract boolean contains(Point2D point);

    public abstract double minX();

    public abstract double minY();

    public abstract double maxX();

    public abstract double maxY();

    public abstract Point2D.Double getCenter();

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

    public double getWidth() {
        return maxX() - minX();
    }

    public double getHeight() {
        return maxY() - minY();
    }

    public CollisionHandler<?> getHandler() {
        return handler;
    }

    public void setHandler(CollisionHandler<?> handler) {
        if (getHandler() != null) {
            this.handler = handler;
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
