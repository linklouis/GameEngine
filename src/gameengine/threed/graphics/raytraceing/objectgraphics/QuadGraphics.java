package gameengine.threed.graphics.raytraceing.objectgraphics;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.geometry.Ray;
import gameengine.threed.geometry.Rect3D;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;

import java.util.List;

public non-sealed class QuadGraphics extends RayTraceable {
    // TODO keep a position variable, and add it to rect's vertices position to not make a new Rect3D for each movement.
    private Rect3D rect;


    /*
     * Construction:
     */

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return null;
    }

    @Override
    public RayTraceableStruct toStruct(Ray perspective) {
        return new RayTraceableStruct(
                surfaceNormal(perspective), rect.vertex1(),
                rect.planeXaxis(), rect.planeYaxis(),
                rect.planeCoords().max(), rect.planeCoords().min());
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

    /**
     * Returns the normal vector of the intersectable object facing towards the
     * {@code VectorLine}.
     *
     * @param perspective
     * @return
     */
    @Override
    public Vector3D surfaceNormal(Ray perspective) {
        if (rect.normal().dotProduct(perspective.getDirection()) > 0) {
            return rect.normal().scalarMultiply(-1);
        }
        return rect.normal();
    }

    /**
     * Finds the first intersection a lightRay will have with the
     * {@code Collider3D}.
     *
     * @param ray               The lightRay to find a collision with.
     * @param curSmallestDist   The largest distance the output is looking for.
     *                          Can be used for optimization by counting out a
     *                          {@code Collider3D} early.
     * @param amountInDirection
     * @return NaN if never enters range or if collision is behind start.
     * Otherwise, the distance to first hit
     */
    @Override
    public double distanceToCollide(final Ray ray, final double curSmallestDist, double amountInDirection) {
        double distance = rect.distanceToCollide(ray);
        return rect.contains(ray, distance) ? distance : Double.NaN;
    }


    /*
     * Utilities:
     */

    @Override
    public Vector3D[] getVertices() {
        return rect.vertices();
    }

    @Override
    public Vector3D getCenter() {
        return rect.vertex1();
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

    public Vector3D getPlaneXAxis() {
        return rect.planeXaxis();
    }

    public Vector3D getPlaneYAxis() {
        return rect.planeYaxis();
    }

    public Vector2D getOnPlaneMin() {
        return rect.planeCoords().min();
    }

    public Vector2D getOnPlaneMax() {
        return rect.planeCoords().max();
    }

    private void setVertices(Vector3D[] vertices) {
        rect = new Rect3D(vertices[0], vertices[1], vertices[2], vertices[3]);
    }

    @Override
    public double getRange() {
        return rect.range();
    }

    @Override
    public String toString() {
        return "QuadGraphics: " + getVertex1() + ", " + getVertex2() + ", "
                + getVertex3() + ", " + getVertex4() + ", " + getTexture();
    }
}
