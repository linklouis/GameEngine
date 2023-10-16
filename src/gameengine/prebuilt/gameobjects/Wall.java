package gameengine.prebuilt.gameobjects;

import gameengine.graphics.Visual;
import gameengine.objects.GameObject;
import gameengine.prebuilt.objectmovement.InPlane;
import gameengine.prebuilt.objectmovement.collisions.Collidable;
import gameengine.prebuilt.objectmovement.collisions.CollisionHandler;
import gameengine.prebuilt.objectmovement.physics.PhysicsObject;
import javafx.scene.paint.Color;

public class Wall extends GameObject {
    private final double WIDTH;
    private final double HEIGHT;

    public Wall(double x, double y, double width, double height,
                Color color, Number mass, CollisionHandler handler) {
        super(new PhysicsObject(), new Collidable(), new InPlane(), new Visual());

        get(PhysicsObject.class).instantiate(this, mass, color, false);
        get(Collidable.class).instantiate(this, new Collidable.Row[(int)(height)], handler);
        get(InPlane.class).instantiate(this, x, y);
        get(Visual.class).instantiate(this, get(PhysicsObject.class));

        WIDTH = width;
        HEIGHT = height;

        populateRows();
    }

    private void populateRows() {
        for (int i = 0; i < get(Collidable.class).getRows().length; i++) {
            double y = i - HEIGHT / 2.0;
            double x = WIDTH;
            get(Collidable.class).newRow(i, -x, x);
        }
    }

    public double getWIDTH() {
        return WIDTH;
    }

    public double getHEIGHT() {
        return HEIGHT;
    }
}
