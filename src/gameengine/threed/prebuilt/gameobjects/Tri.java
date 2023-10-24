package gameengine.threed.prebuilt.gameobjects;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
import gameengine.threed.prebuilt.objectmovement.collisions.RectCollider;
import gameengine.vectormath.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class Tri extends GameObject {



    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
//                add(InPlane3D.class);
//                add(RectCollider.class);
//                add(Visual2D.class);
            }
        };
    }
}
