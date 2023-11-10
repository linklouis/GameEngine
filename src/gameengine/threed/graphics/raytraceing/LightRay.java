package gameengine.threed.graphics.raytraceing;

import gameengine.threed.geometry.Ray;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
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
    private Vector3D color;
    private Vector3D incomingLight = new Vector3D();

    /**
     * Creates a new {@code LightRay}.
     *
     * @param startPosition The initial position of the {@code LightRay}.
     * @param direction The direction the {@code LightRay} will move in.
     */
    public LightRay(final Vector3D startPosition, final Vector3D direction) {
        super(startPosition, direction);
        color = new Vector3D(1);
    }

    public LightRay(final Vector3D startPosition, final Vector3D direction, final Vector3D color) {
        super(startPosition, direction);
        this.color = color;
    }

    public LightRay(final Vector3D startPosition, final Vector3D direction, final Vector3D color, final Vector3D incomingLight) {
        super(startPosition, direction);
        this.color = color;
        this.incomingLight = incomingLight;
    }

    public LightRay(final Vector3D startPosition, final Vector3D direction, final Color color) {
        super(startPosition, direction);
        this.color = new Vector3D(color);
    }

    public LightRay(final Vector3D startPosition, final Reflection reflection) {
        super(startPosition, reflection.direction());
        this.color = new Vector3D(reflection.color());
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
        setDirection(reflectionDetails.direction());
//        color.addMutable(reflectionDetails.color().scalarDivideMutable(numBounces));
        RayTracingTexture texture = collider.getTexture();
//        double lightStrength = collider.surfaceNormal(this).dotProduct(direction) * 2;
        color = color.multiplyAcross(reflectionDetails.color()/*.scalarMultiply(lightStrength)*/);
        incomingLight = incomingLight.add(color.multiplyAcross(texture.getEmission()));
    }

    public void reflect(RayTraceable collider) {
        Reflection reflectionDetails = collider.reflection(this);
        setDirection(reflectionDetails.direction());
//        color.addMutable(reflectionDetails.color());
        RayTracingTexture texture = collider.getTexture();
//        double lightStrength = collider.surfaceNormal(this).dotProduct(direction) * 2;
        color = color.multiplyAcross(reflectionDetails.color()/*.scalarMultiply(lightStrength)*/);
        incomingLight = incomingLight.add(color.multiplyAcross(texture.getEmission()));
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
        RayTracingTexture texture = collider.getTexture();
//        double lightStrength = collider.surfaceNormal(this).dotProduct(direction) * 2;

        return new LightRay(new Vector3D(position), reflectionDetails.direction(),
                color.multiplyAcross(reflectionDetails.color()/*.scalarMultiply(lightStrength)*/),
                        incomingLight.add(color.multiplyAcross(texture.getEmission())));
//                reflectionDetails.color().scalarDivideMutable(numBounces)
    }

    public LightRay getReflected(RayTraceable collider) {
        Reflection reflectionDetails = collider.reflection(this);
        RayTracingTexture texture = collider.getTexture();
//        double lightStrength = collider.surfaceNormal(this).dotProduct(direction) * 2;

        return new LightRay(new Vector3D(position), reflectionDetails.direction(),
                color.multiplyAcross(reflectionDetails.color()/*.scalarMultiply(lightStrength)*/),
                        incomingLight.add(color.multiplyAcross(texture.getEmission())));
//                reflectionDetails.color()
    }


    /*
     * Utilities:
     */

    public Vector3D getColor() {
        return color;
    }

    public Vector3D getIncomingLight() {
        return incomingLight;
    }

    public Vector3D litColor() {
        return incomingLight.multiplyAcross(color);
    }
}
