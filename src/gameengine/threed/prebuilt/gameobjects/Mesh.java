package gameengine.threed.prebuilt.gameobjects;

import gameengine.skeletons.Modifier;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector3D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mesh<PolyType extends  Polygon> extends Modifier {
    private PolyType[] polygons;
    private Vector3D[] vertices;
    private final PolyType[] validPolyArray;


    /*
     * Construction:
     */

    public Mesh(PolyType[] validPolyArray) {
        super();
        this.validPolyArray = validPolyArray;
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
                                "Polygons", Polygon[].class,
                                this::setPolygons),
                        new ModifierInstantiateParameter<>(
                                "Vertices", Vector3D[].class,
                                this::setVertices)
                )
        };
    }


    /*
     * Functionality:
     */

    public static Vector3D[] getVertices(Polygon[] polygons) {
        ArrayList<Vector3D> newVertices = new ArrayList<>();
        for (Polygon poly : polygons) {
            for (Vector3D vertex : poly.getVertices()) {
                boolean alreadyContained = false;
                for (Vector3D other : newVertices) {
                    if (other.equals(vertex)) {
                        alreadyContained = true;
                        break;
                    }
                }

                if (!alreadyContained) {
                    newVertices.add(vertex);
                }
            }
        }

        return newVertices.toArray(new Vector3D[0]);
    }


    /*
     * Utilities:
     */

    public PolyType[] getPolygons() {
        return polygons;
    }

    public void setPolygons(Polygon[] polygons) {
        // TODO test
        ArrayList<Polygon> newPolys = new ArrayList<>();
        Collections.addAll(newPolys, polygons);
        this.polygons = newPolys.toArray(validPolyArray);
        vertices = getVertices(polygons);
    }

//    public void setPolygons(PolyType[] polygons) {
//        this.polygons = polygons;
//        vertices = getVertices(polygons);
//    }

    public Vector3D[] getVertices() {
        return vertices;
    }

    public void setVertices(Vector3D[] vertices) {
        this.vertices = vertices;
    }

    public void setMesh(Vector3D[] vertices, PolyType[] polygons) {
        this.vertices = vertices;
        this.polygons = polygons;
    }
}
