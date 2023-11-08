package gameengine.threed.prebuilt.gameobjects;

import gameengine.threed.GameDriver3D;
import gameengine.threed.graphics.raytraceing.objectgraphics.Polygon;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.vectormath.Vector3D;

public abstract class PolyObject<PolyType extends Polygon> extends Mesh<PolyType> {
    public PolyObject(Mesh<PolyType> mesh) {
        super(mesh);
    }

    public PolyObject(Vector3D[] vertices) {
        super(vertices);
        setPolygons(createMesh(vertices));
    }

    public void initiate(GameDriver3D driver) {
        forEachPoly(driver::newObject);
    }

    public void setTexture(RayTracingTexture texture) {
        forEachPoly(poly -> poly.setTexture(texture));
    }

    protected abstract PolyType[] createMesh(Vector3D[] vertices);
}
