import gameengine.threed.GameDriver3D;
import gameengine.threed.graphics.GraphicsDriver3D;
import gameengine.threed.graphics.raytraceing.RayTracedCamera;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.threed.graphics.raytraceing.objectgraphics.SphereGraphics;
import gameengine.threed.graphics.raytraceing.objectgraphics.TriGraphics;
import gameengine.threed.graphics.raytraceing.textures.BaseTexture;
import gameengine.threed.graphics.raytraceing.textures.RayTracingTexture;
import gameengine.threed.graphics.raytraceing.textures.ReflectingTexture;
import gameengine.threed.graphics.raytraceing.textures.SubsurfaceTexture;
import gameengine.threed.prebuilt.gameobjects.*;
import gameengine.timeformatting.TimeFormatter;
import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

import java.util.Collection;
import java.util.Random;
import java.util.function.Function;

public class RayTracing extends GameDriver3D {
    private final static int SIZE = 400;
    private int renders = 0;
    private long avTime = 0;
    private final int NUM_TO_TEST = 100;
    private RayTracedCamera mainCam;


    /*
     * Construction:
     */

    public RayTracing() {
        super("LightRay Tracing", new GraphicsDriver3D(SIZE, SIZE,
                new RayTracedCamera(-2, -10, -10, new Vector3D(0.8, 3, 1.8),
                        new Vector2D(/*2000, 2000*/700, 700/*1280, 720*//*1920.0, 1080.0*/),
                        10, 10, true, 70/*160*/)));
    }

    public static void main(String[] args) {
        launch(RayTracing.class);
    }

    @Override
    public void initialize() {
//        System.out.println(java.time.LocalDateTime.now());
//        Vector3D.FastGaussian rand = new Vector3D.FastGaussian(1L, 0, 1/*new Vector3D(2324, 545, 1)*/);
//        for (int i = 0; i < 300; i++) {
//            System.out.print(
////                    SubsurfaceTexture.generateSkewedValue(0.7, 0, 90)
//                    rand.nextGaussian()
//                            + "\t");
//        }
//        System.exit(0);
        mainCam = (RayTracedCamera) getGraphicsDriver().getCamera();
//        mainCam.setDirection(Vector3D.I);
//        mainCam.setLocation(new Vector3D(-3, 0, 0));
////        new RectPlane(new Vector3D(0, -1, -1), new Vector3D(0, 2, 2), new BaseTexture(Color.RED, true)).initiate(this);
//        newObject(new Quad(new Vector3D(0, -1, -1), new Vector3D(0, 2, 2), new BaseTexture(Color.AQUA, true)));
//        testSphere();

        setupScene1();
    }


    /*
     * Scene Setup:
     */

    private void testSphere() {
//        newObject(new Sphere(0,0,0, 2, new BaseTexture(Color.RED, false)));
//        newObject(new Sphere(new Vector3D(-5, -5, -5), /*new Vector3D(-10, -10, 10)*/5, new BaseTexture(Color.WHITE, true)));

//        mainCam.setLocation(spiral(Math.TAU, angle).scalarMultiply(5));//new Vector3D(Math.cos(angle), Math.sin(angle), Math.cos(angle) * Math.sin(angle)).scalarMultiply(10));
//        mainCam.setDirection(mainCam.getLocation().atMagnitude(-1));
//        mainCam.setLocation(new Vector3D(0, -10, 10));
        newObject(new Sphere(0,0,0,2, new BaseTexture(Color.WHITE, true)));
        mainCam.setLocation(new Vector3D(0));
        mainCam.setDirection(new Vector3D(0.1, 0, 0));//mainCam.getLocation().atMagnitude(-1)));
        for (double angle = 0; angle < Math.TAU * Math.TAU * 2; angle += 0.07) {
            newObject(new Sphere(spiral(Math.TAU, angle).scalarMultiply(5), 0.2, new BaseTexture(spiral(Math.TAU, angle).toColor(), true)));
//            newObject(new Sphere(spiral(Math.TAU, angle).scalarMultiply(5), 0.2, new BaseTexture(spiral(Math.TAU, angle).toColor(), false)));
        }
    }

