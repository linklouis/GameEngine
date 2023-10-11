import java.awt.geom.Point2D;

public interface UsesLocation {
    default Point2D.Double getLocation() {
        return getParent().get(InPlain.class).getLocation();
    }

    abstract GameObject getParent();
}
