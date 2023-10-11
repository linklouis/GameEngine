import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Circle extends GameObject {
    private final double R;

    public Circle(double x, double y, double r, Color color, long mass) {
        super(new Visual)
        super(x, y, new Row[(int)( r / ROW_HEIGHT)], color, mass);
        R = r;
        populateRows();
    }

    public double getR() {
        return R;
    }

    @Override
    public void updateForces(PhysicsObject[] objects, int frameRate) {
        super.updateForces(objects, frameRate);
        for (PhysicsObject object : objects) {
            if (object != this && isColliding(object)) {
//                Collision.newCollision(this, object);
            }
        }
    }

    public void populateRows() {
        for (int i = 0; i < getRows().length; i++) {
            double y = i - getR() / 2.0;
            double x = Math.sqrt(getR() - Math.pow(y, 2));
            newRow(i, -x, x);
        }
    }
}
