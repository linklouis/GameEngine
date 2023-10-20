import gameengine.threed.graphics.raytraceing.LightRay;
import gameengine.twod.graphics.GraphicsObject2D;
import gameengine.twod.graphics.Visual2D;
import gameengine.drivers.GameDriver2D;
import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
import gameengine.threed.prebuilt.objectmovement.collisions.Collider3D;
import gameengine.utilities.ArgumentContext;
import gameengine.vectormath.Vector3D;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Camera extends GameObject {
    private Vector3D direction;
    private final GameDriver2D gameDriver2D;

    public Camera(double x, double y, double z, Vector3D direction, GameDriver2D gameDriver2D) {
        super(new InPlane3D(), new Visual2D());
        get(InPlane3D.class).instantiate(this, x, y, z);
        get(Visual2D.class).instantiate(this, new Camera2DGraphics());

        this.direction = direction.unitVector();
        this.gameDriver2D = gameDriver2D;
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane3D.class);
            }
        };
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

    public class Camera2DGraphics extends GraphicsObject2D {
        private double scale = 1;
        private int raysPerPixel = 3;
        private boolean alreadyPainted = false;


        private void printXViewAngle() {
            Vector3D localPixelLocation1 = new Vector3D(
                    0 - gameDriver2D.getGraphicsDriver().WIDTH / 2.0,
                    1 - gameDriver2D.getGraphicsDriver().HEIGHT / 2.0,
                    100).scalarDivide(100);
            Vector3D truePixelLocation1 = getLocation().add(getDirection().scalarMultiply(localPixelLocation1.getZ()))
                    .add(getDirection().crossProduct(new Vector3D(0, 0, 1)).scalarMultiply(localPixelLocation1.getX()))
                    .add(getDirection().crossProduct(new Vector3D(0, 1, 0)).scalarMultiply(localPixelLocation1.getY()));
            Vector3D localPixelLocation2 = new Vector3D(
                    gameDriver2D.getGraphicsDriver().WIDTH - gameDriver2D.getGraphicsDriver().WIDTH / 2.0,
                    1 - gameDriver2D.getGraphicsDriver().HEIGHT / 2.0,
                    100).scalarDivide(100);
            Vector3D truePixelLocation2 = getLocation().add(getDirection().scalarMultiply(localPixelLocation2.getZ()))
                    .add(getDirection().crossProduct(new Vector3D(0, 0, 1)).scalarMultiply(localPixelLocation2.getX()))
                    .add(getDirection().crossProduct(new Vector3D(0, 1, 0)).scalarMultiply(localPixelLocation2.getY()));
            double cosineTheta = truePixelLocation1.dotProduct(truePixelLocation2) / (truePixelLocation1.magnitude() * truePixelLocation2.magnitude());

            // Calculate the angle in radians
            double angleInRadians = Math.acos(cosineTheta);

            // Convert the angle from radians to degrees
            double angleInDegrees = Math.toDegrees(angleInRadians);

            System.out.println(angleInDegrees);
        }


        @Override
        public void paint(GraphicsContext gc) {
            Collider3D<?>[] objects = getValidColliders();

            if (!alreadyPainted) {
                paintThreaded(gc, objects);
                System.out.println("Rays traced");
                alreadyPainted = true;
            } else {
                System.out.println("Done drawing");
            }
        }

        private void paintUnthreaded(GraphicsContext gc, Collider3D<?>[] objects) {
            for (double x = 0; x < gameDriver2D.getGraphicsDriver().WIDTH / scale; x += 1 / scale) {
                for (double y = 0; y < gameDriver2D.getGraphicsDriver().HEIGHT / scale; y += 1 / scale) {
                    paintPixelAtUnthreaded(x, y, gc, objects);
                }
            }
        }

        private void paintPixelAtUnthreaded(double x, double y, GraphicsContext gc, Collider3D<?>[] objects) {
            Vector3D average = new Vector3D(0);
            Vector3D localPixelLocation = new Vector3D(
                    x - gameDriver2D.getGraphicsDriver().WIDTH / (2.0 * scale),
                    y - gameDriver2D.getGraphicsDriver().HEIGHT / (2.0 * scale),
                    100).scalarDivide(100);
//            localPixelLocation = new Vector3D(
//                    localPixelLocation.getX(),
//                    localPixelLocation.getY(),
//                    1);

            // Transform the local pixel location to global coordinates
            Vector3D truePixelLocation = getLocation().add(getDirection().scalarMultiply(localPixelLocation.getZ()))
                    .add(getDirection().crossProduct(new Vector3D(0, 0, 1)).scalarMultiply(localPixelLocation.getX()))
                    .add(getDirection().crossProduct(new Vector3D(0, 1, 0)).scalarMultiply(localPixelLocation.getY()));

//                        truePixelLocation = truePixelLocation.add(Vector3D.random(-0.1, 0.1));'

            for (int i = 0; i < raysPerPixel; i++) {
                LightRay ray = new LightRay(truePixelLocation, truePixelLocation, 0.1, 30, 1, objects);
                average = average.add(new Vector3D(ray.getColorFromBounces()));
            }

            average = average.scalarDivide(raysPerPixel);
            Color pixelColor = average.toColor();
            gc.setFill(pixelColor);
            gc.setStroke(pixelColor);
            gc.fillRect(x * scale, y * scale, 1, 1);
        }

        private void paintThreaded(GraphicsContext gc, Collider3D<?>[] objects) {
            // Create a thread pool with a fixed number of threads
            ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); // You can adjust the thread count as needed

            // Split the canvas into smaller tasks for multithreading
            for (double x = 0; x < gameDriver2D.getGraphicsDriver().WIDTH / scale; x += 1 / scale) {
                for (double y = 0; y < gameDriver2D.getGraphicsDriver().HEIGHT / scale; y += 1 / scale) {
                    final double finalX = x;
                    final double finalY = y;
                    threadPool.submit(() -> {
                        paintPixelAtThreaded(finalX, finalY, gc, objects);
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

        private void paintPixelAtThreaded(double x, double y, GraphicsContext gc, Collider3D<?>[] objects) {
            Vector3D average = new Vector3D(0);
            double scaleFactor = 500;
            Vector3D localPixelLocation = new Vector3D(
                    x - gameDriver2D.getGraphicsDriver().WIDTH / (2.0 * scale),
                    y - gameDriver2D.getGraphicsDriver().HEIGHT / (2.0 * scale),
                    scaleFactor).unitVector();

            // Transform the local pixel location to global coordinates
//            Vector3D truePixelLocation = getLocation().add(getDirection().scalarMultiply(localPixelLocation.getZ()))
//                    .add(getDirection().crossProduct(new Vector3D(0, 0, 1)).scalarMultiply(localPixelLocation.getX()))
//                    .add(getDirection().crossProduct(new Vector3D(0, 1, 0)).scalarMultiply(localPixelLocation.getY()));

//            Vector3D truePixelDirectionFromC = // a vector starting at getLocation() [C's location], and ending at the position of the point in the global coordinate system.

            // Calculate the truePixelLocation in global coordinates
            Vector3D truePixelLocation = getLocation()
                    .add(getDirection().scalarMultiply(localPixelLocation.getZ()))
                    .add(getDirection().crossProduct(new Vector3D(0, 1, 0)).scalarMultiply(localPixelLocation.getY()))
                    .add(getDirection().crossProduct(getDirection().crossProduct(new Vector3D(0, 1, 0))).scalarMultiply(localPixelLocation.getX()));

            // Calculate truePixelDirectionFromC
            Vector3D pixelDirectionFromCam = truePixelLocation.subtract(getLocation());

            for (int i = 0; i < raysPerPixel; i++) {
                LightRay ray = new LightRay(getLocation(), pixelDirectionFromCam, 0.1, 100, 5, objects);
                average = average.add(new Vector3D(ray.getColorFromBounces()));
            }

            average = average.scalarDivide(raysPerPixel);
            Color pixelColor = average.toColor();
            Platform.runLater(() -> {
                gc.setFill(pixelColor);
//                gc.setStroke(pixelColor);
                gc.fillRect(x * scale, y * scale, 1, 1);
//                System.out.println("paintingPixel");
            });
        }

        public static void runAllRunnablesConcurrently(List<Runnable> runnables) {
            List<Thread> threads = new ArrayList<>();

            for (Runnable runnable : runnables) {
                Thread thread = new Thread(runnable);
                threads.add(thread);
                thread.start();
            }

            for (Thread thread : threads) {
                try {
                    thread.join(); // Wait for all threads to complete
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public static void runAllRunnablesConcurrently(Runnable[] runnables) {
            Thread[] threads = new Thread[runnables.length];

            for (int i = 0; i < runnables.length; i++) {
                threads[i] = new Thread(runnables[i]);
                threads[i].start();
            }

            for (Thread thread : threads) {
                try {
                    thread.join(); // Wait for all threads to complete
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private Collider3D<?>[] getValidColliders() {
            return gameDriver2D.getObjects().stream()
//                    .filter(object -> object != this)
                    .filter(object -> object.containsModifier(Collider3D.class))
                    .map(object -> object.get(Collider3D.class))
                    .toArray(Collider3D[]::new);
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
}
