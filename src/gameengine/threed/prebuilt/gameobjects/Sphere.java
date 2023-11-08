package gameengine.threed.prebuilt.gameobjects;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.Visual3D;
import gameengine.threed.graphics.raytraceing.objectgraphics.SphereGraphics;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.threed.graphics.raytraceing.textures.ReflectingTexture;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
import gameengine.threed.prebuilt.objectmovement.collisions.SphereCollider;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Sphere extends GameObject {
    public Sphere(double x, double y, double z, double r, RayTracingTexture texture) {
        super(new SphereCollider(), new InPlane3D(), new SphereGraphics(), new Visual3D());
        get(SphereCollider.class).instantiate(this, r);
        get(InPlane3D.class).instantiate(this, x, y, z);
        get(SphereGraphics.class).instantiate(this, r, texture);
        get(Visual3D.class).instantiate(this, get(SphereGraphics.class));
    }

    public Sphere(Vector3D position, double r, RayTracingTexture texture) {
        super(new SphereCollider(), new InPlane3D(), new SphereGraphics(), new Visual3D());
        get(SphereCollider.class).instantiate(this, r);
        get(InPlane3D.class).instantiate(this, position);
        get(SphereGraphics.class).instantiate(this, r, texture);
        get(Visual3D.class).instantiate(this, get(SphereGraphics.class));
    }

    public Sphere(double x, double y, double z, double r, Color color, boolean isLightSource) {
        super(new SphereCollider(), new InPlane3D(), new SphereGraphics(), new Visual3D());
        get(SphereCollider.class).instantiate(this, r);
        get(InPlane3D.class).instantiate(this, x, y, z);
        get(SphereGraphics.class).instantiate(this, r, new ReflectingTexture(color, isLightSource, 0));
        get(Visual3D.class).instantiate(this, get(SphereGraphics.class));
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane3D.class);
                add(SphereCollider.class);
                add(SphereGraphics.class);
                add(Visual3D.class);
            }
        };
    }
}
