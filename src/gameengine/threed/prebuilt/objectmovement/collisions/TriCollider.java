package gameengine.threed.prebuilt.objectmovement.collisions;

import gameengine.threed.graphics.Texture;
import gameengine.threed.graphics.raytraceing.Ray;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TriCollider extends Collider3D<TriCollider> {
    private Vector3D vertex1;
    private Vector3D vertex2;
    private Vector3D vertex3;

    // Precompute values for collision checks
    private Vector3D v0;
    private Vector3D v1;
    private double dot00;
    private double dot01;
    private double dot11;
    private double invDenom;

    private Vector3D dirTo2;
    private Vector3D dirTo3;

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
                        this::computeValues,
//                        new ModifierInstantiateParameter<>(
//                                "collisionHandler", CollisionHandler.class,
//                                this::setHandler),
                        new ModifierInstantiateParameter<>(
                                "vertex1", Vector3D.class,
                                this::setVertex1NoCompute),
                        new ModifierInstantiateParameter<>(
                                "vertex2", Vector3D.class,
                                this::setVertex2NoCompute),
                        new ModifierInstantiateParameter<>(
                                "vertex3", Vector3D.class,
                                this::setVertex3NoCompute),
                        new ModifierInstantiateParameter<>(
                                "texture", Texture.class,
                                this::setTexture)
                )
        };
    }

    /**
     * Precomputes values for collision checks
     */
    private void computeValues() {
        v0 = vertex2.subtract(vertex1);
        v1 = vertex3.subtract(vertex1);

        dot00 = v0.dotProduct(v0);
        dot01 = v0.dotProduct(v1);
        dot11 = v1.dotProduct(v1);

        invDenom = /*1.0 /*/ (dot00 * dot11 - dot01 * dot01);

        dirTo2 = vertex2.subtract(vertex1);
        dirTo3 = vertex3.subtract(vertex1);

    }

    @Override
    public Vector3D surfaceNormal(Ray ray) {
        Vector3D normal = dirTo2.crossProduct(dirTo3)/*.unitVector()*/;
        if (ray.getPosition().subtract(vertex1).dotProduct(normal) < 0) {
            return normal.scalarMultiply(-1);
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
        throw new UnsupportedOperationException("Not valid");
    }

//    public boolean willCollide(Ray ray) {
//        return surfaceNormal(ray).dotProduct(ray.getDirection()) > 0;
//    }

    public double distanceToCollidePlane(Ray ray) {
        Vector3D planeNormal = surfaceNormal(ray);
        return planeNormal.dotProduct(
                vertex1.subtract(ray.getPosition()))
                / planeNormal.dotProduct(ray.getDirection().unitVector());
    }

    @Override
    public double distanceToCollide(Ray ray, double curSmallestDist) {
        double distance = distanceToCollidePlane(ray);

        if (distance <= 0 || distance >= curSmallestDist) {
            return -1; // No collision with the plane
        }

        Vector3D collisionPoint = ray.getPosition().add(
                        ray.getDirection()
                                .unitVector()
                                .scalarMultiply(distance)
        );

        if (inRange(collisionPoint)) {
            return distance;
        }
        return -1;
    }

    /**
     * Assumes the point is on the plane.
     *
     * @return True if the point is within the vertices, otherwise false.
     */
    public boolean inRange(Vector3D point) {
        // Calculate the vectors from the vertices of the triangle to the given point
        Vector3D v2 = point.subtract(vertex1);

        // Calculate dot products
        double dot02 = v0.dotProduct(v2);
        double dot12 = v1.dotProduct(v2);

        // Compute barycentric coordinates
        double u = (dot11 * dot02 - dot01 * dot12);
        double v = (dot00 * dot12 - dot01 * dot02);

        // Check if the point is inside the triangle
        return (u >= 0) && (v >= 0) && ((u + v) <= invDenom);
    }


    /*
     * Utilities:
     */

    @Override
    public double minX() {
        return Math.min(Math.min(vertex1.getX(), vertex2.getX()), vertex3.getX());
    }

    @Override
    public double minY() {
        return Math.min(Math.min(vertex1.getY(), vertex2.getY()), vertex3.getY());
    }

    @Override
    public double minZ() {
        return Math.min(Math.min(vertex1.getZ(), vertex2.getZ()), vertex3.getZ());
    }

    @Override
    public double maxX() {
        return Math.max(Math.max(vertex1.getX(), vertex2.getX()), vertex3.getX());
    }

    @Override
    public double maxY() {
        return Math.max(Math.max(vertex1.getY(), vertex2.getY()), vertex3.getY());
    }

    @Override
    public double maxZ() {
        return Math.max(Math.max(vertex1.getZ(), vertex2.getZ()), vertex3.getZ());
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

    public Vector3D getVertex1() {
        return vertex1;
    }

    public Vector3D getVertex2() {
        return vertex2;
    }

    public Vector3D getVertex3() {
        return vertex3;
    }

    private void setVertex1NoCompute(Vector3D vertex1) {
        this.vertex1 = vertex1;
    }

    private void setVertex2NoCompute(Vector3D vertex2) {
        this.vertex2 = vertex2;
    }

    private void setVertex3NoCompute(Vector3D vertex3) {
        this.vertex3 = vertex3;
    }

    public void setVertex1(Vector3D vertex1) {
        this.vertex1 = vertex1;
        computeValues();
    }

    public void setVertex2(Vector3D vertex2) {
        this.vertex2 = vertex2;
        v0 = vertex2.subtract(vertex1);

        dot00 = v0.dotProduct(v0);
        dot01 = v0.dotProduct(v1);

        invDenom = 1.0 / (dot00 * dot11 - dot01 * dot01);

        dirTo2 = vertex2.subtract(vertex1);

    }

    public void setVertex3(Vector3D vertex3) {
        this.vertex3 = vertex3;
        v1 = vertex3.subtract(vertex1);

        dot01 = v0.dotProduct(v1);
        dot11 = v1.dotProduct(v1);

        invDenom = 1.0 / (dot00 * dot11 - dot01 * dot01);

        dirTo3 = vertex3.subtract(vertex1);

    }

    @Override
    public String toString() {
        return "Tri: " + getVertex1() + ", " + getVertex2() + ", " + getVertex3() + ", " + getTexture();
    }
}
