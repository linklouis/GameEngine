package gameengine.threed.prebuilt.gameobjects;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.threed.graphics.raytraceing.textures.ReflectingTexture;
import gameengine.threed.graphics.raytraceing.objectgraphics.SphereGraphics;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Sphere extends GameObject {
    private Vector3D position;
    public Sphere(double x, double y, double z, double r, RayTracingTexture texture) {
        super(new SphereGraphics());
        get(SphereGraphics.class).instantiate(this, r, texture, (Supplier<Vector3D>) this::getPosition);

        position = new Vector3D(x, y, z);
    }

    public Sphere(Vector3D position, double r, RayTracingTexture texture) {
        super(new SphereGraphics());
        get(SphereGraphics.class).instantiate(this, r, texture, (Supplier<Vector3D>) this::getPosition);

        this.position = position;
    }

    public Sphere(double x, double y, double z, double r, Color color, boolean isLightSource) {
        super(new SphereGraphics());
        get(SphereGraphics.class).instantiate(this, r,
                new ReflectingTexture(color, isLightSource, 0), (Supplier<Vector3D>) this::getPosition);

        position = new Vector3D(x, y, z);
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(SphereGraphics.class);
            }
        };
    }

    public Vector3D getPosition() {
        return position;
    }
}
