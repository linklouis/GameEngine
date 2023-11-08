import gameengine.twod.drivers.GraphicsDriver2D;
import gameengine.twod.graphics.Visual2D;
import gameengine.twod.drivers.GameDriver2D;
import gameengine.twod.prebuilt.gameobjects.Ball;
import gameengine.twod.prebuilt.objectmovement.InPlane;
import gameengine.twod.prebuilt.objectmovement.collisions.LayerCollider2D;
import gameengine.twod.prebuilt.objectmovement.physics.PhysicsEngine2D;
import gameengine.twod.prebuilt.objectmovement.physics.PhysicsObject2D;
import gameengine.vectormath.Vector2D;
import javafx.scene.paint.Color;

import java.util.List;

public class GravityApp extends GameDriver2D {

    private Visual2D pointer;
    private final static int SIZE = 600;

    public GravityApp() {
        super("Gravity Sim", new GraphicsDriver2D(SIZE, SIZE), new PhysicsEngine2D(4));
    }

    public static void main(String[] args) {
        launch(GravityApp.class);
    }

    @Override
    public void initialize() {
//        objects.add(new gameengine.skeletons.GameObject(200, 200, new gameengine.skeletons.GameObject.Row[100], Color.BLANCHEDALMOND) {
//            {
//                for (int i = 0; i < getRows().length; i++) {
//                    newRow(i, (Math.sin(40 * i) - 2) * 50, (Math.sin(40 * i) + 1) * 50);
//                }
//            }
//        });

//        pointer = new gameengine.skeletons.GameObject(0, 0, new gameengine.skeletons.GameObject.Row[10], Color.ALICEBLUE) {
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
        long mass =  (long) (7L * Math.pow(10, 16));
        int r = 80;
        newObject(new Ball(100, 100, r, Color.RED, mass, true, new Vector2D(10, 0),
                getPhysicsEngine().getCollisionHandler()));
        newObject(new Ball(200, 200, r, Color.BLUE, mass, true,
                getPhysicsEngine().getCollisionHandler()));
        newObject(new Ball(300, 100, r, Color.GREEN, mass, true, new Vector2D(15, -5),
                getPhysicsEngine().getCollisionHandler()));
        newObject(new Ball(550, 200, r, Color.OLDLACE, mass, true, new Vector2D(1, -50),
                getPhysicsEngine().getCollisionHandler()));

//        newObject(new Wall(0, 0, 10, SIZE, Color.WHITE, 100,
//                getPhysicsEngine().getCollisionHandler()));
//        newObject(new Wall(SIZE - 10, 0, 10, SIZE, Color.WHITE, 100,
//                getPhysicsEngine().getCollisionHandler()));
//        newObject(new Wall(0, 0, SIZE, 10, Color.WHITE, 100,
//                getPhysicsEngine().getCollisionHandler()));
//        newObject(new Wall(0, SIZE - 10, SIZE, 10, Color.WHITE, 100,
//                getPhysicsEngine().getCollisionHandler()));
//        physicsEngine.add(new Ball(400, 400, r, Color.ORCHID, mass, new Vector2D(-7, 6)));
//        physicsEngine.add(new Ball(500, 300, r, Color.ORANGE, mass, new Vector2D(-3, 39)));
        for (int i = 0; i < 0; i++) {
            newObject(new Ball(
                    Math.random() * SIZE,
                    Math.random() * SIZE,
                    r,
                    Color.rgb((int) (Math.random() * 255),
                            (int) (Math.random() * 255),
                            (int) (Math.random() * 255)),
                    mass, true,
                    new Vector2D(20 * (Math.random() - 0.5),
                            20 * (Math.random() - 0.5)),
                    getPhysicsEngine().getCollisionHandler()));
        }

        forEach(object -> object.get(PhysicsObject2D.class).setRenderVelocityVector(true));
    }

    @Override
    public void updateGame() {
        List.of(getPhysicsEngine().getGameObjects()).forEach(object -> {
            if (object.get(InPlane.class).getX() < Math.abs(object.get(LayerCollider2D.class).minX())) {
                object.get(InPlane.class).setX(Math.abs(object.get(LayerCollider2D.class).minX()));
                object.get(PhysicsObject2D.class)
                        .setVelocity(
                                object.get(PhysicsObject2D.class).getVelocity()
                                        .subtract(
                                                new Vector2D(
                                                        1.9 * object.get(PhysicsObject2D.class).getVelocity().getX(),
                                                        0
                                                )
                                        )
                        );
            }
            if (object.get(InPlane.class).getY() < Math.abs(object.get(LayerCollider2D.class).minY())) {
                object.get(InPlane.class).setY(Math.abs(object.get(LayerCollider2D.class).minY()));
                object.get(PhysicsObject2D.class)
                        .setVelocity(
                                object.get(PhysicsObject2D.class).getVelocity()
                                        .subtract(
                                                new Vector2D(
                                                        0,
                                                        1.9 * object.get(PhysicsObject2D.class).getVelocity().getY()
                                                )
                                        )
                        );
            }
            if (object.get(InPlane.class).getX() > SIZE) {
                object.get(InPlane.class).setX(SIZE);
                object.get(PhysicsObject2D.class)
                        .setVelocity(
                                object.get(PhysicsObject2D.class).getVelocity()
                                        .subtract(
                                                new Vector2D(
                                                        1.9 * object.get(PhysicsObject2D.class).getVelocity().getX(),
                                                        0
                                                )
                                        )
                        );
            }
            if (object.get(InPlane.class).getY() > SIZE) {
                object.get(InPlane.class).setY(SIZE);
                object.get(PhysicsObject2D.class)
                        .setVelocity(
                                object.get(PhysicsObject2D.class).getVelocity()
                                        .subtract(
                                                new Vector2D(
                                                        0,
                                                        1.9 * object.get(PhysicsObject2D.class).getVelocity().getY()
                                                )
                                        )
                        );
            }
        });
    }

//    @Override
//    public void start(Stage stage) {
//        populateObjects();
//
//        VBox root = new VBox();
//        Scene scene = new Scene(root, Color.BLACK);
//        Canvas canvas = new Canvas(SIZE, SIZE);
//
//        root.setStyle("-fx-background-color: black; -fx-padding: 10px;");
////        root.setOnMouseMoved(new EventHandler<MouseEvent>() {
////            @Override public void handle(MouseEvent event) {
////                pointer.setLocation(event.getX(), event.getY());
////            }
////        });
//
////        root.setOnMouseExited(new EventHandler<MouseEvent>() {
////            @Override public void handle(MouseEvent event) {
////
////            }
////        });
//
//        getAnimationTimer(canvas.getGraphicsContext2D()).start();
//
//        stage.setScene(scene);
//        stage.setTitle("Gravity Sim Test");
//        root.getChildren().add(canvas);
//        stage.show();
//    }
}
