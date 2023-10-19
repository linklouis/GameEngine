package gameengine.skeletons;

import gameengine.skeletons.Visual;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.function.Consumer;

public abstract class GraphicsDriver<VisualType extends Visual> {
    public final int WIDTH, HEIGHT;
    public Color background = Color.BLACK;
    private Pane root;
    private Scene scene;


    /*
     * Construction:
     */

    public GraphicsDriver(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
    }

    public GraphicsDriver(int width, int height, Color bgColor) {
        WIDTH = width;
        HEIGHT = height;
        background = bgColor;
    }

    public abstract void initialize(Stage stage);


    /*
     * Functionality:
     */

    public abstract void updateGraphics();


    /*
     * Utilities:
     */

    public abstract void add(VisualType newObject);

    public abstract VisualType get(int index);

    public abstract void forEach(Consumer<VisualType> function);

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
