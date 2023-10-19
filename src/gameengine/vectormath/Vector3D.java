package gameengine.vectormath;

import gameengine.threed.graphics.raytraceing.LightRay;
import javafx.scene.paint.Color;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class Vector3D extends Vector<Vector3D> {
    private static final int SIZE = 3;


    /*
     * Construction:
     */

    public Vector3D(final double x, final double y, final double z) {
        super(x, y, z);
    }

    public Vector3D(final double[] components) {
        super(components);
    }

    public Vector3D(final Color color) {
        super(color.getRed(), color.getGreen(), color.getBlue());
    }


    /*
     * Functionality:
     */

    @Override
    public Vector3D add(Vector3D other) {
        return new Vector3D(
                getX() + other.getX(),
                getY() + other.getY(),
                getZ() + other.getZ()
        );
    }

    @Override
    public Vector3D subtract(final Vector3D other) {
        return new Vector3D(
                getX() - other.getX(),
                getY() - other.getY(),
                getZ() - other.getZ()
        );
    }

    @Override
    public Vector3D scalarMultiply(final double scalar) {
        return new Vector3D(
                getX() * scalar,
                getY() * scalar,
                getZ() * scalar
        );
    }

    @Override
    public Vector3D scalarMultiply(final BigDecimal scalar) {
        return new Vector3D(
                scalar.multiply(BigDecimal.valueOf(getX())).doubleValue(),
                scalar.multiply(BigDecimal.valueOf(getY())).doubleValue(),
                scalar.multiply(BigDecimal.valueOf(getZ())).doubleValue()
        );
    }

    @Override
    public Vector3D scalarDivide(final double scalar) {
        return new Vector3D(
                getX() / scalar,
                getY() / scalar,
                getZ() / scalar
        );
    }

    @Override
    public Vector3D scalarDivide(final BigDecimal scalar) {
        double[] resultComponents = new double[size()];
        for (int i = 0; i < size(); i++) {
            resultComponents[i] = BigDecimal.valueOf(this.getComponents()[i]).divide(scalar, RoundingMode.HALF_DOWN).doubleValue();
        }
        return newVector(resultComponents);
    }

    @Override
    public double dotProduct(final Vector3D other) {
        return getX() * other.getX()
                + getY() * other.getY()
                + getZ() * other.getZ();
    }

    @Override
    public double magnitude() {
        return Math.sqrt(
                getX() * getX()
                + getY() * getY()
                + getZ() * getZ());
    }

    @Override
    public Vector3D unitVector() {
        double magnitude = magnitude();
        return new Vector3D(
                getX() / magnitude,
                getY() / magnitude,
                getZ() / magnitude
        );
    }

    @Override
    public Vector3D forEach(Function<Double, Double> function) {
        return new Vector3D(
                function.apply(getX()),
                function.apply(getY()),
                function.apply(getZ())
        );
    }

// MAYBE MORE EFFICIENT, BUT LESS READABLE
//    @Override
//    public Vector3D max() {
//        if (getX() > getY()) {
//            if (getX() > getZ()) {
//                return new Vector3D(getX(), 0, 0);
//            }
//            return new Vector3D(0, 0, getZ());
//        }
//        if (getY() > getZ()) {
//            return new Vector3D(0, getY(), 0);
//        }
//        return new Vector3D(0, 0, getZ());
//    }
//
//    @Override
//    public Vector3D min() {
//        if (getX() < getY()) {
//            if (getX() < getZ()) {
//                return new Vector3D(getX(), 0, 0);
//            }
//            return new Vector3D(0, 0, getZ());
//        }
//        if (getY() < getZ()) {
//            return new Vector3D(0, getY(), 0);
//        }
//        return new Vector3D(0, 0, getZ());
//    }

    /**
     * @return a Vector3D with only the component with the largest value
     * preserved, and all others set to 0.
     */
    @Override
    public Vector3D max() {
        double maxVal = Math.max(Math.max(getX(), getY()), getZ());
        return new Vector3D(
                getX() == maxVal ? maxVal : 0,
                getY() == maxVal ? maxVal : 0,
                getZ() == maxVal ? maxVal : 0);
    }

    /**
     * @return a Vector3D with only the component with the smallest value
     * preserved, and all others set to 0.
     */
    @Override
    public Vector3D min() {
        double minVal = Math.min(Math.min(getX(), getY()), getZ());
        return new Vector3D(getX() == minVal ? minVal : 0, getY() == minVal ? minVal : 0, getZ() == minVal ? minVal : 0);
    }

    @Override
    public Vector3D abs() {
        return new Vector3D(
                Math.abs(getX()),
                Math.abs(getY()),
                Math.abs(getZ())
        );
    }

    public Color toColor() {
        return new Color(
                LightRay.clamp(getX(), 0, 1),
                LightRay.clamp(getY(), 0, 1),
                LightRay.clamp(getZ(), 0, 1),
                1);
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

    public static Vector3D random(double min, double max) {
        double scale = max - min;
        double x = ThreadLocalRandom.current().nextDouble() * scale + min;
        double y = ThreadLocalRandom.current().nextDouble() * scale + min;
        double z = ThreadLocalRandom.current().nextDouble() * scale + min;
        return new Vector3D(x, y, z);
    }

    public double distance(Vector3D vector) {
        return Math.abs(subtract(vector).magnitude());
    }


    /*
     * Utilities:
     */

    @Override
    public  Vector3D newVector(final double... components) {
        return new Vector3D(components);
    }

    public static Vector3D empty() {
        double[] newComponents = new double[SIZE];
        return new Vector3D(newComponents);
    }

    public Vector3D newEmpty() {
        return empty();
    }
    @Override
    protected  int size() {
        return SIZE;
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
}
