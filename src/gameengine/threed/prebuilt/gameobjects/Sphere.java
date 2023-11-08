package gameengine.threed.prebuilt.gameobjects;

import gameengine.threed.graphics.raytraceing.Ray;
import gameengine.threed.graphics.objectgraphics.skeletons.RayTraceable;
import gameengine.threed.graphics.objectgraphics.textures.RayTracingTexture;
import gameengine.threed.graphics.objectgraphics.textures.ReflectingTexture;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public class Sphere extends RayTraceable {
    private double radius;
    private double radiusSquared;
    private Vector3D position;

    public Sphere(double x, double y, double z, double r, RayTracingTexture texture) {
        position = new Vector3D(x, y, z);
        setRadius(r);
        setTexture(texture);
    }

    public Sphere(Vector3D position, double r, RayTracingTexture texture) {
        this.position = position;
        setRadius(r);
        setTexture(texture);
    }

    public Sphere(double x, double y, double z, double r, Color color, boolean isLightSource) {
        position = new Vector3D(x, y, z);
        setRadius(r);
        setTexture(new ReflectingTexture(color, isLightSource, 0));
    }


    /*
     * Functionality:
     */

    @Override
    public Vector3D surfaceNormal(Ray perspective) {
        return perspective.getPosition().subtract(getCenter())/*.unitVector()*/;
    }

    /**
     * Finds the first intersection a lightLightRay would have with the Sphere.
     *
     * @param lightLightRay The lightLightRay to find a collision with.
     * @param curSmallestDist The largest distance the output is looking for.
     *                        not used for optimization for Spheres.
     * @return {@code NaN} if never enters range or if collision is behind start.
     * Otherwise, the distance to first hit
     */
    @Override
    public double distanceToCollide(Ray ray, double curSmallestDist) {
        double b = -ray.getDirection().dotWithSubtracted(ray.getPosition(), getCenter());
        return b - Math.sqrt(b * b + radiusSquared - ray.getPosition().distanceSquared(getCenter()));
    }


    /*
     * Utilities:
     */

    @Override
    public Vector3D[] getVertices() {
        return new Vector3D[0];
    }

    @Override
    public Vector3D getCenter() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        radiusSquared = radius * radius;
    }

    public boolean contains(Vector3D point) {
        return point.distance(getCenter()) <= radius;
    }

    public Vector3D getPosition() {
        return position;
    }
}
