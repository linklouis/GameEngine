package gameengine.twod.prebuilt.gameobjects;

import gameengine.twod.graphics.Visual2D;
import gameengine.skeletons.GameObject;
import gameengine.twod.prebuilt.objectmovement.InPlane;
import gameengine.twod.prebuilt.objectmovement.collisions.LayerCollider2D;
import gameengine.twod.prebuilt.objectmovement.collisions.CollisionHandler;
import gameengine.twod.prebuilt.objectmovement.physics.PhysicsObject2D;
import javafx.scene.paint.Color;

public class Wall extends GameObject {
    private final double WIDTH;
    private final double HEIGHT;

    public Wall(double x, double y, double width, double height,
                Color color, Number mass, CollisionHandler handler) {
        super(new PhysicsObject2D(), new LayerCollider2D(), new InPlane(), new Visual2D());

        get(PhysicsObject2D.class).instantiate(this, mass, color, false);
        get(LayerCollider2D.class).instantiate(this, new LayerCollider2D.Row[(int)(height)], handler);
        get(InPlane.class).instantiate(this, x, y);
        get(Visual2D.class).instantiate(this, get(PhysicsObject2D.class));

        WIDTH = width;
        HEIGHT = height;

        populateRows();
    }

    private void populateRows() {
        for (int i = 0; i < get(LayerCollider2D.class).getRows().length; i++) {
            double y = i - HEIGHT / 2.0;
            double x = WIDTH;
            get(LayerCollider2D.class).newRow(i, -x, x);
        }
    }

    public double getWIDTH() {
        return WIDTH;
    }

    public double getHEIGHT() {
        return HEIGHT;
    }
}
