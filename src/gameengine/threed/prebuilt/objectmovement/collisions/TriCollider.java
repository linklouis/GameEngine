package gameengine.threed.prebuilt.objectmovement.collisions;

import gameengine.vectormath.Vector3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TriCollider extends Collider3D<TriCollider> {
    private final Vector3D point1;
    private final Vector3D point2;
    private final Vector3D point3;

    public TriCollider(Vector3D point1, Vector3D point2, Vector3D point3) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
    }

    @Override
    public Vector3D surfaceNormal(Vector3D point, Vector3D incident) {
        Vector3D normal = dirTo2().crossProduct(dirTo3());
        if (incident.dotProduct(normal) > 0) {
            normal = normal.scalarMultiply(-1);
        }
        return normal;
    }

    @Override
    public boolean isColliding(TriCollider coll) {
        throw new UnsupportedOperationException("Not implemented Yet");
        // TODO
    }

    @Override
    public boolean contains(Vector3D point) {
        throw new UnsupportedOperationException("Not implemented Yet");
    }


    /*
     * Utilities:
     */

    @Override
    public double minX() {
        return Math.min(Math.min(point1.getX(), point2.getX()), point3.getX());
    }

    @Override
    public double minY() {
        return Math.min(Math.min(point1.getY(), point2.getY()), point3.getY());
    }

    @Override
    public double minZ() {
        return Math.min(Math.min(point1.getZ(), point2.getZ()), point3.getZ());
    }

    @Override
    public double maxX() {
        return Math.max(Math.max(point1.getX(), point2.getX()), point3.getX());
    }

    @Override
    public double maxY() {
        return Math.max(Math.max(point1.getY(), point2.getY()), point3.getY());
    }

    @Override
    public double maxZ() {
        return Math.max(Math.max(point1.getZ(), point2.getZ()), point3.getZ());
    }

    @Override
    public Vector3D getCenter() {
        return new Vector3D(
                (maxX() + minX()) / 2,
                (maxY() + minY()) / 2,
                (maxZ() + minZ()) / 2 );
    }

    @Override
    public Class<TriCollider> getColliderClass() {
        return TriCollider.class;
    }

    @Override
    public void paint(GraphicsContext gc, Color color) {

    }

    public Vector3D getPoint1() {
        return point1;
    }

    public Vector3D getPoint2() {
        return point2;
    }

    public Vector3D getPoint3() {
        return point3;
    }

    public Vector3D dirTo2() {
        return getPoint2().subtract(getPoint1());
    }

    public Vector3D dirTo3() {
        return getPoint3().subtract(getPoint1());
    }
}
