package gameengine.threed.graphics.raytraceing.objectgraphics;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.geometry.Ray;
import gameengine.threed.geometry.Rect3D;
import gameengine.threed.geometry.VectorLine3D;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.twod.Rect;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;

import java.util.List;

public class QuadGraphics extends RayTraceable {
    // TODO keep a position variable, and add it to rect's vertices position to not make a new Rect3D for each movement.
    private Rect3D rect;

    // Precomputed values for collision checks
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
                                "vertex1", Vector3D.class,
                                this::setVertex1NoCompute),
                        new ModifierInstantiateParameter<>(
                                "vertex2", Vector3D.class,
                                this::setVertex2NoCompute),
                        new ModifierInstantiateParameter<>(
                                "vertex3", Vector3D.class,
                                this::setVertex3NoCompute),
                        new ModifierInstantiateParameter<>(
                                "vertex4", Vector3D.class,
                                this::setVertex4NoCompute),
                        new ModifierInstantiateParameter<>(
                                "texture", RayTracingTexture.class,
                                this::setTexture)
                ),
                new ArgumentContext(
                        this::computeValues,
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
        rect = new Rect3D(null, null, null, null, null, null, null);
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
//        calculatePlaneXaxis();
//        calculatePlaneYaxis();

        rect = new Rect3D(rect.vertex1(), rect.vertex2(), rect.vertex3(), rect.vertex4());
        normal = rect.normal();
        center = rect.calculateCenter();
    }

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
        double distance = normal.distToCollidePlane(rect.vertex1(), ray.getPosition(), ray.getDirection());

        if (distance <= 0 || distance >= curSmallestDist || !rect.contains(ray, distance)) {
            return Double.NaN;
        }

        return distance;
    }

//    /**
//     * Assumes the point is on the plane.
//     *
//     * @return True if the point is within the vertices, otherwise false.
//     */
//    public boolean inRange(final VectorLine3D line, double distance) {
//        Vector2D planeCoordinates = onPlane(line, distance);
//        return  minPlaneX <= planeCoordinates.getX() && maxPlaneX >= planeCoordinates.getX()
//                && minPlaneY <= planeCoordinates.getY() && maxPlaneY >= planeCoordinates.getY();
//    }


    /*
     * Utilities:
     */

    @Override
    public Vector3D[] getVertices() {
        return rect.vertices();
    }

    @Override
    public Vector3D getCenter() {
        return center;
    }

    public Vector3D getVertex1() {
        return rect.vertex1();
    }

    public Vector3D getVertex2() {
        return rect.vertex2();
    }

    public Vector3D getVertex3() {
        return rect.vertex3();
    }

    public Vector3D getVertex4() {
        return rect.vertex4();
    }

    private void setVertex1NoCompute(Vector3D vertex1) {
        rect = new Rect3D(vertex1, rect.vertex2(), rect.vertex3(), rect.vertex4(), rect.planeXaxis(), rect.planeYaxis(), rect.planeCoords());
    }

    private void setVertex2NoCompute(Vector3D vertex2) {
        rect = new Rect3D(rect.vertex1(), vertex2, rect.vertex3(), rect.vertex4(), rect.planeXaxis(), rect.planeYaxis(), rect.planeCoords());
    }

    private void setVertex3NoCompute(Vector3D vertex3) {
        rect = new Rect3D(rect.vertex1(), rect.vertex2(), vertex3, rect.vertex4(), rect.planeXaxis(), rect.planeYaxis(), rect.planeCoords());
    }

    private void setVertex4NoCompute(Vector3D vertex4) {
        rect = new Rect3D(rect.vertex1(), rect.vertex2(), rect.vertex3(), vertex4, rect.planeXaxis(), rect.planeYaxis(), rect.planeCoords());
    }

    private void setVertices(Vector3D[] vertices) {
        rect = new Rect3D(vertices[0], vertices[1], vertices[2], vertices[3]);
    }

//    public void setVertex1(Vector3D vertex1) {
//        this.vertex1 = vertex1;
//        computeValues();
//    }
//
//    public void setVertex2(Vector3D vertex2) {
//        this.vertex2 = vertex2;
//        updateXVars();
//
//        normal = planeXaxis.crossProduct(planeYaxis);
//        calculateCenter();
//    }
//
//    public void setVertex3(Vector3D vertex3) {
//        this.vertex3 = vertex3;
//        updateYVars();
//
//        normal = planeXaxis.crossProduct(planeYaxis);
//        calculateCenter();
//    }
//
//    public void updateYVars() {
////        minPlaneY = Math.vertex1(onPlane(vertex1).getY(), onPlane(vertex3).getY());
////        maxPlaneY = Math.vertex3(onPlane(vertex1).getY(), onPlane(vertex3).getY());
//        calculatePlaneYaxis();
//        calculatePlaneCoords();
//    }
//
//    public void updateXVars() {
////        minPlaneX = Math.vertex1(onPlane(vertex1).getX(), onPlane(vertex2).getX());
////        maxPlaneX = Math.vertex3(onPlane(vertex1).getX(), onPlane(vertex2).getX());
//        calculatePlaneXaxis();
//        calculatePlaneCoords();
//    }

//    public void calculatePlaneYaxis() {
//        planeYaxis = vertex4.subtract(vertex1);
//    }
//
//    public void calculatePlaneXaxis() {
//        planeXaxis = vertex2.subtract(vertex1);
//    }
//
//    public void setVertex4(Vector3D vertex) {
//        this.vertex4 = vertex;
//    }

    @Override
    public String toString() {
        return "QuadGraphics: " + getVertex1() + ", " + getVertex2() + ", " + getVertex3() + ", " + getVertex4() + ", " + getTexture();
    }
}
