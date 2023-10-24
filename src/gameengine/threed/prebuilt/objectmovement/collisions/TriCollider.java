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

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
//                        new ModifierInstantiateParameter<>(
//                                "collisionHandler", CollisionHandler.class,
//                                this::setHandler),
                        new ModifierInstantiateParameter<>(
                                "vertex1", Vector3D.class,
                                this::setVertex1),
                        new ModifierInstantiateParameter<>(
                                "vertex2", Vector3D.class,
                                this::setVertex2),
                        new ModifierInstantiateParameter<>(
                                "vertex3", Vector3D.class,
                                this::setVertex3),
                        new ModifierInstantiateParameter<>(
                                "texture", Texture.class,
                                this::setTexture)
                )
        };
    }

    @Override
    public Vector3D surfaceNormal(Ray ray) {
        Vector3D normal = dirTo2().crossProduct(dirTo3());
        if (ray.getStart().subtract(vertex1).dotProduct(normal) < 0) {
//            System.out.println("a");
            normal = normal.scalarMultiply(-1);
        }
//        System.out.println(dirTo2() + ", " + dirTo3() + ", " + normal.unitVector());
        return normal.unitVector();
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

    public boolean willCollide(Ray ray) {
        return surfaceNormal(ray).dotProduct(ray.getDirection()) > 0;
    }

    public double distanceToCollidePlane(Ray ray) {
        Vector3D planeNormal = surfaceNormal(ray);
        return planeNormal.dotProduct(vertex1.subtract(ray.getStart())) / planeNormal.dotProduct(ray.getDirection().unitVector());
    }

    @Override
    public double distanceToCollide(Ray ray) {
        double distance = distanceToCollidePlane(ray);
        if (distance <= 0) {
            return -1; // No collision with the plane
        }

        Vector3D collisionPoint = ray.getStart()
                .add(ray.getDirection().unitVector().scalarMultiply(distance));

//        System.out.println(distance + ", " + collisionPoint);

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
        Vector3D edge1 = vertex2.subtract(vertex1);
        Vector3D edge2 = vertex3.subtract(vertex2);
        Vector3D edge3 = vertex1.subtract(vertex3);

        Vector3D pointToVertex1 = point.subtract(vertex1);
        Vector3D pointToVertex2 = point.subtract(vertex2);
        Vector3D pointToVertex3 = point.subtract(vertex3);

        double dotProduct1 = edge1.crossProduct(pointToVertex1).getZ();
        double dotProduct2 = edge2.crossProduct(pointToVertex2).getZ();
        double dotProduct3 = edge3.crossProduct(pointToVertex3).getZ();

        return (dotProduct1 >= 0 && dotProduct2 >= 0 && dotProduct3 >= 0) ||
                (dotProduct1 <= 0 && dotProduct2 <= 0 && dotProduct3 <= 0);
    }

    public boolean inRange2(Vector3D point) {
        // Calculate the vectors representing the edges of the triangle
        Vector3D edge1 = vertex2.subtract(vertex1);
        Vector3D edge2 = vertex3.subtract(vertex2);
        Vector3D edge3 = vertex1.subtract(vertex3);

        // Calculate the vectors from the vertices to the point
        Vector3D v1 = point.subtract(vertex1);
        Vector3D v2 = point.subtract(vertex2);
        Vector3D v3 = point.subtract(vertex3);

        // Calculate the dot products of the vectors
        double dot1 = edge1.dotProduct(v1);
        double dot2 = edge2.dotProduct(v2);
        double dot3 = edge3.dotProduct(v3);

        // Check if the point is within the triangle by checking the sign of the dot products
        return (dot1 >= 0 && dot2 >= 0 && dot3 >= 0)
                || (dot1 <= 0 && dot2 <= 0 && dot3 <= 0);
    }

    /**
     * Assumes the point is on the plane.
     *
     * @return True if the point is within the vertices, otherwise false.
     */
    public boolean inRange1(Vector3D point) {
        //Finds whether a point is within a triangle defined by 3 other points P1, P2, and P3.
        // Does this by drawing a line from P2 to P3, and finding if
        Vector3D point1ToCollision = point.subtract(vertex1); // get the point relative to P1
        if (point1ToCollision.dotProduct(dirTo3()) < 0
                || point1ToCollision.dotProduct(dirTo2()) < 0 ||
                point.subtract(vertex2).dotProduct(vertex1.subtract(vertex2)) < 0
                        || point.subtract(vertex2).dotProduct(vertex3.subtract(vertex2)) < 0 ||
                point.subtract(vertex3).dotProduct(vertex1.subtract(vertex3)) < 0
                        || point.subtract(vertex3).dotProduct(vertex2.subtract(vertex3)) < 0) {
            return false;
        }
        double d = dirTo2().dotProduct(point1ToCollision) / vertex3.subtract(vertex2).dotProduct(point1ToCollision); // get d in the vector equation of a line (a + td), which starts at P2 and ends at it's intersection with a line drawn from P1 to the point to find.
        if (d < 1) {
            return false;
        }
        Vector3D boundDir = vertex3.subtract(vertex2).scalarMultiply(d);
        Vector3D bound = dirTo2().add(boundDir);
        return point1ToCollision.magnitude() <= bound.magnitude();
//        return point1ToCollision.dotProduct(dirTo2().unitVector()) > 0.75 && point1ToCollision.dotProduct(dirTo3().unitVector()) > 0.75;
//        return (point.subtract(point1).abs().magnitude() <= maxDistFrom1());
    }


    /*
     * Utilities:
     */

    public double maxDistFrom1() {
        return Math.max(dirTo2().abs().magnitude(), dirTo3().abs().magnitude());
    }

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

    private void setVertex1(Vector3D vertex1) {
        this.vertex1 = vertex1;
    }

    private void setVertex2(Vector3D vertex2) {
        this.vertex2 = vertex2;
    }

    private void setVertex3(Vector3D vertex3) {
        this.vertex3 = vertex3;
    }

    public Vector3D dirTo2() {
        return getVertex2().subtract(getVertex1());
    }

    public Vector3D dirTo3() {
        return getVertex3().subtract(getVertex1());
    }

    @Override
    public String toString() {
        return "Tri: " + getVertex1() + ", " + getVertex2() + ", " + getVertex3() + ", " + getTexture();
    }
}