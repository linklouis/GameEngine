package gameengine.threed.graphics.raytraceing;

import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

/**
 * A single ray of light which starts at a point and moves in a direction until
 * it hits a {@link RayTraceable}.
 *
 * @author Louis Link
 * @since 1.0
 */
public class LightRay extends Ray {
    private final Vector3D color;

    /**
     * Creates a new {@code LightRay}.
     *
     * @param startPosition The initial position of the {@code LightRay}.
     * @param direction The direction the {@code LightRay} will move in.
     */
    public LightRay(final Vector3D startPosition, final Vector3D direction) {
        super(startPosition, direction);
        color = new Vector3D(0);
    }

    public LightRay(final Vector3D startPosition, final Vector3D direction, final Vector3D color) {
        super(startPosition, direction);
        this.color = color;
    }

    public LightRay(final Vector3D startPosition, final Vector3D direction, final Color color) {
        super(startPosition, direction);
        this.color = new Vector3D(color);
    }


    /*
     * Functionality:
     */

    /**
     * Updates the {@code LightRay}'s direction based on its reflection of off the
     * given {@link RayTraceable}.
     *
     * @param collider The {@link RayTraceable} to reflect off of.
     */
    @Override
    public void reflect(RayTraceable collider, int numBounces) {
        Reflection reflectionDetails = collider.reflection(this);
        direction = reflectionDetails.direction().unitVector();
        color.addMutable(reflectionDetails.color().scalarDivide(numBounces));
    }

    /**
     * Returns a new {@code LightRay} representing the current {@code LightRay} after
     * reflecting off of {@code collider}. Assumes the current {@code LightRay} is
     * colliding with {@code collider}.
     *
     * @param collider The {@link RayTraceable} to reflect off of.
     * @return A new {@code LightRay} representing the current {@code LightRay} after
     * reflecting off of {@code collider}.
     */
    @Override
    public LightRay getReflected(RayTraceable collider, int numBounces) {
        Reflection reflectionDetails = collider.reflection(this);
        return new LightRay(position, reflectionDetails.direction(),
                reflectionDetails.color().scalarDivide(numBounces));
    }


    /*
     * Utilities:
     */

    public Vector3D getColor() {
        return color;
    }
}
