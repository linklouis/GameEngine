package gameengine.threed.prebuilt.gameobjects;

import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.Visual3D;
import gameengine.threed.graphics.raytraceing.objectgraphics.QuadGraphics;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.vectormath.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class Quad extends Polygon  {
    public Quad(Vector3D point1, Vector3D point2, Vector3D point3, Vector3D point4, RayTracingTexture texture) {
        super(new QuadGraphics());
        get(QuadGraphics.class).instantiate(this, point1, point2, point3, point4, texture);
    }


    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(QuadGraphics.class);
                add(Visual3D.class);
            }
        };
    }

    @Override
    public Vector3D[] getVertices() {
        return get(QuadGraphics.class).getVertices();
    }

    @Override
    public String toString() {
        return "Quad: " + get(QuadGraphics.class).toString();
    }
}
