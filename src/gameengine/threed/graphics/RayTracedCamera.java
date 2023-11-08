package gameengine.threed.graphics;

import gameengine.threed.graphics.objectgraphics.GraphicsObject3D;
import gameengine.threed.graphics.raytraceing.RayIntersectableList;
import gameengine.threed.graphics.objectgraphics.RayTraceable;
import gameengine.threed.graphics.raytraceing.Ray;
import gameengine.threed.graphics.raytraceing.RayPathTracer;
import gameengine.timeformatting.TimeConversionFactor;
import gameengine.timeformatting.TimeFormatter;
import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.File;
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
public class RayTracedCamera {
    private Vector3D position;
    /**
     * a {@link Vector3D} representing the amount the {@code Camera} is facing
     * in each direction.
     */
    private Vector3D direction;
    /**
     * The {@link WritableImage} the {@code Camera} will render the image on.
     */
    private WritableImage image;
    /**
     * The number of degrees in the horizontal field of view.
     */
    private double fieldOfViewDegrees;
    /**
     * A flag indicating that the scene has changed, and the {@code Camera}
     * needs to render a new frame.
     */
    private boolean updateNeeded = true;

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
        this.position = position;
        this.direction = direction.unitVector();
        image = new WritableImage((int) imageDimensions.getX(), (int) imageDimensions.getY());
        setFieldOfViewDegrees(fieldOfViewDegrees);
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
        this.position = position;
        this.direction = direction.unitVector();
        image = new WritableImage((int) imageDimensions.getX(), (int) imageDimensions.getY());
        setFieldOfViewDegrees(60);
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
        this.position = new Vector3D(x, y, z);
        this.direction = direction.unitVector();
        image = new WritableImage((int) imageDimensions.getX(), (int) imageDimensions.getY());
        setFieldOfViewDegrees(fieldOfViewDegrees);
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
        this.position = new Vector3D(x, y, z);
        this.direction = direction.unitVector();
        image = new WritableImage((int) imageDimensions.getX(), (int) imageDimensions.getY());
        setFieldOfViewDegrees(60);
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
    public WritableImage renderImage(final Collection<RayTraceable> renderableObjects) {
        long startTime = System.nanoTime();

        if (multiThreaded) {
            renderThreaded(getImage().getPixelWriter(),
                    new RayIntersectableList(renderableObjects));
        } else {
            renderUnthreaded(getImage().getPixelWriter(),
                    new RayIntersectableList(renderableObjects));
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
                writer.setColor(x, y, calculatePixelColor(rayTo(x, y), objects));
            }
        }
    }

    private Ray rayTo(final double x, final double y) {
        return new Ray(
                getLocation(),
                // Thx to ChatGPT:
                getDirection().transformToNewCoordinates(
                        (x * 2 / getWidth() - 1) * scaleX,
                        (getHeight() - y * 2) * scaleX / getWidth()));
    }

    /**
     * Renders a new frame if it has been requested and applies
     * post-processing, otherwise returns.
     *
     * @param renderableObjects The {@link GraphicsObject3D} objects to be rendered in
     *                          the scene.
     * @return True if a new frame has been rendered, otherwise false.
     */
    public boolean update(Collection<RayTraceable> renderableObjects) {
        if (updateNeeded) {
            renderImage(renderableObjects);
            updateNeeded = false;
            return true;
        }
        return false;
    }

    public void requestUpdate() {
        updateNeeded = true;
    }

    /**
     * Saves {@link #image} to a .png file with the given path.
     *
     * @param fileName The file path to save the image to.
     * @throws IOException
     */
    public void saveToFile(String fileName) throws IOException {
        File file = new File(fileName);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
    }

    /**
     * Saves {@link #image} to a file of the given image format and file path.
     *
     * @param fileName The file path to save the image to.
     * @param format The image format to use to save the file.
     * @throws IOException
     */
    public void saveToFile(String fileName, String format) throws IOException {
        File file = new File(fileName);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), format, file);
    }


    /*
     ** Ray Tracing:
     */

    /**
     * @return The average color of each ray to measure based on
     * {@link #raysPerPixel}.
     */
    private Color calculatePixelColor(final Ray startRay,
                                      final RayIntersectableList objectsInField) {
        RayTraceable firstCollision = (RayTraceable) startRay.firstCollision(objectsInField);

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
                            startRay.getReflected(firstCollision, 1),
                            maxBounces,
                            objectsInField));
        }

        return averageColor.scalarDivide(raysPerPixel).toColor();
    }


    /*
     * Utilities:
     */

    protected void setFieldOfView(final double fieldOfViewDegrees) {
        this.fieldOfViewDegrees = fieldOfViewDegrees;
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

    public void setImage(WritableImage image) {
        this.image = image;
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

    public boolean needsUpdate() {
        return updateNeeded;
    }

    public double getFieldOfViewDegrees() {
        return fieldOfViewDegrees;
    }

    public double getFieldOfViewRadians() {
        return Math.toRadians(fieldOfViewDegrees);
    }

    public void setFieldOfViewDegrees(double fieldOfViewDegrees) {
        setFieldOfView(fieldOfViewDegrees);
    }

    public void setFieldOfViewRadians(double fieldOfViewRadians) {
        setFieldOfView(Math.toDegrees(fieldOfViewRadians));
    }

    public Vector3D getDirection() {
        return direction;
    }

    public void setDirection(Vector3D direction) {
        this.direction = direction.unitVector();
    }

    public Vector3D getLocation() {
        return position;
    }

    public void setLocation(Vector3D newLocation) {
        position = newLocation;
    }

    public WritableImage getImage() {
        return image;
    }

    public double getWidth() {
        return getImage().getWidth();
    }

    public double getHeight() {
        return getImage().getHeight();
    }

    public double getAspectRatio() {
        return image.getHeight() / image.getWidth();
    }

    public Class<? extends GraphicsObject3D> getRayTraceable() {
        return RayTraceable.class;
    }
}