    private void setupScene1() {
//        TextureHelper.setMinimumReflectingAngle(45);
//        TextureHelper.setRandomness(0.01);
        TextureHelper.setReflectivity(0.7);
        TextureHelper.setAbsorption(0.2);
        mainCam.setLocation(mainCam.getLocation().add(mainCam.getDirection().scalarMultiply(-2.5)));

        new QuadRectangle(-1, -2, -3, new Vector3D(2,2, 2),
                TextureHelper.newReflecting(Color.AZURE)).initiate(this);
//        newObject(new Sphere(0, -1, -2, new Vector3D(1,1, 1).magnitude(), new ReflectingTexture(Color.AZURE, false, reflectivity)));

        newObject(new Sphere(3, -1, -2, 2,
                TextureHelper.newReflecting(Color.AQUA)));
        newObject(new Sphere(-1, 2, -3, 3,
                TextureHelper.newReflecting(Color.GREEN)));
        newObject(new Sphere(0, 0, 100, 100,
                TextureHelper.newReflecting(Color.BROWN)));

        newObject(new Sphere(-10, 2, -10, 7, Color.WHITE, true));
    }

    private void setupScene2() {
        mainCam.setDirection(new Vector3D(1, 0, 0));
        mainCam.setLocation(new Vector3D(-10, 0, 0));

        // Center Sphere
        newObject(new Sphere(3, 0, 0, 2,
                new ReflectingTexture(Color.WHITE, false, 0.9)));

        setupBoundingBox(40);

        // Walls:
        TextureHelper.setReflectivity(0.8);
        double wallSize = 10;
        double hWallSize = wallSize / 2;
        // Back
        new RectPlane(hWallSize, -hWallSize, -hWallSize,
                new Vector3D(0, wallSize, wallSize),
                TextureHelper.newReflecting(Color.RED)).initiate(this);
        // Left
        new RectPlane(-hWallSize, -hWallSize, -hWallSize,
                new Vector3D(wallSize, 0, wallSize),
                TextureHelper.newReflecting(Color.BLUE)).initiate(this);
        // Right
        new RectPlane(-hWallSize, hWallSize, -hWallSize,
                new Vector3D(wallSize, 0, wallSize),
                TextureHelper.newReflecting(Color.BLUE)).initiate(this);
        // Bottom
        new RectPlane(-hWallSize, -hWallSize, -hWallSize,
                new Vector3D(wallSize, wallSize, 0),
                TextureHelper.newReflecting(Color.GREEN)).initiate(this);
        // Top
        new RectPlane(-hWallSize, -hWallSize, hWallSize,
                new Vector3D(wallSize, wallSize, 0),
                TextureHelper.newReflecting(Color.GREEN)).initiate(this);

        // Backlight
        newObject(new Sphere(0, 0, 15, 7, Color.WHITE, true));
    }

    private void setupGrassScene() {
        // Setup Camera
        mainCam.setDirection(new Vector3D(1, 0.0, -0.1));
        mainCam.setLocation(new Vector3D(0, 0, 5));

        // Setup Scene:
        TextureHelper.setReflectivity(0);
        double size = 200;
        double pathWidth = 0.2;

        // Sky
        setupBoundingBox(size * 2, TextureHelper.newReflecting(Color.BLUE));

        // Grass
        new RectPlane(0, -size, 0,
                new Vector3D(size, size - pathWidth/2, 0),
                TextureHelper.newReflecting(Color.GREEN)).initiate(this);
        new RectPlane(0, size, 0,
                new Vector3D(size, -size + pathWidth/2, 0),
                TextureHelper.newReflecting(Color.GREEN)).initiate(this);

        // Path
        new RectPlane(0, -pathWidth/2, 0,
                new Vector3D(size, pathWidth, 0),
                TextureHelper.newReflecting(Color.SANDYBROWN)).initiate(this);

        // Sun
        newObject(new Sphere(10, 0, 80, 30, Color.YELLOW, true));
    }

