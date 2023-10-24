package gameengine.threed.prebuilt.objectmovement.collisions;

import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.Texture;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SphereCollider extends Collider3D<SphereCollider> {
    private double radius;

    public SphereCollider() {
        super();
    }

    public SphereCollider(Modifier... modifiers) {
        super(modifiers);
    }

    @Override
    public Vector3D surfaceNormal(Vector3D point, Vector3D incident) {
        return point.subtract(getCenter()).unitVector();
    }

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
//                        new ModifierInstantiateParameter<>(
//                                "collisionHandler", CollisionHandler.class,
//                                this::setHandler),
                        new ModifierInstantiateParameter<>(
                                "radius", Double.class,
                                this::setRadius),
                        new ModifierInstantiateParameter<>(
                                "texture", Texture.class,
                                this::setTexture)
                )
        };
    }

    @Override
    public boolean isColliding(SphereCollider coll) {
        return coll.getCenter()
                .subtract(getCenter())
                .magnitude() > getRadius() + coll.getRadius();
    }

    @Override
    public boolean contains(Vector3D point) {
        return Math.abs(point.subtract(getCenter()).magnitude()) <= getRadius();
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
    public double minZ() {
        return getCenter().getZ() - getRadius();
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
    public double maxZ() {
        return getCenter().getZ() + getRadius();
    }

    @Override
    public Vector3D getCenter() {
        return getFromParent(InPlane3D.class).getLocation();
    }

    @Override
    public Class<SphereCollider> getColliderClass() {
        return SphereCollider.class;
    }

    @Override
    public void paint(GraphicsContext gc, Color color) {

    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        setRange(radius);
    }
}
