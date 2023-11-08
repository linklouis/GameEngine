package gameengine.threed.drivers;

import gameengine.threed.graphics.RayTracedCamera;
import gameengine.threed.graphics.objectgraphics.RayTraceable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GraphicsDriver3D {
    public final int WIDTH, HEIGHT;
    public Color background = Color.BLACK;
    private Pane root;
    private final ImageView view;
    private Scene scene;
    private final List<RayTraceable> visualObjects = new ArrayList<>();
    private RayTracedCamera camera;


    /*
     * Construction:
     */

    public GraphicsDriver3D(int width, int height, RayTracedCamera camera) {
        WIDTH = width;
        HEIGHT = height;
        this.camera = camera;
        view = new ImageView(camera.getImage());
    }

    public GraphicsDriver3D(int width, int height, Color bgColor, RayTracedCamera camera) {
        WIDTH = width;
        HEIGHT = height;
        background = bgColor;
        this.camera = camera;
        view = new ImageView(camera.getImage());
    }

    public void initialize(Stage stage) {
        setRoot(new VBox());
        getRoot().setPrefSize(camera.getWidth(), camera.getHeight());
        setScene(new Scene(getRoot(), Color.BLACK));
        root.getChildren().add(view);

        stage.setScene(getScene());
        stage.show();
    }


    /*
     * Functionality:
     */

    public void updateGraphics() {
        camera.update(visualObjects);
    }


    /*
     * Utilities:
     */

    public void add(RayTraceable newObject) {
        visualObjects.add(newObject);
    }

    public RayTraceable get(int index) {
        return visualObjects.get(index);
    }

    public void forEach(Consumer<RayTraceable> function) {
        visualObjects.forEach(function);
    }

    public RayTracedCamera getCamera() {
        return camera;
    }

    public void setCamera(RayTracedCamera camera) {
        this.camera = camera;
    }

    public List<RayTraceable> getVisualObjects() {
        return visualObjects;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Pane getRoot() {
        return root;
    }

    public Scene getScene() {
        return scene;
    }

    protected void setRoot(Pane root) {
        this.root = root;
    }

    protected void setScene(Scene scene) {
        this.scene = scene;
    }
}
