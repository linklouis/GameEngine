package gameengine.threed.graphics.raytraceing.textures;

import gameengine.threed.graphics.raytraceing.LightRay;
import gameengine.threed.graphics.raytraceing.Reflection;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;
import org.jocl.struct.ArrayLength;
import org.jocl.struct.Struct;
import org.jocl.struct.CLTypes.cl_float4;

public abstract class RayTracingTexture {
    private Color color;
    private double emissionStrength;
    private Vector3D emissionColor;
    private Vector3D emission;

    public RayTracingTexture(Color color, double emissionStrength, Vector3D emissionColor) {
        this.color = color;
        this.emissionStrength = emissionStrength;
        this.emissionColor = new Vector3D(emissionColor);
        calculateEmission();
    }

    public RayTracingTexture(Color color, double emissionStrength, Color emissionColor) {
        this.color = color;
        this.emissionStrength = emissionStrength;
        this.emissionColor = new Vector3D(emissionColor);
        calculateEmission();
    }

    public static int BASE_TEXTURE = 0;
    public static int METALLIC = 1;
    public static int MIRRORED = 2;
    public static int ROUGH_MIRRORED = 3;
    public static int SUBSURFACE = 4;
    public static int MATTE_SUBSURFACE = 5;

    public static class TextureStruct extends Struct {
        public int type;

        public cl_float4 color;
        public float emissionStrength;
        public cl_float4 emissionColor;
        public cl_float4 emission;
        @ArrayLength(3)
        public float otherVars[];

        private float[] initOtherVars(float[] others) {
            if (others.length == 3) {
                return others;
            }
            if (others.length > 3) {
                throw new RuntimeException("More than 3 other vars!! D:");
            }
            float[] corrected = new float[3];
            System.arraycopy(others, 0, corrected, 0, others.length);
            return corrected;
        }

        public TextureStruct(int type,
                             cl_float4 color,
                             float emissionStrength, cl_float4 emissionColor, cl_float4 emission,
                             float[] otherVars) {
            this.type = type;
            this.color = color;
            this.emissionStrength = emissionStrength;
            this.emissionColor = emissionColor;
            this.emission = emission;
            this.otherVars = initOtherVars(otherVars);
        }

        public TextureStruct() {

        }
    }

    public TextureStruct toStruct() {
        return new TextureStruct(type(),
                colorVector().toStruct(),
                (float) emissionStrength, emissionColor.toStruct(), emission.toStruct(),
                getOtherVars());
    }

    protected abstract int type();
    protected abstract float[] getOtherVars();

    public abstract Reflection reflection(final LightRay lightRayDirection,
                                          final Vector3D surfaceNormal);

    public Vector3D scatterRay(Vector3D surfaceNormal) {
        Vector3D reflection = Vector3D.random(-1, 1);
        return reflection.add(surfaceNormal.unitVector()).unitVector();
    }

    public Vector3D reflectRay(final Vector3D rayDirection,
                                  final Vector3D surfaceNormal) {
        return rayDirection.reflectFromNormal(surfaceNormal);
    }

    protected Vector3D defaultColorUpdate(Vector3D rayColor) {
        return rayColor.multiplyAcross(colorVector());
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
        calculateEmission();
    }

    public Vector3D getEmissionColor() {
        return emissionColor;
    }

    public void setEmissionColor(Vector3D emissionColor) {
        this.emissionColor = emissionColor;
        calculateEmission();
    }

    public Vector3D getEmission() {
        return emission;
    }

    protected void calculateEmission() {
        emission = emissionColor.scalarMultiply(emissionStrength);
    }

    public boolean isLightSource() {
        return emissionStrength > 0;
    }

    @Override
    public String toString() {
        return "RayTracingTexture: " + color;
//                + ", " + isLightSource();
    }
}
