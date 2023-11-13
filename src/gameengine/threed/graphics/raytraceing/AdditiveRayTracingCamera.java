package gameengine.threed.graphics.raytraceing;

import gameengine.threed.graphics.raytraceing.objectgraphics.RayIntersectableList;
import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;

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
    }

    private int numRenders = 1;
    private final PixelReader reader = getImage().getPixelReader();
    @Override
    protected void renderPixel(int pixelIndex, RayIntersectableList objects) {
        buffer.put(
                pixelIndex,
                new Vector3D(calculatePixelColor(rayTo(pixelIndex), objects))
                        .add(new Vector3D(
                                reader.getColor(pixelIndex % getWidth(), pixelIndex / getWidth()))
                                .scalarMultiply(numRenders))
                        .scalarDivide(numRenders + 1).oneInt()
        );
    }

    @Override
    protected void updateImage(PixelWriter writer) {
        writer.setPixels(0, 0, getWidth(), getHeight(), PixelFormat.getIntArgbInstance(), buffer, getWidth());
        numRenders++;
    }
}
