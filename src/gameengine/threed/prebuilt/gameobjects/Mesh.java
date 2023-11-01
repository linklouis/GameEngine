package gameengine.threed.prebuilt.gameobjects;

import gameengine.skeletons.Modifier;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class Mesh extends Modifier {
    private Tri[] polygons;
    private Vector3D[] vertices;


    /*
     * Construction:
     */

    public Mesh() {
        super();
    }

    public Mesh(Modifier... modifiers) {
        super(modifiers);
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>();
    }

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
                        new ModifierInstantiateParameter<>(
                                "Polygons", Tri[].class,
                                this::setPolygons),
                        new ModifierInstantiateParameter<>(
                                "Vertices", Vector3D[].class,
                                this::setVertices)
                )
        };
    }


    /*
     * Utilities:
     */

    public Tri[] getPolygons() {
        return polygons;
    }

    public void setPolygons(Tri[] polygons) {
        this.polygons = polygons;
    }

    public Vector3D[] getVertices() {
        return vertices;
    }

    public void setVertices(Vector3D[] vertices) {
        this.vertices = vertices;
    }
}
