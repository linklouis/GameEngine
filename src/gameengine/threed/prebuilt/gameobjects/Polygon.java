package gameengine.threed.prebuilt.gameobjects;

import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.vectormath.Vector3D;

public abstract class Polygon extends RayTraceable {

    public abstract Vector3D[] getVertices();

//    @Override
//    public String toString() {
//        return "Polygon: " + get(RayTraceable.class).toString();
//    }
}
