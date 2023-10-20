package gameengine.vectormath;

import java.awt.geom.Point2D;

public class Vector2D extends Vector<Vector2D> {
    // TODO implement new Vector format
    private static final int SIZE = 2;
    public Vector2D(final double x, final double y) {
        super(x, y);
    }
    public Vector2D(final double[] components) {
        super(components);
    }

    @Override
    public double[] getComponents() {
        return new double[0];
    }

    @Override
    public double getComponent(int index) {
        return 0;
    }

    @Override
    protected void setComponent(int component, double newValue) {

    }

    public Vector2D add(final Point2D other) {
        return super.add(asVector(other));
    }

    public Vector2D subtract(final Point2D other) {
        return super.subtract(asVector(other));
    }

    public double dotProduct(final Point2D other) {
        return super.dotProduct(asVector(other));
    }

    public double getX() {
        return getComponents()[0];
    }

    public double getY() {
        return getComponents()[1];
    }

    public Point2D.Double toPoint() {
        return new Point2D.Double(getX(), getY());
    }

    public Point2D pointAtDistanceFrom(Point2D point) {
        return new Point2D.Double(point.getX() + getX(), point.getY() + getY());
    }

    public static Vector2D asVector(Point2D point) {
        return new Vector2D(point.getX(), point.getY());
    }


    public static Vector2D displacement(Point2D p1, Point2D p2) {
        return new Vector2D(p1.getX() - p2.getX(), p1.getY() - p2.getY());
    }

    public static Vector2D empty() {
        double[] newComponents = new double[SIZE];
        return new Vector2D(newComponents);
    }

    @Override
    public Vector2D newVector(final double... components) {
        return new Vector2D(components);
    }

    @Override
    public Vector2D newEmpty() {
        return empty();
    }

    @Override
    protected int size() {
        return SIZE;
    }
}
