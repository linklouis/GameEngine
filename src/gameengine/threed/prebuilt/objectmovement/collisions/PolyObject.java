package gameengine.threed.prebuilt.objectmovement.collisions;

import gameengine.drivers.GameDriver3D;
import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.prebuilt.gameobjects.Mesh;
import gameengine.threed.prebuilt.gameobjects.Tri;
import gameengine.vectormath.Vector3D;

public abstract class PolyObject extends GameObject {

    public PolyObject(Mesh mesh, Modifier... modifiers) {
        super(modifiers);
        try {
            get(Mesh.class).instantiate(this, mesh.getPolygons(), mesh.getVertices());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public PolyObject(Vector3D[] vertices, Modifier... modifiers) {
        super(modifiers);
        try {
            get(Mesh.class).instantiate(this, createMesh(vertices), vertices);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void initiate(GameDriver3D driver) {
        for (Tri poly : get(Mesh.class).getPolygons()) {
            driver.newObject(poly);
        }
    }

    protected abstract Tri[] createMesh(Vector3D[] vertices);
}
