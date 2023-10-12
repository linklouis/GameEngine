package gameengine.prebuilt.gameobjects;

import gameengine.objects.GameObject;
import gameengine.objects.Modifier;
import gameengine.prebuilt.InPlane;
import gameengine.prebuilt.physics.Collidable;
import gameengine.prebuilt.physics.PhysicsObject;

public class Wall extends GameObject {
    public Wall() {
        super(new PhysicsObject(), new Collidable(), new InPlane());
    }


}
