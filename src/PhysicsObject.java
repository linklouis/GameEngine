import javafx.scene.paint.Color;
import vectormath.Vector2D;

public class PhysicsObject extends GameObject {
    private static final double G = 6.67408 * Math.pow(10, -11);

    private final double mass;
    private Vector2D velocity;

    public PhysicsObject(double x, double y, Row[] rows, Color color, double mass) {
        super(x, y, rows, color);
        this.mass = mass;
    }

    public Vector2D forceOfGravity(PhysicsObject po1) {
        double force = G * getMass() * po1.getMass();
        return new Vector2D(force / (po1.getX() - getX()), force / (po1.getY() - getY()));
    }

    public void update(PhysicsObject[] objects) {
        Vector2D netForce = Vector2D.empty();

        for (PhysicsObject object : objects) {
            netForce = netForce.add(forceOfGravity(object));
        }

        Vector2D resultantVelocity = netForce.scalarDivide(getMass());

        setVelocity(getVelocity().add(resultantVelocity));

        move(getVelocity());
    }

    public void move(Vector2D translation) {
        setLocation(translation.add(this).toPoint());
    }

    public double getMass() {
        return mass;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }
}
