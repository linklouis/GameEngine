package gameengine.threed.graphics.raytraceing;

import gameengine.skeletons.Modifier;
import gameengine.threed.graphics.GraphicsObject3D;
import gameengine.threed.graphics.Texture;
import gameengine.threed.graphics.Visual3D;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public abstract class RayTraceable extends GraphicsObject3D {
    private Texture texture = null;


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

    public Vector3D reflection(Ray ray) {
        return texture.reflection(ray.getDirection(), surfaceNormal(ray));
    }

    /**
     * Finds the first intersection a ray will have with the
     * {@code Collider3D}.
     *
     * @param ray The ray to find a collision with.
     * @param curSmallestDist The largest distance the output is looking for.
     *                        Can be used for optimization by counting out a
     *                        {@code Collider3D} early.
     * @return -1 if never enters range or if collision is behind start.
     * Otherwise, the distance to first hit
     */
    public abstract double distanceToCollide(Ray ray, double curSmallestDist);

    public abstract Vector3D surfaceNormal(Ray ray);


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