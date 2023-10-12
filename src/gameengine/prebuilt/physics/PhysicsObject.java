package gameengine.prebuilt.physics;

import gameengine.objects.GameObject;
import gameengine.objects.Modifier;
import gameengine.prebuilt.InPlane;
import gameengine.prebuilt.Visual;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import gameengine.vectormath.Vector2D;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class PhysicsObject extends Visual {
    private static final double G = 6.67408 * Math.pow(10, -11);

    private long mass;
    private Vector2D velocity = Vector2D.empty();
    private Color color;

    public PhysicsObject() {
        super();
    }

    public PhysicsObject(Modifier[] modifiers) {
        super(modifiers);
    }

    @Override
    public void instantiate(GameObject parent, Object... args) {
        super.instantiate(parent);
        if (args.length == 2) { //  TODO update all other instantiates to check args length
            if (args[0] instanceof Long && args[1] instanceof Color) {
                mass = (long) args[0];
                setColor((Color) args[1]);
            } else {
                throw new IllegalArgumentException();
            }
        } else if (args.length == 3) {
            if (args[0] instanceof Long && args[1] instanceof Color && args[2] instanceof Vector2D) {
                mass = (long) args[0];
                setColor((Color) args[1]);
                setVelocity((Vector2D) args[2]);
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }

    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane.class);
                add(Collidable.class);
            }
        };
    }

    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(getColor());
        for (Collidable.Row row : getParent().get(Collidable.class).getRows()) {
            Collidable.Row adjustedRow = row.at(getLocation());
            adjustedRow.paint(gc);
        }
    }

    public Vector2D forceOfGravity(PhysicsObject po1) {
//        assert po1.containsModifier(gameengine.prebuilt.physics.PhysicsObject.class);
        double scalarForce = G * getMass() * po1.getMass() / Math.pow(getLocation().distance(po1.getLocation()) / 100, 2);
        return Vector2D.displacement(po1.getLocation(), this.getLocation()).unitVector().scalarMultiply(scalarForce);
    }

    protected Point2D.Double getLocation() { // TODO take in a location in instantiate?
        return getParent().get(InPlane.class).getLocation();
    }

    protected Collidable getCollider() {
        return getParent().get(Collidable.class);
    }

    private double getX() {
        return getParent().get(InPlane.class).getX();
    }

    private double getY() {
        return getParent().get(InPlane.class).getY();
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

//    protected class PhysicsDraw extends gameengine.prebuilt.Visual {
//
//        public PhysicsDraw(gameengine.objects.GameObject parent) {
//            super(parent);
//        }
//
//        @Override
//        public void paint(GraphicsContext gc) {
//            {
//                gc.setFill(getColor());
//                for (gameengine.prebuilt.physics.Collidable.Row row : getParent().get(gameengine.prebuilt.physics.Collidable.class).getRows()) {
//                    gameengine.prebuilt.physics.Collidable.Row adjustedRow = row.at(getLocation());
//                    adjustedRow.paint(gc);
//                }
//            }
//        }
//    }

}
