package gameengine.threed.geometry;

import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;

public interface IsPlane extends RayIntersectable {
    Vector3D surfaceNormal();

    Vector3D getPlaneXaxis();

    Vector3D getPlaneYaxis();

    default Vector2D onPlane(Vector3D other) {
        return other.projectToPlane(getPlaneXaxis(), getPlaneYaxis());
    }

    default Vector2D onPlane(final VectorLine3D line, double distance) {
        return line.getPosition()
                .projectToPlane(getPlaneXaxis(), getPlaneYaxis(),
                line.getDirection(), distance);
    }
}
