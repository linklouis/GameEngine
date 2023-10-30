package gameengine.threed.graphics.raytraceing;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.GraphicsObject3D;
import gameengine.threed.graphics.Texture;
import gameengine.threed.graphics.raytraceing.Ray;
import gameengine.threed.graphics.raytraceing.RayTraceable;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class SphereGraphics extends RayTraceable {
    private double radius;


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
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
                        new ModifierInstantiateParameter<>(
                                "radius", Double.class,
                                this::setRadius),
                        new ModifierInstantiateParameter<>(
                                "texture", Texture.class,
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
    }


    /*
     * Functionality:
     */

    @Override
    public Vector3D surfaceNormal(Ray ray) {
        return ray.getPosition().subtract(getCenter())/*.unitVector()*/;
    }

    /**
     * Finds the first intersection a ray would have with the Sphere.
     *
     * @param ray The ray to find a collision with.
     * @param curSmallestDist The largest distance the output is looking for.
     *                        not used for optimization for Spheres.
     * @return -1 if never enters range or if collision is behind start.
     * Otherwise, the distance to first hit
     */
    @Override
    public double distanceToCollide(Ray ray, double curSmallestDist) {
        if (contains(ray.getPosition())) {
            return 0;
        }

        Vector3D Q = ray.getPosition().subtract(getCenter());
        double b = ray.getDirection().atMagnitude(2).dotProduct(Q);
        double d = b * b - 4 * (Q.dotWithSelf() - radius * radius);  // discriminant of quadratic

        if (d <= 0) {
            return -1; // Solutions are complex, no intersections
        }

        // Intersections exists
        d = Math.sqrt(d);
        double t1 = d - b;
        double t2 = -(d + b);

        if (t1 > 0 && (t2 <= 0 || t1 < t2)) {
            return t1 / 2;
        }
        if (t2 > 0) {
            return t2 / 2;
        }
        return -1;
    }

    @Override
    public Vector3D getCenter() {
        return getFromParent(InPlane3D.class).getLocation();
    }

    public boolean contains(Vector3D point) {
        return Math.abs(point.subtract(getCenter()).magnitude()) <= getRadius();
    }


    /*
     * Utilities:
     */

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
