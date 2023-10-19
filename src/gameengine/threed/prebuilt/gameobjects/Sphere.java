package gameengine.threed.prebuilt.gameobjects;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.Visual3D;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
import gameengine.threed.prebuilt.objectmovement.collisions.SphereCollider;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Sphere extends GameObject {
    public Sphere(double x, double y, double z, double r, Color color, boolean isLightSource) {
        super(new SphereCollider(), new InPlane3D(), new Visual3D());
        get(SphereCollider.class).instantiate(this, r, isLightSource, color);
        get(InPlane3D.class).instantiate(this, x, y, z);
        get(Visual3D.class).instantiate(this, get(SphereCollider.class));
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane3D.class);
                add(SphereCollider.class);
//                add(Visual2D.class);
            }
        };
    }
}
