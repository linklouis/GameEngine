package gameengine.threed.geometry;

import gameengine.twod.Rect;
import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;

import static gameengine.utilities.ExtraMath.max;
import static gameengine.utilities.ExtraMath.min;

public record Rect3D(Vector3D vertex1, Vector3D vertex2, Vector3D vertex3, Vector3D vertex4,
                     Vector3D planeXaxis, Vector3D planeYaxis, Rect planeCoords, Vector3D normal) {
    public Rect3D(Vector3D vertex1, Vector3D vertex2, Vector3D vertex3, Vector3D vertex4) {
        this(
                vertex1, vertex2, vertex3, vertex4,
                vertex2.subtract(vertex1), vertex4.subtract(vertex1),
                new Rect(vertex1.projectToPlane(vertex2.subtract(vertex1), vertex4.subtract(vertex1)),
                        vertex3.projectToPlane(vertex2.subtract(vertex1), vertex4.subtract(vertex1)),
                        true),
                vertex2.subtract(vertex1).crossProduct(vertex4.subtract(vertex1))
        );
    }

    public Rect3D recompute() {
        return new Rect3D(vertex1, vertex2, vertex3, vertex4);
    }

    public boolean contains(Vector3D point) {
        return planeCoords.contains(onPlane(point));
    }

    public boolean contains(final VectorLine3D line, double distance) {
        return planeCoords.contains(onPlane(line, distance));
    }

    public Vector2D onPlane(Vector3D other) {
        return other.projectToPlane(planeXaxis, planeYaxis);
    }

    public Vector2D onPlane(final VectorLine3D line, double distance) {
        return line.getPosition().projectToPlane(planeXaxis, planeYaxis, line.getDirection(), distance);
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

    public Vector3D[] vertices() {
        return new Vector3D[] { vertex1, vertex2, vertex3, vertex4 };
    }
}