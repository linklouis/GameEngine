package gameengine.experimental;

import gameengine.objects.GameObject;
import gameengine.prebuilt.InPlane;

import java.awt.geom.Point2D;

public interface UsesLocation {
    default Point2D.Double getLocation() {
        return getParent().get(InPlane.class).getLocation();
    }

    abstract GameObject getParent();
}
