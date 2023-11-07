package gameengine.threed.graphics.raytraceing;

import gameengine.threed.graphics.Camera;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayIntersectableList;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.timeformatting.TimeConversionFactor;
import gameengine.timeformatting.TimeFormatter;
import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A game {@link Camera} that renders a 3D scene using ray tracing
 *
 * @author Louis Link
 * @since 1.0
 */
public class RayTracedCamera extends Camera<RayTraceable> {
    /**
     * The number of rays averaged to find each pixel's color.
     */
    private int raysPerPixel;
    /**
     * Whether the {@code Camera} should utilize multithreading when rendering
     * images. Doing so can speed up rendering.
     */
    private boolean multiThreaded;
    /**
     * The maximum number of times a light ray can bounce before it is declared
     * to never hit a light.
     */
    private final int maxBounces;
    /**
     * The time in nanoseconds it took to render the last frame.
     */
    private long renderTime;
    /**
     * The target size of the tiles for the image to be broken into for
     * increased threaded performance.
     * <p>The actual tile size may end up being different than the set target.
     */
    private int targetTileSize;
    private Vector2D tileDimensions;

    private double scaleX;



    /*
     * Construction:
     */

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
    public RayTracedCamera(final Vector3D position, final Vector3D direction,
                           final Vector2D imageDimensions,
                           final int maxBounces, final int raysPerPixel,
                           final boolean multiThreaded,
                           final double fieldOfViewDegrees) {
        super(position, direction, imageDimensions, fieldOfViewDegrees,
                RayTraceable.class);
        this.maxBounces = maxBounces;
        this.raysPerPixel = raysPerPixel;
        this.multiThreaded = multiThreaded;
        setTargetTileSize(15 / raysPerPixel);
        updateScaleX();
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
    public RayTracedCamera(final Vector3D position, final Vector3D direction,
                           final Vector2D imageDimensions,
                           final int maxBounces, final int raysPerPixel,
                           final boolean multiThreaded) {
        super(position, direction, imageDimensions, 60.0,
                RayTraceable.class);
        this.maxBounces = maxBounces;
        this.raysPerPixel = raysPerPixel;
        this.multiThreaded = multiThreaded;
        setTargetTileSize(15 / raysPerPixel);
        updateScaleX();
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
    public RayTracedCamera(final double x, final double y, final double z,
                           final Vector3D direction,
                           final Vector2D imageDimensions,
                           final int maxBounces, final int raysPerPixel,
                           final boolean multiThreaded,
                           final double fieldOfViewDegrees) {
        super(x, y, z, direction, imageDimensions, fieldOfViewDegrees,
                RayTraceable.class);
        this.maxBounces = maxBounces;
        this.raysPerPixel = raysPerPixel;
        this.multiThreaded = multiThreaded;
        setTargetTileSize(15 / raysPerPixel);
        updateScaleX();
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
    public RayTracedCamera(final double x, final double y, final double z,
                           final Vector3D direction,
                           final Vector2D imageDimensions,
                           final int maxBounces, final int raysPerPixel,
                           final boolean multiThreaded) {
        super(x, y, z, direction, imageDimensions, 60.0,
                RayTraceable.class);
        this.maxBounces = maxBounces;
        this.raysPerPixel = raysPerPixel;
        this.multiThreaded = multiThreaded;
        setTargetTileSize(15 / raysPerPixel);
        updateScaleX();
    }


    /*
     * Functionality:
     */

    /**
     * Render a ray-traced image of the given objects.
     *
     * @param renderableObjects The {@link Visual3D} of the objects to consider
     *                          for the scene.
     * @return The rendered scene as a {@link WritableImage}.
     */
    @Override
    public WritableImage renderImage(final Collection<RayTraceable> renderableObjects) {
        RayTraceable[] objects = renderableObjects.toArray(new RayTraceable[0]);

        long startTime = System.nanoTime();

        if (multiThreaded) {
            renderThreaded(getImage().getPixelWriter(),
                    new RayIntersectableList(objects));
        } else {
            renderUnthreaded(getImage().getPixelWriter(),
                    new RayIntersectableList(objects));
        }


        renderTime = System.nanoTime() - startTime;
        System.out.println("Execution Time: "
                + TimeFormatter.format(renderTime));
        System.out.println("Execution Time: "
                + TimeConversionFactor.MILLISECOND.convert(renderTime)
                + " milliseconds");
        System.out.println("rendered");

        try {
            saveToFile("RayTraced_0_1_("
                    + (int) getWidth() + "," + (int) getHeight() + ")_"
                    + "rays" + raysPerPixel + "_bounces"
                    + maxBounces + ".png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        System.exit(0);

        return getImage();
    }

    private void renderUnthreaded(final PixelWriter writer,
                                  final RayIntersectableList objects) {
        renderPixels(0, 0, getWidth(), getHeight(), writer, objects);
    }

    private void renderThreaded(final PixelWriter writer,
                                final RayIntersectableList objects) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);

        int width = findClosestFactor((int) getWidth(), 150);
        int height = findClosestFactor((int) getHeight(), 150);

        // Split the canvas into smaller tasks for multithreading
        for (int x = 0; x < getWidth(); x += width) {
            for (int y = 0; y < getHeight(); y += height) {
                int finalX = x;
                int finalY = y;
                threadPool.submit(() ->
                        renderPixels(finalX, finalY,
                                width, height,
                                writer, objects));
            }
        }

        // Make sure all threads are completed before moving forward
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void renderPixels(final int startX, final int startY,
                              final double width, final double height,
                              final PixelWriter writer,
                              final RayIntersectableList objects) {
        for (int x = startX; x < startX + width; x++) {
            for (int y = startY; y < startY + height; y++) {
//                writer.setColor(x, y, new PixelRay(
//                        new LightRay(getLocation(), getRayPath(x, y)),
//                        maxBounces, raysPerPixel, objects).getFinalColor()
//                );
                writer.setColor(x, y, calculatePixelColor(rayTo(x, y), objects));
            }
        }
    }

    private LightRay rayTo(final double x, final double y) {
        return new LightRay(
                getLocation(),
                // Thx to ChatGPT:
                getDirection().transformToNewCoordinates(
                        (x * 2 / getWidth() - 1) * scaleX,
                        (getHeight() - y * 2) * scaleX / getWidth()));
    }


    /*
     ** LightRay Tracing:
     */

    /**
     * @return The average color of each ray to measure based on
     * {@link #raysPerPixel}.
     */
    private Color calculatePixelColor(final LightRay startLightRay,
                                      final RayIntersectableList objectsInField) {
        RayTraceable firstCollision = (RayTraceable) startLightRay.firstCollision(objectsInField);

        if (firstCollision == null) {
            return Color.BLACK;
        }
        if (firstCollision.getTexture().isLightSource()) {
            return firstCollision.getColor();
        }

        Vector3D averageColor = new Vector3D(0);
        for (int i = 0; i < raysPerPixel; i++) {
            averageColor.addMutable(
                    RayPathTracer.getColor(
                            startLightRay.getReflected(firstCollision, 1),
                            maxBounces,
                            objectsInField));
        }

        return averageColor.scalarDivide(raysPerPixel).toColor();
    }


    /*
     * Utilities:
     */

    @Override
    protected void setFieldOfView(final double fieldOfViewDegrees) {
        super.setFieldOfView(fieldOfViewDegrees);
        updateScaleX();
    }

    public int getRaysPerPixel() {
        return raysPerPixel;
    }

    public void setRaysPerPixel(final int newRaysPerPixel) {
        raysPerPixel = newRaysPerPixel;
    }

    public boolean isMultiThreaded() {
        return multiThreaded;
    }

    public void setMultiThreaded(boolean newMultiThreaded) {
        multiThreaded = newMultiThreaded;
    }

    public int getMaxBounces() {
        return maxBounces;
    }

    public long getRenderTime() {
        return renderTime;
    }

    public int getTargetTileSize() {
        return targetTileSize;
    }

    public void setTargetTileSize(int targetTileSize) {
        this.targetTileSize = targetTileSize;
        tileDimensions = new Vector2D(
                findClosestFactor((int) getWidth(), targetTileSize),
                findClosestFactor((int) getHeight(), targetTileSize));
    }

    public Vector2D getTileDimensions() {
        return tileDimensions;
    }

    @Override
    public void setImage(WritableImage image) {
        super.setImage(image);
        tileDimensions = new Vector2D(
                findClosestFactor((int) getWidth(), targetTileSize),
                findClosestFactor((int) getHeight(), targetTileSize));
        updateScaleX();
    }

    private static int findClosestFactor(int imageSize, int targetTileSize) {
        int closestFactor = 0;
        int minDifference = Integer.MAX_VALUE;

        for (int i = 1; i <= imageSize; i++) {
            if (imageSize % i == 0) {
                int difference = Math.abs(targetTileSize - i);
                if (difference < minDifference) {
                    minDifference = difference;
                    closestFactor = i;
                }
            }
        }

        return closestFactor;
    }

    public void updateScaleX() {
        scaleX = Math.tan(Math.toRadians(getFieldOfViewDegrees() / 2.0));
    }
}


