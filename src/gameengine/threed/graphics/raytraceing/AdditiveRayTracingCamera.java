package gameengine.threed.graphics.raytraceing;

import gameengine.threed.geometry.Ray;
//import gameengine.threed.graphics.raytraceing.objectgraphics.RayIntersectableList;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.List;

public class AdditiveRayTracingCamera extends RayTracedCamera {
    /**
     * Constructs a new {@code RayTracedCamera} with a position given by a
     * {@link Vector3D} and a specified field of view given in degrees.
     *
     * @param position The position of the {@code Camera} in space.
     * @param direction a {@link Vector3D} representing the amount the
     *                  {@code Camera} is facing in each direction.
     * @param imageDimensions The resolution to render the image at.
     * @param maxBounces The maximum number of times a light ray can bounce
     *                   before it is declared to never hit a light.
     * @param raysPerPixel The number of rays to be averaged to find each
     *                     pixel's color.
     * @param multiThreaded Whether the {@code Camera} should utilize
     *                      multithreading when rendering images.
     * @param fieldOfViewDegrees The number of degrees in the horizontal field
     *                           of view. Default value: 60.0
     */
    public AdditiveRayTracingCamera(
            final Vector3D position, final Vector3D direction,
            final Vector2D imageDimensions,
            final int maxBounces, final int raysPerPixel,
            final boolean multiThreaded, final double fieldOfViewDegrees) {
        super(position, direction, imageDimensions, maxBounces, raysPerPixel,
                multiThreaded, fieldOfViewDegrees);
        populateColors();
    }

    /**
     * Constructs a new {@code RayTracedCamera} with a position given with
     * coordinates and a default field of view of 60 degrees.
     *
     * @param position The position of the {@code Camera} in space.
     * @param direction a {@link Vector3D} representing the amount the
     *                  {@code Camera} is facing in each direction.
     * @param imageDimensions The resolution to render the image at.
     * @param maxBounces The maximum number of times a light ray can bounce
     *                   before it is declared to never hit a light.
     * @param raysPerPixel The number of rays to be averaged to find each
     *                     pixel's color.
     * @param multiThreaded Whether the {@code Camera} should utilize
     *                      multithreading when rendering images.
     */
    public AdditiveRayTracingCamera(
            final Vector3D position, final Vector3D direction,
            final Vector2D imageDimensions,
            final int maxBounces, final int raysPerPixel,
            final boolean multiThreaded) {
        super(position, direction, imageDimensions, maxBounces, raysPerPixel,
                multiThreaded, 60.0);
        populateColors();
    }

    /**
     * Constructs a new {@code RayTracedCamera} with a position given with
     * coordinates and a specified field of view given in degrees.
     *
     * @param x The position of the {@code Camera} on the x-axis.
     * @param y The position of the {@code Camera} on the y-axis.
     * @param z The position of the {@code Camera} on the z-axis.
     * @param direction a {@link Vector3D} representing the amount the
     *                  {@code Camera} is facing in each direction.
     * @param imageDimensions The resolution to render the image at.
     * @param maxBounces The maximum number of times a light ray can bounce
     *                   before it is declared to never hit a light.
     * @param raysPerPixel The number of rays to be averaged to find each
     *                     pixel's color.
     * @param multiThreaded Whether the {@code Camera} should utilize
     *                      multithreading when rendering images.
     * @param fieldOfViewDegrees The number of degrees in the horizontal field
     *                           of view. Default value: 60.0
     */
    public AdditiveRayTracingCamera(
            final double x, final double y, final double z,
            final Vector3D direction, final Vector2D imageDimensions,
            final int maxBounces, final int raysPerPixel,
            final boolean multiThreaded, final double fieldOfViewDegrees) {
        super(x, y, z, direction, imageDimensions, maxBounces, raysPerPixel,
                multiThreaded, fieldOfViewDegrees);
        populateColors();
    }

