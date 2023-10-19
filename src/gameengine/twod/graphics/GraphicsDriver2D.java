package gameengine.twod.graphics;

import gameengine.skeletons.GraphicsDriver;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GraphicsDriver2D extends GraphicsDriver<Visual2D> {
    private final List<Visual2D> visualObjects = new ArrayList<>();
    private Canvas canvas;


    /*
     * Construction:
     */

    public GraphicsDriver2D(int width, int height) {
        super(width, height);
    }

    public GraphicsDriver2D(int width, int height, Color bgColor) {
        super(width, height, bgColor);
    }

    @Override
    public void initialize(Stage stage) {
        setRoot(new VBox());
        setScene(new Scene(getRoot(), Color.BLACK));

        canvas = new Canvas(WIDTH, HEIGHT);
        getRoot().getChildren().add(canvas);

        stage.setScene(getScene());
        stage.show();
    }


    /*
     * Functionality:
     */

    @Override
    public void updateGraphics() {
        getGraphicsContext().clearRect(0, 0, WIDTH, HEIGHT);
        forEach(object -> object.paint(getGraphicsContext()));
    }


    /*
     * Utilities:
     */

    public void add(Visual2D newObject) {
        visualObjects.add(newObject);
    }

    public Visual2D get(int index) {
        return visualObjects.get(index);
    }

    public void forEach(Consumer<Visual2D> function) {
        visualObjects.forEach(function);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public GraphicsContext getGraphicsContext() {
        return getCanvas().getGraphicsContext2D();
    }
}