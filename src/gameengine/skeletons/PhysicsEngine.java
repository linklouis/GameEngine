package gameengine.skeletons;

import gameengine.twod.prebuilt.objectmovement.physics.PhysicsObject2D;

import java.util.ArrayList;
import java.util.function.Supplier;

public abstract class PhysicsEngine<PhysicsObjectType> extends ArrayList<PhysicsObjectType> {
    // TODO
    private Supplier<Double> frameRateSupplier;

    public abstract void updateObjects();

    protected abstract void updateGravityForces();

    protected abstract void evaluateForces();

    public double getFrameRate() {
        return Math.round(frameRateSupplier.get());
    }

    public void setFrameRateSupplier(Supplier<Double> newFrameRateSupplier) {
        this.frameRateSupplier = newFrameRateSupplier;
    }
}
