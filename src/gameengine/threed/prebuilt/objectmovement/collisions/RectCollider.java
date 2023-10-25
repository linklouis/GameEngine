package gameengine.threed.prebuilt.objectmovement.collisions;

import gameengine.threed.graphics.Texture;
import gameengine.threed.graphics.raytraceing.Ray;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RectCollider extends Collider3D<RectCollider> {
    /**
     * A Vector3D from the center to one corner.
     * Defines the size of the Rectangle.
     */
    private Vector3D space;
    private double range;

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
//                        new ModifierInstantiateParameter<>(
//                                "collisionHandler", CollisionHandler.class,
//                                this::setHandler),
                        new ModifierInstantiateParameter<>(
                                "space", Vector3D.class,
                                this::setSpace),
                        new ModifierInstantiateParameter<>(
                                "texture", Texture.class,
                                this::setTexture)
                )
        };
    }

    @Override
    public double distanceToCollide(Ray ray) {
        return distanceToEnterRange(ray.getPosition(), ray.getDirection());
    }

    /**
     * Finds the first intersection a ray, starting at start and moving in
     * direction dir, would have with the range sphere.
     *
     * @return -1 if never enters range or if collision is behind start.
     * Otherwise, the distance to first hit
     */
    public double distanceToEnterRange(Vector3D start, Vector3D dir) {
        if (contains(start)) {
            return 0;
        }
        double stepSize = dir.magnitude();
        dir = dir.unitVector();

        Vector3D Q = start.subtract(getCenter());
        double b = dir.scalarMultiply(2).dotProduct(Q);
//        double c = Q.dotProduct(Q) - getRange() * getRange();
        double d = b * b - 4 * (Q.dotProduct(Q) - getRange() * getRange())/*c*/;  // discriminant of quadratic

        if (d <= 0) {
            return -1; // Solutions are complex, so no intersections
        } else {
            // Intersections exists
            double t1 = (-b + Math.sqrt(d)) / (2);
            double t2 = (-b - Math.sqrt(d)) / (2);
            if (Math.abs(t1 - t2) <= stepSize) {
                return -1;
            }
            if (Math.min(t1, t2) < 0) {
                if (Math.max(t1, t2) < 0) {
                    return -1;
                }
                return Math.max(t1, t2);
            }
            return Math.min(t1, t2);
        }
    }

    @Override
    public Vector3D surfaceNormal(Ray ray) {
        Vector3D displacement = ray.getPosition().subtract(getCenter());

        return displacement.abs().subtract(space.abs()).abs()
                .min()
                .multiplyAcross(displacement)
                .unitVector();
//        Vector3D displacement = point.subtract(getCenter());
//
//        Vector3D smallest = displacement.abs().subtract(space.abs()).abs()
//                .min();
//        double x = 0;
//        double y = 0;
//        double z = 0;
//        if (smallest.getX() != 0) {
//            x = space.getX();
//        }
//        if (smallest.getY() != 0) {
//            y = space.getY();
//        }
//        if (smallest.getZ() != 0) {
//            z = space.getZ();
//        }
//
//        return new Vector3D(x, y, z);

//        // Calculate relative position of the point
//        Vector3D displacement = point.subtract(getCenter());
//
//        // Calculate the absolute values of displacement and space vectors
//        Vector3D absDisplacement = displacement.abs();
//        Vector3D absSpace = space.abs();
//
//        // Determine which component of displacement is the largest
//        double maxComponent = Math.max(Math.max(absDisplacement.getX(), absDisplacement.getY()), absDisplacement.getZ());
//
//        // Check which face the point is on and return the corresponding normal
//        if (absDisplacement.getX() == maxComponent) {
//            // Point is on a face perpendicular to the X-axis
//            return new Vector3D(displacement.getX() > 0 ? 1 : -1, 0, 0);
//        } else if (absDisplacement.getY() == maxComponent) {
//            // Point is on a face perpendicular to the Y-axis
//            return new Vector3D(0, displacement.getY() > 0 ? 1 : -1, 0);
//        } else {
//            // Point is on a face perpendicular to the Z-axis
//            return new Vector3D(0, 0, displacement.getZ() > 0 ? 1 : -1);
//        }

//        Vector3D spaceNormalized = space.unitVector();
//        Vector3D side1 = spaceNormalized.crossProduct(getX()); // Assuming +X is one of the sides
//        Vector3D side2 = spaceNormalized.crossProduct(side1);

    }

    @Override
    public boolean isColliding(RectCollider coll) {
        return  !(separatedAlongAxis(minX(), maxX(), coll.minX(), coll.maxX())
                || separatedAlongAxis(minY(), maxY(), coll.minY(), coll.maxY())
                || separatedAlongAxis(minZ(), maxZ(), coll.minZ(), coll.maxZ()));
    }

    public static boolean separatedAlongAxis(double minA, double maxA, double minB, double maxB) {
        return maxA < minB || minA > maxB;
    }

    public static boolean containsOnAxis(double minA, double maxA, double locB) {
        return maxA < locB || minA > locB;
    }

    @Override
    public boolean contains(Vector3D point) {
        return !(containsOnAxis(minX(), maxX(), point.getX())
                || containsOnAxis(minY(), maxY(), point.getY())
                || containsOnAxis(minZ(), maxZ(), point.getZ()));
    }

    @Override
    public double minX() {
        return getCenter().getX() - Math.abs(space.getX());
    }

    @Override
    public double minY() {
        return getCenter().getY() - Math.abs(space.getY());
    }

    @Override
    public double minZ() {
        return getCenter().getZ() - Math.abs(space.getZ());
    }

    @Override
    public double maxX() {
        return getCenter().getX() + Math.abs(space.getX());
    }

    @Override
    public double maxY() {
        return getCenter().getY() + Math.abs(space.getY());
    }

    @Override
    public double maxZ() {
        return getCenter().getZ() + Math.abs(space.getZ());
    }

    @Override
    public Vector3D getCenter() {
        return getFromParent(InPlane3D.class).getLocation();
    }

    @Override
    public Class<RectCollider> getColliderClass() {
        return RectCollider.class;
    }

    @Override
    public void paint(GraphicsContext gc, Color color) {

    }

    public Vector3D getSpace() {
        return space;
    }

    public void setSpace(Vector3D space) {
        this.space = space;
        setRange(Math.abs(space.magnitude()));
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }
}
