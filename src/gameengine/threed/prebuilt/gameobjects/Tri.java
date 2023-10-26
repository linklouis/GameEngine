package gameengine.threed.prebuilt.gameobjects;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.Texture;
import gameengine.threed.graphics.Visual3D;
import gameengine.threed.prebuilt.objectmovement.collisions.TriCollider;
import gameengine.vectormath.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class Tri extends GameObject {

    public Tri(Vector3D point1, Vector3D point2, Vector3D point3, Texture texture) {
        super(new TriCollider(), new Visual3D());
        get(TriCollider.class).instantiate(this, point1, point2, point3, texture);
        get(Visual3D.class).instantiate(this, get(TriCollider.class));
    }


    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(TriCollider.class);
//                add(InPlane3D.class);
                add(Visual3D.class);
            }
        };
    }

    @Override
    public String toString() {
        TriCollider collider = get(TriCollider.class);
        return "Tri: " + collider.getVertex1() + ", " + collider.getVertex2() + ", " + collider.getVertex3();
    }
}
