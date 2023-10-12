import gameengine.objects.GameObject;
import gameengine.prebuilt.InPlane;
import gameengine.prebuilt.Visual;
import gameengine.prebuilt.gameobjects.Ball;
import gameengine.prebuilt.physics.Collidable;
import gameengine.prebuilt.physics.PhysicsObject;
import gameengine.vectormath.Vector2D;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.ArrayList;
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

        //100000000000L
        char[] decimals = new char[20];
        decimals[0] = '7';
        for (int i = 1; i < decimals.length; i++) {
            decimals[i] = '0';
        }
        BigDecimal mass =  new BigDecimal(decimals);//(long) (7L * Math.pow(10, 16));
        objects.add(new Ball(100, 100, 80, Color.RED, mass));//, new Vector2D(10, 0)));
        objects.add(new Ball(200, 200, 80, Color.BLUE, mass));
        objects.add(new Ball(300, 100, 80, Color.GREEN, mass));//, new Vector2D(15, -5)));
        objects.add(new Ball(600, 200, 80, Color.OLDLACE, mass));//, new Vector2D(1, -50)));
        objects.add(new Ball(400, 400, 80, Color.ORCHID, mass));//, new Vector2D(-7, 6)));
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
                // TODO add "Game" class or smth to take care of all this shit

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
                        if (object.get(InPlane.class).getX() < Math.abs(object.get(Collidable.class).minX())) {
                            object.get(InPlane.class).setX(Math.abs(object.get(Collidable.class).minX()));
                            object.get(PhysicsObject.class)
                                    .setVelocity(
                                            object.get(PhysicsObject.class).getVelocity()
                                                    .subtract(
                                                            new Vector2D(
                                                                    1.9 * object.get(PhysicsObject.class).getVelocity().getX(),
                                                                    0
                                                            )
                                                    )
                                    );
                        }
                        if (object.get(InPlane.class).getY() < Math.abs(object.get(Collidable.class).minY())) {
                            object.get(InPlane.class).setY(Math.abs(object.get(Collidable.class).minY()));
                            object.get(PhysicsObject.class)
                                    .setVelocity(
                                            object.get(PhysicsObject.class).getVelocity()
                                                    .subtract(
                                                            new Vector2D(
                                                                    0,
                                                                    1.9 * object.get(PhysicsObject.class).getVelocity().getY()
                                                            )
                                                    )
                                    );
                        }
                        if (object.get(InPlane.class).getX() > SIZE) {
                            object.get(InPlane.class).setX(SIZE);
                            object.get(PhysicsObject.class)
                                    .setVelocity(
                                            object.get(PhysicsObject.class).getVelocity()
                                                    .subtract(
                                                            new Vector2D(
                                                                    1.9 * object.get(PhysicsObject.class).getVelocity().getX(),
                                                                    0
                                                            )
                                                    )
                                    );
                        }
                        if (object.get(InPlane.class).getY() > SIZE) {
                            object.get(InPlane.class).setY(SIZE);
                            object.get(PhysicsObject.class)
                                    .setVelocity(
                                            object.get(PhysicsObject.class).getVelocity()
                                                    .subtract(
                                                            new Vector2D(
                                                                    0,
                                                                    1.9 * object.get(PhysicsObject.class).getVelocity().getY()
                                                            )
                                                    )
                                    );
                        }
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
