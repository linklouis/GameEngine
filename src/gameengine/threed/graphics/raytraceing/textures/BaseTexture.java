package gameengine.threed.graphics.raytraceing.textures;

import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

/**
 * The most basic possible texture, which always scatters light with no
 * exceptions.
 *
 * @author Louis Link
 * @since 1.0
 */
public class BaseTexture extends RayTracingTexture {
    /**
     * Creates a new {@code BaseTexture}.
     *
     * @param color The color of the surface
     * @param isLightSource Whether the surface emits light
     */
    public BaseTexture(final Color color, final boolean isLightSource) {
        super(color, isLightSource);
    }

    /**
     * Scatters the incoming {@code Ray} and ensures that it is facing in a
     * valid direction.
     *
     * @param rayDirection The direction of the incoming {@code Ray}.
     * @param surfaceNormal The normal vector of the surface. Magnitude is not
     *                      variable.
     * @return The new direction for the {@code Ray} to move in.
     */
    @Override
    public Vector3D reflection(final Vector3D rayDirection,
                               final Vector3D surfaceNormal) {
        return scatterRay(surfaceNormal);
    }
}
