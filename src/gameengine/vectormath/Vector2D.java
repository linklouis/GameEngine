package gameengine.vectormath;

import org.jocl.struct.Struct;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class Vector2D implements Vector<Vector2D> {
    /*
     * Unit Vectors:
     */
    public static final Vector2D I = new Vector2D(1, 0);
    public static final Vector2D J = new Vector2D(0, 1);

    private double x;
    private double y;


    /*
     * Construction:
     */

    public Vector2D(double value) {
        this.x = value;
        this.y = value;
    }

    public Vector2D(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(final double[] components) {
        this.x = components[0];
        this.y = components[1];
    }

    public Vector2D(final Vector2D other) {
        this.x = other.getX();
        this.y = other.getY();
    }

    public Vector2D(final Point2D point) {
        this.x = point.getX();
        this.y = point.getY();
    }


    /*
     * Functionality:
     */

    public static class Vector2DStruct extends Struct {
        public float x, y;

        public Vector2DStruct(float x, float y){
            this.x = x;
            this.y = y;
        }

        public Vector2DStruct(double x, double y){
            this.x = (float) x;
            this.y = (float) y;
        }

        public float dotProduct(final Vector2D.Vector2DStruct other) {
            return    x * other.x
                    + y * other.y;
        }
    }

    public Vector2DStruct toStruct() {
        return new Vector2D.Vector2DStruct(x, y);
    }

    @Override
    public Vector2D add(Vector2D other) {
        return new Vector2D(
                getX() + other.getX(),
                getY() + other.getY()
        );
    }

    public Vector2D add(Point2D other) {
        return new Vector2D(
                getX() + other.getX(),
                getY() + other.getY()
        );
    }

    public Point2D toPoint() {
        return new Point2D.Double(getX(), getY());
    }

//    public Vector2D addMutable(Vector2D other) {
//        x += other.getX();
//        y += other.getY();
//        return this;
//    }

    @Override
    public Vector2D subtract(final Vector2D other) {
        return new Vector2D(
                getX() - other.getX(),
                getY() - other.getY()
        );
    }

    @Override
    public Vector2D scalarMultiply(final double scalar) {
        return new Vector2D(
                getX() * scalar,
                getY() * scalar
        );
    }

    @Override
    public Vector2D scalarMultiply(final BigDecimal scalar) {
        return new Vector2D(
                scalar.multiply(BigDecimal.valueOf(getX())).doubleValue(),
                scalar.multiply(BigDecimal.valueOf(getY())).doubleValue()
        );
    }

    @Override
    public Vector2D scalarDivide(final double scalar) {
        return new Vector2D(
                getX() / scalar,
                getY() / scalar
        );
    }

    @Override
    public Vector2D scalarDivide(final BigDecimal scalar) {
        return new Vector2D(
                BigDecimal.valueOf(x)
                        .divide(scalar, RoundingMode.HALF_DOWN).doubleValue(),
                BigDecimal.valueOf(y)
                        .divide(scalar, RoundingMode.HALF_DOWN).doubleValue()
    );
    }

    @Override
    public double dotProduct(final Vector2D other) {
        return getX() * other.getX() + getY() * other.getY();
    }

    @Override
    public double magnitude() {
        return Math.sqrt(getX() * getX() + getY() * getY());
    }

    public double dotWithSelf() {
        return getX() * getX() + getY() * getY();
    }

    @Override
    public Vector2D unitVector() {
        double magnitude = magnitude();
        return new Vector2D(
                getX() / magnitude,
                getY() / magnitude
        );
    }

    @Override
    public Vector2D atMagnitude(double newMagnitude) {
        double magnitude = magnitude();
        return new Vector2D(
                getX() * newMagnitude / magnitude,
                getY() * newMagnitude / magnitude
        );
    }

    @Override
    public Vector2D forEach(Function<Double, Double> function) {
        return new Vector2D(
                function.apply(getX()),
                function.apply(getY())
        );
    }

    /**
     * @return a Vector2D with only the component with the largest value
     * preserved, and all others set to 0.
     */
    @Override
    public Vector2D max() {
        double maxVal = Math.max(getX(), getY());
        return new Vector2D(
                getX() == maxVal ? maxVal : 0,
                getY() == maxVal ? maxVal : 0);
    }

    /**
     * @return a Vector2D with only the component with the smallest value
     * preserved, and all others set to 0.
     */
    @Override
    public Vector2D min() {
        double minVal = Math.min(getX(), getY());
        return new Vector2D(
                getX() == minVal ? minVal : 0,
                getY() == minVal ? minVal : 0);
    }

    @Override
    public Vector2D abs() {
        return new Vector2D(
                Math.abs(getX()),
                Math.abs(getY())
        );
    }

    public static Vector2D random(double min, double max) {
        double x = ThreadLocalRandom.current().nextGaussian((min + max) / 2.0, (max - min) / 2.0);
        double y = ThreadLocalRandom.current().nextGaussian((min + max) / 2.0, (max - min) / 2.0);
        return new Vector2D(x, y);
    }

    public Vector2D signs() {
        return new Vector2D(
                getX() < 0 ? -1 : 1,
                getY() < 0 ? -1 : 1
        );
    }

    public double distance(Vector2D other) {
        return Math.abs(subtract(other).magnitude());
    }

    public static Vector2D displacement(Point2D location, Point2D location1) {
        return new Vector2D(location).subtract(new Vector2D(location1));
    }

    public Vector2D onlyX() {
        return new Vector2D(getX(), 0);
    }

    public Vector2D onlyY() {
        return new Vector2D(0, getY());
    }

    public Vector2D onlyZ() {
        return new Vector2D(0, 0);
    }


    /*
     * Utilities:
     */

    @Override
    public Vector2D newEmpty() {
        return new Vector2D(0);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    private void setX(double x) {
        this.x = x;
    }

    private void setY(double y) {
        this.y = y;
    }

    public Vector2D closestMatch(Vector2D... vectors) {
        if (vectors.length == 0) {
            return null;
        }
        if (vectors.length == 1) {
            return vectors[0];
        }

        Vector2D closest = vectors[0];
        double bestDifference = directionDifference(vectors[0]);

        for (Vector2D vector : vectors) {
            if (directionDifference(vector) < bestDifference) {
                closest = vector;
                bestDifference = directionDifference(closest);
            }
        }

        return closest;
    }
}
