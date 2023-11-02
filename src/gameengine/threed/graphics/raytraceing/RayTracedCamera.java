package gameengine.threed.graphics.raytraceing;

import gameengine.threed.graphics.Camera;
import gameengine.threed.graphics.Visual3D;
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
                    new RayTraceableList(objects));
        } else {
            renderUnthreaded(getImage().getPixelWriter(),
                    new RayTraceableList(objects));
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
                                  final RayTraceableList objects) {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                renderPixelAt(x, y, writer, objects);
            }
        }
    }

    private void renderThreaded(final PixelWriter writer,
                                final RayTraceableList objects) {
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
                                finalX + width, finalY + height,
                                writer, objects));
//                        renderPixelAt(finalX, finalY, writer, objects));
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

    private void renderPixels(int startX, int startY, int endX, int endY, PixelWriter writer,
                              RayTraceableList objects) {
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                renderPixelAt(x, y, writer, objects);
            }
        }
    }

//    private void renderPixelAt(int x, int y, PixelWriter writer,
//                               RayTraceableList objects) {
//        writer.setColor(x, y,
//                new PixelRay(
//                        new Ray(getLocation(), getRayPath(x, y)),
//                        maxBounces, raysPerPixel, objects).getFinalColor());
//
////        writer.setColor(x, y, PixelRayStatic.getFinalColor(
////                new Ray(getLocation(), getRayPath(x, y)),
////                maxBounces, raysPerPixel, objects));
//    }
//
//    private Vector3D getRayPath(final double x, final double y) {
//        // Thx to ChatGPT
//        updateScaleX();
//        return getDirection().transformToNewCoordinates(
//                (x * 2.0 / getWidth() - 1) * scaleX,
//                (getHeight() - y * 2.0) * scaleX / getWidth(),
//                screenDistance);
//    }

    private void renderPixelAt(int x, int y, PixelWriter writer,
                               RayTraceableList objects) {
//        writer.setColor(x, y, new PixelRay(
//                new Ray(getLocation(), getRayPath(x, y)),
//                maxBounces, raysPerPixel, objects).getFinalColor()
//        );

        writer.setColor(x, y, PixelRayStatic.getFinalColor(
                new Ray(getLocation(), getRayPath(x, y)),
                maxBounces, raysPerPixel, objects));
    }

    private Vector3D getRayPath1(final double x, final double y) {
        // Thx to ChatGPT
//        return getDirection()
//                .scalarMultiply(screenDistance)
//                .add(getDirection()
//                        .crossWithJ((getHeight() - y * 2) * scaleX / getWidth()))
//                .add(getDirection()
//                        .crossWithSelfCrossedWithJ((x * 2 / getWidth() - 1) * scaleX));
        return  getDirection().transformToNewCoordinates(
                (x * 2 / getWidth() - 1) * scaleX,
                (getHeight() - y * 2) * scaleX / getWidth());
    }

    private Vector3D getRayPath(final double x2, final double y2) {
        // Thx to ChatGPT
        double xd = getDirection().getX();
        double yd = getDirection().getY();
        double zd = getDirection().getZ();
//        double w = getWidth();
//        double h = getHeight()
        double x = (x2 * 2 / getWidth() - 1) * scaleX;
        double y = (getAspectRatio() - y2 * 2 / getWidth()) * scaleX;
//        return new Vector3D(
//                xd * (1 + yd * scaleX) - zd * scaleY,
//                yd - (zd * zd + x * x) * scaleX,
//                zd * (1 + yd * scaleX) + xd * scaleY
//        );
//        return new Vector3D(
//                xd * (1 + yd * (x * 2 / w - 1) * scaleX) - zd * (h - y * 2) * scaleX / w,
//                yd - (zd * zd + xd * xd) * (x * 2 / w - 1) * scaleX,
//                zd * (1 + yd * (x * 2 / w - 1) * scaleX) + xd * (h - y * 2) * scaleX / w
//        );
        // x: xd + xd * yd * (x * 2 / w - 1) * scaleX - zd * (h - y * 2) * scaleX / w,
//        return new Vector3D(
//                xd + scaleX * (xd * yd * (x * 2 - w) - zd * (h - y * 2)) / w,
//                yd - scaleX * (zd * zd + xd * xd) * (x * 2 / w - 1),
//                zd + scaleX * (zd * yd * (x * 2 - w) + xd * (h - y * 2)) / w
//        );
//        return new Vector3D(
//                xd + scaleX * (xd * yd * (x2 - w) - zd * (h - y2)) / w,
//                yd - scaleX * (zd * zd + xd * xd) * (x2 / w - 1),
//                zd + scaleX * (zd * yd * (x2 - w) + xd * (h - y2)) / w
//        );
//        return new Vector3D(
//                xd + scaleX * (xd * yd * x - zd * y),
//                yd - scaleX * (zd * zd + xd * xd) * x,
//                zd + scaleX * (zd * yd * x + xd * y)
//        );
//        return new Vector3D(
//                xd + (xd * yd * x - zd * y),
//                yd - x * (zd * zd + xd * xd),
//                zd + (zd * yd * x + xd * y)
//        );
        return new Vector3D(
                xd + xd * yd * x - zd * y,
                yd - x * (zd * zd + xd * xd),
                zd + zd * yd * x + xd * y
        );
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


