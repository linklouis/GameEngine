package gameengine.threed.prebuilt.objectmovement.collisions;

import gameengine.threed.graphics.Texture;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RectCollider extends Collider3D<RectCollider> {
    /**
     * A Vector3D from the center to one corner.
     * Defines the size of the Rectangle.
     */
    private Vector3D space;

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
//                        new ModifierInstantiateParameter<>(
//                                "collisionHandler", CollisionHandler.class,
//                                this::setHandler),
                        new ModifierInstantiateParameter<>(
                                "space", Vector3D.class,
                                this::setSpace),
                        new ModifierInstantiateParameter<>(
                                "texture", Texture.class,
                                this::setTexture)
                )
        };
    }

    @Override
    public Vector3D surfaceNormal(Vector3D point) {
        Vector3D displacementNorm = getCenter().subtract(point);

        return space.subtract(displacementNorm.abs())
                .min()
                .unitVector()
                .multiplyAcross(displacementNorm.signs());
    }

    @Override
    public boolean isColliding(RectCollider coll) {
        return  !(separatedAlongAxis(minX(), maxX(), coll.minX(), coll.maxX())
                || separatedAlongAxis(minY(), maxY(), coll.minY(), coll.maxY())
                || separatedAlongAxis(minZ(), maxZ(), coll.minZ(), coll.maxZ()));
    }

    public static boolean separatedAlongAxis(double minA, double maxA, double minB, double maxB) {
        return maxA < minB || minA > maxB;
    }

    public static boolean containsOnAxis(double minA, double maxA, double locB) {
        return maxA < locB || minA > locB;
    }

    @Override
    public boolean contains(Vector3D point) {
        return !(containsOnAxis(minX(), maxX(), point.getX())
                || containsOnAxis(minY(), maxY(), point.getY())
                || containsOnAxis(minZ(), maxZ(), point.getZ()));
    }

    @Override
    public double minX() {
        return getCenter().getX() - Math.abs(space.getX());
    }

    @Override
    public double minY() {
        return getCenter().getY() - Math.abs(space.getY());
    }

    @Override
    public double minZ() {
        return getCenter().getZ() - Math.abs(space.getZ());
    }

    @Override
    public double maxX() {
        return getCenter().getX() + Math.abs(space.getX());
    }

    @Override
    public double maxY() {
        return getCenter().getY() + Math.abs(space.getY());
    }

    @Override
    public double maxZ() {
        return getCenter().getZ() + Math.abs(space.getZ());
    }

    @Override
    public Vector3D getCenter() {
        return getFromParent(InPlane3D.class).getLocation();
    }

    @Override
    public Class<RectCollider> getColliderClass() {
        return RectCollider.class;
    }

    @Override
    public void paint(GraphicsContext gc, Color color) {

    }

    public Vector3D getSpace() {
        return space;
    }

    public void setSpace(Vector3D space) {
        this.space = space;
        setRange(Math.abs(space.magnitude()));
    }
}
