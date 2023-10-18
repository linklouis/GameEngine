import gameengine.objects.GameObject;
import gameengine.prebuilt.InPlane3D;
import gameengine.prebuilt.objectmovement.collisions.Collider3D;
import gameengine.prebuilt.objectmovement.collisions.SphereCollider;
import gameengine.vectormath.Vector3D;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class LightRay extends Vector3D {
    private final Vector3D start;
    private Vector3D position;
    private final double maxDistance;
    private final int numBounces;
    private Vector3D color = Vector3D.empty();

    private static boolean hitlight = false;

    private final Collider3D[] objectsInField;

    /*
     * Construction:
     */

    public LightRay(Vector3D start, double x, double y, double z, double marchDistance,
                    double maxDistance, int numBounces, Collider3D[] objectsInField) {
        super(new Vector3D(x, y, z)
                .unitVector()
                .scalarMultiply(marchDistance)
                .getComponents());
        this.start = start;
        position = start;
        this.maxDistance = maxDistance;
        this.numBounces = numBounces;
        this.objectsInField = objectsInField;
    }

    public LightRay(Vector3D start, Vector3D vector, double marchDistance,
                    double maxDistance, int numBounces, Collider3D[] objectsInField) {
        super(vector
                .unitVector()
                .scalarMultiply(marchDistance)
                .getComponents());
        this.start = start;
        position = start;
        this.maxDistance = maxDistance;
        this.numBounces = numBounces;
        this.objectsInField = objectsInField;
    }

    public LightRay(Vector3D start, Vector3D direction, LightRay ray) {
        super(direction
                .unitVector()
                .scalarMultiply(ray.magnitude())
                .getComponents());
        this.start = start;
        position = start;
        this.maxDistance = ray.maxDistance;
        this.numBounces = ray.numBounces;
        this.objectsInField = ray.objectsInField;
    }


    /*
     * Functionality:
     */

    public Collider3D<?> firstCollision() {
        Collider3D<?>[] potentialColliders = potentialColliders();

        while (position.subtract(start).magnitude() < maxDistance) {
            for (Collider3D<?> collider : potentialColliders) {
                if (collider.contains(position)) {
//                    System.out.println("Ray collided" + collider + ", " + vectorFromColor(collider.getColor()));
                    return collider;
                }
            }

            position = add(position);
        }

//        System.out.println("did not hit");
        return null;
    }

    private double distanceTraveled() {
        return Math.abs(position.subtract(start).magnitude());
    }

    public Color getColorFromBounces() {
        int bounces = 1;
        LightRay currentRay = new LightRay(start, this, this);
        while (bounces < numBounces + 1) {
            Collider3D<?> collision = currentRay.firstCollision();
            if (collision != null) {
                color = color.add(vectorFromColor(collision.getColor())
                        .scalarDivide(bounces));
//                        .scalarDivide(currentRay.distanceTraveled()));
//                System.out.println(color);
                if (collision instanceof SphereCollider) {
                    if (((SphereCollider) collision).isLightSource()) {
//                        if (/*bounces == 2 &&*/ !hitlight) {
//                            System.out.println("Hit Light!");
//                            hitlight = true;
//                        }
                        return colorFromVector(color);
                    }
                }
//                Vector3D newDirection = currentRay.add(
//                        currentRay.scalarMultiply(currentRayInDirection * -2)); // HELP Assume collision is a sphere, I want to rays to bounce off realistically.
//                currentRay = new LightRay(currentRay.position.subtract(currentRay), newDirection, this);

//                double currentRayInDirection = currentRay.dotProduct(collision.getCenter().subtract(currentRay.position));
//
//                // Calculate the incident vector
//                Vector3D incident = collision.getCenter().subtract(currentRay.position);
//
//                // Calculate the surface normal at the collision point
//                Vector3D surfaceNormal = incident.unitVector();
//
//                // Calculate the reflection vector
//                Vector3D reflection = incident.subtract(surfaceNormal.scalarMultiply(2 * currentRayInDirection));
                // Calculate the normal vector for the sphere collider
                Vector3D surfaceNormal = currentRay.position.subtract(collision.getCenter()).unitVector();
//
//                double currentRayInDirection = currentRay.dotProduct(surfaceNormal);
//                Vector3D incident = currentRay.unitVector().scalarMultiply(-1);
//
//                Vector3D reflection = incident.subtract(surfaceNormal.scalarMultiply(2 * currentRayInDirection));
                Vector3D reflection = Vector3D.random(0, 1);
                if (reflection.dotProduct(surfaceNormal) < 0) {
                    reflection = reflection.scalarMultiply(-1);
                }

                // Create a new LightRay with the reflection direction
                currentRay = new LightRay(currentRay.position.subtract(currentRay), reflection, this);

//                if (currentRay.dotProduct(currentRay.subtract(collision.getCenter())) < 0) {
//                    currentRay = new LightRay(currentRay, reflection.scalarMultiply(-1), this);
//                }
//                if (bounces == 2) {
//                    System.out.println(collision);//currentRay.dotProduct(currentRay.subtract(collision.getCenter())));
//                }
            } else {
//                if (bounces > 1) {
//                    return Color.LAVENDER;
//                }
                return Color.BLACK;
            }

            bounces++;
        }
        return Color.BLACK;//colorFromVector(color);
    }

    public static Vector3D vectorFromColor(Color color) {
        return new Vector3D(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static Color colorFromVector(Vector3D colorVector) {
        return new Color(
                clamp(colorVector.getX(), 0, 1),
                clamp(colorVector.getY(), 0, 1),
                clamp(colorVector.getZ(), 0, 1),
                1);
    }

    private static double clamp(double num, double min, double max) {
        return Math.min(max, Math.max(min, num));
    }

    private Collider3D<?>[] potentialColliders() {
//        System.out.println(Arrays.stream(objectsInField)
//                .filter(gameObject ->
//                        gameObject.containsModifier(Collider3D.class))
//                .map(gameObject -> gameObject.get(Collider3D.class)));
//        return Arrays.stream(objectsInField)
//                .filter(gameObject ->
//                        gameObject.containsModifier(Collider3D.class))
////                .filter(gameObject ->
////                        this.dotProduct(
////                                gameObject.get(InPlane3D.class).getLocation()
////                                        .subtract(start)
////                        ) > 0)
//                .map(gameObject -> gameObject.get(Collider3D.class))
//                .toArray(Collider3D<?>[]::new);
        return objectsInField;
    }


    /*
     * Utilities:
     */

    public Color getColor() {
        return colorFromVector(color);
    }

    public void setColor(Vector3D color) {
        this.color = color;
    }
}