    /**
     * Constructs a new {@code RayTracedCamera} with a position given with
     * coordinates and a default field of view of 60 degrees.
     *
     * @param x The position of the {@code Camera} on the x-axis.
     * @param y The position of the {@code Camera} on the y-axis.
     * @param z The position of the {@code Camera} on the z-axis.
     * @param direction a {@link Vector3D} representing the amount the
     *                  {@code Camera} is facing in each direction.
     * @param imageDimensions The resolution to render the image at.
     * @param maxBounces The maximum number of times a light ray can bounce
     *                   before it is declared to never hit a light.
     * @param raysPerPixel The number of rays to be averaged to find each
     *                     pixel's color.
     * @param multiThreaded Whether the {@code Camera} should utilize
     *                      multithreading when rendering images.
     */
    public AdditiveRayTracingCamera(
            final double x, final double y, final double z,
            final Vector3D direction, final Vector2D imageDimensions,
            final int maxBounces, final int raysPerPixel,
            final boolean multiThreaded) {
        super(x, y, z, direction, imageDimensions, maxBounces, raysPerPixel,
                multiThreaded, 60.0);
        populateColors();
    }

    private int numRenders = 0;
    private final PixelReader reader = getImage().getPixelReader();

    @Override
    protected void updateImage(PixelWriter writer) {
        writer.setPixels(0, 0, getWidth(), getHeight(), PixelFormat.getIntArgbInstance(), buffer, getWidth());
        numRenders++;
    }

//    @Override
//    protected void renderPixel(int pixelIndex, RayIntersectableList objects) {
//        buffer.put(
//                pixelIndex,
//                new Vector3D(calculatePixelColor(rayTo(pixelIndex), objects,
//                        new Vector3D(reader.getColor(pixelIndex % getWidth(), pixelIndex / getWidth()))
//                                .scalarMultiply(numRenders * (getRaysPerPixel() - 1))
//                ))
//                        .scalarDivide(numRenders + 1).oneInt()
//        );
//    }
//
//    protected Vector3D calculatePixelColor(final LightRay startRay,
//                                        final RayIntersectableList objectsInField,
//                                        final Vector3D averageColor) {
//        RayTraceable firstCollision = (RayTraceable) startRay.firstCollision(objectsInField);
//
//        if (firstCollision == null) {
//            return new Vector3D();
//        }
////        if (firstCollision.getTexture().getColor().equals(Color.BLACK)
////                || firstCollision.getTexture().isLightSource()) {
////            return averageColor.add(new Vector3D(firstCollision.getColor()));
////        }
//
//        for (int i = 0; i < getRaysPerPixel(); i++) {
//            averageColor.addMutable(
//                    RayPathTracer.getColor(
//                            startRay.getReflected(firstCollision),
//                            getMaxBounces(),
//                            objectsInField, 2));
//        }
//
//        return averageColor.scalarDivide(getRaysPerPixel() - 1);
//    }

    private void populateColors() {
        for (int i = 0; i < getWidth() * getHeight(); i++) {
            colors[i] = new Vector3D();
        }
    }
    private final Vector3D[] colors = new Vector3D[getWidth() * getHeight()];

    @Override
    protected void renderPixel(int pixelIndex, RayTraceable[] objects) {
        calculatePixelColorVector(rayTo(pixelIndex), objects, colors[pixelIndex]);
        buffer.put(pixelIndex, colors[pixelIndex].scalarDivide(numRenders).oneInt());
//        colors[pixelIndex] = color;
    }

    protected void calculatePixelColorVector(final LightRay startRay,
                                                 final RayTraceable[] objectsInField,
                                                 Vector3D averageColor) {
        RayTraceable firstCollision = startRay.firstCollision(Ray.toStructs(objectsInField), objectsInField);

        if (firstCollision == null) {
//            averageColor.addMutable(new Vector3D(Color.SKYBLUE));
            averageColor.addMutable(LightRay.getSkyColor(startRay.getDirection()));
            return/* averageColor*/;
        }
        if (firstCollision.getTexture().getColor().equals(Color.BLACK)
                || firstCollision.getTexture().isLightSource()) {
//            averageColor.addMutable(new Vector3D(firstCollision.getColor())/*.scalarDivide(getRaysPerPixel())*/);
            averageColor.addMutable(startRay.getReflected(firstCollision).getIncomingLight());
            return /*averageColor.add(new Vector3D(firstCollision.getColor()))*/;
        }

//        Vector3D averageColor = new Vector3D();
        for (int i = 0; i <= getRaysPerPixel(); i++) {
            averageColor.addMutable(
                            startRay.getReflected(firstCollision).getColor(
                            getMaxBounces(),
                            objectsInField, 2).scalarDivide(getRaysPerPixel()));
        }

//        return averageColor/*.scalarDivide(getRaysPerPixel())*/;
    }
}
