package gameengine.threed.prebuilt.gameobjects;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.threed.graphics.Visual3D;
import gameengine.threed.graphics.raytraceing.objectgraphics.TriGraphics;
import gameengine.vectormath.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class Tri extends GameObject {

    public Tri(Vector3D point1, Vector3D point2, Vector3D point3, RayTracingTexture texture) {
        super(new TriGraphics(), new Visual3D());
        get(TriGraphics.class).instantiate(this, point1, point2, point3, texture);
        get(Visual3D.class).instantiate(this, get(TriGraphics.class));
    }


    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(TriGraphics.class);
                add(Visual3D.class);
            }
        };
    }

    @Override
    public String toString() {
        TriGraphics collider = get(TriGraphics.class);
        return "Tri: " + collider.getVertex1() + ", " + collider.getVertex2() + ", " + collider.getVertex3();
    }
}
