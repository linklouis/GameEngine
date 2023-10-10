import java.awt.*;
import java.util.Vector;

public class PhysicsObject {//extends CircleGameObj {
    private static final double G = 6.67408 * Math.pow(10, -11);

    private Point location = new Point(0, 0);

    private double distance(Point p1, Point p2) {
        return p1.distance(p2);
    }

//    public Vector<Double> forceOfGravity(PhysicsObject po1) {
//
//    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }
}
