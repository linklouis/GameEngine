package gameengine.threed.graphics.raytraceing.textures;

import gameengine.threed.graphics.raytraceing.LightRay;
import gameengine.threed.graphics.raytraceing.Reflection;
import gameengine.utilities.ExtraMath;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public abstract class SubsurfaceTexture extends RayTracingTexture {
    private final double specularProbability;

    public SubsurfaceTexture(final Color color,
                             final double emissionStrength,
                             final Vector3D emissionColor,
                             final double specularProbability) {
        super(color, emissionStrength, emissionColor);
        this.specularProbability = specularProbability;
    }

    public SubsurfaceTexture(final Color color,
                             final double emissionStrength,
                             final Color emissionColor,
                             final double specularProbability) {
        super(color, emissionStrength, emissionColor);
        this.specularProbability = specularProbability;
    }

    public SubsurfaceTexture(final Color color,
                             final double emissionStrength,
                             final double specularProbability) {
        super(color, emissionStrength, new Vector3D(1));
        this.specularProbability = specularProbability;
    }

    @Override
    public final Reflection reflection(final LightRay lightRay,
                                 final Vector3D surfaceNormal) {
        if (ExtraMath.selectRandom(true, false, specularProbability)) {
            return new Reflection(
                    reflectRay(lightRay.getDirection(), surfaceNormal),
                    lightRay.getColor());
        }
        return nonSpecularReflection(lightRay, surfaceNormal);
    }

    protected abstract Reflection nonSpecularReflection(final LightRay lightRay, final Vector3D surfaceNormal);

    public double getSpecularProbability() {
        return specularProbability;
    }
}
