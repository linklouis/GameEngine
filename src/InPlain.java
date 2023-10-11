import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class InPlain extends Modifier {
    private Point2D.Double location;

    public InPlain(GameObject parent) {
        super(parent);
    }

    public InPlain(GameObject parent, Modifier[] modifiers) {
        super(parent, modifiers);
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>();
    }

    @Override
    public void instantiate(Object... args) {
        if (args[0] instanceof Double && args[1] instanceof Double) {
            location = new Point2D.Double((Double) args[0], (Double) args[1]);
        } else if (args[0] instanceof Point2D.Double) {
            location = (Point2D.Double) args[0];
        }
        throw new IllegalArgumentException();
    }

    public void setLocation(double x, double y) {
        location = new Point2D.Double(x, y);
    }

    public void setLocation(Point2D newLocation) {
        location = (Point2D.Double) newLocation;
    }

    public Point2D.Double getLocation() {
        return location;
    }

    public double getX() {
        return getLocation().getX();
    }

    public double getY() {
        return getLocation().getY();
    }

    public double distance(Point2D point) {
        return getLocation().distance(point);
    }

    public double distance(GameObject gObj) {
        assert gObj.containsModifier(InPlain.class);
        return getLocation().distance(gObj.get(InPlain.class).location);
    }
}
