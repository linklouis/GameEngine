package gameengine.threed.graphics.raytraceing;

import gameengine.threed.graphics.Camera;
import gameengine.threed.graphics.Visual3D;
import gameengine.threed.prebuilt.objectmovement.collisions.Collider3D;
import gameengine.vectormath.Vector3D;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RayTracedCamera extends Camera {
    /**
     * Size of the projected screen
     */
    private final double screenProjectionSize = 50000;
    /**
     * The distance from the camera the screen is being simulated to be in
     * order to find the direction from the camera to each pixel
     */
    private final double screenDistance = 0.7;
    /**
     * The number of light rays to average the color of to find a pixel's color
     */
    private int raysPerPixel;
    private boolean multiThreaded;
    private double marchDistance;
    private int maxBounces;
    private double maxDistance;
    public long renderTime;


    /*
     * Construction:
     */

    public RayTracedCamera(double x, double y, double z, Vector3D direction,
                           int imageWidth, int imageHeight,
                           double marchDistance, int maxBounces,
                           double maxDistance, int raysPerPixel, boolean multiThreaded, double fieldOfViewDegrees) {
        super(x, y, z, direction, imageWidth, imageHeight, fieldOfViewDegrees);
        this.marchDistance = marchDistance;
        this.maxBounces = maxBounces;
        this.maxDistance = maxDistance;
        this.raysPerPixel = raysPerPixel;
        this.multiThreaded = multiThreaded;
    }

    public RayTracedCamera(double x, double y, double z, Vector3D direction,
                           int imageWidth, int imageHeight,
                           double marchDistance, int maxBounces,
                           double maxDistance, int raysPerPixel,
                           boolean multiThreaded) {
        super(x, y, z, direction, imageWidth, imageHeight, 60.0);
        this.marchDistance = marchDistance;
        this.maxBounces = maxBounces;
        this.maxDistance = maxDistance;
        this.raysPerPixel = raysPerPixel;
        this.multiThreaded = multiThreaded;
    }

//    @Override
//    protected void setFieldOfView(double fieldOfViewDegrees) {
//        super.setFieldOfView(fieldOfViewDegrees);
//
//        double halfFieldOfViewRadians = Math.toRadians(fieldOfViewDegrees / 2.0);
//        double aspectRatio = getWidth() / getHeight();
//        double halfImageWidth = getImage().getWidth() / 2.0;
//        double halfImageHeight = getImage().getHeight() / 2.0;
//
//        // Calculate the correct screen distance to achieve the desired FOV
//        screenDistance = halfImageWidth / Math.tan(halfFieldOfViewRadians);
//        // Adjust for the aspect ratio
//        screenDistance /= aspectRatio;
//    }

    @Override
    protected void setFieldOfView(double fieldOfViewDegrees) {
        super.setFieldOfView(fieldOfViewDegrees);

//        double halfFieldOfViewRadians = Math.toRadians(fieldOfViewDegrees / 2.0);
//
//        // Calculate the correct screen distance to achieve the desired FOV
//        screenDistance = 1;//screenProjectionSize / (2.0 * Math.tan(halfFieldOfViewRadians));
    }




    /*
     * Functionality:
     */

    @Override
    public void renderImage(Visual3D[] renderableObjects) {
        Collider3D<?>[] objects = getValidColliders(renderableObjects);

        long startTime = System.nanoTime();

        if (multiThreaded) {
            renderThreaded(getImage(), objects);
        } else {
            renderUnthreaded(getImage(), objects);
        }


        long endTime = System.nanoTime();
        renderTime = (endTime - startTime) / 1_000_000; // Convert nanoseconds to milliseconds
        System.out.println("Execution Time: " + renderTime + " milliseconds");


        System.out.println("rendered");
//        try {
//            saveToFile("RayTraced_0_1_(" + (int) getWidth() + "," + (int) getHeight() + ")_"
//                    + "rays" + raysPerPixel + "_bounces" + maxBounces + ".png");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void renderUnthreaded(WritableImage image, Collider3D<?>[] objects) {
        for (int pixelX = 0; pixelX < getWidth(); pixelX++) {
            for (int pixelY = 0; pixelY < getHeight(); pixelY++) {
                renderPixelAt(pixelX, pixelY, image, objects);
            }
        }
    }

//    public static int numRendered = 0;
    private void renderThreaded(WritableImage image, Collider3D<?>[] objects) {
//        numRendered = 0;
        int numThreads = Runtime.getRuntime().availableProcessors();
//        System.out.println(numThreads);
        ExecutorService threadPool = Executors.newFixedThreadPool(numThreads - 1);

        // Split the canvas into smaller tasks for multithreading
        for (int pixelX = 0; pixelX < getWidth(); pixelX++) {
            for (int pixelY = 0; pixelY < getHeight(); pixelY++) {
                int finalX = pixelX;
                int finalY = pixelY;
                threadPool.submit(() -> {
                    renderPixelAt(finalX, finalY, image, objects);
                });
            }
        }

//        System.out.println("start");
        // Make sure all threads are completed before moving forward
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        System.out.println("end");

    }

    private void renderPixelAt(int x, int y, WritableImage image,
                               Collider3D<?>[] objects) {
        Vector3D rayPath = getRayPath(x, y);

        Vector3D average = Vector3D.empty();
        for (int i = 0; i < raysPerPixel; i++) {
            LightRay ray = new LightRay(
                    getLocation(), rayPath, marchDistance, maxDistance, maxBounces, objects);
            average = average.add(LightRay.vectorFromColor(ray.getColorFromBounces()));
        }
        average = average.scalarDivide(raysPerPixel);

        Color pixelColor = LightRay.colorFromVector(average);
        image.getPixelWriter().setColor(x, y, pixelColor);
//        if (numRendered < getWidth() * getHeight()) {
//            if (numRendered % 1 == 0) {
//                System.out.println(numRendered + " / " + getWidth() * getHeight());
//            }
//            numRendered++;
//        } else {
//            System.out.println("Done");
//        }
    }

    private Vector3D localPixelLocation(double x, double y) {
        double halfWidth = getWidth() / 2.0;
        double halfHeight = getHeight() / 2.0;

        double aspectRatio = getWidth() / getHeight();
        double scaleX = Math.tan(Math.toRadians(getFieldOfViewDegrees() / 2.0));
        double scaleY = scaleX / aspectRatio;

        double localX = (x - halfWidth) / halfWidth * scaleX;
        double localY = (halfHeight - y) / halfHeight * scaleY;

        return new Vector3D(localX, localY, screenDistance);
    }

    private Vector3D getRayPath(double x, double y) {
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

//    public double getPixelsPerUnit() {
//        return pixelsPerUnit;
//    }

    public double getScreenDistance() {
        return screenDistance;
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

    public double getMarchDistance() {
        return marchDistance;
    }

    public int getMaxBounces() {
        return maxBounces;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public double getScreenProjectionSize() {
        return screenProjectionSize;
    }
}


