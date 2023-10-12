package gameengine.prebuilt;

import gameengine.objects.GameObject;
import gameengine.objects.Modifier;
import gameengine.utilities.ModifierInstantiateParameter;

import java.awt.geom.Point2D;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class InPlane extends Modifier {
    private Point2D.Double location = new Point2D.Double(0, 0);

    public InPlane() {
        super();
    }

    public InPlane(Modifier... modifiers) {
        super(modifiers);
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>();
    }

    @Override
    public void instantiate(GameObject parent, Object... args) {
        try {
            super.instantiate(parent, args);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
//        if (args[0] instanceof Double && args[1] instanceof Double) {
//            location = new Point2D.Double((Double) args[0], (Double) args[1]);
//        } else if (args[0] instanceof Point2D.Double) {
//            location = (Point2D.Double) args[0];
//        } else {
//            throw new IllegalArgumentException();
//        }
    }

    @Override
    public ModifierInstantiateParameter<?>[][] getValidArguments() {
        return new ModifierInstantiateParameter<?>[][] {
                { new ModifierInstantiateParameter<>("location", Point2D.class, this) },
                { new ModifierInstantiateParameter<>("x", Double.class, (Double x) -> this.setLocation(x, getY())),
                        new ModifierInstantiateParameter<>("y", Double.class, (Double y) -> this.setLocation(getX(), y)) }
        };
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

    public void setX(double x) {
        setLocation(x, getLocation().getY());
    }

    public void setY(double y) {
        setLocation(getLocation().getX(), y);
    }

    public double distance(Point2D point) {
        return getLocation().distance(point);
    }

    public double distance(GameObject gObj) {
        assert gObj.containsModifier(InPlane.class);
        return getLocation().distance(gObj.get(InPlane.class).location);
    }
}
