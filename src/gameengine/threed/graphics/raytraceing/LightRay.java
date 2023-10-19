package gameengine.threed.graphics.raytraceing;

import gameengine.threed.graphics.Visual3D;
import gameengine.threed.prebuilt.objectmovement.collisions.Collider3D;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

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
//        Vector3D actualMoveSize = scalarMultiply(3);
//        if (actualMoveSize.magnitude() > 1) {
//            actualMoveSize = unitVector();
//        }
//        Vector3D checkMoveSize = scalarMultiply(0.1);
        Vector3D actualMoveSize = scalarMultiply(1);
        Vector3D checkMoveSize = scalarMultiply(1);

        while (position.subtract(start).magnitude() < maxDistance) {
            for (Collider3D<?> collider : potentialColliders) {
                if (collider.contains(position)) {
                    position = position.subtract(actualMoveSize);
                    int numMoves = 0;
                    while(!collider.contains(position) && numMoves < 100) {
                        position = position.add(checkMoveSize);
                        numMoves++;
                    }
//                    if (numMoves >= 100) {
//                        System.out.println("a");
//                    }
                    position = position.subtract(checkMoveSize);
//                    System.out.println("Ray collided" + collider + ", " + vectorFromColor(collider.getColor()));
                    return collider;
                }
            }

            position = position.add(actualMoveSize);
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
                color = color.add(new Vector3D(
                        collision.getFromParent(Visual3D.class)
                                .getAppearance().getColor())
                        .scalarDivide((double) bounces /2 + 0.5));
                if (collision.getTexture().isLightSource()) {
                        return color.toColor();
                }

                // Create a new gameengine.threed.graphics.raytraceing.LightRay with the reflection direction
                currentRay = new LightRay(currentRay.position, collision.reflection(currentRay), currentRay);
            } else {
                return Color.BLACK;
            }

            bounces++;
        }
        return Color.BLACK;
    }

    public static double clamp(double num, double min, double max) {
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
        return color.toColor();
    }

    public void setColor(Vector3D color) {
        this.color = color;
    }

    public Vector3D getPosition() {
        return position;
    }

}
