package gameengine.vectormath;

import java.awt.geom.Point2D;

public class Vector3D extends Vector<Vector3D> {
    private static final int SIZE = 3;

    public Vector3D(final double x, final double y, final double z) {
        super(x, y, z);
    }

    public Vector3D(final double[] components) {
        super(components);
    }

    public double getX() {
        return getComponents()[0];
    }

    public double getY() {
        return getComponents()[1];
    }

    public double getZ() {
        return getComponents()[2];
    }

    public Vector3D crossProduct(final Vector3D other) {
        final double[] resultComponents = new double[size()];
        resultComponents[0] =
                this.getY() * other.getZ() - this.getZ() * other.getY();
        resultComponents[1] =
                this.getZ() * other.getX() - this.getX() * other.getZ();
        resultComponents[2] =
                this.getX() * other.getY() - this.getY() * other.getX();
        return newVector(resultComponents);
    }

    public static Vector3D empty() {
        double[] newComponents = new double[SIZE];
        return new Vector3D(newComponents);
    }

    public static Vector3D random(double min, double max) {
        double scale = max - min;
        double x = Math.random() * scale + min;
        double y = Math.random() * scale + min;
        double z = Math.random() * scale + min;
        return new Vector3D(x, y, z);
    }


    public double distance(Vector3D vector) {
        return Math.abs(subtract(vector).magnitude());
    }

    @Override
    public  Vector3D newVector(final double... components) {
        return new Vector3D(components);
    }

    public Vector3D newEmpty() {
        return empty();
    }
    @Override
    protected  int size() {
        return SIZE;
    }
}
