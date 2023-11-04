package gameengine.threed.graphics.raytraceing.objectgraphics;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.raytraceing.Ray;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class SphereGraphics extends RayTraceable {
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
        return perspective.getPosition().subtract(getCenter())/*.unitVector()*/;
    }

    /**
     * Finds the first intersection a lightRay would have with the Sphere.
     *
     * @param lightRay The lightRay to find a collision with.
     * @param curSmallestDist The largest distance the output is looking for.
     *                        not used for optimization for Spheres.
     * @return -1 if never enters range or if collision is behind start.
     * Otherwise, the distance to first hit
     */
    @Override
    public double distanceToCollide(Ray lightRay, double curSmallestDist) {
//        double b = lightRay.getDirection().dotWithSubtracted(lightRay.getPosition(), getCenter());
//        double a = radiusSquared - lightRay.getPosition().distanceSquared(getCenter());
//        if (a < -b * b) {
//            return -1;
//        }
//        if (b > 0) {
//
//        }
//        double d = b * b + a;  // discriminant of quadratic
//        double b = lightRay.getDirection().dotWithSubtracted(lightRay.getPosition(), getCenter());
//        double d = b * b + radiusSquared - lightRay.getPosition().distanceSquared(getCenter());
//        return d >= 0 ? Math.sqrt(d) - b : -1;
        double b = lightRay.getDirection().dotWithSubtracted(lightRay.getPosition(), getCenter()) / lightRay.getDirection().magnitude();
        double d = b * b
                - (lightRay.getPosition().distanceSquared(getCenter())
                - radius * radius);  // discriminant of quadratic
        if (d <= 0) {
            return -1; // Solutions are complex, no intersections
        }
        return d - b;

//        if (d <= 0) {
//            return -1; // Solutions are complex, no intersections
//        }
//
//        // Intersections exists
//        d = Math.sqrt(d);
//        if (d > b) {
//            if (b > 0) {
//                return d - b;
//            }
////            if (b <= -d) {
//                return -(d + b);
////            }
//        }
//        if (d > 0) {
//            return -1;
//        }
//        if (d <= -b) {
//            return -(d + b);
//        }
//        return -1;



//        double t1 = d - b;
//        double t2 = -(d + b);
//
//        if (t1 > 0 && (t2 <= 0 || t1 < t2)) {
//            return t1;
//        }
//        if (t2 > 0) {
//            return t2;
//        }
//        return -1;
    }
//    public double distanceToCollide(Ray lightRay, double curSmallestDist) {
//        double b = lightRay.getDirection().dotWithSubtracted(lightRay.getPosition(), getCenter()) / lightRay.getDirection().magnitude();
//        double d = b * b
//                - (lightRay.getPosition().distanceSquared(getCenter())
//                - radius * radius);  // discriminant of quadratic
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
//            return t1;
//        }
//        if (t2 > 0) {
//            return t2;
//        }
//        return -1;
//    }
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
}
