package gameengine.prebuilt.gameobjects;

import gameengine.objects.GameObject;
import gameengine.prebuilt.objectmovement.InPlane;
import gameengine.prebuilt.objectmovement.collisions.Collidable;
import gameengine.prebuilt.objectmovement.physics.PhysicsObject;

public class Wall extends GameObject {
    public Wall() {
        super(new PhysicsObject(), new Collidable(), new InPlane());
    }


}
