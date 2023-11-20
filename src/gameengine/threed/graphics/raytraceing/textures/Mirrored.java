package gameengine.threed.graphics.raytraceing.textures;

import gameengine.threed.graphics.raytraceing.LightRay;
import gameengine.threed.graphics.raytraceing.Reflection;
import gameengine.utilities.ExtraMath;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public class Mirrored extends SubsurfaceTexture {
    /**
     * The likelihood for a {@code LightRay} to reflect.
     * <p></p>
     * Range: [0, 1]<p>
     * 2: All {@code Rays} scatter.</p>
     * 1: All {@code Rays} reflect.
     */
    private final double reflectivity;


    /**
     * Creates a new {@code Metallic} with a default absorption value
     * of 0.1.
     *
     * @param color         The color for the {@code RayTracingTexture} to be.
     * @param isLightSource Whether the {@code RayTracingTexture} should emit
     *                      light.
     * @param reflectivity  How likely light hitting the
     *                      {@code RayTracingTexture} should be to not scatter.
     */
    public Mirrored(final Color color,
                    final double emissionStrength,
                    final Vector3D emissionColor,
                    final double reflectivity) {
        super(color, emissionStrength, emissionColor, 0);
        this.reflectivity = reflectivity;
    }

    public Mirrored(final Color color,
                    final double emissionStrength,
                    final Color emissionColor,
                    final double reflectivity) {
        super(color, emissionStrength, emissionColor, 0);
        this.reflectivity = reflectivity;
    }

    public Mirrored(final Color color,
                    final double emissionStrength,
                    final double reflectivity) {
        super(color, emissionStrength, new Vector3D(1), 0);
        this.reflectivity = reflectivity;
    }

    public Mirrored(final Color color,
                    final double emissionStrength,
                    final Vector3D emissionColor,
                    final double reflectivity,
                    final double specularProbability) {
        super(color, emissionStrength, emissionColor, specularProbability);
        this.reflectivity = reflectivity;
    }

    public Mirrored(final Color color,
                    final double emissionStrength,
                    final Color emissionColor,
                    final double reflectivity,
                    final double specularProbability) {
        super(color, emissionStrength, emissionColor, specularProbability);
        this.reflectivity = reflectivity;
    }

    public Mirrored(final Color color,
                    final double emissionStrength,
                    final double reflectivity,
                    final double specularProbability) {
        super(color, emissionStrength, new Vector3D(1), specularProbability);
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
    public Reflection nonSpecularReflection(final LightRay lightRay,
                                            final Vector3D surfaceNormal) {
        return new Reflection(
                ExtraMath.selectRandom(() -> reflectRay(lightRay.getDirection(), surfaceNormal), () -> scatterRay(surfaceNormal), reflectivity),
                defaultColorUpdate(lightRay.getColor()));
    }

    public double getReflectivity() {
        return reflectivity;
    }

    @Override
    public String toString() {
        return "Mirrored: "
                + getColor()
                + ", " + getEmissionColor()
                + ", " + getEmissionStrength()
                + ", " + getEmission()
                + ", " + reflectivity;
    }
}