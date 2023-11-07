package gameengine.threed.prebuilt.gameobjects;

import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.raytraceing.objectgraphics.TriGraphics;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.vectormath.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class Tri extends Polygon {

    public Tri(Vector3D point1, Vector3D point2, Vector3D point3, RayTracingTexture texture) {
        super(new TriGraphics());
        get(TriGraphics.class).instantiate(this, point1, point2, point3, texture);
    }


    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(TriGraphics.class);
            }
        };
    }

    @Override
    public Vector3D[] getVertices() {
        return get(TriGraphics.class).getVertices();
    }

    @Override
    public String toString() {
        return "Tri: " + get(TriGraphics.class).toString();
    }
}
