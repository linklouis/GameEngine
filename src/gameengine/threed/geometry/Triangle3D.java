package gameengine.threed.geometry;

import gameengine.vectormath.Vector3D;

public record Triangle3D(Vector3D vertex1, Vector3D vertex2, Vector3D vertex3,
                         Vector3D v0, Vector3D v1,
                         double dot00, double dot01, double dot11,
                         double invDenom/*,
                         Vector3D dirTo2, Vector3D dirTo3*/) {
    public Triangle3D(Triangle3D parent) {
        this(parent.vertex1, parent.vertex2, parent.vertex3, parent.v0,
                parent.v1, parent.dot00, parent.dot01, parent.dot11,
                parent.invDenom/*, parent.dirTo2, parent.dirTo3*/);
    }

    public static Triangle3D computeValues(Vector3D vertex1, Vector3D vertex2, Vector3D vertex3) {
        Vector3D v0 = vertex2.subtract(vertex1);
        Vector3D v1 = vertex3.subtract(vertex1);
        double dot00 = v0.dotSelf();
        double dot01 = v0.dotProduct(v1);
        double dot11 = v1.dotSelf();
//        dirTo2 = vertex2.subtract(vertex1);
//        dirTo3 = vertex3.subtract(vertex1);

        return new Triangle3D(vertex1, vertex2, vertex3,
                v0, v1,
                dot00, dot01, dot11,
                (dot00 * dot11 - dot01 * dot01)/*
                vertex2.subtract(vertex1), vertex3.subtract(vertex1)*/);
    }

    public Vector3D normal() {
        return vertex1.normal(vertex2, vertex3);
    }
}
