package gameengine.prebuilt.gameobjects;

import gameengine.graphics.Visual;
import gameengine.objects.GameObject;
import gameengine.objects.Modifier;
import gameengine.prebuilt.objectmovement.InPlane;
import gameengine.prebuilt.objectmovement.collisions.LayerCollider;
import gameengine.prebuilt.objectmovement.collisions.CollisionHandler;
import gameengine.prebuilt.objectmovement.physics.PhysicsObject;
import gameengine.vectormath.Vector2D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Ball extends GameObject {
    private final double R;


    public Ball(double x, double y, double r, Color color, Number mass, boolean hasGravity, CollisionHandler handler) {
        super(new PhysicsObject(), new LayerCollider(), new InPlane(), new Visual());
        R = r;
        get(PhysicsObject.class).instantiate(this, mass, color, true, hasGravity); // TODO figure out cleaner way to do this
        get(LayerCollider.class).instantiate(this, new LayerCollider.Row[(int)( r / LayerCollider.ROW_HEIGHT)], handler);
        get(InPlane.class).instantiate(this, x, y);
        get(Visual.class).instantiate(this, get(PhysicsObject.class));
        populateRows();
    }

    public Ball(double x, double y, double r, Color color, Number mass, boolean hasGravity, Vector2D velocity, CollisionHandler handler) {
        super(new PhysicsObject(), new LayerCollider(), new InPlane(), new Visual());
        R = r;
        get(PhysicsObject.class).instantiate(this, mass, color, velocity, true, hasGravity); // TODO figure out cleaner way to do this
        get(LayerCollider.class).instantiate(this, new LayerCollider.Row[(int)( r / LayerCollider.ROW_HEIGHT)], handler);
        get(InPlane.class).instantiate(this, x, y);
        get(Visual.class).instantiate(this, get(PhysicsObject.class));
        populateRows();
    }

    private void populateRows() {
        for (int i = 0; i < get(LayerCollider.class).getRows().length; i++) {
            double y = i - getR() / 2.0;
            double x = Math.sqrt(getR() - Math.pow(y, 2));
            get(LayerCollider.class).newRow(i, -x, x);
        }
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane.class);
                add(LayerCollider.class);
            }
        };
    }

    public double getR() {
        return R;
    }
}
