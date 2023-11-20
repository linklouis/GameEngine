package gameengine.threed.graphics.raytraceing.objectgraphics;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.geometry.Ray;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;

import java.util.ArrayList;
import java.util.List;

public non-sealed class SphereGraphics extends RayTraceable {
    private double radius;
    private double radiusSquared;
    InPlane3D position = null;


    /*
     * Construction:
     */

    public SphereGraphics() {
        super();
    }

    public SphereGraphics(Modifier... modifiers) {
        super(modifiers);
    }
    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane3D.class);
            }
        };
    }

    @Override
    public RayTraceableStruct toStruct(Ray perspective) {
        return new RayTraceableStruct(
                surfaceNormal(perspective).toStruct(),
                getCenter().toStruct(),
                radius);
    }

    @Override
    public Vector3D[] getVertices() {
        return new Vector3D[0];
    }

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
                        new ModifierInstantiateParameter<>(
                                "radius", Double.class,
                                this::setRadius),
                        new ModifierInstantiateParameter<>(
                                "texture", RayTracingTexture.class,
                                this::setTexture)
                )
        };
    }

    @Override
    public void instantiate(GameObject parent, Object... args) {
        try {
            super.instantiate(parent, args);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        position = getFromParent(InPlane3D.class);
    }


    /*
     * Functionality:
     */

    @Override
    public Vector3D surfaceNormal(Ray perspective) {
        return perspective.getPosition().subtract(getCenter()).unitVector();
    }

    /**
     * Finds the first intersection a lightRay would have with the Sphere.
     *
     * @param lightRay          The lightRay to find a collision with.
     * @param curSmallestDist   The largest distance the output is looking for.
     *                          not used for optimization for Spheres.
     * @param amountInDirection
     * @return {@code NaN} if never enters range or if collision is behind start.
     * Otherwise, the distance to first hit
     */
//    @Override
    public double distanceToCollide(Ray lightRay, double curSmallestDist, double amountInDirection) {
//        double b = -lightRay.getDirection().dotWithSubtracted(lightRay.getPosition(), getCenter());
//
//        if (b > 0) {
//            return b - Math.sqrt(b * b + radiusSquared - lightRay.getPosition().distanceSquared(getCenter()));
//        }
//        return Double.NaN;
//        double b = -lightRay.getDirection().dotWithSubtracted(lightRay.getPosition(), getCenter());

        if (amountInDirection <= 0) {
            return -amountInDirection - Math.sqrt(amountInDirection * amountInDirection + radiusSquared - lightRay.getPosition().distanceSquared(getCenter()));
        }
        return Double.NaN;
    }

    public double distanceToCollideMoreTests(Ray lightRay, double curSmallestDist) {
        double b = -lightRay.getDirection().dotWithSubtracted(lightRay.getPosition(), getCenter());
//        double a = lightRay.getPosition().distanceSquared(getCenter());

        if (b > 0) {
//            if ((!Double.isNaN(b - Math.sqrt(b * b + radiusSquared - a))) && b - Math.sqrt(b * b + radiusSquared - a) >= 0) {
//                System.out.println("A " + (b - Math.sqrt(b * b + radiusSquared - a)));
//            }
//            return b - Math.sqrt(b * b + radiusSquared - a);
            return b - Math.sqrt(b * b + radiusSquared - lightRay.getPosition().distanceSquared(getCenter()));
        }
//        if (!Double.isNaN((b + Math.sqrt(b * b + radiusSquared - a))) && b + Math.sqrt(b * b + radiusSquared - a) >= 0) {
//            System.out.println("B " + (b + Math.sqrt(b * b + radiusSquared - a)));
//        }
        return Double.NaN;//b + Math.sqrt(b * b + radiusSquared - a);
//        if (a + Math.sqrt(b) > radiusSquared) {
//            return Math.sqrt(d) + b;
//        }
//        return a + Math.sqrt(b) > radiusSquared ? Math.sqrt(d) + b : Double.NaN;
    }

    public double distanceToCollideTests(Ray lightRay, double curSmallestDist) {
        double b = lightRay.getDirection().dotWithSubtracted(lightRay.getPosition(), getCenter()) / lightRay.getDirection().magnitude();
        double a = lightRay.getPosition().distanceSquared(getCenter());
        double d = b * b - (a - radius * radius);  // discriminant of quadratic

//        System.out.println();
//        int path = 0;
//
//        if (d <= 0) {
////            System.out.println("c");
////            return -1; // Solutions are complex, no intersections
//        } else {
//
//            d = Math.sqrt(d);
//            double t1 = d - b;
//            double t2 = -(d + b);
//            if (t1 > 0 && (t2 <= 0 || t1 < t2)) {
////            System.out.println("a");
//                path = 1;
//            } else if (t2 > 0) {
//                path = 2;
////            System.out.println("b");
//            }
////        else {
////            System.out.println("c");
////        }
//            d *= d;
//        }

        if (b < 0) {
//            if (path != 2) {
//                System.out.println("b");
//                System.out.println(path);
//                System.out.println(-Math.sqrt(d) - b);
//            }
            return -Math.sqrt(d) - b;
        }
        if (a + Math.sqrt(-b) > radiusSquared) {
//            if (path != 1) {
//                System.out.println("a");
//            }
            return Math.sqrt(d) - b;
        }
//        if (path != 0) {
//            System.out.println("c");
//        }
        return a + Math.sqrt(-b) > radiusSquared ? Math.sqrt(d) - b : Double.NaN;
    }

    public double distanceToCollide2(Ray lightRay, double curSmallestDist, double amountInDirection) {
        if (contains(lightRay.getPosition())) {
            return 0;
        }

        double b = lightRay.getDirection()
                .dotWithSubtracted(lightRay.getPosition(), getCenter())
                * 2 / lightRay.getDirection().magnitude();
        double d = b * b
                - 4 * (lightRay.getPosition().distanceSquared(getCenter())
                        - radius * radius);  // discriminant of quadratic

//        if (d <= 0) {
//            return -1; // Solutions are complex, no intersections
//        }
//        return getCenter().distance(lightRay.getPosition()) - radius;

        // Intersections exists
//        return -Math.sqrt(d) - b;
        d = Math.sqrt(d);
        double t1 = d - b;
        double t2 = -(d + b);
//        if (t1 > 0) {
//            if (t2 > 0) {
//                return Math.min(t1, t2);
//            }
//            return t1;
//        }
//        if (t2 > 0) {
//            return t2;
//        }
//        return Double.NaN;
//
        if (t1 > 0 && (t2 <= 0 || t1 < t2)) {
            return t1 / 2;
        }
        if (t2 > 0) {
            return t2 / 2;
        }
        return Double.NaN;
    }

    @Override
    public Vector3D getCenter() {
        return position.getLocation();
    }

    public boolean contains(Vector3D point) {
        return point.distance(getCenter()) <= radius;
    }


    /*
     * Utilities:
     */

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        radiusSquared = radius * radius;
    }

    @Override
    public double getRange() {
        return radius;
    }
}
