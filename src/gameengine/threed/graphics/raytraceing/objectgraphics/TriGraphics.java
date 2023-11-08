package gameengine.threed.graphics.raytraceing.objectgraphics;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.geometry.Ray;
import gameengine.threed.geometry.Triangle3D;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector3D;

import java.util.List;

public class TriGraphics extends RayTraceable {
    private Triangle3D triData;

    // Precomputed values for collision checks
//    private Vector3D dirTo2;
//    private Vector3D dirTo3;
    private Vector3D normal;
    private Vector3D center;


    /*
     * Construction:
     */

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return null;
    }

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
                        this::computeValues,
                        new ModifierInstantiateParameter<>(
                                "vertices", Vector3D[].class,
                                this::setVertices),
//                        new ModifierInstantiateParameter<>(
//                                "vertex1", Vector3D.class,
//                                this::setVertex1NoCompute),
//                        new ModifierInstantiateParameter<>(
//                                "vertex2", Vector3D.class,
//                                this::setVertex2NoCompute),
//                        new ModifierInstantiateParameter<>(
//                                "vertex3", Vector3D.class,
//                                this::setVertex3NoCompute),
                        new ModifierInstantiateParameter<>(
                                "texture", RayTracingTexture.class,
                                this::setTexture)
                )
        };
    }

    @Override
    public void instantiate(GameObject parent, Object... args) {
        try {
            super.instantiate(parent, args);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    /*
     * Functionality:
     */

    /**
     * Precomputes values for collision checks
     */
    private void computeValues() {
//        dirTo2 = triData.vertex2().subtract(triData.vertex1());
//        dirTo3 = triData.vertex3().subtract(triData.vertex1());

        normal = triData.normal();//triData.vertex2().subtract(triData.vertex1()).crossProduct(triData.vertex3().subtract(triData.vertex1()));
        calculateCenter();
    }

    @Override
    public Vector3D surfaceNormal(Ray perspective) {
        if (normal.dotProduct(perspective.getDirection()) > 0) {
            return normal.scalarMultiply(-1);
        }
        return normal;
    }

    /**
     * Finds the first intersection a ray will have with the
     * {@code TriGraphics}.
     *
     * @param ray The ray to find a collision with.
     * @param curSmallestDist The largest distance the output is looking for.
     *                        Can be used to count out {@code TriColliders}
     *                        before having to check if it's in the triangle.
     * @return -1 if never enters range or if collision is behind start.
     * Otherwise, the distance to first hit
     */
    @Override
    public double distanceToCollide(Ray ray, double curSmallestDist) {
//                normal.dotWithSubtracted(triData.vertex1(), ray.getPosition())
//                / normal.dotWithUnitOf(ray.getDirection());
        double distance = normal.distToCollidePlane(triData.vertex1(), ray.getPosition(), ray.getDirection());
        if (distance <= 0 || distance >= curSmallestDist || !inRange(ray.pointAtDistance(distance))) {
            return Double.NaN;
        }
        return distance;
    }

    /**
     * Assumes the point is on the plane.
     *
     * @return True if the point is within the vertices, otherwise false.
     */
    public boolean inRange(Vector3D point) {
        // ChatGPT
        double dot02 = triData.v0().dotWithSubtracted(point, triData.vertex1());
        double dot12 = triData.v1().dotWithSubtracted(point, triData.vertex1());

        // Compute barycentric coordinates
        double u = (triData.dot11() * dot02 - triData.dot01() * dot12);
        double v = (triData.dot00() * dot12 - triData.dot01() * dot02);

        // Check if the point is inside the triangle
        return (u >= 0) && (v >= 0) && ((u + v) <= triData.invDenom());
    }


    /*
     * Utilities:
     */

    @Override
    public Vector3D[] getVertices() {
        return new Vector3D[] { triData.vertex1(), triData.vertex2(), triData.vertex3() };
    }

    public double minX() {
        return Math.min(Math.min(triData.vertex1().getX(), triData.vertex2().getX()), triData.vertex3().getX());
    }
    public double minY() {
        return Math.min(Math.min(triData.vertex1().getY(), triData.vertex2().getY()), triData.vertex3().getY());
    }

    public double minZ() {
        return Math.min(Math.min(triData.vertex1().getZ(), triData.vertex2().getZ()), triData.vertex3().getZ());
    }

    public double maxX() {
        return max(triData.vertex1().getX(), getVertex2().getX(), triData.vertex3().getX())/*Math.vertex3(Math.vertex3(triData.vertex1().getX(), triData.vertex2().getX()), triData.vertex3().getX())*/;
    }

    public double maxY() {
        return Math.max(Math.max(triData.vertex1().getY(), triData.vertex2().getY()), triData.vertex3().getY());
    }

    public double maxZ() {
        return Math.max(Math.max(triData.vertex1().getZ(), triData.vertex2().getZ()), triData.vertex3().getZ());
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
        return triData.vertex1();
    }

    public Vector3D getVertex2() {
        return triData.vertex2();
    }

    public Vector3D getVertex3() {
        return triData.vertex3();
    }

    protected void setVertices(Vector3D[] vertices) {
        triData = Triangle3D.computeValues(vertices[0], vertices[1], vertices[2]);
    }

//    private void setVertex1NoCompute(Vector3D triData.vertex1()) {
//        this.vertex1 = triData.vertex1();
//    }
//
//    private void setVertex2NoCompute(Vector3D triData.vertex2()) {
//        this.vertex2 = triData.vertex2();
//    }
//
//    private void setVertex3NoCompute(Vector3D triData.vertex3()) {
//        this.vertex3 = triData.vertex3();
//    }

//    public void setVertex1(Vector3D triData.vertex1()) {
//        this.vertex1 = triData.vertex1();
//        computeValues();
//    }
//
//    public void setVertex2(Vector3D triData.vertex2()) {
//        this.vertex2 = triData.vertex2();
//        v0 = triData.vertex2().subtract(triData.vertex1());
//
//        dot00 = v0.dotProduct(v0);
//        dot01 = v0.dotProduct(v1);
//
//        invDenom = 1.0 / (dot00 * dot11 - dot01 * dot01);
//
//        dirTo2 = triData.vertex2().subtract(triData.vertex1());
//
//        normal = dirTo2.crossProduct(dirTo3);
//        calculateCenter();
//    }
//
//    public void setVertex3(Vector3D triData.vertex3()) {
//        this.vertex3 = triData.vertex3();
//        v1 = triData.vertex3().subtract(triData.vertex1());
//
//        dot01 = v0.dotProduct(v1);
//        dot11 = v1.dotProduct(v1);
//
//        invDenom = 1.0 / (dot00 * dot11 - dot01 * dot01);
//
//        dirTo3 = triData.vertex3().subtract(triData.vertex1());
//
//        normal = dirTo2.crossProduct(dirTo3);
//        calculateCenter();
//    }

    @Override
    public String toString() {
        return "TriGraphics: " + getVertex1() + ", " + getVertex2() + ", " + getVertex3() + ", " + getTexture();
    }
}
