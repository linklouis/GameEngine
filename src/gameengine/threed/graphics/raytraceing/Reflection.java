package gameengine.threed.graphics.raytraceing;

import gameengine.vectormath.Vector3D;

public record Reflection(Vector3D direction, Vector3D color) {
    public Reflection {
        direction = direction.unitVector();
    }
}
