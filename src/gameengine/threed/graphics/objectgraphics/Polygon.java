package gameengine.threed.graphics.objectgraphics;

import gameengine.vectormath.Vector3D;

public abstract class Polygon extends RayTraceable {

    public abstract Vector3D[] getVertices();

    public Vector3D getVertex(int index) {
        return getVertices()[index];
    }

    @Override
    public Vector3D getCenter() {
        Vector3D av = new Vector3D(0);

        for (Vector3D vertex : getVertices()) {
            av.add(vertex);
        }

        return av.scalarDivide(getVertices().length);
    }
}
