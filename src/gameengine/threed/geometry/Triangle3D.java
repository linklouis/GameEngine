package gameengine.threed.geometry;

import gameengine.vectormath.Vector3D;

import static gameengine.utilities.ExtraMath.max;
import static gameengine.utilities.ExtraMath.min;

public record Triangle3D(Vector3D vertex1, Vector3D vertex2, Vector3D vertex3,
                         Vector3D v0, Vector3D v1,
                         double dot00, double dot01, double dot11,
                         double invDenom) {
    public Triangle3D(Triangle3D parent) {
        this(parent.vertex1, parent.vertex2, parent.vertex3, parent.v0,
                parent.v1, parent.dot00, parent.dot01, parent.dot11,
                parent.invDenom);
    }

    public static Triangle3D computeValues(Vector3D vertex1, Vector3D vertex2, Vector3D vertex3) {
        Vector3D v0 = vertex2.subtract(vertex1);
        Vector3D v1 = vertex3.subtract(vertex1);
        double dot00 = v0.dotSelf();
        double dot01 = v0.dotProduct(v1);
        double dot11 = v1.dotSelf();

        return new Triangle3D(
                vertex1, vertex2, vertex3,
                v0, v1,
                dot00, dot01, dot11,
                (dot00 * dot11 - dot01 * dot01));
    }

    public double minX() {
        return min(vertex1.getX(), vertex2.getX(), vertex3.getX());
    }
    public double minY() {
        return min(vertex1.getY(), vertex2.getY(), vertex3.getY());
    }

    public double minZ() {
        return min(vertex1.getZ(), vertex2.getZ(), vertex3.getZ());
    }

    public double maxX() {
        return max(vertex1.getX(), vertex2.getX(), vertex3.getX());
    }

    public double maxY() {
        return max(vertex1.getY(), vertex2.getY(), vertex3.getY());
    }

    public double maxZ() {
        return max(vertex1.getZ(), vertex2.getZ(), vertex3.getZ());
    }

    public Vector3D calculateCenter() {
        return new Vector3D(
                (maxX() + minX()) / 2,
                (maxY() + minY()) / 2,
                (maxZ() + minZ()) / 2 );
    }

    public Vector3D normal() {
        return vertex1.normal(vertex2, vertex3);
    }
}
