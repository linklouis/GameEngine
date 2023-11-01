package gameengine.threed.graphics.raytraceing.textures;

import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;

public class SubsurfaceTexture extends RayTracingTexture {
    private final double minimumReflectionAngle;
    private final double randomness;

    public SubsurfaceTexture(final Color color, final boolean isLightSource,
                             final double minimumReflectionAngle, double randomness) {
        super(color, isLightSource);
        this.minimumReflectionAngle = minimumReflectionAngle;
        this.randomness = randomness;
    }

    @Override
    public Vector3D reflection(final Vector3D rayDirection, final Vector3D surfaceNormal) {
        if (rayDirection.angleInDegreesWith(surfaceNormal.scalarMultiply(-1))
                + ThreadLocalRandom.current()
                .nextDouble(-randomness, randomness)
                >= minimumReflectionAngle) {
            return reflectRay(rayDirection, surfaceNormal);
        }
        return scatterRay(surfaceNormal);
    }
}
