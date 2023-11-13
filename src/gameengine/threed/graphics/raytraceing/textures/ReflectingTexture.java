package gameengine.threed.graphics.raytraceing.textures;

import gameengine.threed.graphics.raytraceing.LightRay;
import gameengine.threed.graphics.raytraceing.Reflection;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

/**
 * A basic implementation of {@link RayTracingTexture} that chooses randomly
 * whether to scatter or reflect a light ray, and, if reflected, whether to
 * impact the color of that ray, which provides a variable reflectivity and a
 * basic imitation of subsurface scattering.
 *
 * @author Louis Link
 * @since 1.0
 */
public class ReflectingTexture extends RayTracingTexture {
    /**
     * The likelihood for a {@code LightRay} to reflect.
     * <p></p>
     * Range: [0, 1]<p>
     * 2: All {@code Rays} scatter.</p>
     * 1: All {@code Rays} reflect.
     */
    private final double reflectivity;

    /**
     * Creates a new {@code ReflectingTexture} with a default absorption value
     * of 0.1.
     *
     * @param color         The color for the {@code RayTracingTexture} to be.
     * @param isLightSource Whether the {@code RayTracingTexture} should emit
     *                      light.
     * @param reflectivity  How likely light hitting the
     *                      {@code RayTracingTexture} should be to not scatter.
     */
    public ReflectingTexture(final Color color,
                             final double emissionStrength,
                             final Vector3D emissionColor,
                             final double reflectivity) {
        super(color, emissionStrength, emissionColor);
        this.reflectivity = reflectivity;
    }

    public ReflectingTexture(final Color color,
                             final double emissionStrength,
                             final Color emissionColor,
                             final double reflectivity) {
        super(color, emissionStrength, emissionColor);
        this.reflectivity = reflectivity;
    }

    public ReflectingTexture(final Color color,
                             final double emissionStrength,
                             final double reflectivity) {
        super(color, emissionStrength, new Vector3D(1));
        this.reflectivity = reflectivity;
    }


    /*
     * Functionality:
     */

    /**
     * Calculates how a light lightRay hitting the surface with a given angle should
     * bounce given that the surface has the given normal vector.
     *
     * @param lightRay The {@code LightRay} incident on the surface
     * @param surfaceNormal The normal vector to the surface where the lightRay is
     *                      hitting.
     * @return The direction that the light lightRay should bounce as a
     *          {@link Vector3D}.
     */
    @Override
    public Reflection reflection(final LightRay lightRay,
                                 final Vector3D surfaceNormal) {
        return new Reflection(
                scatterRay(surfaceNormal).blendWith(reflectRay(lightRay.getDirection(), surfaceNormal), reflectivity),
                defaultColorUpdate(lightRay.getColor()));
    }

    public double getReflectivity() {
        return reflectivity;
    }

    @Override
    public String toString() {
        return "ReflectingTexture: "
                + getColor()
                + ", " + getEmissionColor()
                + ", " + getEmissionStrength()
                + ", " + getEmission()
                + ", " + reflectivity;
    }
}
