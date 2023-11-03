package gameengine.threed.graphics.raytraceing.objectgraphics;

import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.GraphicsObject3D;
import gameengine.threed.graphics.Visual3D;
import gameengine.threed.graphics.raytraceing.LightRay;
import gameengine.threed.graphics.raytraceing.Reflection;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.threed.utilities.RayIntersectable;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public abstract class RayTraceable extends GraphicsObject3D implements RayIntersectable {
    private RayTracingTexture texture = null;


    /*
     * Construction:
     */

    public RayTraceable() {
        super();
    }

    public RayTraceable(final Modifier[] modifiers) {
        super(modifiers);
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        List<Class<? extends Modifier>> modifiers = new ArrayList<>();
        modifiers.add(Visual3D.class);
        return modifiers;
    }


    /*
     * Functionality:
     */

    public Reflection reflection(LightRay lightRay) {
        return texture.reflection(lightRay, surfaceNormal(lightRay));
    }


    /*
     * Utilities:
     */

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
