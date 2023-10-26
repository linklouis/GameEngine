import gameengine.drivers.GameDriver3D;
import gameengine.threed.graphics.BaseTexture;
import gameengine.threed.graphics.Visual3D;
import gameengine.threed.graphics.raytraceing.RayTracedCamera;
import gameengine.threed.prebuilt.gameobjects.RectPlane;
import gameengine.threed.prebuilt.gameobjects.Rectangle;
import gameengine.threed.prebuilt.objectmovement.physics.PhysicsEngine3D;
import gameengine.threed.graphics.GraphicsDriver3D;
import gameengine.threed.prebuilt.gameobjects.Sphere;
import gameengine.timeformatting.TimeFormatter;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public class RayTracing extends GameDriver3D {
    private final static int SIZE = 400;
    private int renders = 0;
    private long avTime = 0;
    private final int NUM_TO_TEST = 5;
    private RayTracedCamera mainCam;

    public RayTracing() {
        super("Ray Tracing", new GraphicsDriver3D(SIZE, SIZE,
                new RayTracedCamera(-2, -10, -10, new Vector3D(0.8, 3, 1.8),
                        700, 700, 10, 10, true)),
                new PhysicsEngine3D());
    }

    public static void main(String[] args) {
        launch(RayTracing.class);
    }

    @Override
    public void initialize() {
        System.out.println(java.time.LocalDateTime.now());
        mainCam = (RayTracedCamera) getGraphicsDriver().getCamera();
        setupScene2();
    }

    private void setupScene2() {
        double reflectivity = 0.8;

        mainCam.setDirection(new Vector3D(1, 0, 0));
        mainCam.setLocation(new Vector3D(-10, 0, 0));

        newObject(new Sphere(0, 0, 0, 2, new BaseTexture(Color.WHITE, false, 0.9)));

        double wallSize = 10;
        double hWallSize = wallSize / 2;
        new RectPlane(hWallSize, -hWallSize, -hWallSize, new Vector3D(0, wallSize, wallSize), new BaseTexture(Color.RED, false, reflectivity)).initiate(this);
        new RectPlane(-hWallSize, -hWallSize, -hWallSize, new Vector3D(wallSize, 0, wallSize), new BaseTexture(Color.BLUE, false, reflectivity)).initiate(this);
        new RectPlane(-hWallSize, hWallSize, -hWallSize, new Vector3D(wallSize, 0, wallSize), new BaseTexture(Color.BLUE, false, reflectivity)).initiate(this);
        new RectPlane(-hWallSize, -hWallSize, -hWallSize, new Vector3D(wallSize, wallSize, 0), new BaseTexture(Color.GREEN, false, reflectivity)).initiate(this);
        new RectPlane(-hWallSize, -hWallSize, hWallSize, new Vector3D(wallSize, wallSize, 0), new BaseTexture(Color.GREEN, false, reflectivity)).initiate(this);

        newObject(new Rectangle(-12, 0, 0, new Vector3D(1, 5, 5), new BaseTexture(Color.RED, true, 0)));

        setupBoundingBox(40);

        newObject(new Sphere(0, 0, 15, 7, Color.WHITE, true));
    }

    private void setupBoundingBox(double wallSize) {
        double hWallSize = wallSize / 2;
        Color color = Color.GRAY;
//        new Rectangle(-size, -size / 2, -size / 2, new Vector3D(1, size, size), new BaseTexture(Color.GRAY, true, 0)).initiate(this);
//        new Rectangle(size, -size / 2, -size / 2, new Vector3D(1, size, size), new BaseTexture(Color.GRAY, false, 0)).initiate(this);
//        new Rectangle(-size / 2, -size, -size / 2, new Vector3D(size, 1, size), new BaseTexture(Color.GRAY, false, 0)).initiate(this);
//        new Rectangle(-size / 2, size, -size / 2, new Vector3D(size, 1, size), new BaseTexture(Color.GRAY, false, 0)).initiate(this);
//        new Rectangle(-size / 2, -size / 2, -size, new Vector3D(size, size, 1), new BaseTexture(Color.GRAY, false, 0)).initiate(this);
//        new Rectangle(-size / 2, -size / 2, size, new Vector3D(size, size, 1), new BaseTexture(Color.GRAY, false, 0)).initiate(this);
        new Rectangle(hWallSize, -hWallSize, -hWallSize, new Vector3D(1, wallSize, wallSize),  new BaseTexture(color, false, 0)).initiate(this);
        new Rectangle(-hWallSize, -hWallSize, -hWallSize, new Vector3D(1, wallSize, wallSize),  new BaseTexture(color, false, 0)).initiate(this);
        new Rectangle(-hWallSize, -hWallSize, -hWallSize, new Vector3D(wallSize, 1, wallSize),new BaseTexture(color, false, 0)).initiate(this);
        new Rectangle(-hWallSize, hWallSize, -hWallSize, new Vector3D(wallSize, 1, wallSize), new BaseTexture(color, false, 0)).initiate(this);
        new Rectangle(-hWallSize, -hWallSize, -hWallSize, new Vector3D(wallSize, wallSize, 1),new BaseTexture(color, false, 0)).initiate(this);
        new Rectangle(-hWallSize, -hWallSize, hWallSize, new Vector3D(wallSize, wallSize, 1),  new BaseTexture(color, false, 0)).initiate(this);
        new Rectangle(-hWallSize, -hWallSize, -hWallSize, new Vector3D(wallSize, wallSize, 1),  new BaseTexture(color, false, 0)).initiate(this);
//        new BaseTexture(Color.GRAY, false, 0)).initiate(this);
    }

    private void setupScene1() {
        double reflectivity = 0.7;

        new Rectangle(-1, -2, -3, new Vector3D(2,2, 2), new BaseTexture(Color.AZURE, false, reflectivity)).initiate(this);
//        newObject(new Sphere(0, -1, -2, new Vector3D(1,1, 1).magnitude(), new BaseTexture(Color.AZURE, false, reflectivity)));

        newObject(new Sphere(3, -1, -2, 2, new BaseTexture(Color.AQUA, false, reflectivity)));
        newObject(new Sphere(-1, 2, -3, 3, new BaseTexture(Color.GREEN, false, reflectivity)));
        newObject(new Sphere(0, 0, 100, 100, new BaseTexture(Color.BROWN, false, reflectivity)));

        newObject(new Sphere(-10, 2, -10, 7, Color.WHITE, true));
    }

    private void setupScene3() {
        double reflectivity = 0.5;

        mainCam.setDirection(new Vector3D(0, 0, -1));
        mainCam.setLocation(new Vector3D(0, 0, 10));

        newObject(new Sphere(0, 0, 18, 7, new BaseTexture(Color.WHITE, true, 0)));

        newObject(new Sphere(-2, -1, 0, 2, new BaseTexture(Color.RED, false, reflectivity)));
        newObject(new Sphere(2, -1, 0, 2, new BaseTexture(Color.GREEN, false, reflectivity)));
        newObject(new Sphere(0, Math.PI/1.262, 0, 2, new BaseTexture(Color.BLUE, false, reflectivity)));

        newObject(new Sphere(-1, 2, -22, 20, new BaseTexture(Color.BROWN, false, 0)));

//        setupBoundingBox(20);
    }

    private void setupScene4() {
        double reflectivity = 0;//.5;

        mainCam.setDirection(new Vector3D(-0.5, -0.5, -1));
        mainCam.setLocation(new Vector3D(5, 5, 10));

        newObject(new Sphere(0, 0, 18, 7, new BaseTexture(Color.WHITE, true, 0)));

        new Rectangle(0, 0, 0, new Vector3D(2, 3, 4), new BaseTexture(Color.RED, false, reflectivity)).initiate(this);

//        for (Visual3D collider : getGraphicsDriver().getVisualObjects()) {
//            System.out.println(collider.getFromParent(Collider3D.class).getAppearance());
//        }

//        newObject(new Sphere(0, 0, 0, 1, new BaseTexture(Color.WHITE, true, 0)));

//        newObject(new Sphere(0, 0, -13, -10, new BaseTexture(Color.BROWN, false, 0)));
//        double dist = 10;
//        Vector3D p1 = new Vector3D(3, 0, 0)/*new Vector3D(Math.random() * dist - dist/2, Math.random() * dist - dist/2, 0)*/;
//        Vector3D p2 = new Vector3D(3, 2, 0)/*new Vector3D(Math.random() * dist - dist/2, Math.random() * dist - dist/2, 0)*/;
//        Vector3D p3 = new Vector3D(3, 2, 2)/*new Vector3D(Math.random() * dist - dist/2, Math.random() * dist - dist/2, 0)*/;
//
//        newObject(new Tri(p1, p2, p3,
//                new BaseTexture(Color.RED, true, reflectivity)));
//
////        newObject(new Tri(
////                new Vector3D(2, 0, 0),
////                new Vector3D(2, 0, 1),
////                new Vector3D(2, 1, 1), new BaseTexture(Color.WHITE, true, 0)));
//
////        newObject(new Sphere(0, 0, 0, 10, new BaseTexture(Color.BROWN, true, 0)));
//
//
//        double r = 0.2;
//        newObject(new Sphere(p1, r, new BaseTexture(Color.RED, true, 0)));
//        newObject(new Sphere(p2, r, new BaseTexture(Color.GREEN, true, 0)));
//        newObject(new Sphere(p3, r, new BaseTexture(Color.BLUE, true, 0)));
    }

    private void setupScene5() {
        double reflectivity = 0.5;

        mainCam.setDirection(new Vector3D(-0.5, -0.5, -1));
        mainCam.setLocation(new Vector3D(5, 5, 10));

        newObject(new Sphere(0, 0, 20, 10, new BaseTexture(Color.WHITE, true, 0)));

        setupBoundingBox(5);

//        double range = 10;
//        setupRandRect(range, reflectivity);
//        setupRandRect(range, reflectivity);
//        setupRandRect(range, reflectivity);
//        setupRandRect(range, reflectivity);
//        setupRandRect(range, reflectivity);
//        setupRandRect(range, reflectivity);
//        setupRandRect(range, reflectivity);
//        setupRandRect(range, reflectivity);
//        setupRandRect(range, reflectivity);
//        setupRandRect(range, reflectivity);
//        setupRandRect(range, reflectivity);
//        setupRandRect(range, reflectivity);
    }

    private void setupRandRect(double range, double reflectivity) {
        new Rectangle(
                randomInRange(range), randomInRange(range), randomInRange(range),
                new Vector3D(randomInRange(range),randomInRange(range), randomInRange(range)),
                new BaseTexture(new Color(Math.random(), Math.random(), Math.random(), 1), false, reflectivity)).initiate(this);
    }

    private double randomInRange(double range) {
        return Math.random() * range - range / 2;
    }

    private void measureTimeWithoutDisplay() {
        while (true) {
            measureRender();
        }
    }

    private void measureRender() {
        mainCam.requestUpdate();
        mainCam.update(getGraphicsDriver().getVisualObjects().toArray(new Visual3D[0]));
        if (renders < NUM_TO_TEST) {
            getGraphicsDriver().getCamera().requestUpdate();
            avTime += mainCam.renderTime;
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
            System.exit(0);
        }
    }

    private void initDebugVisuals() {
//        newObject(new Sphere(0,0,0,10,Color.BLANCHEDALMOND, true));

//        double axisMarkerDistance = 2;
//
//        newObject(new Sphere(axisMarkerDistance, 0, 0, 1, new Color(1, 0, 0, 1), true));
//        newObject(new Sphere(-axisMarkerDistance, 0, 0, 1,new Color(1, 0.5, 0.5, 1), true));
//
//        newObject(new Sphere(0, axisMarkerDistance, 0, 1, new Color(0, 1, 0, 1), true));
//        newObject(new Sphere(0, -axisMarkerDistance, 0, 1, new Color(0.5, 1, 0.5, 1), true));
//
//        newObject(new Sphere(0, 0, axisMarkerDistance, 1, new Color(0, 0, 1, 1), true));
//        newObject(new Sphere(0, 0, -axisMarkerDistance, 1, new Color(0.5, 0.5, 1, 1), true));
    }

    @Override
    public void updateGame() {
        if (renders < NUM_TO_TEST) {
            getGraphicsDriver().getCamera().requestUpdate();
            avTime += mainCam.renderTime;
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
