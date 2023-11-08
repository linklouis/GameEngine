package gameengine.threed.geometry;

import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;

public interface ConstrainedPlane extends IsPlane {
    Vector3D minCoordinates();

    Vector3D maxCoordinates();

    boolean contains(Vector3D point);

    boolean contains(Vector2D point);


}
