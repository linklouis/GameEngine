package gameengine.threed.graphics.raytraceing.objectgraphics;

import gameengine.threed.graphics.GraphicsObject3D;
import gameengine.threed.graphics.raytraceing.Ray;
import gameengine.threed.graphics.raytraceing.Reflection;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.threed.utilities.RayIntersectable;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public abstract class RayTraceable extends GraphicsObject3D implements RayIntersectable {
    private RayTracingTexture texture = null;


    /*
     * Functionality:
     */

    public Reflection reflection(Ray ray) {
        return texture.reflection(ray, surfaceNormal(ray));
    }


    /*
     * Utilities:
     */

    public abstract Vector3D[] getVertices();

    public RayTracingTexture getTexture() {
        return texture;
    }

    public void setTexture(RayTracingTexture texture) {
        this.texture = texture;
    }

    public Color getColor() {
        return getTexture().getColor();
    }

    public Vector3D colorVector() {
        return new Vector3D(getTexture().getColor());
    }
    public void setColor(Color color) {
        getTexture().setColor(color);
    }
}
