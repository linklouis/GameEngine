package gameengine.vectormath;

import javafx.scene.paint.Color;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class Vector3D implements Vector<Vector3D> {
    private static final int SIZE = 3;
    private double x;
    private double y;
    private double z;


    /*
     * Construction:
     */

    public Vector3D(double value) {
        this.x = value;
        this.y = value;
        this.z = value;
    }

    public Vector3D(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D(final double[] components) {
        this.x = components[0];
        this.y = components[1];
        this.z = components[2];
    }

    public Vector3D(final Color color) {
        this.x = color.getRed();
        this.y = color.getGreen();
        this.z = color.getBlue();
    }

    public Vector3D(final Vector3D vector) {
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
    }


    /*
     * Functionality:
     */

//    public interface VectorOperation {
//        void doOperation(Vector3D vector1, Vector3D vector2);
//    }
//
//    public static VectorOperation add =
//            (Vector3D vector1, Vector3D vector2) -> {
//        vector1.setX(vector1.getX() + vector2.getX());
//        vector1.setY(vector1.getY() + vector2.getY());
//        vector1.setZ(vector1.getZ() + vector2.getZ());
//    };
//
//
//
//    public Vector3D doWhile(Vector3D vector1, Vector3D vector2,
//                            VectorOperation operation,
//                            Supplier<Boolean> check) {
//        Vector3D vectorA = new Vector3D(vector1);
//        while (check.get()) {
//            operation.doOperation(vector1, vector2);
//        }
//        return vectorA;
//    }

    @Override
    public Vector3D add(Vector3D other) {
        return new Vector3D(
                x + other.getX(),
                y + other.getY(),
                z + other.getZ()
        );
    }

    public Vector3D addMutable(Vector3D other) {
        x += other.getX();
        y += other.getY();
        z += other.getZ();
        return this;
    }

    @Override
    public Vector3D subtract(final Vector3D other) {
        return new Vector3D(
                x - other.getX(),
                y - other.getY(),
                z - other.getZ()
        );
    }

    @Override
    public Vector3D scalarMultiply(final double scalar) {
        return new Vector3D(
                x * scalar,
                y * scalar,
                z * scalar
        );
    }

    @Override
    public Vector3D scalarMultiply(final BigDecimal scalar) {
        return new Vector3D(
                scalar.multiply(BigDecimal.valueOf(x)).doubleValue(),
                scalar.multiply(BigDecimal.valueOf(y)).doubleValue(),
                scalar.multiply(BigDecimal.valueOf(z)).doubleValue()
        );
    }

    @Override
    public Vector3D scalarDivide(final double scalar) {
        return new Vector3D(
                x / scalar,
                y / scalar,
                z / scalar
        );
    }

//    public void scalarDivideMutable(final double scalar) {
//        x /= scalar;
//        y /= scalar;
//        z /= scalar;
//    }

    @Override
    public Vector3D scalarDivide(final BigDecimal scalar) {
        return new Vector3D(
                BigDecimal.valueOf(x)
                        .divide(scalar, RoundingMode.HALF_DOWN).doubleValue(),
                BigDecimal.valueOf(y)
                        .divide(scalar, RoundingMode.HALF_DOWN).doubleValue(),
                BigDecimal.valueOf(z)
                        .divide(scalar, RoundingMode.HALF_DOWN).doubleValue()
        );
    }

    @Override
    public double dotProduct(final Vector3D other) {
        return x * other.getX() + y * other.getY() + z * other.getZ();
    }

    @Override
    public double magnitude() {
        return Math.sqrt(
                x * x
                + y * y
                + z * z);
    }

    public double dotWithSelf() {
        return x * x
                + y * y
                + z * z;
    }

    @Override
    public Vector3D unitVector() {
        double magnitude = magnitude();
        return new Vector3D(
                x / magnitude,
                y / magnitude,
                z / magnitude
        );
    }

    @Override
    public Vector3D atMagnitude(double newMagnitude) {
        double magnitude = magnitude();
        return new Vector3D(
                x * newMagnitude / magnitude,
                y * newMagnitude / magnitude,
                z * newMagnitude / magnitude
        );
    }

    @Override
    public Vector3D forEach(Function<Double, Double> function) {
        return new Vector3D(
                function.apply(x),
                function.apply(y),
                function.apply(z)
        );
    }

    /**
     * @return a Vector3D with only the component with the largest value
     * preserved, and all others set to 0.
     */
    @Override
    public Vector3D max() {
        double maxVal = Math.max(Math.max(x, y), z);
        return new Vector3D(
                x == maxVal ? maxVal : 0,
                y == maxVal ? maxVal : 0,
                z == maxVal ? maxVal : 0);
    }

    /**
     * @return a Vector3D with only the component with the smallest value
     * preserved, and all others set to 0.
     */
    @Override
    public Vector3D min() {
        double minVal = Math.min(Math.min(x, y), z);
        return new Vector3D(
                x == minVal ? minVal : 0,
                y == minVal ? minVal : 0,
                z == minVal ? minVal : 0);
    }

    @Override
    public Vector3D abs() {
        return new Vector3D(
                Math.abs(x),
                Math.abs(y),
                Math.abs(z)
        );
    }

    public Color toColor() {
        return new Color(
                Vector.clamp(x, 0, 1),
                Vector.clamp(y, 0, 1),
                Vector.clamp(z, 0, 1),
                1);
    }

    public Vector3D crossProduct(final Vector3D other) {
        return newVector(
                this.getY() * other.getZ() - this.getZ() * other.getY(),
                this.getZ() * other.getX() - this.getX() * other.getZ(),
                this.getX() * other.getY() - this.getY() * other.getX()
        );
    }

    public static Vector3D random(double min, double max) {
        double x = ThreadLocalRandom.current().nextGaussian((min + max) / 2.0, (max - min) / 2.0);
        double y = ThreadLocalRandom.current().nextGaussian((min + max) / 2.0, (max - min) / 2.0);
        double z = ThreadLocalRandom.current().nextGaussian((min + max) / 2.0, (max - min) / 2.0);
//        double x = ThreadLocalRandom.current().nextDouble(min, max);
//        double y = ThreadLocalRandom.current().nextDouble(min, max);
//        double z = ThreadLocalRandom.current().nextDouble(min, max);
        return new Vector3D(x, y, z);
    }

    public Vector3D signs() {
        return new Vector3D(
                x < 0 ? -1 : 1,
                y < 0 ? -1 : 1,
                z < 0 ? -1 : 1
        );
    }

    public Vector3D multiplyAcross(Vector3D vector) {
        return new Vector3D(
               x * vector.getX(),
               y * vector.getY(),
               z * vector.getZ()
        );
    }

    public Vector3D divideAcross(Vector3D vector) {
        return new Vector3D(
                x / vector.getX(),
                y / vector.getY(),
                z / vector.getZ()
        );
    }

    public double distance(Vector3D other) {
        return Math.sqrt(
                (x - other.getX()) * (x - other.getX())
                + (y - other.getY()) * (y - other.getY())
                + (z - other.getZ()) * (z - other.getZ()));
//                Math.pow(x - other.getX(), 2)
//                + Math.pow(y - other.getY(), 2)
//                + Math.pow(z - other.getZ(), 2));
    }

    public Vector3D onlyX() {
        return new Vector3D(x, 0, 0);
    }

    public Vector3D onlyY() {
        return new Vector3D(0, y, 0);
    }

    public Vector3D onlyZ() {
        return new Vector3D(0, 0, z);
    }

    /*
     * Utilities:
     */

//    @Override
    public  Vector3D newVector(final double... components) {
        return new Vector3D(components);
    }

//    @Override
//    public double[] getComponents() {
//        return new double[] { x, y, z };
//    }

//    @Override
//    public double getComponent(int index) {
//        return getComponents()[index];
//    }

//    @Override
    protected void setComponent(int component, double newValue) {
        switch (component) {
            case 0:
                x = newValue;
                return;
            case 1:
                y = newValue;
                return;
            case 2:
                z = newValue;
        }
    }

    public Vector3D newEmpty() {
        return new Vector3D(0);
    }
//    @Override
//    protected int size() {
//        return SIZE;
//    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    private void setX(double x) {
        this.x = x;
    }

    private void setY(double y) {
        this.y = y;
    }

    private void setZ(double z) {
        this.z = z;
    }
}
