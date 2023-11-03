package gameengine.threed.utilities;

import gameengine.vectormath.Vector3D;

public interface VectorLineIntersectable {
    boolean willCollide(VectorLine3D line);

    Vector3D getCenter();
}
