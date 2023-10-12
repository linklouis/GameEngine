package gameengine.prebuilt.gameobjects;

import gameengine.objects.GameObject;
import gameengine.prebuilt.InPlane;
import gameengine.prebuilt.physics.Collidable;
import gameengine.prebuilt.physics.PhysicsCollisionHandler;
import gameengine.prebuilt.physics.PhysicsObject;
import gameengine.vectormath.Vector2D;
import javafx.scene.paint.Color;

public class Ball extends GameObject {
    private final double R;

    public Ball(double x, double y, double r, Color color, long mass) {
        super(new PhysicsObject(), new Collidable(), new InPlane());
        R = r;
        get(PhysicsObject.class).instantiate(this, mass, color); // TODO figure out cleaner way to do this
        get(Collidable.class).instantiate(this, new Collidable.Row[(int)( r / Collidable.ROW_HEIGHT)], new PhysicsCollisionHandler(0.9, 10));
        get(InPlane.class).instantiate(this, x, y);
        populateRows();
    }

    public Ball(double x, double y, double r, Color color, long mass, Vector2D velocity) {
        super(new PhysicsObject(), new Collidable(), new InPlane());
        R = r;
        get(PhysicsObject.class).instantiate(this, mass, color, velocity); // TODO figure out cleaner way to do this
        get(Collidable.class).instantiate(this, new Collidable.Row[(int)( r / Collidable.ROW_HEIGHT)], new PhysicsCollisionHandler(0.9, 10));
        get(InPlane.class).instantiate(this, x, y);
        populateRows();
    }

    private void populateRows() {
        for (int i = 0; i < get(Collidable.class).getRows().length; i++) {
            double y = i - getR() / 2.0;
            double x = Math.sqrt(getR() - Math.pow(y, 2));
            get(Collidable.class).newRow(i, -x, x);
        }
    }

    public double getR() {
        return R;
    }
}
