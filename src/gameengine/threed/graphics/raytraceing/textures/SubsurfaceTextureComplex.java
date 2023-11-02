package gameengine.threed.graphics.raytraceing.textures;

import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

import java.util.Random;

public class SubsurfaceTextureComplex extends RayTracingTexture {
    private final double scatterCoefficient; // Adjust the scattering strength
    private final double absorptionCoefficient; // Adjust the absorption strength
    private final double scatteringAnisotropy; // Controls the direction of scattering
    private final Color originalColor;

    public SubsurfaceTextureComplex(final Color color, final boolean isLightSource,
                             double scatterCoefficient, double absorptionCoefficient, double scatteringAnisotropy) {
        super(color, isLightSource);
        this.scatterCoefficient = scatterCoefficient;
        this.absorptionCoefficient = absorptionCoefficient;
        this.scatteringAnisotropy = scatteringAnisotropy;
        originalColor = getColor();
    }

    public Vector3D reflection(final Vector3D rayDirection, final Vector3D surfaceNormal) {
        // Perform random walk for subsurface scattering
        return randomWalk(rayDirection, surfaceNormal, scatterCoefficient, absorptionCoefficient, scatteringAnisotropy);
    }

    // Simulate subsurface scattering using random walk
    private Vector3D randomWalk(Vector3D rayDirection, Vector3D surfaceNormal,
                                double scatterCoefficient, double absorptionCoefficient, double scatteringAnisotropy) {
        setColor(originalColor);
        Random random = new Random();

        // Iterate for a certain number of steps (you can adjust this)
        int numSteps = 100;
        Vector3D newDirection = rayDirection;

        for (int step = 0; step < numSteps; step++) {
            // Generate a random step direction
            double theta = random.nextDouble() * 2 * Math.PI;
            double phi = random.nextDouble() * Math.PI;

            double x = Math.sin(phi) * Math.cos(theta);
            double y = Math.sin(phi) * Math.sin(theta);
            double z = Math.cos(phi);

            Vector3D stepDirection = new Vector3D(x, y, z);

            // Calculate the probability of scattering in the new direction
            double scatteringProbability = calculateScatteringProbability(scatterCoefficient, absorptionCoefficient, scatteringAnisotropy, surfaceNormal, newDirection, stepDirection);

            // Randomly decide if scattering occurs
            if (random.nextDouble() < scatteringProbability) {
                newDirection = stepDirection;
            }

            // Absorption in the material
//            Color absorbedColor = new Vector3D(getColor()).scalarMultiply(Math.exp(-absorptionCoefficient)).toColor();
//            setColor(absorbedColor);;

            // You may want to accumulate the color during each step to simulate absorption
            // color = color.multiply(Math.exp(-absorptionCoefficient));

            // You can also implement boundary checks to handle exiting the material
            // if (outsideBoundary) break;
        }

        return newDirection;
    }

    // Calculate the probability of scattering in the new direction
    private double calculateScatteringProbability(double scatterCoefficient, double absorptionCoefficient, double scatteringAnisotropy,
                                                  Vector3D surfaceNormal, Vector3D incidentDirection, Vector3D scatteringDirection) {
        // Calculate the cosine of the angle between the incident and scattering directions
        double cosTheta = incidentDirection.dotProduct(scatteringDirection);

        // Calculate the Henyey-Greenstein phase function based on the scattering anisotropy
        double g = scatteringAnisotropy;
        double denominator = 1 + g * g - 2 * g * cosTheta;
        double phaseFunction = 1 / (4 * Math.PI) * (1 - g * g) / Math.pow(denominator, 1.5);

        // Calculate the scattering probability
        double scatteringProbability = (1 - Math.exp(-scatterCoefficient)) * phaseFunction;

        return scatteringProbability;
    }

//    private double calculateScatteringProbability(double scatterCoefficient, double absorptionCoefficient, double scatteringAnisotropy,
//                                                  Vector3D surfaceNormal, Vector3D incidentDirection, Vector3D scatteringDirection) {
//        // Implement the scattering probability calculation here
//        // This can be a complex function depending on the material properties and scattering model
//        // A common model is the Henyey-Greenstein phase function
//        // You may need to integrate over the phase function to get the probability
//
//        // For a simple example, you can use a constant scattering probability:
//        return scatterCoefficient;
//    }
}
