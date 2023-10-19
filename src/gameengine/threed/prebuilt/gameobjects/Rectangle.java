package gameengine.threed.prebuilt.gameobjects;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.BaseTexture;
import gameengine.threed.graphics.Texture;
import gameengine.threed.graphics.Visual3D;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
import gameengine.threed.prebuilt.objectmovement.collisions.RectCollider;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Rectangle extends GameObject {
    public Rectangle(double x, double y, double z, Vector3D space, Texture texture) {
        super(new RectCollider(), new InPlane3D(), new Visual3D());
        get(RectCollider.class).instantiate(this, space, texture);
        get(InPlane3D.class).instantiate(this, x, y, z);
        get(Visual3D.class).instantiate(this, get(RectCollider.class));
    }

    public Rectangle(double x, double y, double z, Vector3D space, Color color, boolean isLightSource) {
        super(new RectCollider(), new InPlane3D(), new Visual3D());
        get(RectCollider.class).instantiate(this, space, new BaseTexture(color, isLightSource, 0));
        get(InPlane3D.class).instantiate(this, x, y, z);
        get(Visual3D.class).instantiate(this, get(RectCollider.class));
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane3D.class);
                add(RectCollider.class);
//                add(Visual2D.class);
            }
        };
    }
}
