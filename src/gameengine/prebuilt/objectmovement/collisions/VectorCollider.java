package gameengine.prebuilt.objectmovement.collisions;

import gameengine.objects.Modifier;
import gameengine.prebuilt.objectmovement.InPlane;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.geom.Point2D;

public class VectorCollider extends Collider<VectorCollider> {
    private Vector2D[] vectors;

    /*
     * Construction:
     */

    public VectorCollider() {
        super();
    }

    public VectorCollider(Modifier... modifiers) {
        super(modifiers);
    }

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
                        new ModifierInstantiateParameter<>(
                                "collisionHandler", CollisionHandler.class,
                                this::setHandler),
                        new ModifierInstantiateParameter<>(
                                "vectors", Vector2D[].class,
                                this::setVectors)
                )
        };
    }


    /*
     * Functionality:
     */

    @Override
    public boolean isColliding(VectorCollider coll) {
        return false; //TODO
    }

    @Override
    public boolean contains(Point2D point) {
        Vector2D displacementFromPoint = Vector2D
                .displacement(getCenter(), point);
        double edgeDistanceAtPoint = displacementFromPoint
                .closestMatch(getVectors()).magnitude();
        return edgeDistanceAtPoint >= displacementFromPoint.magnitude();
    }

    @Override
    public double minX() {
        double minimum = getVector(0).getX();
        for (Vector2D vector : getVectors()) {
            if (vector.getX() < minimum) {
                minimum = vector.getX();
            }
        }
        return getRootLocation().getX() + minimum;
    }

    @Override
    public double minY() {
        double minimum = getVector(0).getY();
        for (Vector2D vector : getVectors()) {
            if (vector.getY() < minimum) {
                minimum = vector.getY();
            }
        }
        return getRootLocation().getY() + minimum;
    }

    @Override
    public double maxX() {
        double maximum = getVector(0).getX();
        for (Vector2D vector : getVectors()) {
            if (vector.getX() > maximum) {
                maximum = vector.getX();
            }
        }
        return getRootLocation().getX() + maximum;
    }

    @Override
    public double maxY() {
        double maximum = getVector(0).getY();
        for (Vector2D vector : getVectors()) {
            if (vector.getY() > maximum) {
                maximum = vector.getY();
            }
        }
        return getRootLocation().getY() + maximum;
    }

    @Override
    public Point2D.Double getCenter() {
        return new Point2D.Double((minX() - maxX()) / 2,(minY() - maxY()) / 2 );
    }

    @Override
    public Class<VectorCollider> getColliderClass() {
        return null;
    }

    @Override
    public void paint(GraphicsContext gc, Color color) {

    }


    /*
     * Utilities:
     */

    public Vector2D[] getVectors() {
        return vectors;
    }

    public Vector2D getVector(int index) {
        return getVectors()[0];
    }

    public void setVectors(Vector2D[] vectors) {
        this.vectors = vectors;
    }

    public Point2D.Double getRootLocation() { // TODO take in a location in instantiate?
        return getFromParent(InPlane.class).getLocation();
    }
}
