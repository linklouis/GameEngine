package gameengine.threed.prebuilt.gameobjects;

import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.threed.graphics.raytraceing.textures.Metallic;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
import gameengine.threed.prebuilt.objectmovement.collisions.PolyObject;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class TriRectangle extends PolyObject<Tri> {
    private RayTracingTexture texture;


    /*
     * Construction:
     */

    public TriRectangle(double x, double y, double z, Vector3D space,
                        RayTracingTexture texture) {
        super(generateVertices(x, y, z, space), new Mesh<>(new Tri[0]), new InPlane3D());
        get(InPlane3D.class).instantiate(this, x, y, z);
        setTexture(texture);
    }

    public TriRectangle(double x, double y, double z, Vector3D space, Color color,
                        boolean isLightSource) {
        super(generateVertices(x, y, z, space), new Mesh<>(new Tri[0]), new InPlane3D());
        setTexture(new Metallic(color, isLightSource ? 1 : 0, 0));
        get(InPlane3D.class).instantiate(this, x, y, z);
    }

    private static Vector3D[] generateVertices(double x, double y, double z,
                                               Vector3D space) {
        return new Vector3D[] {
                new Vector3D(x, y, z),
                new Vector3D(
                        x + space.getX(),
                        y,
                        z),
                new Vector3D(
                        x + space.getX(),
                        y + space.getY(),
                        z),
                new Vector3D(
                        x,
                        y + space.getY(),
                        z),
                new Vector3D(
                        x,
                        y,
                        z + space.getZ()),
                new Vector3D(
                        x + space.getX(),
                        y,
                        z + space.getZ()),
                new Vector3D(
                        x + space.getX(),
                        y + space.getY(),
                        z + space.getZ()),
                new Vector3D(
                        x,
                        y + space.getY(),
                        z + space.getZ())
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
        RayTracingTexture texture = new Metallic(Color.WHITE, 1, 0);

        return new Tri[]{
                // Front face triangles
                new Tri(vertices[0], vertices[1], vertices[2], texture),
                new Tri(vertices[0], vertices[2], vertices[3], texture),

                // Right face triangles
                new Tri(vertices[1], vertices[5], vertices[6], texture),
                new Tri(vertices[1], vertices[6], vertices[2], texture),

                // Back face triangles
                new Tri(vertices[5], vertices[4], vertices[7], texture),
                new Tri(vertices[5], vertices[7], vertices[6], texture),

                // Left face triangles
                new Tri(vertices[4], vertices[0], vertices[3], texture),
                new Tri(vertices[4], vertices[3], vertices[7], texture),

                // Top face triangles
                new Tri(vertices[3], vertices[2], vertices[6], texture),
                new Tri(vertices[3], vertices[6], vertices[7], texture),

                // Bottom face triangles
                new Tri(vertices[0], vertices[4], vertices[5], texture),
                new Tri(vertices[0], vertices[5], vertices[1], texture),
        };
    }


    /*
     * Utilities:
     */

    private Color randomColor() {
        return new Color(Math.random(), Math.random(), Math.random(), 1);
    }

    public RayTracingTexture getTexture() {
        return texture;
    }

    public void setTexture(RayTracingTexture texture) {
        this.texture = texture;
//        int num = 1;
        for (Polygon poly : get(Mesh.class).getPolygons()) {
//            double col = (double) num / get(Mesh.class).getPolygons().length;
            poly.get(RayTraceable.class).setTexture(texture);//new Metallic(new Color(col, col, col, 1), texture.isLightSource(), texture.getReflectivity()));
//            num++;
        }
    }
}