    private void setupOrnamentsScene() {
        mainCam.setDirection(new Vector3D(0, 0, -1));
        mainCam.setLocation(new Vector3D(0, 0, 10));

        TextureHelper.setReflectivity(0.5);

        // Ornaments
        newObject(new Sphere(-2, -1, 0, 2,
                TextureHelper.newReflecting(Color.RED)));
        newObject(new Sphere(2, -1, 0, 2,
                TextureHelper.newReflecting(Color.GREEN)));
        newObject(new Sphere(0, Math.PI/1.262, 0, 2,
                TextureHelper.newReflecting(Color.BLUE)));

        // Ground
        newObject(new Sphere(-1, 2, -22, 20,
                TextureHelper.newReflecting(Color.BROWN)));

        // Light
        newObject(new Sphere(0, 0, 18, 7,
                new ReflectingTexture(Color.WHITE, true, 0)));
    }

    private void setupScene4() {
        double reflectivity = 0;//.5;

        mainCam.setDirection(new Vector3D(-0.5, -0.5, -1));
        mainCam.setLocation(new Vector3D(5, 5, 10));

        newObject(new Sphere(0, 0, 18, 7, new ReflectingTexture(Color.WHITE, true, 0)));

        new TriRectangle(0, 0, 0, new Vector3D(2, 3, 4), new ReflectingTexture(Color.RED, false, reflectivity)).initiate(this);

//        for (RayTraceable collider : getGraphicsDriver().getVisualObjects()) {
//            System.out.println(collider.getFromParent(Collider3D.class).getAppearance());
//        }

//        newObject(new Sphere(0, 0, 0, 1, new ReflectingTexture(Color.WHITE, true, 0)));

//        newObject(new Sphere(0, 0, -13, -10, new ReflectingTexture(Color.BROWN, false, 0)));
//        double dist = 10;
//        Vector3D p1 = new Vector3D(3, 0, 0)/*new Vector3D(Math.random() * dist - dist/2, Math.random() * dist - dist/2, 0)*/;
//        Vector3D p2 = new Vector3D(3, 2, 0)/*new Vector3D(Math.random() * dist - dist/2, Math.random() * dist - dist/2, 0)*/;
//        Vector3D p3 = new Vector3D(3, 2, 2)/*new Vector3D(Math.random() * dist - dist/2, Math.random() * dist - dist/2, 0)*/;
//
//        newObject(new Tri(p1, p2, p3,
//                new ReflectingTexture(Color.RED, true, reflectivity)));
//
////        newObject(new Tri(
////                new Vector3D(2, 0, 0),
////                new Vector3D(2, 0, 1),
////                new Vector3D(2, 1, 1), new ReflectingTexture(Color.WHITE, true, 0)));
//
////        newObject(new Sphere(0, 0, 0, 10, new ReflectingTexture(Color.BROWN, true, 0)));
//
//
//        double r = 0.2;
//        newObject(new Sphere(p1, r, new ReflectingTexture(Color.RED, true, 0)));
//        newObject(new Sphere(p2, r, new ReflectingTexture(Color.GREEN, true, 0)));
//        newObject(new Sphere(p3, r, new ReflectingTexture(Color.BLUE, true, 0)));
    }

