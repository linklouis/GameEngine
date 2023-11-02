package gameengine.threed.graphics.raytraceing.textures;

import gameengine.threed.graphics.raytraceing.Ray;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;

public class SubsurfaceTexture extends RayTracingTexture {

    private final double minimumReflectionAngle;
    private final double randomness;

    public SubsurfaceTexture(final Color color, final boolean isLightSource, double minimumReflectionAngle, double randomness) {
        super(color, isLightSource);
        this.minimumReflectionAngle = minimumReflectionAngle;
        this.randomness = randomness;
    }

    @Override
    public Ray reflection(final Ray ray, Vector3D surfaceNormal) {
        if (ray.getDirection().angleInDegreesWith(surfaceNormal.scalarMultiply(-1)) / 90 > 1
            || ray.getDirection().angleInDegreesWith(surfaceNormal.scalarMultiply(-1)) / 90 < 0) {
            surfaceNormal = surfaceNormal.scalarMultiply(-1);
//            System.out.println("IT HURTSSSS");
        }
//        if (rayDirection.angleInDegreesWith(surfaceNormal.scalarMultiply(-1)) / 180 < 0) {
//            System.out.println("HELP");
//        }
        if (
                generateSkewedValue(
                        ray.getDirection().angleInDegreesWith(surfaceNormal.scalarMultiply(-1)) / 90,
                        0, 90)
//                ThreadLocalRandom.current()
//                .nextGaussian(
//                        rayDirection.angleInDegreesWith(
//                                surfaceNormal.scalarMultiply(-1)),
//                        90)
                >= minimumReflectionAngle) {
            return new Ray(
                    ray.getPosition(),
                    reflectRay(ray.getDirection(), surfaceNormal),
                    ray.getColor().add(colorVector())
            );
        }
        Vector3D reflection = Vector3D.random(surfaceNormal, 1 - minimumReflectionAngle / 90 + 0.1);
        while (reflection.angleInDegreesWith(surfaceNormal) > minimumReflectionAngle) {
            reflection = Vector3D.random(surfaceNormal, 1 - minimumReflectionAngle / 90 + 0.1);
        }
        return new Ray(
                ray.getPosition(),
                reflection,
                ray.getColor().add(colorVector()));
    }

//@Override
//public Vector3D reflection(final Vector3D rayDirection, final Vector3D surfaceNormal) {
//    double cosTheta = Math.abs(rayDirection.dotProduct(surfaceNormal));
//
//    if (ThreadLocalRandom.current().nextDouble() < cosTheta) {
//        // Scatter the ray
//        return Vector3D.random(-1 ,1);
//    } else {
//        // Reflect the ray
//        return reflectRay(rayDirection, surfaceNormal);
//    }
//}

    public static double generateSkewedValue(double variable, double min, double max) {
        if (variable < 0.0 || variable > 1.0) {
            throw new IllegalArgumentException("Variable must be between 0 and 1.");
        }

        double range = max - min;
        double skewness = 2.0 * variable - 1.0; // Transform variable to skewness (-1 to 1)

        // Calculate the mode (most common value)
        double mode = min + range * 0.5 * (1.0 + skewness);

        double u = ThreadLocalRandom.current().nextDouble(); // Uniform random number between 0 and 1

        // Use the CDF of the skewed distribution to map u to the skewed value
        double skewedValue;
        if (u < variable) {
            skewedValue = min + Math.sqrt(u * range * (mode - min));
        } else {
            skewedValue = max - Math.sqrt((1.0 - u) * range * (max - mode));
        }

        return skewedValue;
    }
}
