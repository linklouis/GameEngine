package gameengine.threed.prebuilt.objectmovement;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class InPlane3D extends Modifier {
    private Vector3D location = new Vector3D(0);

    public InPlane3D() {
        super();
    }

    public InPlane3D(Modifier... modifiers) {
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
    }

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
                        new ModifierInstantiateParameter<>(
                                "location", Vector3D.class, this::setLocation)
                ),
                new ArgumentContext(
                        new ModifierInstantiateParameter<>(
                                "x", Double.class,
                                (Double x) -> this.setLocation(x, getY(), getZ())),
                        new ModifierInstantiateParameter<>(
                                "y", Double.class,
                                (Double y) -> this.setLocation(getX(), y, getZ())),
                        new ModifierInstantiateParameter<>(
                                "z", Double.class,
                                (Double z) -> this.setLocation(getX(), getY(), z))
                )
        };
    }

    public void setLocation(double x, double y, double z) {
        location = new Vector3D(x, y, z);
    }

    public void setLocation(Vector3D newLocation) {
        location = newLocation;
    }

    public void updateLocation(Vector3D displacement) {
        location = location.add(displacement);
    }

    public Vector3D getLocation() {
        return location;
    }

    public double getX() {
        return getLocation().getX();
    }

    public double getY() {
        return getLocation().getY();
    }

    public double getZ() {
        return getLocation().getZ();
    }

    public void setX(double x) {
        setLocation(x, getLocation().getY(), getLocation().getZ());
    }

    public void setY(double y) {
        setLocation(getLocation().getX(), y, getLocation().getZ());
    }

    public void setZ(double z) {
        setLocation(getLocation().getX(), getLocation().getY(), z);
    }

    public double distance(Vector3D point) {
        return getLocation().distance(point);
    }

    public double distance(GameObject gObj) {
        assert gObj.containsModifier(InPlane3D.class);
        return distance(gObj.get(InPlane3D.class).location);
    }
}
