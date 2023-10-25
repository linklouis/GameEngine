package gameengine.threed.prebuilt.gameobjects;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.BaseTexture;
import gameengine.threed.graphics.Texture;
import gameengine.threed.graphics.Visual3D;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
import gameengine.threed.prebuilt.objectmovement.collisions.PolyObject;
import gameengine.threed.prebuilt.objectmovement.collisions.RectCollider;
import gameengine.threed.prebuilt.objectmovement.collisions.TriCollider;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Rectangle extends PolyObject {
    private Texture texture;
    public Rectangle(double x, double y, double z, Vector3D space, Texture texture) {
        super(new Vector3D[] {
//                new Vector3D(x, y, z), space

//                new Vector3D(x, y, z),
//                new Vector3D(x + space.getX(), y, z),
//                new Vector3D(x + space.getX(), y + space.getY(), z),
//                new Vector3D(x, y + space.getY(), z),
//
//                new Vector3D(x + space.getX(), y, z),
//                new Vector3D(x, y + space.getY(), z + space.getZ()),
//                new Vector3D(x, y, z + space.getZ()),
//                new Vector3D( + space.getX(), y + space.getY(), z + space.getZ())
                new Vector3D(x, y, z),
                new Vector3D(x + space.getX(), y, z),
                new Vector3D(x + space.getX(), y + space.getY(), z),
                new Vector3D(x, y + space.getY(), z),
                new Vector3D(x, y, z + space.getZ()),
                new Vector3D(x + space.getX(), y, z + space.getZ()),
                new Vector3D(x + space.getX(), y + space.getY(), z + space.getZ()),
                new Vector3D(x, y + space.getY(), z + space.getZ())
        }, new Mesh(), new InPlane3D());
        get(InPlane3D.class).instantiate(this, x, y, z);
        setTexture(texture);
    }

    public Rectangle(double x, double y, double z, Vector3D space, Color color, boolean isLightSource) {
        super(new Vector3D[] {
//                new Vector3D(x, y, z),
//                space
                new Vector3D(x, y, z),
                new Vector3D(x + space.getX(), y, z),
                new Vector3D(x + space.getX(), y + space.getY(), z),
                new Vector3D(x + space.getX(), y, z + space.getZ()),
                new Vector3D(x, y + space.getY(), z),
                new Vector3D(x, y + space.getY(), z + space.getZ()),
                new Vector3D(x, y, z + space.getZ()),
                new Vector3D( + space.getX(), y + space.getY(), z + space.getZ())
        }, new Mesh(), new InPlane3D());
        setTexture(new BaseTexture(color, isLightSource, 0));
        get(InPlane3D.class).instantiate(this, x, y, z);
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
//        Texture texture = new BaseTexture(Color.WHITE, true, 0);
//
//        return new Tri[] {
//                new Tri(vertices[0], vertices[0].add(vertices[1].onlyX()),  vertices[0].add(vertices[1].onlyY()), texture),
//        };
        Texture texture = new BaseTexture(Color.WHITE, true, 0);

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

//        Tri[] polys = new Tri[12]; // 12 triangles for a rectangular prism
//        // Front face triangles
//        polys[0] = new Tri(vertices[0], vertices[1], vertices[2], texture);
//        polys[1] = new Tri(vertices[0], vertices[2], vertices[3], texture);
//
//        // Right face triangles
//        polys[2] = new Tri(vertices[1], vertices[5], vertices[6], texture);
//        polys[3] = new Tri(vertices[1], vertices[6], vertices[2], texture);
//
//        // Back face triangles
//        polys[4] = new Tri(vertices[5], vertices[4], vertices[7], texture);
//        polys[5] = new Tri(vertices[5], vertices[7], vertices[6], texture);
//
//        // Left face triangles
//        polys[6] = new Tri(vertices[4], vertices[0], vertices[3], texture);
//        polys[7] = new Tri(vertices[4], vertices[3], vertices[7], texture);
//
//        // Top face triangles
//        polys[8] = new Tri(vertices[3], vertices[2], vertices[6], texture);
//        polys[9] = new Tri(vertices[3], vertices[6], vertices[7], texture);
//
//        // Bottom face triangles
//        polys[10] = new Tri(vertices[0], vertices[4], vertices[5], texture);
//        polys[11] = new Tri(vertices[0], vertices[5], vertices[1], texture);

//        return polys;
    }

    private Color randomColor() {
        return new Color(Math.random(), Math.random(), Math.random(), 1);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
//        int num = 1;
        for (Tri poly : get(Mesh.class).getPolygons()) {
//            double col = (double) num / get(Mesh.class).getPolygons().length;
            poly.get(TriCollider.class).setTexture(texture);//new BaseTexture(new Color(col, col, col, 1), texture.isLightSource(), texture.getReflectivity()));
//            num++;
        }
    }
}
