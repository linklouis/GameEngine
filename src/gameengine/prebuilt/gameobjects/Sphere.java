package gameengine.prebuilt.gameobjects;

import gameengine.objects.GameObject;
import gameengine.objects.Modifier;
import gameengine.prebuilt.InPlane3D;
import gameengine.prebuilt.objectmovement.collisions.SphereCollider;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Sphere extends GameObject {
    public Sphere(double x, double y, double z, double r, Color color, boolean isLightSource) {
        super(new SphereCollider(), new InPlane3D());//, new Visual());
        get(SphereCollider.class).instantiate(this, r, color, isLightSource);
        get(InPlane3D.class).instantiate(this, x, y, z);
//        get(Visual.class).instantiate(this, get(SphereCollider.class));
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane3D.class);
                add(SphereCollider.class);
//                add(Visual.class);
            }
        };
    }
}
