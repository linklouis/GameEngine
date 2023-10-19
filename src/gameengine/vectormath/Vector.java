package gameengine.vectormath;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public abstract class Vector<T extends Vector<T>> {
    /**
     * The components of the vector. Ensured to be
     * determined length by {@link Vector} implementations.
     */
    private final double[] components;

    /**
     * @param comps double array of values to set as the components.
     *                   Must be checked to ensure correct length
     *                   in implementations.
     */
    public Vector(final double... comps) {
        if (comps.length != size()) {
            throw new IllegalArgumentException("A " + size()
                    + "D vector must have exactly " + size()
                    + " components. Given " + comps.length);
        }
        components = Arrays.copyOf(comps, comps.length);
    }

    public T add(final T other) {
        double[] resultComponents = new double[size()];
        for (int i = 0; i < size(); i++) {
            resultComponents[i] = this.getComponent(i) + other.getComponent(i);
        }
        return newVector(resultComponents);
    }

    public T subtract(final T other) {
        double[] resultComponents = new double[size()];
        for (int i = 0; i < size(); i++) {
            resultComponents[i] = this.getComponent(i) - other.getComponent(i);
        }
        return newVector(resultComponents);
    }

    public T scalarMultiply(final double scalar) {
//        double[] resultComponents = new double[size()];
//        for (int i = 0; i < size(); i++) {
//            resultComponents[i] = this.getComponent(i) * scalar;
//        }
//        return newVector(resultComponents);
        return forEach(component -> component * scalar);
    }

    public T scalarMultiply(final BigDecimal scalar) {
//        double[] resultComponents = new double[size()];
//        for (int i = 0; i < size(); i++) {
//            resultComponents[i] = scalar.multiply(BigDecimal.valueOf(this.getComponent(i))).doubleValue();
//        }
//        return newVector(resultComponents);
        return forEach(component -> scalar.multiply(BigDecimal.valueOf(component)).doubleValue());
    }


    public T scalarDivide(final double scalar) {
//        double[] resultComponents = new double[size()];
//        for (int i = 0; i < size(); i++) {
//            resultComponents[i] = this.getComponent(i) / scalar;
//        }
//        return newVector(resultComponents);
        return forEach(component -> component / scalar);
    }

    public T scalarDivide(final BigDecimal scalar) {
//        double[] resultComponents = new double[size()];
//        for (int i = 0; i < size(); i++) {
//            resultComponents[i] = BigDecimal.valueOf(this.getComponent(i)).divide(scalar, RoundingMode.HALF_DOWN).doubleValue();
//        }
//        return newVector(resultComponents);
        return forEach(component -> scalar.divide(BigDecimal.valueOf(component), RoundingMode.HALF_DOWN).doubleValue());
    }

    public double dotProduct(final T other) {
        double result = 0;
        for (int i = 0; i < size(); i++) {
            result += this.getComponent(i) * other.getComponent(i);
        }
        return result;
    }

    public double magnitude() {
        double sum = 0;
        for (double component : getComponents()) {
            sum += component * component;
        }
        return Math.sqrt(sum);
    }

    public T unitVector() {
//        double[] newComponents = new double[size()];
        double magnitude = magnitude();
//        for (int i = 0; i < size(); i++) {
//            newComponents[i] = getComponent(i) / magnitude;
//        }
//        return newVector(newComponents);
        return forEach(component -> component / magnitude);
    }

    public T forEach(Function<Double, Double> function) {
        double[] resultComponents = new double[size()];
        for (int i = 0; i < size(); i++) {
            resultComponents[i] = function.apply(this.getComponent(i));
        }
        return newVector(resultComponents);
    }

    public T max() {
        int maxIndex = 0;
        for (int i = 0; i < size(); i++) {
            if (getComponent(i) > getComponent(maxIndex)) {
                maxIndex = i;
            }
        }
        double[] resultComponents = new double[size()];
        for (int i = 0; i < size(); i++) {
            if (i == maxIndex) {
                resultComponents[i] = getComponent(i);
            } else {
                resultComponents[i] = 0;
            }
        }
        return newVector(resultComponents);
    }

    public T min() {
        int minIndex = 0;
        for (int i = 0; i < size(); i++) {
            if (getComponent(i) < getComponent(minIndex)) {
                minIndex = i;
            }
        }
        double[] resultComponents = new double[size()];
        for (int i = 0; i < size(); i++) {
            if (i == minIndex) {
                resultComponents[i] = getComponent(i);
            } else {
                resultComponents[i] = 0;
            }
        }
        return newVector(resultComponents);
    }

    public T abs() {
        return forEach(Math::abs);
    }

    public static <VectorType extends Vector<VectorType>> VectorType sum(VectorType empty, VectorType... vectors) {
        VectorType sum = empty.newEmpty();

        for (VectorType vector : vectors) {
            sum = sum.add(vector);
        }

        return sum;
    }

    public static <VectorType extends Vector<VectorType>> VectorType sum(VectorType empty, List<VectorType> vectors) {
        VectorType sum = empty.newEmpty();

        for (VectorType vector : vectors) {
            sum = sum.add(vector);
        }

        return sum;
    }

    public double directionDifference(T vector) {
        return this.dotProduct(vector) / magnitude();
    }

    public T closestMatch(T... vectors) {
        if (vectors.length == 0) {
            return null;
        }
        if (vectors.length == 1) {
            return vectors[0];
        }

        T closest = vectors[0];
        double bestDifference = directionDifference(vectors[0]);

        for (T vector : vectors) {
            if (directionDifference(vector) < bestDifference) {
                closest = vector;
                bestDifference = directionDifference(closest);
            }
        }

        return closest;
    }

//    public static <T extends Vector> T empty() {
//        double[] newComponents = new double[T.size()];
//        for (int i = 0; i < T.size(); i++) {
//            newComponents[i] = 0;
//        }
//        return T.newVector(newComponents);
//    }

//    public static <T extends Vector<T>> T empty(Class<T> vectorClass) {
//        try {
//            double[] newComponents = new double[T.size()];
//            return vectorClass.getConstructor(double[].class).newInstance((Object) newComponents);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to create an instance of Vector class", e);
//        }
//    }

    public double[] getComponents() {
        return components;
    }

    public double getComponent(final int index) {
        return getComponents()[index];
    }

    @Override
    public String toString() {
        String output = "(";
        for (double v : getComponents()) {
            output += v + ", ";
        }
        output += ")";
        return output;
    }

    public static Vector empty() {
        return null;
    }

    public abstract T newVector(double... comps);
//    {
//        try {
//            throw new ExecutionControl.NotImplementedException(
//                    "NewVector method is not implemented for the Vector implementation.");
//        } catch (ExecutionControl.NotImplementedException e) {
//            throw new UnsupportedOperationException(e);
//        }
//    }
    protected abstract int size();
//    {
//        try {
//            throw new ExecutionControl.NotImplementedException(
//                    "Size method is not implemented for the Vector implementation.");
//        } catch (ExecutionControl.NotImplementedException e) {
//            throw new UnsupportedOperationException(e);
//        }
//    }

    public abstract T newEmpty();
}
