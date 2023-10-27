package gameengine.threed.prebuilt.objectmovement.collisions;

import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.Texture;
import gameengine.threed.graphics.raytraceing.Ray;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SphereCollider extends Collider3D<SphereCollider> {
    private double radius;


    /*
     * Construction:
     */

    public SphereCollider() {
        super();
    }

    public SphereCollider(Modifier... modifiers) {
        super(modifiers);
    }

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
//                        new ModifierInstantiateParameter<>(
//                                "collisionHandler", CollisionHandler.class,
//                                this::setHandler),
                        new ModifierInstantiateParameter<>(
                                "radius", Double.class,
                                this::setRadius),
                        new ModifierInstantiateParameter<>(
                                "texture", Texture.class,
                                this::setTexture)
                )
        };
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
        double t1 = (d - b);
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
    public boolean isColliding(SphereCollider coll) {
        return coll.getCenter()
                .subtract(getCenter())
                .magnitude() > getRadius() + coll.getRadius();
    }

    @Override
    public boolean contains(Vector3D point) {
        return Math.abs(point.subtract(getCenter()).magnitude()) <= getRadius();
    }

    @Override
    public double minX() {
        return getCenter().getX() - getRadius();
    }

    @Override
    public double minY() {
        return getCenter().getY() - getRadius();
    }

    @Override
    public double minZ() {
        return getCenter().getZ() - getRadius();
    }

    @Override
    public double maxX() {
        return getCenter().getX() + getRadius();
    }

    @Override
    public double maxY() {
        return getCenter().getY() + getRadius();
    }

    @Override
    public double maxZ() {
        return getCenter().getZ() + getRadius();
    }

    @Override
    public Vector3D getCenter() {
        return getFromParent(InPlane3D.class).getLocation();
    }

    @Override
    public Class<SphereCollider> getColliderClass() {
        return SphereCollider.class;
    }

    @Override
    public void paint(GraphicsContext gc, Color color) {
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
