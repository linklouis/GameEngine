package gameengine.threed.graphics.raytraceing.textures;

import gameengine.threed.graphics.raytraceing.LightRay;
import gameengine.threed.graphics.raytraceing.Reflection;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public abstract class RayTracingTexture {
    private Color color;
    private double emissionStrength;
    private Vector3D emissionColor;

    public RayTracingTexture(Color color, double emissionStrength, Vector3D emissionColor) {
        this.color = color;
        this.emissionStrength = emissionStrength;
        this.emissionColor = new Vector3D(emissionColor);
    }

    public RayTracingTexture(Color color, double emissionStrength, Color emissionColor) {
        this.color = color;
        this.emissionStrength = emissionStrength;
        this.emissionColor = new Vector3D(emissionColor);
    }

    public abstract Reflection reflection(final LightRay lightRayDirection,
                                          final Vector3D surfaceNormal);

    public Vector3D scatterRay(Vector3D surfaceNormal) {
//        Vector3D reflection = Vector3D.random(-1, 1);
//        if (reflection.dotProduct(surfaceNormal) < 0) {
//            return reflection.scalarMultiply(-1);
//        }
//        return reflection;
        return Vector3D.random(surfaceNormal, 1);
    }

    public Vector3D reflectRay(final Vector3D rayDirection,
                                  final Vector3D surfaceNormal) {
        return rayDirection.reflectFromNormal(surfaceNormal);
//          return rayDirection.subtract(
//                surfaceNormal.scalarMultiply(
//                        2 * rayDirection.dotProduct(surfaceNormal)));
                              /*  / Math.pow(surfaceNormal.magnitude(), 2)*/
    }


    /*
     * Utilities:
     */

    public Color getColor() {
        return color;
    }

    public Vector3D colorVector() {
        return new Vector3D(color);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getEmissionStrength() {
        return emissionStrength;
    }

    public void setEmissionStrength(double emissionStrength) {
        this.emissionStrength = emissionStrength;
    }

    public Vector3D getEmissionColor() {
        return emissionColor;
    }

    public void setEmissionColor(Vector3D emissionColor) {
        this.emissionColor = emissionColor;
    }

    @Override
    public String toString() {
        return "RayTracingTexture: " + color;
//                + ", " + isLightSource();
    }
}
