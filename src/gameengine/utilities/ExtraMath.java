package gameengine.utilities;

import gameengine.threed.geometry.Ray;
import gameengine.vectormath.Vector3D;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public interface ExtraMath {
    static double min(double a, double b, double c) {
        return Math.min(Math.min(a, b), c);
    }

    static double max(double a, double b, double c) {
        return Math.max(Math.max(a, b), c);
    }

    static double average(double a, double b, double c) {
        return (a + b + c) / 3;
    }

    static double min(double a, double b, double c, double d) {
        return Math.min(Math.min(a, b), Math.min(c, d));
    }

    static double max(double a, double b, double c, double d) {
        return Math.max(Math.max(a, b), Math.max(c, d));
    }

    static double average(double a, double b, double c, double d) {
        return (a + b + c + d) / 4;
    }

    static double clamp(double num) {
        return Math.min(1, Math.max(0, num));
    }

    static double clamp(double num, double min, double max) {
        return Math.min(max, Math.max(min, num));
    }

    static <T> T selectRandom(Supplier<T> a, Supplier<T> b, double threshold) {
        threshold = clamp(threshold, 0, 1);
        return ThreadLocalRandom.current().nextDouble() > threshold ?
                b.get()
                :
                a.get();
    }

    static double distToCollideSphere(Ray lightRay, Vector3D center, double radiusSquared) {
        double b = -lightRay.getDirection().dotWithSubtracted(lightRay.getPosition(), center);
        return b - Math.sqrt(b * b + radiusSquared - lightRay.getPosition().distanceSquared(center));
    }
}
