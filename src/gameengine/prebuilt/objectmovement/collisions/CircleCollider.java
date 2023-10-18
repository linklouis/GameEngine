package gameengine.prebuilt.objectmovement.collisions;

import gameengine.objects.Modifier;
import gameengine.prebuilt.objectmovement.InPlane;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.geom.Point2D;

public class CircleCollider extends Collider<CircleCollider> {
    private double radius;

    public CircleCollider() {
        super();
    }

    public CircleCollider(Modifier... modifiers) {
        super(modifiers);
    }

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
                        new ModifierInstantiateParameter<>(
                                "collisionHandler", CollisionHandler.class,
                                this::setHandler),
                        new ModifierInstantiateParameter<>(
                                "radius", Double.class,
                                this::setRadius),
                        new ModifierInstantiateParameter<>(
                                "color", Color.class,
                                this::setColor)
                )
        };
    }

    @Override
    public boolean isColliding(CircleCollider coll) {
        return coll.getCenter().distance(getCenter()) > getRadius() + coll.getRadius();
    }

    @Override
    public boolean contains(Point2D point) {
        return point.distance(getCenter()) <= getRadius();
    }

    @Override
    public double minX() {
        return getCenter().getX() - getRadius();
    }

    @Override
    public double minY() {
        return getCenter().getY() - getRadius();
    }

    @Override
    public double maxX() {
        return getCenter().getX() + getRadius();
    }

    @Override
    public double maxY() {
        return getCenter().getY() + getRadius();
    }

    @Override
    public Point2D.Double getCenter() {
        return getFromParent(InPlane.class).getLocation();
    }

    @Override
    public Class<CircleCollider> getColliderClass() {
        return CircleCollider.class;
    }

    @Override
    public void paint(GraphicsContext gc, Color color) {

    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
