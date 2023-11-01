package gameengine.threed.prebuilt.gameobjects;

import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.threed.graphics.raytraceing.textures.ReflectingTexture;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
import gameengine.threed.prebuilt.objectmovement.collisions.PolyObject;
import gameengine.threed.graphics.raytraceing.objectgraphics.TriGraphics;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class RectPlane extends PolyObject {
    private RayTracingTexture texture;
    public RectPlane(Vector3D position, Vector3D displacement,
                     RayTracingTexture texture) {
        super(generateVertices(position, displacement),
                new Mesh(), new InPlane3D());
        get(InPlane3D.class).instantiate(this,
                position.getX(), position.getY(), position.getZ());

        setTexture(texture);
    }

    public RectPlane(double x, double y, double z,
                     Vector3D displacement, RayTracingTexture texture) {
        super(generateVertices(new Vector3D(x, y, z), displacement),
                new Mesh(), new InPlane3D());
        get(InPlane3D.class).instantiate(this, x, y, z);

        setTexture(texture);
    }

    private static Vector3D[] generateVertices(Vector3D position,
                                               Vector3D displacement) {
        return new Vector3D[] {
                position,
                new Vector3D(
                        position.getX() + displacement.getX(),
                        position.getY()
                                + (displacement.getZ() != 0 ?
                                displacement.getY()
                                : 0),
                        position.getZ()),
                new Vector3D(
                        position.getX() + displacement.getX(),
                        position.getY() + displacement.getY(),
                        position.getZ() + displacement.getZ()),
                new Vector3D(
                        position.getX(),
                        position.getY()
                                + (displacement.getZ() == 0 ?
                                displacement.getY()
                                : 0),
                        position.getZ() + displacement.getZ()),
        };
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane3D.class);
//                add(Visual2D.class);
            }
        };
    }

    @Override
    protected Tri[] createMesh(Vector3D[] vertices) {
        RayTracingTexture texture = new ReflectingTexture(Color.WHITE, true, 0);

        return new Tri[]{
                new Tri(vertices[0], vertices[1], vertices[2], texture),
                new Tri(vertices[0], vertices[2], vertices[3], texture)
        };
    }

    private Color randomColor() {
        return new Color(Math.random(), Math.random(), Math.random(), 1);
    }

    public RayTracingTexture getTexture() {
        return texture;
    }

    public void setTexture(RayTracingTexture texture) {
        this.texture = texture;
        for (Tri poly : get(Mesh.class).getPolygons()) {
            poly.get(TriGraphics.class).setTexture(texture);
        }
    }
}
