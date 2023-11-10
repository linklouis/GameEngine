package gameengine.vectormath;

import gameengine.utilities.ExtraMath;
import gameengine.utilities.Random;
import javafx.scene.paint.Color;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import static gameengine.utilities.ExtraMath.clamp;

public class Vector3D implements Vector<Vector3D>, Comparable<Vector3D> {
    /*
     * Unit Vectors:
     */
    public static final Vector3D I = new Vector3D(1, 0, 0);
    public static final Vector3D J = new Vector3D(0, 1, 0);
    public static final Vector3D K = new Vector3D(0, 0, 1);

    /*
     * Components:
     */
    private double x;
    private double y;
    private double z;
    private double magnitude = Double.NaN;


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
        this.magnitude = vector.magnitude();
    }

    public Vector3D() {
        x = 0;
        y = 0;
        z = 0;
        magnitude = 0;
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
        magnitude = Double.NaN;
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

    public Vector3D reflectFromNormal(Vector3D normal) {
        double dotProdx2 = 2 * (x * normal.x + y * normal.y + z * normal.z);
        return new Vector3D(
                x - normal.x * dotProdx2,
                y - normal.y * dotProdx2,
                z - normal.z * dotProdx2
        );
    }

    public double dotWithSubtracted(final Vector3D v1, final Vector3D v2) {
        return    x * (v1.x - v2.x)
                + y * (v1.y - v2.y)
                + z * (v1.z - v2.z);
    }

    public double distToCollidePlane(final Vector3D vertex, final Vector3D position, final Vector3D direction) {
//        normal.dotWithSubtracted(vertex1, ray.getPosition())
//                / normal.dotWithUnitOf(ray.getDirection())
        return (  x * (vertex.x - position.x)
                + y * (vertex.y - position.y)
                + z * (vertex.z - position.z))
                /
                (  x * direction.x
                 + y * direction.y
                 + z * direction.z);
    }

    public Vector3D addAtMagnitude(final Vector3D other, final double newMagnitude) {
        return new Vector3D(
                x + other.x * newMagnitude / other.magnitude(),
                y + other.y * newMagnitude / other.magnitude(),
                z + other.z * newMagnitude / other.magnitude());
    }

    public Vector3D addMultiplied(final Vector3D other, final double newMagnitude) {
        return new Vector3D(
                x + other.x * newMagnitude,
                y + other.y * newMagnitude,
                z + other.z * newMagnitude);
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

    public Vector3D scalarDivideMutable(final double scalar) {
        x /= scalar;
        y /= scalar;
        z /= scalar;
        return this;
    }

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

    public double dotSelf() {
        return x * x + y * y + z * z;
    }

    @Override
    public double magnitude() {
        if (Double.isNaN(magnitude)) {
            magnitude = Math.sqrt(
                    x * x
                            + y * y
                            + z * z);
        }
        return magnitude;
    }

    public double magnitudeSquared() {
        return x * x
                + y * y
                + z * z;
    }

    public void computeMagnitude() {
        if (Double.isNaN(magnitude)) {
            magnitude = Math.sqrt(
                    x * x
                            + y * y
                            + z * z);
        }
    }

    @Override
    public Vector3D unitVector() {
        return new Vector3D(
                x / magnitude(),
                y / magnitude(),
                z / magnitude()
        );
    }

    public Vector3D unitVectorMutable() {
        x /= magnitude();
        y /= magnitude();
        z /= magnitude();
        return this;
    }

    @Override
    public Vector3D atMagnitude(double newMagnitude) {
        return new Vector3D(
                x * newMagnitude / magnitude(),
                y * newMagnitude / magnitude(),
                z * newMagnitude / magnitude()
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
                clamp(x, 0, 1),
                clamp(y, 0, 1),
                clamp(z, 0, 1),
                1);
    }

    public Vector2D to2D() {
        return new Vector2D(x, y);
    }

    public Vector3D crossProduct(final Vector3D other) {
        return new Vector3D(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x);
    }

    public Vector3D normal(final Vector3D v1, Vector3D v2) {
        return new Vector3D(
                (y - v1.y) * (z - v2.z) - (z - v1.z) * (y - v2.y),
                (z - v1.z) * (x - v2.x) - (x - v1.x) * (z - v2.z),
                (x - v1.x) * (y - v2.y) - (y - v1.y) * (x - v2.x));
    }

    public Vector3D crossWithJ(final double multiplier) {
        return new Vector3D(
                -z * multiplier,
                0,
                x * multiplier);
    }

    public Vector3D crossWithSelfCrossedWithJ(final double multiplier) {
        return new Vector3D(
                y * x * multiplier,
                -(z * z + x * x) * multiplier,
                y * z * multiplier);
    }

    public Vector3D transformToNewCoordinates(final double scaleX, final double scaleY) {
        return new Vector3D(
                x * (1 + y * scaleX) - z * scaleY,
                y - (z * z + x * x) * scaleX,
                z * (1 + y * scaleX) + x * scaleY
        );
    }

    /**
     * Project this 3D vector onto a 2D plane defined by its orthogonal unit
     * vectors, planeX and planeY.
     *
     * @param planeX The unit vector representing one axis of the plane.
     * @param planeY The unit vector representing another axis of the plane
     *               (orthogonal to planeX).
     * @return A 2D vector representing the projection of this vector onto the
     * specified plane.
     */
    public Vector2D projectToPlane(Vector3D planeX, Vector3D planeY) {
//        return new Vector2D(
//                dotProduct(planeX),
//                dotProduct(planeY)
//        );
        return new Vector2D(
                x * planeX.x + y * planeX.y + z * planeX.z,
                x * planeY.x + y * planeY.y + z * planeY.z
        );
    }

    public Vector2D projectToPlane(Vector3D planeX, Vector3D planeY, Vector3D direction, double distance) {
        return new Vector2D(
                (x + direction.x * distance) * planeX.x
                        + (y + direction.y * distance) * planeX.y
                        + (z + direction.z * distance) * planeX.z,
                (x + direction.x * distance) * planeY.x
                        + (y + direction.y * distance) * planeY.y
                        + (z + direction.z * distance) * planeY.z
        );
    }

    public static Vector3D random() {
        return new Vector3D(
                ThreadLocalRandom.current().nextGaussian(0, 1),
                ThreadLocalRandom.current().nextGaussian(0, 1),
                ThreadLocalRandom.current().nextGaussian(0, 1)
        );
    }

    public static Vector3D random(double min, double max) {
        return randomFromStdev((min + max) / 2.0, (max - min) / 2.0);
    }

    public static Vector3D randomFromStdev(double mean, double stdev) {
        return new Vector3D(
                ThreadLocalRandom.current().nextGaussian(mean, stdev),
                ThreadLocalRandom.current().nextGaussian(mean, stdev),
                ThreadLocalRandom.current().nextGaussian(mean, stdev)
        );
    }

    public static Vector3D random(Vector3D model, double range) {
        return new Vector3D(
                ThreadLocalRandom.current().nextGaussian(model.x, range),
                ThreadLocalRandom.current().nextGaussian(model.y, range),
                ThreadLocalRandom.current().nextGaussian(model.z, range)
        );
    }

    public Vector3D signs() {
        return new Vector3D(
                x < 0 ? -1 : 1,
                y < 0 ? -1 : 1,
                z < 0 ? -1 : 1
        );
    }

    public byte[] bytes() {
        return new byte[] { (byte) (x * 255), (byte) (y * 255), (byte) (z * 255) };
    }

    public static byte[] bytes(Color color) {
        return new byte[] { (byte) (color.getRed() * 255), (byte) (color.getGreen() * 255), (byte) (color.getBlue() * 255) };
    }

    public int oneInt() {
        return (255 << 24) | ((int) clamp(x * 255, 0, 255) << 16) | ((int) clamp(y * 255, 0, 255) << 8) | (int) clamp(z * 255, 0, 255);
    }

    public static int oneInt(Color color) {
        return ((255 << 24) | ((int) (color.getRed() * 255) << 16) | ((int) (color.getGreen() * 255) << 8) | (int) (color.getBlue() * 255));
    }


    public static Vector3D average(Vector3D a, Vector3D b, Vector3D c) {
        return new Vector3D(
                ExtraMath.average(a.getX(), b.getX(), c.getX()),
                ExtraMath.average(a.getY(), b.getY(), c.getY()),
                ExtraMath.average(a.getZ(), b.getZ(), c.getZ())
        );
    }

    public static Vector3D average(Vector3D a, Vector3D b, Vector3D c, Vector3D d) {
        return new Vector3D(
                ExtraMath.average(a.getX(), b.getX(), c.getX(), d.getX()),
                ExtraMath.average(a.getY(), b.getY(), c.getY(), d.getY()),
                ExtraMath.average(a.getZ(), b.getZ(), c.getZ(), d.getZ())
        );
    }

    public double distanceSquared(final Vector3D other) {
        return (x - other.x) * (x - other.x)
                + (y - other.y) * (y - other.y)
                + (z - other.z) * (z - other.z);
    }

    public double distance(Vector3D other) {
        return Math.sqrt(distanceSquared(other));
    }

    public Vector3D multiplyAcross(Vector3D other) {
        return new Vector3D(
                x * other.x,
                y * other.y,
                z * other.z
        );
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

    public String toString() {
        return "Vector3D(" + x + ", " + y + ", " + z + ")";
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure {@link Integer#signum
     * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
     * all {@code x} and {@code y}.  (This implies that {@code
     * x.compareTo(y)} must throw an exception if and only if {@code
     * y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code
     * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
     * == signum(y.compareTo(z))}, for all {@code z}.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     * @apiNote It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     */
    @Override
    public int compareTo(Vector3D o) {
        if (Math.abs(x - o.x) < 0.0001 && Math.abs(y - o.y) < 0.0001 && Math.abs(z - o.z) < 0.0001) {
            return 0;
        }
        if (magnitude() > o.magnitude()) {
            return 1;
        }
        return -1;
    }

    //    public static class FastGaussian {
//        private long seed;
//
//        public FastGaussian(long seed) {
//            this.seed = seed;
//        }
//
//        public FastGaussian(Vector3D seed) {
//            setSeed(seed);
//        }
//
//        public double nextGaussian() {
//            Math.random();
//            seed ^= (seed << 21);
//            seed ^= (seed >>> 35);
//            seed ^= (seed << 4);
//            return (seed >> 12) * 2.3283064365386963e-16;
//        }
//
//        public void setSeed(long seed) {
//            this.seed = seed;
//        }
//
//        public void setSeed(Vector3D seed) {
//            this.seed = (long) ((seed.getX() + seed.getY()) * seed.getZ());
//        }
//    }
public static class FastGaussian {
    private double mean;
    private double stdDev;
    private long seed;
    private boolean hasValue = false;
    private double storedValue;

    public FastGaussian(long seed, double mean, double stdDev) {
        this.seed = seed;
        this.mean = mean;
        this.stdDev = stdDev;
    }

    public FastGaussian(Vector3D seed, double mean, double stdDev) {
        this.seed = (long) ((seed.getX() + seed.getY()) * seed.getZ());
        this.mean = mean;
        this.stdDev = stdDev;
    }

    public double nextGaussian() {
        if (hasValue) {
            hasValue = false;
            return storedValue;
        } else {
            double u, v, s;
            do {
                u = customRandom() * 2.0 - 1.0;
                v = customRandom() * 2.0 - 1.0;
                s = u * u + v * v;
            } while (s >= 1.0 || s == 0.0);

            double multiplier = Math.sqrt(-2.0 * Math.log(s) / s);
            storedValue = mean + stdDev * u * multiplier;
            hasValue = true;
            return mean + stdDev * v * multiplier;
        }
    }

    private double customRandom() {
        seed ^= (seed << 21);
        seed ^= (seed >>> 35);
        seed ^= (seed << 4);
        return (seed >> 12) * 2.3283064365386963e-16;
    }
}
}
