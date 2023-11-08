package gameengine.threed.drivers;

import gameengine.skeletons.GraphicsDriver;
import gameengine.threed.graphics.Camera;
import gameengine.threed.graphics.GraphicsObject3D;
import gameengine.threed.graphics.Visual3D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GraphicsDriver3D<RenderType extends GraphicsObject3D> extends GraphicsDriver<Visual3D> {
    private final List<Visual3D> visualObjects = new ArrayList<>();
    private final List<RenderType> cameraObjects = new ArrayList<>();
    private final ImageView view;
    private Camera<RenderType> camera;


    /*
     * Construction:
     */

    public GraphicsDriver3D(int width, int height, Camera<RenderType> camera) {
        super(width, height);
        this.camera = camera;
        view = new ImageView(camera.getImage());
    }

    public GraphicsDriver3D(int width, int height, Color bgColor, Camera<RenderType> camera) {
        super(width, height, bgColor);
        this.camera = camera;
        view = new ImageView(camera.getImage());
    }

    @Override
    public void initialize(Stage stage) {
        setRoot(new VBox());
        getRoot().setPrefSize(camera.getWidth(), camera.getHeight());
        setScene(new Scene(getRoot(), Color.BLACK));
        getRoot().getChildren().add(view);

        stage.setScene(getScene());
        stage.show();
    }


    /*
     * Functionality:
     */

    @Override
    public void updateGraphics() {
        camera.update(cameraObjects);
    }


    /*
     * Utilities:
     */

    public void add(Visual3D newObject) {
        visualObjects.add(newObject);
        if (camera.canRender(newObject)) {
            cameraObjects.add(camera.getRenderableFrom(newObject).get());
        }
//        cameraObjects = camera.getValidObjects(visualObjects);
    }

    public Visual3D get(int index) {
        return visualObjects.get(index);
    }

    public void forEach(Consumer<Visual3D> function) {
        visualObjects.forEach(function);
    }

    public Camera<RenderType> getCamera() {
        return camera;
    }

    public void setCamera(Camera<RenderType> camera) {
        this.camera = camera;
    }

    public List<Visual3D> getVisualObjects() {
        return visualObjects;
    }

    public List<RenderType> getCameraObjects() {
        return cameraObjects;
    }
}
