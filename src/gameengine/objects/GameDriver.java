package gameengine.objects;

import gameengine.graphics.GraphicsDriver;
import gameengine.graphics.Visual;
import gameengine.prebuilt.objectmovement.physics.PhysicsEngine;
import gameengine.prebuilt.objectmovement.physics.PhysicsObject;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class GameDriver extends Application {
     private final List<GameObject> objects = new ArrayList<>();
     private final GraphicsDriver graphicsDriver;
     private final PhysicsEngine physicsEngine;
     private AnimationTimer animationTimer;
     private final String name;

     private final long[] frameTimes = new long[100];
     private int frameTimeIndex = 0 ;
     private double frameRate = 60;


     /*
      * Construction:
      */

    public GameDriver(int width, int height, PhysicsEngine physicsEngine) {
        this.graphicsDriver = new GraphicsDriver(width, height);
        this.physicsEngine = physicsEngine;
        name = "New Game";
    }

    public GameDriver(GraphicsDriver graphicsDriver, PhysicsEngine physicsEngine) {
        this.graphicsDriver = graphicsDriver;
        this.physicsEngine = physicsEngine;
        name = "New Game";
    }

    public GameDriver(String name, int width, int height, PhysicsEngine physicsEngine) {
        this.graphicsDriver = new GraphicsDriver(width, height);
        this.physicsEngine = physicsEngine;
        this.name = name;
    }

    public GameDriver(String name, GraphicsDriver graphicsDriver, PhysicsEngine physicsEngine) {
        this.graphicsDriver = graphicsDriver;
        this.physicsEngine = physicsEngine;
        this.name = name;
    }

    public GameDriver(int width, int height, int numIterations) {
        this.graphicsDriver = new GraphicsDriver(width, height);
        this.physicsEngine = new PhysicsEngine(numIterations, getFrameRateSupplier());
        name = "New Game";
    }

    public GameDriver(GraphicsDriver graphicsDriver, int numIterations) {
        this.graphicsDriver = graphicsDriver;
        this.physicsEngine = new PhysicsEngine(numIterations, getFrameRateSupplier());
        name = "New Game";
    }

    public GameDriver(String name, int width, int height, int numIterations) {
        this.graphicsDriver = new GraphicsDriver(width, height);
        this.physicsEngine = new PhysicsEngine(numIterations, getFrameRateSupplier());
        this.name = name;
    }

    public GameDriver(String name, GraphicsDriver graphicsDriver, int numIterations) {
        this.graphicsDriver = graphicsDriver;
        this.physicsEngine = new PhysicsEngine(numIterations, getFrameRateSupplier());
        this.name = name;
    }


    public abstract void initialize();

    @Override
    public void start(Stage stage) {
        stage.setTitle(name);
        initialize();
        graphicsDriver.initialize(stage);
        animationTimer = setupAnimationTimer(graphicsDriver.getGraphicsContext());

        animationTimer.start();
    }


    /*
     * Functionality:
     */

    public abstract void updateGame();

    protected AnimationTimer setupAnimationTimer(GraphicsContext gc) {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateFrameRateCounter(now);

                updateGame();
                physicsEngine.updateObjects();
                graphicsDriver.updateGraphics(gc);
            }
        };
    }

    private void updateFrameRateCounter(long now) {
        long oldFrameTime = frameTimes[frameTimeIndex];
        frameTimes[frameTimeIndex] = now;
        frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
        long elapsedNanos = now - oldFrameTime;
        long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
        frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
        if (frameRate < 1 || frameRate > 500) {
            frameRate = 1_000_000_000.0 / (now - frameTimes[(frameTimeIndex == 0 ? frameTimes.length : frameTimeIndex) - 1]);
        }
//        System.out.println(frameRate);
    }


    /*
     * Utilities:
     */

    public void newObject(GameObject object) {
        if (object.containsModifier(PhysicsObject.class)) {
            physicsEngine.add(object.get(PhysicsObject.class));
        }
        if (object.containsModifier(Visual.class)) {
            graphicsDriver.add(object.get(Visual.class));
        }

        objects.add(object);
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public GraphicsDriver getGraphicsDriver() {
        return graphicsDriver;
    }

    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    public String getName() {
        return name;
    }

    public double getFrameRate() {
        return frameRate;
    }

    public Supplier<Double> getFrameRateSupplier() {
        return this::getFrameRate;
    }
}
