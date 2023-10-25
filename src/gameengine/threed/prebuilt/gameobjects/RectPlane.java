package gameengine.threed.prebuilt.gameobjects;

import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.BaseTexture;
import gameengine.threed.graphics.Texture;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
import gameengine.threed.prebuilt.objectmovement.collisions.PolyObject;
import gameengine.threed.prebuilt.objectmovement.collisions.TriCollider;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RectPlane extends PolyObject {
    private Texture texture;
    public RectPlane(Vector3D pointA, Vector3D displacement, Texture texture) {
        //TODO
        super(new Vector3D[] {
                pointA,
                new Vector3D(pointA.getX() + displacement.getX(), pointA.getY() + displacement.getY(), pointA.getZ()),
                pointA.add(displacement),
                new Vector3D(pointA.getX(), pointA.getY(), pointA.getZ() + displacement.getZ())
        }, new Mesh(), new InPlane3D());
        get(InPlane3D.class).instantiate(this, pointA.getX(), pointA.getY(), pointA.getZ());
        setTexture(texture);
    }

    public RectPlane(double x, double y, double z, Vector3D displacement, Texture texture) {
        //TODO
        super(new Vector3D[] {
//                new Vector3D(x, y, z),
//                new Vector3D(x + displacement.getX(), y, z),
//                new Vector3D(x + displacement.getX(), y + displacement.getY(), z),
//                new Vector3D(x, y + displacement.getY(), z),
                new Vector3D(x, y, z),
                 new Vector3D(x + displacement.getX(), y + (displacement.getZ() != 0 ? displacement.getY() : 0), z),
                new Vector3D(x + displacement.getX(), y + displacement.getY(), z + displacement.getZ()),
                new Vector3D(x, y + (displacement.getZ() == 0 ? displacement.getY() : 0), z + displacement.getZ()),
        }, new Mesh(), new InPlane3D());
//        System.out.println(Arrays.toString(new Vector3D[]{
//                new Vector3D(x, y, z),
//                new Vector3D(x + displacement.getX(), y, z),
//                new Vector3D(x + displacement.getX(), y + displacement.getY(), z + displacement.getZ()),
//                new Vector3D(x, y + displacement.getY(), z + displacement.getZ())
//        }));
        get(InPlane3D.class).instantiate(this, x, y, z);
        setTexture(texture);
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
        Texture texture = new BaseTexture(Color.WHITE, true, 0);

        return new Tri[]{
                // Front face triangles
                new Tri(vertices[0], vertices[1], vertices[2], texture),
                new Tri(vertices[0], vertices[2], vertices[3], texture),
        };
    }

    private Color randomColor() {
        return new Color(Math.random(), Math.random(), Math.random(), 1);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        for (Tri poly : get(Mesh.class).getPolygons()) {
            poly.get(TriCollider.class).setTexture(texture);
        }
    }
}
