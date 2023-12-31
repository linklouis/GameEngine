package gameengine.threed.prebuilt.gameobjects;

import gameengine.threed.graphics.objectgraphics.skeletons.Polygon;
import gameengine.threed.graphics.raytraceing.Ray;
import gameengine.threed.graphics.objectgraphics.textures.RayTracingTexture;
import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;

public class Quad extends Polygon {
    private Vector3D vertex1;
    private Vector3D vertex2;
    private Vector3D vertex3;
    private Vector3D vertex4;

    // Precomputed values for collision checks
    private Vector3D planeXaxis;
    private Vector3D planeYaxis;
    private double minPlaneX;
    private double minPlaneY;
    private double maxPlaneX;
    private double maxPlaneY;
    private Vector3D normal;
    private Vector3D center;


    /*
     * Construction:
     */

    public Quad(Vector3D v1, Vector3D v2, Vector3D v3, Vector3D v4, RayTracingTexture texture) {
        vertex1 = v1;
        vertex2 = v2;
        vertex3 = v3;
        vertex4 = v4;
        setTexture(texture);
        computeValues();
    }

    public Quad(Vector3D[] vertices, RayTracingTexture texture) {
        vertex1 = vertices[0];
        vertex2 = vertices[1];
        vertex3 = vertices[2];
        vertex4 = vertices[3];
        setTexture(texture);
        computeValues();
    }

    public Quad(double x, double y, double z, Vector3D displacement, RayTracingTexture texture) {
        Vector3D[] vertices = generateVertices(new Vector3D(x, y, z), displacement);
        vertex1 = vertices[0];
        vertex2 = vertices[1];
        vertex3 = vertices[2];
        vertex4 = vertices[3];
        setTexture(texture);
        computeValues();
    }

    /**
     * Precomputes values for collision checks
     */
    private void computeValues() {
        calculatePlaneXaxis();
        calculatePlaneYaxis();

        normal = planeXaxis.crossProduct(planeYaxis);
        calculateCenter();

        minPlaneX = Math.min(onPlane(vertex1).getX(), onPlane(vertex2).getX());
        maxPlaneX = Math.max(onPlane(vertex1).getX(), onPlane(vertex2).getX());
        minPlaneY = Math.min(onPlane(vertex1).getY(), onPlane(vertex3).getY());
        maxPlaneY = Math.max(onPlane(vertex1).getY(), onPlane(vertex3).getY());
    }


    /*
     * Functionality:
     */

    /**
     * Returns the normal vector of the intersectable object facing towards the
     * {@code VectorLine}.
     *
     * @param perspective
     * @return
     */
    @Override
    public Vector3D surfaceNormal(Ray perspective) {
        if (normal.dotProduct(perspective.getDirection()) > 0) {
            return normal.scalarMultiply(-1);
        }
        return normal;
    }

    /**
     * Finds the first intersection a lightRay will have with the
     * {@code Collider3D}.
     *
     * @param ray             The lightRay to find a collision with.
     * @param curSmallestDist The largest distance the output is looking for.
     *                        Can be used for optimization by counting out a
     *                        {@code Collider3D} early.
     * @return NaN if never enters range or if collision is behind start.
     * Otherwise, the distance to first hit
     */
    @Override
    public double distanceToCollide(final Ray ray, final double curSmallestDist) {
        double distance = normal.distToCollidePlane(vertex1, ray.getPosition(), ray.getDirection());

        if (distance <= 0 || distance >= curSmallestDist || !inRange(ray, distance)) {
            return Double.NaN;
        }

        return distance;
    }

    /**
     * Assumes the point is on the plane.
     *
     * @return True if the point is within the vertices, otherwise false.
     */
    public boolean inRange(final Ray line, double distance) {
        Vector2D planeCoordinates = onPlane(line, distance);
        return  minPlaneX <= planeCoordinates.getX() && maxPlaneX >= planeCoordinates.getX()
                && minPlaneY <= planeCoordinates.getY() && maxPlaneY >= planeCoordinates.getY();
    }

    public Vector2D onPlane(Vector3D other) {
        return other.projectToPlane(planeXaxis, planeYaxis);
    }

