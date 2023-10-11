import javafx.scene.paint.Color;
import vectormath.Vector2D;

public class PhysicsObject extends GameObject {
    private static final double G = 6.67408 * Math.pow(10, -11);

    private final long mass;
    private Vector2D velocity = Vector2D.empty();

    public PhysicsObject(double x, double y, Row[] rows, Color color, long mass) {
        super(x, y, rows, color);
        this.mass = mass;
    }

    public Vector2D forceOfGravity(PhysicsObject po1) {
        double scalarForce = G * getMass() * po1.getMass() / Math.pow(distance(po1) / 100, 2);
        return Vector2D.displacement(po1, this).unitVector().scalarMultiply(scalarForce);
    }

    public void updateForces(PhysicsObject[] objects, int frameRate) {
        Vector2D netForce = Vector2D.empty();

        for (PhysicsObject object : objects) {
            if (object != this) {
                netForce = netForce.add(forceOfGravity(object));
            }
        }

        Vector2D resultantVelocity = netForce.scalarDivide(getMass()).scalarDivide(frameRate);

        setVelocity(getVelocity().add(resultantVelocity));
    }

    public void updatePosition(int frameRate) {
        move(getVelocity().scalarDivide(frameRate));
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
