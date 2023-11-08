package gameengine.utilities;

public interface ExtraMath {
    static double min(double a, double b, double c) {
        return Math.min(Math.min(a, b), c);
    }

    static double max(double a, double b, double c) {
        return Math.max(Math.max(a, b), c);
    }

    static double clamp(double num, double min, double max) {
        return Math.min(max, Math.max(min, num));
    }
}