    public Vector2D onPlane(final Ray line, double distance) {
        return line.getPosition().projectToPlane(planeXaxis, planeYaxis, line.getDirection(), distance);
    }

    private static Vector3D[] generateVertices(Vector3D position,
                                               Vector3D displacement) {
        return new Vector3D[] {
                position,
                new Vector3D(
                        position.getX() + displacement.getX(),
                        position.getY()
                                + (displacement.getZ() != 0 ?
                                displacement.getY()
                                : 0),
                        position.getZ()),
                new Vector3D(
                        position.getX() + displacement.getX(),
                        position.getY() + displacement.getY(),
                        position.getZ() + displacement.getZ()),
                new Vector3D(
                        position.getX(),
                        position.getY()
                                + (displacement.getZ() == 0 ?
                                displacement.getY()
                                : 0),
                        position.getZ() + displacement.getZ()),
        };
    }


    /*
     * Utilities:
     */

    @Override
    public Vector3D[] getVertices() {
        return new Vector3D[] { vertex1, vertex2, vertex3, vertex4 };
    }

    public double minX() {
        return Math.min(Math.min(vertex1.getX(), vertex2.getX()), vertex3.getX());
    }
    public double minY() {
        return Math.min(Math.min(vertex1.getY(), vertex2.getY()), vertex3.getY());
    }

    public double minZ() {
        return Math.min(Math.min(vertex1.getZ(), vertex2.getZ()), vertex3.getZ());
    }

    public double maxX() {
        return max(vertex1.getX(), getVertex2().getX(), vertex3.getX())/*Math.max(Math.max(vertex1.getX(), vertex2.getX()), vertex3.getX())*/;
    }

    public double maxY() {
        return Math.max(Math.max(vertex1.getY(), vertex2.getY()), vertex3.getY());
    }

    public double maxZ() {
        return Math.max(Math.max(vertex1.getZ(), vertex2.getZ()), vertex3.getZ());
    }

    private double max(double a, double b, double c) {
        if (a > b) {
            return Math.max(a, c);
        }
        return Math.max(b, c);
    }

    @Override
    public Vector3D getCenter() {
        return center;
    }

    public void calculateCenter() {
        center = new Vector3D(
                (maxX() + minX()) / 2,
                (maxY() + minY()) / 2,
                (maxZ() + minZ()) / 2 );
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

    public Vector3D getVertex4() {
        return vertex4;
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

    private void setVertices(Vector3D[] vertices) {
        vertex1 = vertices[0];
        vertex2 = vertices[1];
        vertex3 = vertices[2];
        vertex4 = vertices[3];
    }

    public void setVertex1(Vector3D vertex1) {
        this.vertex1 = vertex1;
        computeValues();
    }

    public void setVertex2(Vector3D vertex2) {
        this.vertex2 = vertex2;
        updateXVars();

        normal = planeXaxis.crossProduct(planeYaxis);
        calculateCenter();
    }

    public void setVertex3(Vector3D vertex3) {
        this.vertex3 = vertex3;
        updateYVars();

        normal = planeXaxis.crossProduct(planeYaxis);
        calculateCenter();
    }

    public void updateYVars() {
        minPlaneY = Math.min(onPlane(vertex1).getY(), onPlane(vertex3).getY());
        maxPlaneY = Math.max(onPlane(vertex1).getY(), onPlane(vertex3).getY());
        calculatePlaneYaxis();
    }

    public void updateXVars() {
        minPlaneX = Math.min(onPlane(vertex1).getX(), onPlane(vertex2).getX());
        maxPlaneX = Math.max(onPlane(vertex1).getX(), onPlane(vertex2).getX());
        calculatePlaneXaxis();
    }

    public void calculatePlaneYaxis() {
        planeYaxis = vertex4.subtract(vertex1);
    }

    public void calculatePlaneXaxis() {
        planeXaxis = vertex2.subtract(vertex1);
    }

    public void setVertex4(Vector3D vertex) {
        this.vertex4 = vertex;
    }

    @Override
    public String toString() {
        return "Quad: " + getVertex1() + ", " + getVertex2() + ", " + getVertex3() + ", " + getVertex4() + ", " + getTexture();
    }
}
