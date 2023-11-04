package gameengine.threed.prebuilt.gameobjects;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.Visual3D;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.vectormath.Vector3D;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Polygon extends GameObject {
    public Polygon(final RayTraceable graphics, Modifier... modifiers) {
        super(new ArrayList<>() {
            {
                add(new Visual3D());
                add(graphics);
                Collections.addAll(this, modifiers);
            }
        });
        get(Visual3D.class).instantiate(this, graphics);
    }

    public abstract Vector3D[] getVertices();

    @Override
    public String toString() {
        return "Polygon: " + get(RayTraceable.class).toString();
    }
}
