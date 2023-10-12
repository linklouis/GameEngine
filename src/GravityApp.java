import gameengine.objects.GameObject;
import gameengine.prebuilt.gameobjects.Ball;
import gameengine.prebuilt.physics.Collidable;
import gameengine.prebuilt.physics.Collision;
import gameengine.prebuilt.physics.PhysicsObject;
import gameengine.prebuilt.Visual;
import gameengine.vectormath.Vector2D;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GravityApp extends Application {

    private List<GameObject> objects = new ArrayList<>();
    private Visual pointer;

    public static void main(String[] args) {
        launch();
    }

    private static int SIZE = 600;

    private void populateObjects() {
//        objects.add(new gameengine.objects.GameObject(200, 200, new gameengine.objects.GameObject.Row[100], Color.BLANCHEDALMOND) {
//            {
//                for (int i = 0; i < getRows().length; i++) {
//                    newRow(i, (Math.sin(40 * i) - 2) * 50, (Math.sin(40 * i) + 1) * 50);
//                }
//            }
//        });

//        pointer = new gameengine.objects.GameObject(0, 0, new gameengine.objects.GameObject.Row[10], Color.ALICEBLUE) {
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

        objects.add(new Ball(100, 100, 80, Color.RED, 100000000000L, new Vector2D(10, 0)));
        objects.add(new Ball(200, 200, 80, Color.BLUE, 100000000000L));
        objects.add(new Ball(300, 100, 80, Color.GREEN, 100000000000L, new Vector2D(15, -5)));
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

                PhysicsObject[] physicsObjects = objects.stream()
                        .map(gObj -> gObj.get(PhysicsObject.class))
                        .toList()
                        .toArray(new PhysicsObject[0]);

                Collidable[] colliders = objects.stream()
                        .map(gObj -> gObj.get(Collidable.class))
                        .toList()
                        .toArray(new Collidable[0]);

                objects.forEach(object -> {
                    object.get(PhysicsObject.class).updateForces(physicsObjects, 60);
                    for (int i = 0; i < 4; i++) {
                        object.get(PhysicsObject.class).updatePosition(60 * 4);
                        gameengine.prebuilt.physics.Collision.getAndHandleCollisions(colliders);
                    }
                });

//                gameengine.prebuilt.physics.Collision.findCollisions(collidables);
//                if (Collision.getCollisions().length > 0) {
//                    System.out.println(Arrays.toString(Collision.getCollisions()));
//                }
//                gameengine.prebuilt.physics.Collision.handleCollisions();
                gameengine.prebuilt.physics.Collision.getAndHandleCollisions(colliders);

                objects.forEach(object -> object.get(PhysicsObject.class).paint(gc));
            }
        };
    }
}