    private void scene5RandomRects() {
        mainCam.setDirection(new Vector3D(-0.5, -0.5, -1));
        mainCam.setLocation(new Vector3D(5, 5, 10));

        newObject(new Sphere(0, 0, 20, 10,
                new ReflectingTexture(Color.WHITE, true, 0)));

        setupBoundingBox(5);

        Function<Color, RayTracingTexture> textureSupplier =
                TextureHelper.reflectingSupplier(0.5);

        double range = 10;
        setupRandRect(range, textureSupplier);
        setupRandRect(range, textureSupplier);
        setupRandRect(range, textureSupplier);
        setupRandRect(range, textureSupplier);
        setupRandRect(range, textureSupplier);
        setupRandRect(range, textureSupplier);
        setupRandRect(range, textureSupplier);
        setupRandRect(range, textureSupplier);
        setupRandRect(range, textureSupplier);
        setupRandRect(range, textureSupplier);
        setupRandRect(range, textureSupplier);
        setupRandRect(range, textureSupplier);
    }

    private void setupScene6() {
        TextureHelper.setReflectivity(0.3);
        newObject(new Sphere(-10, 2, -10, 7, Color.WHITE, true));

        double range = 7;
        int numSpheres = 10;
//        Random rand = new Random();
        for (int i = 0; i < numSpheres; i++) {
//            newObject(new Sphere(
//                    rand.nextDouble(-range, range),
//                    rand.nextDouble(-range, range),
//                    rand.nextDouble(-range, range),
//                    rand.nextDouble(0.3, range / 6),
//                    TextureHelper.newReflecting(Color.AQUA)));
            setupRandSphere(range, TextureHelper.reflectingSupplier());
        }
    }

    Sphere light = new Sphere(20, 0, 0, 3, new BaseTexture(Color.WHITE, true));
    private void setupSubsurfaceScene() {
        mainCam.setDirection(new Vector3D(1, 0, -0.05));
        mainCam.setLocation(new Vector3D(0, 0, 0.3));

        TextureHelper.setMinimumReflectingAngle(80);
        TextureHelper.setRandomness(20);
        TextureHelper.setReflectivity(0.1);

        new RectPlane(
                0, -5, 0, new Vector3D(5, 10, 0),
                TextureHelper.newSubsurface(Color.BLUE)
        ).initiate(this);

        newObject(light);
    }


    /*
     * Object and Scene Initialization Utils:
     */

    private void setupBoundingBox(double wallSize) {
        setupBoundingBox(wallSize,
                new ReflectingTexture(Color.GRAY, false, 0));
    }

    private void setupBoundingBox(double wallSize, RayTracingTexture texture) {
        double hWallSize = wallSize / 2;
        new RectPlane(hWallSize, -hWallSize, -hWallSize, new Vector3D(0, wallSize, wallSize), texture).initiate(this);
        new RectPlane(-hWallSize, -hWallSize, -hWallSize, new Vector3D(0, wallSize, wallSize), texture).initiate(this);
        new RectPlane(-hWallSize, -hWallSize, -hWallSize, new Vector3D(wallSize, 0, wallSize), texture).initiate(this);
        new RectPlane(-hWallSize, hWallSize, -hWallSize, new Vector3D(wallSize, 0, wallSize), texture).initiate(this);
        new RectPlane(-hWallSize, -hWallSize, -hWallSize, new Vector3D(wallSize, wallSize, 0), texture).initiate(this);
        new RectPlane(-hWallSize, -hWallSize, hWallSize, new Vector3D(wallSize, wallSize, 0), texture).initiate(this);
    }

    private void initDebugVisuals() {
        newObject(new Sphere(0,0,0,10,Color.BLANCHEDALMOND, true));

        double axisMarkerDistance = 2;

        newObject(new Sphere(axisMarkerDistance, 0, 0, 1,
                new Color(1, 0, 0, 1), true));
        newObject(new Sphere(-axisMarkerDistance, 0, 0, 1,
                new Color(1, 0.5, 0.5, 1), true));

        newObject(new Sphere(0, axisMarkerDistance, 0, 1,
                new Color(0, 1, 0, 1), true));
        newObject(new Sphere(0, -axisMarkerDistance, 0, 1,
                new Color(0.5, 1, 0.5, 1), true));

        newObject(new Sphere(0, 0, axisMarkerDistance, 1,
                new Color(0, 0, 1, 1), true));
        newObject(new Sphere(0, 0, -axisMarkerDistance, 1,
                new Color(0.5, 0.5, 1, 1), true));
    }

