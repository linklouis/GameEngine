package gameengine.vectormath;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

public interface Vector<T extends Vector<T>> {

    T add(final T other);

    T subtract(final T other);

    T scalarMultiply(final double scalar);

    T scalarMultiply(final BigDecimal scalar);

    T scalarDivide(final double scalar);

    T scalarDivide(final BigDecimal scalar);

    double dotProduct(final T other);

    double magnitude();

    T unitVector();

    T atMagnitude(double newMagnitude);

    T forEach(Function<Double, Double> function);

    T max();

    T min();

    T abs();

    default double directionDifference(T vector) {
        return this.dotProduct(vector) / magnitude();
    }

    public abstract T newEmpty();


    /*
     * Statics:
     */

    static double clamp(double num, double min, double max) {
        return Math.min(max, Math.max(min, num));
    }

    static <VectorType extends Vector<VectorType>> VectorType sum(VectorType empty, VectorType... vectors) {
        VectorType sum = empty.newEmpty();

        for (VectorType vector : vectors) {
            sum = sum.add(vector);
        }

        return sum;
    }

    static <VectorType extends Vector<VectorType>> VectorType sum(VectorType empty, List<VectorType> vectors) {
        VectorType sum = empty.newEmpty();

        for (VectorType vector : vectors) {
            sum = sum.add(vector);
        }

        return sum;
    }
}
