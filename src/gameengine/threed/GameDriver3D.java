package gameengine.threed;

import gameengine.skeletons.GameObject;
import gameengine.threed.graphics.GraphicsDriver3D;
import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class GameDriver3D extends Application {
    private final List<GameObject> objects = new ArrayList<>();
    private final GraphicsDriver3D graphicsDriver;
    private AnimationTimer animationTimer;
    private final String name;

    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0 ;
    private double frameRate = 60;
    /*
     * Construction:
     */

    public GameDriver3D(GraphicsDriver3D graphicsDriver) {
        this.graphicsDriver = graphicsDriver;
        name = "New Game";
    }

    public GameDriver3D(String name, GraphicsDriver3D graphicsDriver) {
        this.graphicsDriver = graphicsDriver;
        this.name = name;
    }


    /*
     * Functionality:
     */

    public abstract void initialize();

    public void start(Stage stage) {
        stage.setTitle(name);
        initialize();
        graphicsDriver.initialize(stage);
        animationTimer = setupAnimationTimer();

        animationTimer.start();
    }

    public abstract void updateGame();

    public void forEach(Consumer<GameObject> function) {
        objects.forEach(function);
    }

    protected AnimationTimer setupAnimationTimer() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateFrameRateCounter(now);

                updateGame();
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
    }


    /*
     * Utilities:
     */

    public void newObject(GameObject object) {
        if (object.containsModifier(RayTraceable.class)) {
            getGraphicsDriver().add(object.get(RayTraceable.class));
        }

        getObjects().add(object);
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public GraphicsDriver3D getGraphicsDriver() {
        return graphicsDriver;
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
