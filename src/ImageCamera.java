import gameengine.graphics.GraphicsObject;
import gameengine.graphics.Visual;
import gameengine.objects.GameDriver;
import gameengine.objects.GameObject;
import gameengine.objects.Modifier;
import gameengine.prebuilt.InPlane3D;
import gameengine.prebuilt.objectmovement.collisions.Collider3D;
import gameengine.utilities.ArgumentContext;
import gameengine.vectormath.Vector3D;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImageCamera extends GameObject {
    private Vector3D direction;
    private final GameDriver gameDriver;
    private WritableImage image;
    /**
     * The factor by which the projected screen in scaled down by
     */
    private double pixelsPerUnit = 100;
    /**
     * The distance from the camera the screen is being simulated to be in
     * order to find the direction from the camera to each pixel
     */
    private double screenDistance = 3;
    /**
     * The number of light rays to average the color of to find a pixel's color
     */
    private int raysPerPixel = 3;

    /**
     * The number of degrees in the horizontal field of view.
     */
    private double fieldOfViewDegrees;
    private boolean updateNeeded = true;


    /*
     * Construction:
     */

    public ImageCamera(double x, double y, double z, Vector3D direction, int imageWidth, int imageHeight, double fieldOfViewDegrees, GameDriver gameDriver) {
        super(new InPlane3D(), new Visual());
        get(InPlane3D.class).instantiate(this, x, y, z);
        get(Visual.class).instantiate(this, new CameraGraphics());

        this.direction = direction.unitVector();
        this.gameDriver = gameDriver;
        image = new WritableImage(imageWidth, imageHeight);
        setFieldOfViewDegrees(fieldOfViewDegrees);
    }

    public ImageCamera(double x, double y, double z, Vector3D direction, int imageWidth, int imageHeight, GameDriver gameDriver) {
        super(new InPlane3D(), new Visual());
        get(InPlane3D.class).instantiate(this, x, y, z);
        get(Visual.class).instantiate(this, new CameraGraphics());

        this.direction = direction.unitVector();
        this.gameDriver = gameDriver;
        image = new WritableImage(imageWidth, imageHeight);
        setFieldOfViewDegrees(60);
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane3D.class);
            }
        };
    }

    public void setFieldOfView(double fieldOfViewDegrees) {
        this.fieldOfViewDegrees = fieldOfViewDegrees;
        // Calculate the new `pixelsPerMeter` and `screenDistance` based on the field of view.
        double halfFieldOfViewRadians = Math.toRadians(fieldOfViewDegrees / 2.0);
        double halfImageWidth = image.getWidth() / 2.0;
        pixelsPerUnit = halfImageWidth / Math.tan(halfFieldOfViewRadians);
        screenDistance = halfImageWidth / Math.tan(halfFieldOfViewRadians);
    }


    /*
     * Functionality:
     */

    public void renderImage(boolean multiThreaded) {
        Collider3D<?>[] objects = getValidColliders();

        if (multiThreaded) {
            renderThreaded(image, objects);
        } else {
            renderUnthreaded(image, objects);
        }

        System.out.println("rendered");
    }

    private void renderUnthreaded(WritableImage image, Collider3D<?>[] objects) {
        for (double x = 0; x < gameDriver.getGraphicsDriver().WIDTH / pixelsPerUnit; x += 1 / pixelsPerUnit) {
            for (double y = 0; y < gameDriver.getGraphicsDriver().HEIGHT / pixelsPerUnit; y += 1 / pixelsPerUnit) {
                renderPixelAt(x, y, image, objects);
            }
        }
    }

    private void renderThreaded(WritableImage image, Collider3D<?>[] objects) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);

        // Split the canvas into smaller tasks for multithreading
        for (double x = 0; x < image.getWidth() / pixelsPerUnit; x += 1 / pixelsPerUnit) {
            for (double y = 0; y < image.getHeight() / pixelsPerUnit; y += 1 / pixelsPerUnit) {
                final double finalX = x;
                final double finalY = y;
                threadPool.submit(() -> {
                    renderPixelAt(finalX, finalY, image, objects);
                });
            }
        }

        // Make sure all threads are completed before moving forward
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // Handle the exception
        }
    }

    private void renderPixelAt(double x, double y, WritableImage image,
                               Collider3D<?>[] objects) {
        Vector3D rayPath = getRayPath(x, y);

        Vector3D average = Vector3D.empty();
        for (int i = 0; i < raysPerPixel; i++) {
            LightRay ray = new LightRay(
                    getLocation(), rayPath, 0.1, 30, 20, objects);
            average = average.add(LightRay.vectorFromColor(ray.getColorFromBounces()));
        }
        average = average.scalarDivide(raysPerPixel);

        Color pixelColor = LightRay.colorFromVector(average);
        image.getPixelWriter().setColor((int) Math.round(x * pixelsPerUnit), (int) Math.round(y * pixelsPerUnit), pixelColor);
    }

    private Vector3D localPixelLocation(double x, double y) {
        return new Vector3D(
                x - gameDriver.getGraphicsDriver().WIDTH / (2.0 * pixelsPerUnit),
                y - gameDriver.getGraphicsDriver().HEIGHT / (2.0 * pixelsPerUnit),
                screenDistance).unitVector();
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

    private Collider3D<?>[] getValidColliders() {
        return gameDriver.getObjects().stream()
                .filter(object -> object != this)
                .filter(object -> object.containsModifier(Collider3D.class))
                .map(object -> object.get(Collider3D.class))
                .toArray(Collider3D[]::new);
    }

    public void requestUpdate() {
        updateNeeded = true;
    }

    public class CameraGraphics extends GraphicsObject {
        @Override
        public void paint(GraphicsContext gc) {
            if (updateNeeded) {
                renderImage(true);
            }

            ObservableList<Node> rootChildren = gameDriver.getGraphicsDriver().getRoot().getChildren();
            rootChildren.setAll(new ImageView(image));

//            if (rootChildren.size() == 1 && rootChildren.get(0) instanceof ImageView) {
//                ((ImageView) rootChildren.get(0)).setImage(image);
//            } else {
//                rootChildren.setAll(new ImageView(image));
//            }
        }

        @Override
        public List<Class<? extends Modifier>> getDependencies() {
            return null;
        }

        @Override
        public ArgumentContext[] getArgumentContexts() {
            return new ArgumentContext[0];
        }
    }


    /*
     * Utilities:
     */

    public boolean needsUpdate() {
        return updateNeeded;
    }

    public Vector3D getDirection() {
        return direction;
    }

    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

    public Vector3D getLocation() {
        return get(InPlane3D.class).getLocation();
    }

    public double getFieldOfViewDegrees() {
        return fieldOfViewDegrees;
    }

    public double getFieldOfViewRadians() {
        return Math.toRadians(fieldOfViewDegrees);
    }

    public void setFieldOfViewDegrees(double fieldOfViewDegrees) {
        this.fieldOfViewDegrees = fieldOfViewDegrees;
    }

    public void setFieldOfViewRadians(double fieldOfViewRadians) {
        this.fieldOfViewDegrees = Math.toDegrees(fieldOfViewRadians);
    }

    public double getPixelsPerUnit() {
        return pixelsPerUnit;
    }

    public double getScreenDistance() {
        return screenDistance;
    }

    public int getRaysPerPixel() {
        return raysPerPixel;
    }

    public void setRaysPerPixel(int raysPerPixel) {
        this.raysPerPixel = raysPerPixel;
    }

    public GameDriver getGameDriver() {
        return gameDriver;
    }

    public WritableImage getImage() {
        return image;
    }
}
