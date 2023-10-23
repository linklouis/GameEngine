package gameengine.vectormath;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;

public abstract class Vector<T extends Vector<T>> {
    protected Vector() {
    }

    public Vector(double value) {
        for (int index = 0; index < size(); index++) {
            setComponent(index, value);
        }
    }

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
        for (int index = 0; index < size(); index++) {
            setComponent(index, comps[index]);
        }
    }


    /*
     * Functionality:
     */

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
        return forEach(component -> component * scalar);
    }

    public T scalarMultiply(final BigDecimal scalar) {
        return forEach(component -> scalar.multiply(BigDecimal.valueOf(component)).doubleValue());
    }


    public T scalarDivide(final double scalar) {
        return forEach(component -> component / scalar);
    }

    public T scalarDivide(final BigDecimal scalar) {
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
        double magnitude = magnitude();
        return forEach(component -> component / magnitude);
    }

    public T atMagnitude(double newMagnitude) {
        return unitVector().scalarMultiply(newMagnitude);
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


    /*
     * Utilities:
     */

    public abstract double[] getComponents();

    public abstract double getComponent(final int index);

    protected abstract void setComponent(final int component, double newValue);

    @Override
    public String toString() {
        String output = "(";
        for (double v : getComponents()) {
            output += v + ", ";
        }
        output += ")";
        return output;
    }

    public abstract T newVector(double... comps);
    protected abstract int size();

    public abstract T newEmpty();
}
