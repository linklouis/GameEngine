package gameengine.graphics;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GraphicsDriver extends ArrayList<Visual> {
    public final int WIDTH, HEIGHT;
    public Color background = Color.BLACK;
    private VBox root;
    private Scene scene;
    private Canvas canvas;


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

    public void initialize(Stage stage) {
        root = new VBox();
        scene = new Scene(root, Color.BLACK);
        canvas = new Canvas(WIDTH, HEIGHT);

        stage.setScene(scene);
        root.getChildren().add(canvas);
        stage.show();
    }


    /*
     * Functionality:
     */

    public void updateGraphics(GraphicsContext gc) {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        forEach(object -> object.paint(gc));
    }


    /*
     * Utilities:
     */

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public VBox getRoot() {
        return root;
    }

    public Scene getScene() {
        return scene;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public GraphicsContext getGraphicsContext() {
        return getCanvas().getGraphicsContext2D();
    }
}
