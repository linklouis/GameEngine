import gameengine.drivers.GameDriver3D;
import gameengine.threed.graphics.BaseTexture;
import gameengine.threed.graphics.Visual3D;
import gameengine.threed.graphics.raytraceing.RayTracedCamera;
import gameengine.threed.prebuilt.gameobjects.Rectangle;
import gameengine.threed.prebuilt.objectmovement.physics.PhysicsEngine3D;
import gameengine.threed.graphics.GraphicsDriver3D;
import gameengine.threed.prebuilt.gameobjects.Sphere;
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
                        700, 700, 0.3,
                        10, 100, 100, true)),
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
        double reflectivity = 0;

        mainCam.setDirection(new Vector3D(1, 0, 0));
        mainCam.setLocation(new Vector3D(-15, 0, 0));

//        newObject(new Rectangle(5, 0, 0, new Vector3D(1, 5, 5), new BaseTexture(Color.RED, false, reflectivity)));
//        newObject(new Rectangle(0, -5, 0, new Vector3D(5, 1, 5), new BaseTexture(Color.BLUE, false, reflectivity)));
        newObject(new Rectangle(0, 5, 0, new Vector3D(5, 1, 5), new BaseTexture(Color.BLUE, false, reflectivity)));
        newObject(new Rectangle(0, 0, -5, new Vector3D(5, 5, 1), new BaseTexture(Color.GREEN, false, reflectivity)));

        newObject(new Sphere(0, 0, 15, 7, Color.WHITE, true));
    }

    private void setupScene1() {
        double reflectivity = 0.3;

        newObject(new Rectangle(0, -1, -2, new Vector3D(1,1, 1), new BaseTexture(Color.AZURE, false, reflectivity)));
//        newObject(new Sphere(0, -1, -2, new Vector3D(1,1, 1).magnitude(), new BaseTexture(Color.AZURE, false, reflectivity)));

        newObject(new Sphere(3, -1, -2, 2, new BaseTexture(Color.AQUA, false, reflectivity)));
        newObject(new Sphere(-1, 2, -3, 3, new BaseTexture(Color.GREEN, false, reflectivity)));
        newObject(new Sphere(0, 0, 100, 100, new BaseTexture(Color.BROWN, false, reflectivity)));
        newObject(new Sphere(-10, 2, -10, 7, Color.WHITE, true));
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
                            + "\nMarch distance: " + mainCam.getMarchDistance()
                            + "\nMax distance: " + mainCam.getMaxDistance()
                            + "\nMax bounces: " + mainCam.getMaxBounces()
                            + "\nAverage render time: " + avTime
            );
            System.out.println(
                    mainCam.getWidth() + "\t"
                            + mainCam.getRaysPerPixel() + "\t"
                            + (mainCam.getWidth()
                            * mainCam.getHeight()
                            * mainCam.getRaysPerPixel()) + "\t"
                            + mainCam.getMarchDistance() + "\t"
                            + mainCam.getMaxDistance() + "\t"
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
                    + "\nMarch distance: " + mainCam.getMarchDistance()
                    + "\nMax distance: " + mainCam.getMaxDistance()
                    + "\nMax bounces: " + mainCam.getMaxBounces()
                    + "\nAverage render time: " + avTime
            );
            System.out.println(
                    mainCam.getWidth() + "\t"
                    + mainCam.getRaysPerPixel() + "\t"
                    + (mainCam.getWidth()
                            * mainCam.getHeight()
                            * mainCam.getRaysPerPixel()) + "\t"
                    + mainCam.getMarchDistance() + "\t"
                    + mainCam.getMaxDistance() + "\t"
                    + mainCam.getMaxBounces() + "\t"
                    + avTime
            );
//            System.exit(0);
        }
    }
}
