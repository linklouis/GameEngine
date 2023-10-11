import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GravityApp extends Application {

    private List<PhysicsObject> objects = new ArrayList<>();
    private GameObject pointer;

    public static void main(String[] args) {
        launch();
    }

    private static int SIZE = 600;

    private void populateObjects() {
//        objects.add(new GameObject(200, 200, new GameObject.Row[100], Color.BLANCHEDALMOND) {
//            {
//                for (int i = 0; i < getRows().length; i++) {
//                    newRow(i, (Math.sin(40 * i) - 2) * 50, (Math.sin(40 * i) + 1) * 50);
//                }
//            }
//        });

//        pointer = new GameObject(0, 0, new GameObject.Row[10], Color.ALICEBLUE) {
//            {
//                for (int i = 0; i < getRows().length; i++) {
//                    double y2rd = Math.pow(i - getRows().length / 2.0, 2);
//                    double x = Math.sqrt(3 - y2rd);
//                    newRow(i, -x, x);
//                }
//            }
//
//        };
//
//        objects.add(pointer);

        objects.add(new CircleGameObj(100, 100, 80, Color.RED, 100000000000L));
        objects.add(new CircleGameObj(200, 200, 80, Color.BLUE, 100000000000L));
    }

    @Override
    public void start(Stage stage) {
        populateObjects();

        VBox root = new VBox();
        Scene scene = new Scene(root, Color.BLACK);
        Canvas canvas = new Canvas(SIZE, SIZE);

        root.setStyle("-fx-background-color: black; -fx-padding: 10px;");
//        root.setOnMouseMoved(new EventHandler<MouseEvent>() {
//            @Override public void handle(MouseEvent event) {
//                pointer.setLocation(event.getX(), event.getY());
//            }
//        });

//        root.setOnMouseExited(new EventHandler<MouseEvent>() {
//            @Override public void handle(MouseEvent event) {
//
//            }
//        });

        getAnimationTimer(canvas.getGraphicsContext2D()).start();

        stage.setScene(scene);
        stage.setTitle("Gravity Sim Test");
        root.getChildren().add(canvas);
        stage.show();
    }

    private AnimationTimer getAnimationTimer(GraphicsContext gc) {
        return new AnimationTimer() {
            @Override
            public void handle(long l) {
                gc.clearRect(0, 0, SIZE, SIZE);
                objects.forEach(object -> {
                    object.updateForces(objects.toArray(new PhysicsObject[0]), 60);
                    object.updatePosition(60);
                });
//                Collision.handleCollisions();
                objects.forEach(object -> object.paint(gc));
//                if (pointer.isColliding(objects.get(0))) {
//                    pointer.setColor(Color.RED);
//                    gc.setFill(Color.RED);
//                    pointer.getColliding(objects.get(0)).at(objects.get(0)).paint(gc);
//                } else {
//                    pointer.setColor(Color.WHITE);
//                }
//                pointer.paint(gc);
            }
        };
    }
}