    private void evenSpheresAndTris(double reflectivity) {
        int numTris = 0;
        int numSpheres = 0;

        for (RayTraceable visual : getGraphicsDriver().getVisualObjects()) {
            if (visual instanceof TriGraphics) {
                numTris++;
            } else if (visual instanceof SphereGraphics) {
                numSpheres++;
            }
        }

        double range = 5;
        while (numTris != numSpheres) {
            if (numSpheres > numTris) {
                setupRandSphere(range,
                        TextureHelper.reflectingSupplier(reflectivity));
                numSpheres++;
            } else {
                setupRandTri(range,
                        TextureHelper.reflectingSupplier(reflectivity));
                numTris++;
            }
        }
    }

    private void setupRandRect(final double range,
                               final Function<Color, RayTracingTexture>
                                       textureSupplier) {
        new TriRectangle(
                randomInRange(range), randomInRange(range), randomInRange(range),
                Vector3D.random(-range, range),
                textureSupplier.apply(Vector3D.random(0, 1).toColor())
        ).initiate(this);
    }

    private void setupRandTri(final double range,
                              final Function<Color, RayTracingTexture>
                                      textureSupplier) {
        newObject(new Tri(
                Vector3D.random(-range, range),
                Vector3D.random(-range, range),
                Vector3D.random(-range, range),
                textureSupplier.apply(Vector3D.random(0, 1).toColor())
        ));
    }

    private void setupRandSphere(final double range,
                              final Function<Color, RayTracingTexture>
                                      textureSupplier) {
        newObject(new Sphere(
                Vector3D.random(-range, range),
                randomInRange(0.3, range / 6),
                textureSupplier.apply(Vector3D.random(0, 1).toColor())
        ));
    }

    private double randomInRange(double range) {
        return Math.random() * range - range / 2;
    }

    private double randomInRange(double origin, double range) {
        return new Random().nextDouble(origin, range);
    }

    private static class TextureHelper {
        private static double minimumReflectingAngle = 0;
        private static double randomness = 0;

        private static double reflectivity = 0;
        private static double absorption = 0.9;

        public static SubsurfaceTexture newSubsurface(final Color color) {
            return new SubsurfaceTexture(color, false, minimumReflectingAngle, randomness);
        }

        public static ReflectingTexture newReflecting(final Color color) {
            return new ReflectingTexture(color, false, reflectivity, absorption);
        }

        public static Function<Color, RayTracingTexture>
            reflectingSupplier(final double reflectivity) {
            return (Color color) ->
                    new ReflectingTexture(color, false, reflectivity);
        }

        public static Function<Color, RayTracingTexture> reflectingSupplier() {
            final double finalReflectivity = reflectivity;
            return (Color color) ->
                    new ReflectingTexture(color, false, finalReflectivity);
        }

        public static BaseTexture newBase(final Color color) {
            return new BaseTexture(color, false);
        }

        public static void setMinimumReflectingAngle(double minimumReflectingAngle) {
            TextureHelper.minimumReflectingAngle = minimumReflectingAngle;
        }

        public static void setRandomness(double randomness) {
            TextureHelper.randomness = randomness;
        }

        public static void setReflectivity(double reflectivity) {
            TextureHelper.reflectivity = reflectivity;
        }

        public static void setAbsorption(double absorption) {
            TextureHelper.absorption = absorption;
        }
    }


    /*
     * Benchmarking:
     */

    private void measureTimeWithoutDisplay() {
        while (true) {
            measureRender();
        }
    }

