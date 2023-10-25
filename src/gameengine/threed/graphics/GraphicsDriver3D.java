package gameengine.threed.graphics;

import gameengine.skeletons.GraphicsDriver;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GraphicsDriver3D extends GraphicsDriver<Visual3D> {
    private final List<Visual3D> visualObjects = new ArrayList<>();
    private Camera camera;


    /*
     * Construction:
     */

    public GraphicsDriver3D(int width, int height, Camera camera) {
        super(width, height);
        this.camera = camera;
    }

    public GraphicsDriver3D(int width, int height, Color bgColor, Camera camera) {
        super(width, height, bgColor);
        this.camera = camera;
    }

    @Override
    public void initialize(Stage stage) {
        setRoot(new VBox());
        getRoot().setPrefSize(camera.getWidth(), camera.getHeight());
        setScene(new Scene(getRoot(), Color.BLACK));

        stage.setScene(getScene());
        stage.show();
    }


    /*
     * Functionality:
     */

    @Override
    public void updateGraphics() {
        camera.update(visualObjects.toArray(new Visual3D[0]));
        camera.displayOn(getRoot());
    }


    /*
     * Utilities:
     */

    public void add(Visual3D newObject) {
        visualObjects.add(newObject);
    }

    public Visual3D get(int index) {
        return visualObjects.get(index);
    }

    public void forEach(Consumer<Visual3D> function) {
        visualObjects.forEach(function);
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public List<Visual3D> getVisualObjects() {
        return visualObjects;
    }
}
