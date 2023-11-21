package gameengine.threed.graphics.raytraceing.textures;

import gameengine.threed.graphics.raytraceing.LightRay;
import gameengine.threed.graphics.raytraceing.Reflection;
import gameengine.utilities.ExtraMath;
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
public class MatteSubsurface extends RayTracingTexture {
    /**
     * The likelihood for a {@code LightRay} to reflect.
     * <p></p>
     * Range: [0, 1]<p>
     * 2: All {@code Rays} scatter.</p>
     * 1: All {@code Rays} reflect.
     */
    private final double smoothness;
    private final double specularProbability;

    /**
     * Creates a new {@code Metallic} with a default absorption value
     * of 0.1.
     *
     * @param isLightSource       Whether the {@code RayTracingTexture} should emit
     *                            light.
     * @param color               The color for the {@code RayTracingTexture} to be.
     * @param smoothness          How likely light hitting the
     *                            {@code RayTracingTexture} should be to not scatter.
     * @param specularProbability
     */
    public MatteSubsurface(final Color color,
                      final double emissionStrength,
                      final Vector3D emissionColor,
                      final double smoothness,
                      final double specularProbability) {
        super(color, emissionStrength, emissionColor);
        this.smoothness = smoothness;
        this.specularProbability = specularProbability;
    }

    public MatteSubsurface(final Color color,
                      final double emissionStrength,
                      final Color emissionColor,
                      final double smoothness,
                      final double specularProbability) {
        super(color, emissionStrength, emissionColor);
        this.smoothness = smoothness;
        this.specularProbability = specularProbability;
    }

    public MatteSubsurface(final Color color,
                      final double emissionStrength,
                      final double smoothness,
                      final double specularProbability) {
        super(color, emissionStrength, new Vector3D(1));
        this.smoothness = smoothness;
        this.specularProbability = specularProbability;
    }


    /*
     * Functionality:
     */

    @Override
    protected int type() {
        return RayTracingTexture.MATTE_SUBSURFACE;
    }

    @Override
    protected float[] getOtherVars() {
        return new float[] {(float) smoothness, (float) specularProbability};
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
        if (ExtraMath.selectRandom(() -> true, () -> false, specularProbability)) {
            return new Reflection(
                    scatterRay(surfaceNormal).blendWith(reflectRay(lightRay.getDirection(), surfaceNormal), smoothness),
                    lightRay.getColor());
        }
        return new Reflection(
                scatterRay(surfaceNormal),
                defaultColorUpdate(lightRay.getColor()));
    }

    public double getSmoothness() {
        return smoothness;
    }

    @Override
    public String toString() {
        return "Metallic: "
                + getColor()
                + ", " + getEmissionColor()
                + ", " + getEmissionStrength()
                + ", " + getEmission()
                + ", " + smoothness;
    }
}
