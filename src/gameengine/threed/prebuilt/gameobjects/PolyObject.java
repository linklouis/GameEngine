package gameengine.threed.prebuilt.gameobjects;

import gameengine.skeletons.Modifier;
import gameengine.threed.GameDriver3D;
import gameengine.vectormath.Vector3D;

public abstract class PolyObject<PolyType extends Polygon> {
    private Mesh<PolyType> mesh;

    public PolyObject(Mesh<PolyType> mesh) {
        this.mesh = mesh;
    }

    public PolyObject(Vector3D[] vertices) {
        mesh = new Mesh<>(createMesh(vertices), vertices);
    }

    public void initiate(GameDriver3D driver) {
        for (Polygon poly : mesh.getPolygons()) {
            driver.newObject(poly);
        }
    }

    protected abstract PolyType[] createMesh(Vector3D[] vertices);

    public Mesh<PolyType> getMesh() {
        return mesh;
    }

    public void setMesh(Mesh<PolyType> mesh) {
        this.mesh = mesh;
    }
}
