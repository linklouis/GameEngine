package gameengine.twod.prebuilt.gameobjects;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.twod.graphics.Visual2D;
import gameengine.twod.prebuilt.objectmovement.InPlane;
import gameengine.twod.prebuilt.objectmovement.collisions.CollisionHandler;
import gameengine.twod.prebuilt.objectmovement.collisions.LayerCollider2D;
import gameengine.twod.prebuilt.objectmovement.physics.PhysicsObject2D;
import gameengine.vectormath.Vector2D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Ball extends GameObject {
    private final double R;


    public Ball(double x, double y, double r, Color color, Number mass, boolean hasGravity, CollisionHandler handler) {
        super(new PhysicsObject2D(), new LayerCollider2D(), new InPlane(), new Visual2D());
        R = r;
        get(PhysicsObject2D.class).instantiate(this, mass, color, true, hasGravity); // TODO figure out cleaner way to do this
        get(LayerCollider2D.class).instantiate(this, new LayerCollider2D.Row[(int)( r / LayerCollider2D.ROW_HEIGHT)], handler);
        get(InPlane.class).instantiate(this, x, y);
        get(Visual2D.class).instantiate(this, get(PhysicsObject2D.class));
        populateRows();
    }

    public Ball(double x, double y, double r, Color color, Number mass, boolean hasGravity, Vector2D velocity, CollisionHandler handler) {
        super(new PhysicsObject2D(), new LayerCollider2D(), new InPlane(), new Visual2D());
        R = r;
        get(PhysicsObject2D.class).instantiate(this, mass, color, velocity, true, hasGravity); // TODO figure out cleaner way to do this
        get(LayerCollider2D.class).instantiate(this, new LayerCollider2D.Row[(int)( r / LayerCollider2D.ROW_HEIGHT)], handler);
        get(InPlane.class).instantiate(this, x, y);
        get(Visual2D.class).instantiate(this, get(PhysicsObject2D.class));
        populateRows();
    }

    private void populateRows() {
        for (int i = 0; i < get(LayerCollider2D.class).getRows().length; i++) {
            double y = i - getR() / 2.0;
            double x = Math.sqrt(getR() - Math.pow(y, 2));
            get(LayerCollider2D.class).newRow(i, -x, x);
        }
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane.class);
                add(LayerCollider2D.class);
            }
        };
    }

    public double getR() {
        return R;
    }
}
