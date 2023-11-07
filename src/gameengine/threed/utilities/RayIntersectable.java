package gameengine.threed.utilities;

import gameengine.threed.graphics.raytraceing.Ray;
import gameengine.vectormath.Vector3D;

public interface RayIntersectable {
    /**
     * Finds the first intersection a lightRay will have with the
     * {@code Collider3D}.
     *
     * @param ray The lightRay to find a collision with.
     * @param curSmallestDist The largest distance the output is looking for.
     *                        Can be used for optimization by counting out a
     *                        {@code Collider3D} early.
     * @return -1 if never enters range or if collision is behind start.
     * Otherwise, the distance to first hit
     */
    double distanceToCollide(Ray ray, double curSmallestDist);

    /**
     * Returns the normal vector of the intersectable object facing towards the
     * {@code VectorLine}.
     *
     * @param perspective
     * @return
     */
    Vector3D surfaceNormal(Ray perspective);

    default boolean willCollide(Ray ray) {
        return distanceToCollide((Ray) ray, Double.MAX_VALUE) > 0;
    }

    Vector3D getCenter();
}
