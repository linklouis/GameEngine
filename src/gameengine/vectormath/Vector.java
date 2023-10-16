package gameengine.vectormath;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
            resultComponents[i] = this.getComponents()[i] + other.getComponents()[i];
        }
        return newVector(resultComponents);
    }

    public T subtract(final T other) {
        double[] resultComponents = new double[size()];
        for (int i = 0; i < size(); i++) {
            resultComponents[i] = this.getComponents()[i] - other.getComponents()[i];
        }
        return newVector(resultComponents);
    }

    public T scalarMultiply(final double scalar) {
        double[] resultComponents = new double[size()];
        for (int i = 0; i < size(); i++) {
            resultComponents[i] = this.getComponents()[i] * scalar;
        }
        return newVector(resultComponents);
    }

    public T scalarMultiply(final BigDecimal scalar) {
        double[] resultComponents = new double[size()];
        for (int i = 0; i < size(); i++) {
            resultComponents[i] = scalar.multiply(BigDecimal.valueOf(this.getComponents()[i])).doubleValue();
        }
        return newVector(resultComponents);
    }


    public T scalarDivide(final double scalar) {
        double[] resultComponents = new double[size()];
        for (int i = 0; i < size(); i++) {
            resultComponents[i] = this.getComponents()[i] / scalar;
        }
        return newVector(resultComponents);
    }

    public T scalarDivide(final BigDecimal scalar) {
        double[] resultComponents = new double[size()];
        for (int i = 0; i < size(); i++) {
            resultComponents[i] = BigDecimal.valueOf(this.getComponents()[i]).divide(scalar, RoundingMode.HALF_DOWN).doubleValue();
        }
        return newVector(resultComponents);
    }

    public double dotProduct(final T other) {
        double result = 0;
        for (int i = 0; i < size(); i++) {
            result += this.getComponents()[i] * other.getComponents()[i];
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
        double[] newComponents = new double[size()];
        double magnitude = magnitude();
        for (int i = 0; i < size(); i++) {
            newComponents[i] = getComponent(i) / magnitude;
        }
        return newVector(newComponents);
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

    protected double[] getComponents() {
        return components;
    }

    protected double getComponent(final int index) {
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
