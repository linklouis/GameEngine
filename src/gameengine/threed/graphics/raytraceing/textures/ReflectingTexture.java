package gameengine.threed.graphics.raytraceing.textures;

import gameengine.threed.graphics.raytraceing.Ray;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;

/**
 * A basic implementation of {@link RayTracingTexture} that chooses randomly whether to
 * scatter a light ray, which provides a variable reflectivity and a basic
 * imitation of subsurface scattering.
 *
 * @author Louis Link
 * @since 1.0
 */
public class ReflectingTexture extends RayTracingTexture {
    private final double reflectivity;

    /**
     * Creates a new {@code ReflectingTexture}.
     *
     * @param color The color for the {@code RayTracingTexture} to be.
     * @param isLightSource Whether the {@code RayTracingTexture} should emit light.
     * @param reflectivity How likely light hitting the {@code RayTracingTexture} should
     *                     be to not scatter.
     */
    public ReflectingTexture(final Color color,
                             final boolean isLightSource,
                             final double reflectivity) {
        super(color, isLightSource);
        this.reflectivity = reflectivity;
    }

    /**
     * Calculates how a light ray hitting the surface with a given angle should
     * bounce given that the surface has the given normal vector.
     *
     * @param rayDirection The direction at which the light ray is incident to
     *                     the surface.
     * @param surfaceNormal The normal vector to the surface where the ray is
     *                      hitting.
     * @return The direction that the light ray should bounce as a
     *          {@link Vector3D}.
     */
    @Override
    public Ray reflection(final Ray ray,
                               final Vector3D surfaceNormal) {
        if (ThreadLocalRandom.current().nextDouble() > getReflectivity()) {
            return new Ray(
                    ray.getPosition(),
                    scatterRay(surfaceNormal),
                    colorVector());
        } else {
            if (ThreadLocalRandom.current().nextDouble() > 0.7) {
                return new Ray(
                        ray.getPosition(),
                        reflectRay(ray.getDirection(), surfaceNormal),
                        colorVector());
            }
            return new Ray(
                    ray.getPosition(),
                    reflectRay(ray.getDirection(), surfaceNormal),
                    new Vector3D(0));
        }
    }

    public double getReflectivity() {
        return reflectivity;
    }

    @Override
    public String toString() {
        return "ReflectingTexture: " + getColor() + ", " + isLightSource() + ", " + reflectivity;
    }
}
