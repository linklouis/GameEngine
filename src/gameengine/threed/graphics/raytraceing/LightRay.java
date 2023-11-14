package gameengine.threed.graphics.raytraceing;

import gameengine.threed.geometry.Ray;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayIntersectableList;
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

    private static Vector3D sunPos = new Vector3D(2, 0,3).unitVector();
    public static Vector3D getSkyColor(Vector3D dir) {
//        if (dir.angleInDegreesWith(sunPos) < 4) {
//            return new Vector3D(Color.LIGHTYELLOW).scalarMultiply(3 * Math.exp(-Math.abs(dir.dotProduct(sunPos))));
//        }
        return new Vector3D(Color.SKYBLUE).scalarMultiply(0.3 * (dir.dotProduct(sunPos) + 0.7)).add(new Vector3D(Color.LIGHTYELLOW).scalarMultiply(3 / (1 + Math.pow(dir.angleInDegreesWith(sunPos), 2))));//.scalarMultiply(3 * Math.exp(-Math.abs(dir.angleInDegreesWith(sunPos) / 2))));
    }

    public Vector3D getColor(final int maxBounces,
                             final RayIntersectableList objectsInField, int startingBounce) {
        RayTraceable collision;

        for (int bounces = startingBounce; bounces <= maxBounces; bounces++) {
            collision = (RayTraceable) firstCollision(objectsInField);

            if (collision == null) {
//                System.out.println("a");
                return getIncomingLight();//.add(getSkyColor(getDirection()));
            }

            reflect(collision);

            if (collision.getTexture().getColor().equals(Color.BLACK)) {
                return getIncomingLight();
            }
        }

        return getIncomingLight();
    }

    /**
     * Updates the {@code LightRay}'s direction based on its reflection of off the
     * given {@link RayTraceable}.
     *
     * @param collider The {@link RayTraceable} to reflect off of.
     */
    @Override
    public void reflect(RayTraceable collider) {
        Reflection reflectionDetails = collider.reflection(this);
        setDirection(reflectionDetails.direction());
        incomingLight = updatedIncomingLight(collider.getTexture().getEmission());
        color = reflectionDetails.color();
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
    public LightRay getReflected(RayTraceable collider) {
        Reflection reflectionDetails = collider.reflection(this);
        return new LightRay(new Vector3D(position), reflectionDetails.direction(),
                reflectionDetails.color(),
                updatedIncomingLight(collider.getTexture().getEmission()));
    }

    private Vector3D updatedIncomingLight(Vector3D emission) {
        return incomingLight.add(color.multiplyAcross(emission));
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
}
