package gameengine.threed.graphics;

import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

/**
 * A basic implementation of {@link Texture} that chooses randomly whether to
 * scatter a light ray, which provides a variable reflectivity and a basic
 * imitation of subsurface scattering.
 *
 * @author Louis Link
 * @since 1.0
 */
public class BaseTexture extends Texture {
    /**
     * Creates a new {@code BaseTexture}.
     *
     * @param color The color for the {@code Texture} to be.
     * @param isLightSource Whether the {@code Texture} should emit light.
     * @param reflectivity How likely light hitting the {@code Texture} should
     *                     be to not scatter.
     */
    public BaseTexture(final Color color,
                       final boolean isLightSource,
                       final double reflectivity) {
        super(color, isLightSource, reflectivity);
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
    public Vector3D reflection(final Vector3D rayDirection,
                               Vector3D surfaceNormal) {
        Vector3D reflection;
        surfaceNormal = surfaceNormal.unitVector();

        if (Math.random() > getReflectivity()) {
            reflection = Vector3D.random(-1, 1);
            if (reflection.dotProduct(surfaceNormal) < 0) {
                reflection = reflection.scalarMultiply(-1);
            }
            return reflection;
        } else {
            Vector3D incident = surfaceNormal
                    .scalarMultiply(rayDirection.dotProduct(surfaceNormal));
            return rayDirection.subtract(incident.scalarMultiply(2));
        }
    }
}
