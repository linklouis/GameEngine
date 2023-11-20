package gameengine.threed.graphics.raytraceing.objectgraphics;

import gameengine.skeletons.Modifier;
import gameengine.threed.geometry.Ray;
import gameengine.threed.graphics.GraphicsObject3D;
import gameengine.threed.graphics.Visual3D;
import gameengine.threed.graphics.raytraceing.LightRay;
import gameengine.threed.graphics.raytraceing.Reflection;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.threed.geometry.RayIntersectable;
import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;
import org.jocl.struct.Struct;

import java.util.ArrayList;
import java.util.List;

import org.jocl.struct.CLTypes.cl_float4;
import org.jocl.struct.CLTypes.cl_float2;

public sealed abstract class RayTraceable extends GraphicsObject3D implements RayIntersectable
        permits QuadGraphics, SphereGraphics, TriGraphics {
    private RayTracingTexture texture = null;


    /*
     * Construction:
     */

    public RayTraceable() {
        super();
    }

    public RayTraceable(final Modifier[] modifiers) {
        super(modifiers);
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        List<Class<? extends Modifier>> modifiers = new ArrayList<>();
        modifiers.add(Visual3D.class);
        return modifiers;
    }


    /*
     * Functionality:
     */

    public Reflection reflection(LightRay lightRay) {
        return texture.reflection(lightRay, surfaceNormal(lightRay));
    }
    public abstract RayTraceableStruct toStruct(Ray perspective);

    public static class RayTraceableStruct extends Struct {
        public final int type; // 0 = Sphere, 1 = Tri, 2 = Rect

        public cl_float4 normal;
        public cl_float4 vertexOrCenter;

        public cl_float4 side1; // v0 for tris, planeXAxis for quads
        public cl_float4 side2; // v1 for tris, planeYAxis for quads

        // Tris:
        public float dot00; // Also r for spheres
        public float dot01;
        public float dot11;
        public float invDenom;

        // Quads:
        public cl_float2 max;
        public cl_float2 min;

        // Sphere
        public RayTraceableStruct(cl_float4 normal,
                                  cl_float4 vertexOrCenter,
                                  double dot00) {
            this.type = 0;
            this.normal = normal;
            this.vertexOrCenter = vertexOrCenter;
            this.side1 = null;
            this.side2 = null;
            this.dot00 = (float) dot00;
            this.dot01 = 0;
            this.dot11 = 0;
            this.invDenom = 0;
        }

        // Tri
        public RayTraceableStruct(Vector3D normal, Vector3D vertexOrCenter,
                                  Vector3D side1, Vector3D side2,
                                  double dot00, double dot01, double dot11, double invDenom) {
            this.type = 1;
            this.normal = normal.toStruct();
            this.vertexOrCenter = vertexOrCenter.toStruct();
            this.side1 = side1.toStruct();
            this.side2 = side2.toStruct();
            this.dot00 = (float) dot00;
            this.dot01 = (float) dot01;
            this.dot11 = (float) dot11;
            this.invDenom = (float) invDenom;
        }

        public RayTraceableStruct(cl_float4 normal, cl_float4 vertexOrCenter,
                                  cl_float4 side1, cl_float4 side2,
                                  float dot00, float dot01, float dot11, float invDenom) {
            this.type = 1;
            this.normal = normal;
            this.vertexOrCenter = vertexOrCenter;
            this.side1 = side1;
            this.side2 = side2;
            this.dot00 = dot00;
            this.dot01 = dot01;
            this.dot11 = dot11;
            this.invDenom = invDenom;
        }

        // Quad
        public RayTraceableStruct(Vector3D normal, Vector3D vertexOrCenter,
                                  Vector3D side1, Vector3D side2,
                                  Vector2D max, Vector2D min) {
            this.type = 2;
            this.normal = normal.toStruct();
            this.vertexOrCenter = vertexOrCenter.toStruct();
            this.side1 = side1.toStruct();
            this.side2 = side2.toStruct();
            this.dot00 = 0;
            this.dot01 = 0;
            this.dot11 = 0;
            this.invDenom = 0;
            this.max = max.toStruct();
            this.min = min.toStruct();
        }

        // Other
        public RayTraceableStruct(int type,
                                  cl_float4 normal, cl_float4 vertexOrCenter,
                                  cl_float4 side1, cl_float4 side2,
                                  float dot00, float dot01, float dot11, float invDenom,
                                  cl_float2 max, cl_float2 min) {
            this.type = type;
            this.normal = normal;
            this.vertexOrCenter = vertexOrCenter;
            this.side1 = side1;
            this.side2 = side2;
            this.dot00 = dot00;
            this.dot01 = dot01;
            this.dot11 = dot11;
            this.invDenom = invDenom;
            this.max = max;
            this.min = min;
        }

        public RayTraceableStruct(int type,
                                  cl_float4 normal, cl_float4 vertexOrCenter,
                                  cl_float4 side1, cl_float4 side2,
                                  double dot00, double dot01, double dot11, double invDenom,
                                  cl_float2 max, cl_float2 min) {
            this.type = type;
            this.normal = normal;
            this.vertexOrCenter = vertexOrCenter;
            this.side1 = side1;
            this.side2 = side2;
            this.dot00 = (float) dot00;
            this.dot01 = (float) dot01;
            this.dot11 = (float) dot11;
            this.invDenom = (float) invDenom;
            this.max = max;
            this.min = min;
        }

        public RayTraceableStruct(int type, Vector3D normal, Vector3D vertexOrCenter,
                                  Vector3D side1, Vector3D side2,
                                  double dot00, double dot01, double dot11, double invDenom,
                                  Vector2D max, Vector2D min) {
            this.type = type;
            this.normal = normal.toStruct();
            this.vertexOrCenter = vertexOrCenter.toStruct();
            this.side1 = side1.toStruct();
            this.side2 = side2.toStruct();
            this.dot00 = (float) dot00;
            this.dot01 = (float) dot01;
            this.dot11 = (float) dot11;
            this.invDenom = (float) invDenom;
            this.max = max.toStruct();
            this.min = min.toStruct();
        }
    }


    /*
     * Utilities:
     */

    public abstract Vector3D[] getVertices();

    public RayTracingTexture getTexture() {
        return texture;
    }

    public void setTexture(RayTracingTexture texture) {
        this.texture = texture;
    }

    public Color getColor() {
        return getTexture().getColor();
    }

    public Vector3D colorVector() {
        return new Vector3D(getTexture().getColor());
    }
    public void setColor(Color color) {
        getTexture().setColor(color);
    }
}
