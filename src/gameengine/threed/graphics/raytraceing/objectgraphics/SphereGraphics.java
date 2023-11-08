package gameengine.threed.graphics.raytraceing.objectgraphics;

import gameengine.skeletons.GameObject;
import gameengine.threed.graphics.raytraceing.Ray;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.utilities.ArgumentContext;
import gameengine.vectormath.Vector3D;

import java.util.function.Supplier;

public class SphereGraphics extends RayTraceable {
    private double radius;
    private double radiusSquared;
    Supplier<Vector3D> positionSupplier = null;


    /*
     * Construction:
     */

    public SphereGraphics(double radius, RayTracingTexture texture, Supplier<Vector3D> positionSupplier) {
        super();
        this.radius = radius;
        setTexture(texture);
        this.positionSupplier = positionSupplier;
    }

    @Override
    public Vector3D[] getVertices() {
        return new Vector3D[0];
    }


    /*
     * Functionality:
     */

    @Override
    public Vector3D surfaceNormal(Ray perspective) {
        return perspective.getPosition().subtract(getCenter())/*.unitVector()*/;
    }

    /**
     * Finds the first intersection a lightLightRay would have with the Sphere.
     *
     * @param lightLightRay The lightLightRay to find a collision with.
     * @param curSmallestDist The largest distance the output is looking for.
     *                        not used for optimization for Spheres.
     * @return {@code NaN} if never enters range or if collision is behind start.
     * Otherwise, the distance to first hit
     */
    @Override
    public double distanceToCollide(Ray ray, double curSmallestDist) {
        double b = -ray.getDirection().dotWithSubtracted(ray.getPosition(), getCenter());

//        if (b > 0) {
            return b - Math.sqrt(b * b + radiusSquared - ray.getPosition().distanceSquared(getCenter()));
//        }
//        return Double.NaN;
    }

    public double distanceToCollideMoreTests(Ray ray, double curSmallestDist) {
        double b = -ray.getDirection().dotWithSubtracted(ray.getPosition(), getCenter());
//        double a = ray.getPosition().distanceSquared(getCenter());

        if (b > 0) {
//            if ((!Double.isNaN(b - Math.sqrt(b * b + radiusSquared - a))) && b - Math.sqrt(b * b + radiusSquared - a) >= 0) {
//                System.out.println("A " + (b - Math.sqrt(b * b + radiusSquared - a)));
//            }
//            return b - Math.sqrt(b * b + radiusSquared - a);
            return b - Math.sqrt(b * b + radiusSquared - ray.getPosition().distanceSquared(getCenter()));
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

    public double distanceToCollideTests(Ray ray, double curSmallestDist) {
        double b = ray.getDirection().dotWithSubtracted(ray.getPosition(), getCenter()) / ray.getDirection().magnitude();
        double a = ray.getPosition().distanceSquared(getCenter());
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

//    public double distanceToCollide(Ray lightRay, double curSmallestDist) {
//        if (contains(lightRay.getPosition())) {
//            return 0;
//        }
//
//        double b = lightRay.getDirection()
//                .dotWithSubtracted(lightRay.getPosition(), getCenter())
//                * 2 / lightRay.getDirection().magnitude();
//        double d = b * b
//                - 4 * (lightRay.getPosition().distanceSquared(getCenter())
//                        - radius * radius);  // discriminant of quadratic
//
//        if (d <= 0) {
//            return -1; // Solutions are complex, no intersections
//        }
//
//        // Intersections exists
//        d = Math.sqrt(d);
//        double t1 = d - b;
//        double t2 = -(d + b);
//
//        if (t1 > 0 && (t2 <= 0 || t1 < t2)) {
//            return t1 / 2;
//        }
//        if (t2 > 0) {
//            return t2 / 2;
//        }
//        return -1;
//    }

    @Override
    public Vector3D getCenter() {
        return positionSupplier.get();
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

    public void setPositionSupplier(Supplier<Vector3D> supplier) {
        positionSupplier = supplier;
    }
}
