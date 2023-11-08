package gameengine.threed.prebuilt.gameobjects;

import gameengine.threed.graphics.raytraceing.Ray;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.threed.graphics.raytraceing.textures.ReflectingTexture;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public class Sphere extends RayTraceable {
    private double radius;
    private double radiusSquared;
    private Vector3D position;
//    private final SphereGraphics graphics;

    public Sphere(double x, double y, double z, double r, RayTracingTexture texture) {
        position = new Vector3D(x, y, z);
        radius = r;
        setTexture(texture);
//        graphics = new SphereGraphics(r, texture, this::getPosition);
    }

    public Sphere(Vector3D position, double r, RayTracingTexture texture) {
        this.position = position;
        radius = r;
        setTexture(texture);
//        graphics = new SphereGraphics(r, texture, this::getPosition);
    }

    public Sphere(double x, double y, double z, double r, Color color, boolean isLightSource) {
        position = new Vector3D(x, y, z);
        radius = r;
        setTexture(new ReflectingTexture(color, isLightSource, 0));
//        graphics = new SphereGraphics(
//                r,
//                new ReflectingTexture(color, isLightSource, 0),
//                this::getPosition);
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

    public boolean contains(Vector3D point) {
        return point.distance(getCenter()) <= radius;
    }

    public Vector3D getPosition() {
        return position;
    }
}
