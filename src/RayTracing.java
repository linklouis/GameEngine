import gameengine.objects.GameDriver;
import gameengine.prebuilt.gameobjects.Sphere;
import gameengine.vectormath.Vector3D;
import javafx.scene.paint.Color;

public class RayTracing extends GameDriver {
    private final static int SIZE = 600;

    public RayTracing() {
        super("Ray Tracing", SIZE, SIZE, 4);
    }

    public static void main(String[] args) {
        launch(RayTracing.class);
    }

    @Override
    public void initialize() {
        ImageCamera mainCam = new ImageCamera(-6, 0, -2, new Vector3D(1, 0, 0.2), 800, 800, this);
        newObject(mainCam);

        newObject(new Sphere(0, -3, -2, 2, Color.AQUA, false));
        newObject(new Sphere(0, 2, -3, 3, Color.GREEN, false));

        newObject(new Sphere(0, 0, 100, 100, Color.BROWN, false));

        newObject(new Sphere(-5, 0, -10, 6, Color.WHITE, true));

        mainCam.renderImage(true);
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

    }
}
