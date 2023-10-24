package gameengine.threed.graphics;

import gameengine.skeletons.GraphicsObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.raytraceing.Ray;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public abstract class GraphicsObject3D extends GraphicsObject {
    private Texture texture;

    /*
     * Construction:
     */

    public GraphicsObject3D() {
        super();
    }

    public GraphicsObject3D(Modifier... modifiers) {
        super(modifiers);
    }


    /*
     * Functionality:
     */

    public Vector3D reflection(Ray ray) {
        return texture.reflection(ray.getDirection(), surfaceNormal(ray.getPosition(), ray.getDirection()));
    }

    public abstract Vector3D surfaceNormal(Vector3D point, Vector3D incident);


    /*
     * Utilities:
     */

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Color getColor() {
        return getTexture().getColor();
    }

    public void setColor(Color color) {
        getTexture().setColor(color);
    }
}
