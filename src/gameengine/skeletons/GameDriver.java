package gameengine.skeletons;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class GameDriver<GraphicsDriverType extends GraphicsDriver, PhysicsEngineType extends PhysicsEngine> extends Application {
    private final List<GameObject> objects = new ArrayList<>();
    private final GraphicsDriverType graphicsDriver;
    private final PhysicsEngineType physicsEngine;
    private AnimationTimer animationTimer;
    private final String name;

    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0 ;
    private double frameRate = 60;


    /*
     * Construction:
     */

    public GameDriver(GraphicsDriverType graphicsDriver, PhysicsEngineType physicsEngine) {
        this.graphicsDriver = graphicsDriver;
        this.physicsEngine = physicsEngine;
        physicsEngine.setFrameRateSupplier(getFrameRateSupplier());
        name = "New Game";
    }

    public GameDriver(String name, GraphicsDriverType graphicsDriver, PhysicsEngineType physicsEngine) {
        this.graphicsDriver = graphicsDriver;
        this.physicsEngine = physicsEngine;
        physicsEngine.setFrameRateSupplier(getFrameRateSupplier());
        this.name = name;
    }

    public abstract void initialize();

    @Override
    public void start(Stage stage) {
        stage.setTitle(name);
        initialize();
        graphicsDriver.initialize(stage);
        animationTimer = setupAnimationTimer();

        animationTimer.start();
    }


    /*
     * Functionality:
     */

    public abstract void updateGame();

    protected AnimationTimer setupAnimationTimer() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateFrameRateCounter(now);

                updateGame();
                physicsEngine.updateObjects();
                graphicsDriver.updateGraphics();
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

    public abstract void newObject(GameObject object);

    public void forEach(Consumer<GameObject> function) {
        objects.forEach(function);
    }


    /*
     * Utilities:
     */

    public List<GameObject> getObjects() {
        return objects;
    }

    public GraphicsDriverType getGraphicsDriver() {
        return graphicsDriver;
    }

    public PhysicsEngineType getPhysicsEngine() {
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
