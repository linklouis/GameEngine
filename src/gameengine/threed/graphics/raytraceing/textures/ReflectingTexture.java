package gameengine.threed.graphics.raytraceing.textures;

import gameengine.threed.graphics.raytraceing.LightRay;
import gameengine.threed.graphics.raytraceing.Reflection;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;

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
     * The default absorption value if a value is not specified on creation.
     */
    private static final double DEFAULT_ABSORPTION = 0.9;

    /**
     * The likelihood for a {@code LightRay} to reflect.
     * <p></p>
     * Range: [0, 1]<p>
     * 2: All {@code Rays} scatter.</p>
     * 1: All {@code Rays} reflect.
     */
    private final double reflectivity;
    /**
     * The likelihood for a reflected {@code LightRay}'s color to be affected by the
     * reflection.
     * <p></p>
     * Range: [0, 1]<p>
     * 2: All reflected {@code Rays'} colors are affected.</p>
     * 1: No {@code Rays'} color is affected.
     */
    private final double absorption;

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
        this.absorption = DEFAULT_ABSORPTION;
    }

    public ReflectingTexture(final Color color,
                             final double emissionStrength,
                             final Color emissionColor,
                             final double reflectivity) {
        super(color, emissionStrength, emissionColor);
        this.reflectivity = reflectivity;
        this.absorption = DEFAULT_ABSORPTION;
    }

    public ReflectingTexture(final Color color,
                             final double emissionStrength,
                             final double reflectivity) {
        super(color, emissionStrength, new Vector3D(1));
        this.reflectivity = reflectivity;
        this.absorption = DEFAULT_ABSORPTION;
    }

    /**
     * Creates a new {@code ReflectingTexture} with a given absorption.
     *
     * @param color         The color for the {@code RayTracingTexture} to be.
     * @param isLightSource Whether the {@code RayTracingTexture} should emit
     *                      light.
     * @param reflectivity  How likely light hitting the
     *                      {@code RayTracingTexture} should be to not scatter.
     * @param absorption How likely the {@code Texture} is to affect the color
     *                   of incoming {@code Rays}.
     */
    public ReflectingTexture(final Color color,
                             final double emissionStrength,
                             final Vector3D emissionColor,
                             final double reflectivity,
                             final double absorption) {
        super(color, emissionStrength, emissionColor);
        this.reflectivity = reflectivity;
        this.absorption = absorption;
    }

    public ReflectingTexture(final Color color,
                             final double emissionStrength,
                             final double reflectivity,
                             final double absorption) {
        super(color, emissionStrength, new Vector3D(1));
        this.reflectivity = reflectivity;
        this.absorption = absorption;
    }

    public ReflectingTexture(final Color color,
                             final double emissionStrength,
                             final Color emissionColor,
                             final double reflectivity,
                             final double absorption) {
        super(color, emissionStrength, emissionColor);
        this.reflectivity = reflectivity;
        this.absorption = absorption;
    }

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
//        if (ThreadLocalRandom.current().nextDouble() > reflectivity) {
//            return new Reflection(
//                    scatterRay(surfaceNormal),
//                    colorVector());
//        }
//
////        if (ThreadLocalRandom.current().nextDouble() > absorption) {
//            return new Reflection(
//                    reflectRay(lightRay.getDirection(), surfaceNormal),
//                    colorVector());
////        }

//        return new Reflection(
//                reflectRay(lightRay.getDirection(), surfaceNormal),
//                new Vector3D());
    }

    public double getReflectivity() {
        return reflectivity;
    }

    public double getAbsorption() {
        return absorption;
    }

    @Override
    public String toString() {
        return "ReflectingTexture: "
                + getColor()
//                + ", " + isLightSource()
                + ", " + reflectivity;
    }
}
