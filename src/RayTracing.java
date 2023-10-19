import gameengine.drivers.GameDriver3D;
import gameengine.threed.graphics.Visual3D;
import gameengine.threed.graphics.raytraceing.RayTracedCamera;
import gameengine.threed.prebuilt.objectmovement.physics.PhysicsEngine3D;
import gameengine.threed.graphics.GraphicsDriver3D;
import gameengine.threed.prebuilt.gameobjects.Sphere;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public class RayTracing extends GameDriver3D {
    private final static int SIZE = 400;
    private int renders = 0;
    private long avTime = 0;
    private int numToTest = 5;
    private RayTracedCamera mainCam;


    public RayTracing() {
        super("Ray Tracing", new GraphicsDriver3D(SIZE, SIZE,
                new RayTracedCamera(-2, -10, -10, new Vector3D(0.8, 3, 1.8),
                        700, 700, 0.3,
                        10, 100, 10, true)),
                new PhysicsEngine3D());
    }

    public static void main(String[] args) {
        launch(RayTracing.class);
    }

    @Override
    public void initialize() {
        mainCam = (RayTracedCamera) getGraphicsDriver().getCamera();
        newObject(new Sphere(3, -1, -2, 2, Color.AQUA, false));
        newObject(new Sphere(-1, 2, -3, 3, Color.GREEN, false));

        newObject(new Sphere(0, 0, 100, 100, Color.BROWN, false));
//        newObject(new Sphere(0, 205, 0, 100, Color.GRAY, true));
//        newObject(new Sphere(0, -125, 0, 100, Color.DARKGRAY, true));

//        newObject(new Sphere(-15, 0, -10, 7, Color.WHITE, true));
        newObject(new Sphere(-10, 2, -10, 7, Color.WHITE, true));
//        newObject(new Sphere(-5, -14, -10, 3, Color.WHITE, true));
//        newObject(new Sphere(-10, -20, -10, 3, Color.WHITE, true));

        mainCam.requestUpdate();
//        while (true) {
//            measureRender();
//        }
    }

    private void measureRender() {
        mainCam.requestUpdate();
        mainCam.update(getGraphicsDriver().getVisualObjects().toArray(new Visual3D[0]));
        if (renders < numToTest) {
            getGraphicsDriver().getCamera().requestUpdate();
            avTime += mainCam.renderTime;
            avTime /= 2;
            renders++;
        } else if (renders < numToTest + 1) {
            System.out.println(
                    "\nBenchmark Complete."
                            + "\nsize: " + getGraphicsDriver().getWidth()
                            + "\nRays/Pixel: " + mainCam.getRaysPerPixel()
                            + "\nTotal rays: " + (
                            getGraphicsDriver().getWidth()
                                    * getGraphicsDriver().getHeight()
                                    * mainCam.getRaysPerPixel())
                            + "\nMarch distance: " + mainCam.getMarchDistance()
                            + "\nMax distance: " + mainCam.getMaxDistance()
                            + "\nMax bounces: " + mainCam.getMaxBounces()
                            + "\nAverage render time: " + avTime
            );
            System.out.println(
                    getGraphicsDriver().getWidth() + "\t"
                            + mainCam.getRaysPerPixel() + "\t"
                            + (getGraphicsDriver().getWidth()
                            * getGraphicsDriver().getHeight()
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
        if (renders < numToTest) {
            getGraphicsDriver().getCamera().requestUpdate();
            avTime += mainCam.renderTime;
            avTime /= 2;
            renders++;
        } else if (renders < numToTest + 1) {
            System.out.println(
                    "\nBenchmark Complete."
                    + "\nsize: " + getGraphicsDriver().getWidth()
                    + "\nRays/Pixel: " + mainCam.getRaysPerPixel()
                    + "\nTotal rays: " + (
                        getGraphicsDriver().getWidth()
                        * getGraphicsDriver().getHeight()
                        * mainCam.getRaysPerPixel())
                    + "\nMarch distance: " + mainCam.getMarchDistance()
                    + "\nMax distance: " + mainCam.getMaxDistance()
                    + "\nMax bounces: " + mainCam.getMaxBounces()
                    + "\nAverage render time: " + avTime
            );
            System.out.println(
                    getGraphicsDriver().getWidth() + "\t"
                    + mainCam.getRaysPerPixel() + "\t"
                    + (getGraphicsDriver().getWidth()
                            * getGraphicsDriver().getHeight()
                            * mainCam.getRaysPerPixel()) + "\t"
                    + mainCam.getMarchDistance() + "\t"
                    + mainCam.getMaxDistance() + "\t"
                    + mainCam.getMaxBounces() + "\t"
                    + avTime
            );
            System.exit(0);
        }
    }
}
