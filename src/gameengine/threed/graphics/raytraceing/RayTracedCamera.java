package gameengine.threed.graphics.raytraceing;

import gameengine.threed.graphics.Camera;
import gameengine.threed.graphics.Visual3D;
import gameengine.threed.prebuilt.objectmovement.collisions.Collider3D;
import gameengine.timeformatting.TimeFormatter;
import gameengine.vectormath.Vector3D;
import javafx.scene.image.WritableImage;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RayTracedCamera extends Camera {
    /**
     * Size of the projected screen
     */
    private final double SCREEN_PROJECTION_SIZE = 50000;
    /**
     * The distance from the camera the screen is being simulated to be in
     * order to find the direction from the camera to each pixel
     */
    private final double SCREEN_DISTANCE = 0.7;
    /**
     * The number of light rays to average the color of to find a pixel's color
     */
    private int raysPerPixel;
    private boolean multiThreaded;
    private final int MAX_BOUNCES;
    public long renderTime;


    /*
     * Construction:
     */

    public RayTracedCamera(double x, double y, double z, Vector3D direction,
                           int imageWidth, int imageHeight,
                           int maxBounces,
                           int raysPerPixel, boolean multiThreaded, double fieldOfViewDegrees) {
        super(x, y, z, direction, imageWidth, imageHeight, fieldOfViewDegrees);
        this.MAX_BOUNCES = maxBounces;
        this.raysPerPixel = raysPerPixel;
        this.multiThreaded = multiThreaded;
    }

    public RayTracedCamera(double x, double y, double z, Vector3D direction,
                           int imageWidth, int imageHeight,
                           int maxBounces,
                           int raysPerPixel,
                           boolean multiThreaded) {
        super(x, y, z, direction, imageWidth, imageHeight, 60.0);
        this.MAX_BOUNCES = maxBounces;
        this.raysPerPixel = raysPerPixel;
        this.multiThreaded = multiThreaded;
    }


    /*
     * Functionality:
     */

    @Override
    public WritableImage renderImage(Visual3D[] renderableObjects) {
        Collider3D<?>[] objects = getValidColliders(renderableObjects);

        long startTime = System.nanoTime();

        if (multiThreaded) {
            renderThreaded(getImage(), new Collider3DList(objects));
        } else {
            renderUnthreaded(getImage(), new Collider3DList(objects));
        }


        long endTime = System.nanoTime();
        renderTime = (endTime - startTime) / 1_000_000; // Convert nanoseconds to milliseconds
        System.out.println("Execution Time: " + TimeFormatter.format(renderTime));
        System.out.println("Execution Time: " + renderTime + " milliseconds");


        System.out.println("rendered");
        try {
            saveToFile("RayTraced_0_1_(" + (int) getWidth() + "," + (int) getHeight() + ")_"
                    + "rays" + raysPerPixel + "_bounces" + MAX_BOUNCES + ".png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return getImage();
    }

    private void renderUnthreaded(WritableImage image, Collider3DList objects) {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                PixelRay pixelRay = new PixelRay(
                        new Ray(getLocation(), getRayPath(x, y)),
                        MAX_BOUNCES, raysPerPixel, objects);

                image.getPixelWriter().setColor(x, y, pixelRay.getFinalColor());
            }
        }
    }

    private void renderThreaded(WritableImage image, Collider3DList objects) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService threadPool = Executors.newFixedThreadPool(numThreads - 1);

        // Split the canvas into smaller tasks for multithreading
        for (int pixelX = 0; pixelX < getWidth(); pixelX++) {
            for (int pixelY = 0; pixelY < getHeight(); pixelY++) {
                int finalX = pixelX;
                int finalY = pixelY;

                threadPool.submit(() -> {
                    image.getPixelWriter().setColor(finalX, finalY,
                            new PixelRay(
                                    new Ray(getLocation(), getRayPath(finalX, finalY)),
                                    MAX_BOUNCES, raysPerPixel, objects).getFinalColor());
//                    renderPixelAt(finalX, finalY, image, objects);
                });
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

    private void renderPixelAt(int x, int y, WritableImage image,
                               Collider3DList objects) {
        PixelRay pixelRay = new PixelRay(
                new Ray(getLocation(), getRayPath(x, y)),
                MAX_BOUNCES, raysPerPixel, objects);

        image.getPixelWriter().setColor(x, y, pixelRay.getFinalColor());
    }

//    private void renderPixelAtHDR(int x, int y, WritableImage image, Collider3D<?>[] objects) {
//        Vector3D rayPath = getRayPath(x, y);
//        int num = 0;
//
//        Vector3D average = new Vector3D(0);
//        for (int i = 0; i < raysPerPixel; i++) {
//            PixelRay ray = new PixelRay(
//                    getLocation(), rayPath, marchDistance, maxDistance, maxBounces, objects);
//            Vector3D newColor = new Vector3D(ray.getColorFromBounces());
//            average = average.add(newColor);
//            if (newColor.magnitude() != 0) {
//                average = average.add(newColor);
//                num++;
//            } else {
//                num += 2;
//            }
//        }
//        average = average.scalarDivide(num);
//
//        // Create an OpenEXR image file
//        try (OutputFile outputFile = new OutputFile("output.hdr")) {
//            // Create a frame buffer to store the HDR color value
//            FrameBuffer frameBuffer = new FrameBuffer();
//            PixelType pixelType = PixelType.FLOAT;
//            int numChannels = 3;
//            frameBuffer.insert("R", new Channel(pixelType, numChannels));
//
//            // Convert the color to HDR format and write to the frame buffer
//            for (int channel = 0; channel < numChannels; channel++) {
//                float[] data = new float[1];
//                data[0] = (float) average.getComponent(channel);
//                frameBuffer.getTypedChannel(pixelType, numChannels, channel).set(x, y, data);
//            }
//
//            // Write the frame buffer to the OpenEXR image file
//            outputFile.setFrameBuffer(frameBuffer);
//            outputFile.writePixels(1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private Vector3D localPixelLocation(double x, double y) {
        // Easier to read version, but less efficient:
//        double halfWidth = getWidth() / 2.0;
//        double halfHeight = getHeight() / 2.0;
//
//        double aspectRatio = getWidth() / getHeight();
//        double scaleX = Math.tan(Math.toRadians(getFieldOfViewDegrees() / 2.0));
//        double scaleY = scaleX / aspectRatio;
//
//        double localX = (x - halfWidth) / halfWidth * scaleX;
//        double localY = (halfHeight - y) / halfHeight * scaleY;
//
//        return new Vector3D(localX, localY, screenDistance);
//        double halfWidth = getWidth() / 2.0;
//        double halfHeight = getHeight() / 2.0;

        double scaleX = Math.tan(Math.toRadians(getFieldOfViewDegrees() / 2.0));

        return new Vector3D(
                scaleX * (2 * x - getWidth()) / getWidth(),
                scaleX * getWidth() * (getHeight() - 2 * y) / (getHeight() * getHeight()),
                SCREEN_DISTANCE);
    }

    private Vector3D getRayPath(double x, double y) {
        // Thx to ChatGPT
        Vector3D localPixelLocation = localPixelLocation(x, y);

        // Calculate the truePixelLocation in global coordinates
        Vector3D truePixelLocation = getLocation()
                .add(getDirection()
                        .scalarMultiply(localPixelLocation.getZ()))
                .add(getDirection()
                        .crossProduct(new Vector3D(0, 1, 0))
                        .scalarMultiply(localPixelLocation.getY()))
                .add(getDirection()
                        .crossProduct(getDirection()
                                .crossProduct(new Vector3D(0, 1, 0)))
                        .scalarMultiply(localPixelLocation.getX()));

        // Calculate truePixelDirectionFromC
        return truePixelLocation.subtract(getLocation());
    }

    private Collider3D<?>[] getValidColliders(Visual3D[] visuals) {
        return Arrays.stream(visuals)
                .filter(object ->
                        object.getParent().containsModifier(Collider3D.class))
                .map(object -> object.getFromParent(Collider3D.class))
                .toArray(Collider3D[]::new);
    }


    /*
     * Utilities:
     */

    @Override
    protected void setFieldOfView(double fieldOfViewDegrees) {
        super.setFieldOfView(fieldOfViewDegrees);
    }

    public double getScreenDistance() {
        return SCREEN_DISTANCE;
    }

    public int getRaysPerPixel() {
        return raysPerPixel;
    }

    public void setRaysPerPixel(int raysPerPixel) {
        this.raysPerPixel = raysPerPixel;
    }

    public boolean isMultiThreaded() {
        return multiThreaded;
    }

    public void setMultiThreaded(boolean multiThreaded) {
        this.multiThreaded = multiThreaded;
    }

    public int getMaxBounces() {
        return MAX_BOUNCES;
    }

    public double getScreenProjectionSize() {
        return SCREEN_PROJECTION_SIZE;
    }
}


