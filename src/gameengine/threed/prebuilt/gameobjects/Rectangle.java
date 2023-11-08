package gameengine.threed.prebuilt.gameobjects;

import gameengine.threed.graphics.textures.RayTracingTexture;
import gameengine.threed.graphics.textures.ReflectingTexture;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public class Rectangle extends PolyObject<Quad> {
    /*
     * Construction:
     */

    public Rectangle(double x, double y, double z, Vector3D space,
                     RayTracingTexture texture) {
        super(generateVertices(x, y, z, space));
        setTexture(texture);
    }

    public Rectangle(double x, double y, double z, Vector3D space, Color color,
                     boolean isLightSource) {
        super(generateVertices(x, y, z, space));
        setTexture(new ReflectingTexture(color, isLightSource, 0));
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
    protected Quad[] createMesh(Vector3D[] vertices) {
        RayTracingTexture texture = new ReflectingTexture(Color.WHITE, true, 0);

        return new Quad[] {
                // Front face
                new Quad(vertices[0], vertices[1], vertices[2], vertices[3], texture),

                // Right face
                new Quad(vertices[1], vertices[5], vertices[6], vertices[2], texture),

                // Back face
                new Quad(vertices[5], vertices[4], vertices[7], vertices[6], texture),

                // Left face
                new Quad(vertices[4], vertices[0], vertices[3], vertices[7], texture),

                // Top face
                new Quad(vertices[3], vertices[2], vertices[6], vertices[7], texture),

                // Bottom face
                new Quad(vertices[0], vertices[4], vertices[5], vertices[1], texture),
        };
    }


    /*
     * Utilities:
     */

    private Color randomColor() {
        return new Color(Math.random(), Math.random(), Math.random(), 1);
    }
}
