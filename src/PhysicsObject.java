import javafx.scene.canvas.GraphicsContext;
import vectormath.Vector2D;

import java.util.List;

public class PhysicsObject extends Collidable {
    private static final double G = 6.67408 * Math.pow(10, -11);

    private long mass;
    private Vector2D velocity = Vector2D.empty();

    public PhysicsObject(GameObject parent) {
        super(parent);
    }

    public PhysicsObject(GameObject parent, Modifier[] modifiers) {
        super(parent, modifiers);
    }

    @Override
    public void instantiate(Object... args) {
        super.instantiate(args);
        if (args[1] instanceof Long) {
            mass = (long) args[1];
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        List<Class<? extends Modifier>> dependencies = super.getDependencies();
        dependencies.add(Visual.class);
        return dependencies;
    }

    public Vector2D forceOfGravity(PhysicsObject po1) {
//        assert po1.containsModifier(PhysicsObject.class);
        double scalarForce = G * getMass() * po1.getMass() / Math.pow(getLocation().distance(po1.getLocation()) / 100, 2);
        return Vector2D.displacement(po1.getLocation(), this.getLocation()).unitVector().scalarMultiply(scalarForce);
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
        getLocation().setLocation(translation.add(getLocation()).toPoint());
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
    protected class PhysicsDraw extends Visual {

        public PhysicsDraw(GameObject parent) {
            super(parent);
        }

        @Override
        public void paint(GraphicsContext gc) {
            {
                gc.setFill(getColor());
                for (Row row : getRows()) {
                    Row adjustedRow = row.at(getLocation());
                    adjustedRow.paint(gc);
                }
            }
        }
    }

}
