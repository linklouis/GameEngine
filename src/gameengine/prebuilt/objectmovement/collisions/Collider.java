package gameengine.prebuilt.objectmovement.collisions;

import gameengine.graphics.GraphicsObject;
import gameengine.objects.GameObject;
import gameengine.objects.Modifier;
import gameengine.prebuilt.objectmovement.InPlane;
import gameengine.prebuilt.objectmovement.physics.PhysicsObject;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public abstract class Collider<ColliderType extends Collider<ColliderType>>  extends GraphicsObject {
    private CollisionHandler<?> handler = null; // TODO make a CollisionHandler interface and just pass in the handler type + have collection of default handlers?

    private Color color;


    /*
     * Construction:
     */

    public Collider() {
        super();
    }

    public Collider(Modifier... modifiers) {
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
                                (CollisionHandler handler) -> this.handler = handler)
                )
        };
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        List<Class<? extends Modifier>> modifiers = new ArrayList<>();
        modifiers.add(InPlane.class);
        if (handler instanceof PhysicsCollisionHandler) {
            modifiers.add(PhysicsObject.class);
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

    public boolean isColliding(PhysicsObject pObj) {
        ColliderType coll = pObj.getFromParent(getColliderClass());
        return isColliding(coll);
    }

    public boolean inRange(Collider<?> collider) {
        return
                (
                        (maxY() > collider.minY() && minY() < collider.maxY()) ||
                        (collider.maxY() > minY() && collider.minY() < maxY())
                ) && (
                        (maxX() > collider.minX() && minX() < collider.maxX()) ||
                        (collider.maxX() > minX() && collider.minX() < maxX())
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
