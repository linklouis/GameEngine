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

public non-sealed class TriGraphics extends RayTraceable {
    private Triangle3D triData;
    private Vector3D normal;


    /*
     * Construction:
     */

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return null;
    }

    @Override
    public RayTraceableStruct toStruct() {
        return new RayTraceableStruct(
                triData.normal(), triData.vertex1(),
                triData.v0(), triData.v1(),
                triData.dot00(), triData.dot01(), triData.dot11(), triData.invDenom(),
                getTexture().toStruct());
    }

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
                        new ModifierInstantiateParameter<>(
                                "vertices", Vector3D[].class,
                                this::setVertices),
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
     * @param ray               The ray to find a collision with.
     * @param curSmallestDist   The largest distance the output is looking for.
     *                          Can be used to count out {@code TriColliders}
     *                          before having to check if it's in the triangle.
     * @param amountInDirection
     * @return -1 if never enters range or if collision is behind start.
     * Otherwise, the distance to first hit
     */
    @Override
    public double distanceToCollide(Ray ray, double curSmallestDist, double amountInDirection) {
//        if (distanceToCollideSphere(ray, getCenter(), ))
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

    @Override
    public Vector3D getCenter() {
        return triData.center();
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

    public Vector3D v0() {
        return triData.v0();
    }

    public Vector3D v1() {
        return triData.v1();
    }

    public double dot00() {
        return triData.dot00();
    }

    public double dot01() {
        return triData.dot01();
    }

    public double dot11() {
        return triData.dot01();
    }

    public double invDenom() {
        return triData.invDenom();
    }

    protected void setVertices(Vector3D[] vertices) {
        triData = Triangle3D.computeValues(vertices[0], vertices[1], vertices[2]);
        normal = triData.normal();
    }

    @Override
    public double getRange() {
        return triData.range();
    }

    @Override
    public String toString() {
        return "TriGraphics: " + getVertex1() + ", " + getVertex2() + ", "
                + getVertex3() + ", " + getTexture();
    }
}
