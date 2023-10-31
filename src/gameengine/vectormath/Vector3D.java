package gameengine.vectormath;

import javafx.scene.paint.Color;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class Vector3D implements Vector<Vector3D> {
    /*
     * Unit Vectors:
     */
    private static final Vector3D i = new Vector3D(1, 0, 0);
    private static final Vector3D j = new Vector3D(0, 1, 0);
    private static final Vector3D k = new Vector3D(0, 0, 1);

    /*
     * Components:
     */
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
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }


    /*
     * Functionality:
     */

    @Override
    public Vector3D add(Vector3D other) {
        return new Vector3D(
                x + other.x,
                y + other.y,
                z + other.z
        );
    }

    public Vector3D addMutable(Vector3D other) {
        x += other.x;
        y += other.y;
        z += other.z;
        return this;
    }

    @Override
    public Vector3D subtract(final Vector3D other) {
        return new Vector3D(
                x - other.x,
                y - other.y,
                z - other.z
        );
    }

    public double dotWithSubtracted(final Vector3D v1, final Vector3D v2) {
        return x * (v1.x - v2.x) + y * (v1.y - v2.y) + z * (v1.z - v2.z);
    }

    public double dotWithUnit(final Vector3D other) {
        double magnitude = other.magnitude();
        return x * (other.x / other.magnitude())
                + y * (other.y / other.magnitude())
                + z * (other.z / other.magnitude());
    }

    public Vector3D addAtMagnitude(final Vector3D other, final double newMagnitude) {
        double magnitude = other.magnitude();
        return new Vector3D(
                x + other.x * newMagnitude / magnitude,
                y + other.y * newMagnitude / magnitude,
                z + other.z * newMagnitude / magnitude);
    }

//    public double dotWithAddedAndSubtracted(final Vector3D v1, final Vector3D v2, double dist, final Vector3D v3) {
//        return x * (v1.x + v2.x * dist - v3.x) + y * (v1.y + v2.y * dist - v3.y) + z * (v1.z + v2.z * dist - v3.z);
//    }

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
        return x * other.x + y * other.y + z * other.z;
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
        return new Vector3D(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
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
               x * vector.x,
               y * vector.y,
               z * vector.z
        );
    }

    public Vector3D divideAcross(Vector3D vector) {
        return new Vector3D(
                x / vector.x,
                y / vector.y,
                z / vector.z
        );
    }

    public double distance(Vector3D other) {
        return Math.sqrt(
                (x - other.x) * (x - other.x)
                + (y - other.y) * (y - other.y)
                + (z - other.z) * (z - other.z));
//                Math.pow(x - other.x, 2)
//                + Math.pow(y - other.y, 2)
//                + Math.pow(z - other.z, 2));
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

    @Override
    public Vector3D newEmpty() {
        return new Vector3D(0);
    }

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