    private void measureRender() {
        mainCam.requestUpdate();
        mainCam.update((Collection<RayTraceable>) getGraphicsDriver().getCameraObjects());
        if (renders < NUM_TO_TEST) {
            getGraphicsDriver().getCamera().requestUpdate();
            avTime += mainCam.getRenderTime();
            avTime /= 2;
            renders++;
        } else if (renders < NUM_TO_TEST + 1) {
            System.out.println(
                    "\nBenchmark Complete."
                            + "\nSize: " + mainCam.getWidth()
                            + "\nRays/Pixel: " + mainCam.getRaysPerPixel()
                            + "\nTotal rays: " + (
                            mainCam.getWidth()
                                    * mainCam.getHeight()
                                    * mainCam.getRaysPerPixel())
                            + "\nMax bounces: " + mainCam.getMaxBounces()
                            + "\nAverage render time: " + TimeFormatter.format(avTime)
            );
            System.out.println(
                    mainCam.getWidth() + "\t"
                            + mainCam.getRaysPerPixel() + "\t"
                            + (mainCam.getWidth()
                            * mainCam.getHeight()
                            * mainCam.getRaysPerPixel()) + "\t"
                            + mainCam.getMaxBounces() + "\t"
                            + avTime
            );
//            System.exit(0);
        }
    }

    double angle = 0;

    private Vector3D spiral(double length, double distance) {
        double sin = Math.sin(Math.PI - distance / length);
        return new Vector3D(
                sin * Math.cos(Math.PI * distance),
                sin * Math.sin(Math.PI * distance),
                Math.cos(Math.PI - distance/length)
        );
    }
    @Override
    public void updateGame() {
//        if (mainCam.getFieldOfViewDegrees() < 180) {
//            mainCam.setFieldOfViewDegrees(mainCam.getFieldOfViewDegrees() + 2);
//        }
//        InPlane3D lightLoc = light.get(InPlane3D.class);
//        lightLoc.updateLocation(new Vector3D(0, 0, 0.3));
//        mainCam.setLocation(spiral(Math.TAU, angle).scalarMultiply(5));//new Vector3D(Math.cos(angle), Math.sin(angle), Math.cos(angle) * Math.sin(angle)).scalarMultiply(10));
//        mainCam.setDirection(mainCam.getLocation().atMagnitude(-1));

//        mainCam.setDirection(spiral(Math.TAU, angle));
//        angle += 0.1;

//        mainCam.setLocation(mainCam.getLocation().add(new Vector3D(0, 0.15, 0)));
//        mainCam.setDirection(mainCam.getDirection().add(new Vector3D(0, -0.01, 0)));
//        for (GraphicsObject3D object : getGraphicsDriver().getCameraObjects()) {
//                InPlane3D loc = object.getFromParent(InPlane3D.class);
//                loc.updateLocation(Vector3D.random(0, 0.3));
//        }
        if (renders < NUM_TO_TEST) {
            getGraphicsDriver().getCamera().requestUpdate();
            avTime += mainCam.getRenderTime();
            avTime /= 2;
            renders++;
        } else if (renders < NUM_TO_TEST + 1) {
            System.out.println(
                    "\nBenchmark Complete."
                    + "\nSize: " + mainCam.getWidth()
                    + "\nRays/Pixel: " + mainCam.getRaysPerPixel()
                    + "\nTotal rays: " + (
                            mainCam.getWidth()
                        * mainCam.getHeight()
                        * mainCam.getRaysPerPixel())
                    + "\nMax bounces: " + mainCam.getMaxBounces()
                    + "\nAverage render time: " + TimeFormatter.format(avTime)
            );
            System.out.println(
                    mainCam.getWidth() + "\t"
                    + mainCam.getRaysPerPixel() + "\t"
                    + (mainCam.getWidth()
                            * mainCam.getHeight()
                            * mainCam.getRaysPerPixel()) + "\t"
                    + mainCam.getMaxBounces() + "\t"
                    + avTime
            );
            renders++;
//            System.exit(0);
        }
    }
}
