package gameengine.threed.geometry;

import gameengine.vectormath.Vector3D;

public interface RayIntersectable extends VectorLineIntersectable {
    /**
     * Finds the first intersection a lightRay will have with the
     * {@code Collider3D}.
     *
     * @param ray               The lightRay to find a collision with.
     * @param curSmallestDist   The largest distance the output is looking for.
     *                          Can be used for optimization by counting out a
     *                          {@code Collider3D} early.
     * @param amountInDirection
     * @return -1 if never enters range or if collision is behind start.
     * Otherwise, the distance to first hit
     */
    double distanceToCollide(Ray ray, double curSmallestDist, double amountInDirection);

    default double distanceToCollide(Ray ray, double curSmallestDist) {
        return distanceToCollide(ray, curSmallestDist,
                ray.getDirection().dotWithSubtracted(ray.getPosition(), getCenter()));
    }

    double getRange();

    default double closestDistTo(Vector3D point) {
//        System.out.println(getCenter().distance(point) + ", " + getRange());
        return getCenter().distance(point) - getRange();
    }

    /**
     * Returns the normal vector of the intersectable object facing towards the
     * {@code VectorLine}.
     *
     * @param perspective
     * @return
     */
    Vector3D surfaceNormal(Ray perspective);

    default boolean willCollide(VectorLine3D line) {
        return distanceToCollide((Ray) line, Double.MAX_VALUE) > 0;
    }
}
