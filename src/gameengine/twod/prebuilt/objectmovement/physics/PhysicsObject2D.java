package gameengine.twod.prebuilt.objectmovement.physics;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.twod.graphics.GraphicsObject2D;
import gameengine.twod.prebuilt.objectmovement.InPlane;
import gameengine.twod.prebuilt.objectmovement.collisions.Collider2D;
import gameengine.twod.prebuilt.objectmovement.collisions.LayerCollider2D;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector;
import gameengine.vectormath.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PhysicsObject2D extends GraphicsObject2D {
    private static final double G = 6.67408 * Math.pow(10, -11);

    private final List<Vector2D> forces = new ArrayList<>();

    private boolean active = true;
    private boolean hasGravity = false;
    private boolean renderVelocityVector = false;

    private long mass;
    private Vector2D velocity = new Vector2D(0);
    private Color color;


    /*
     * Construction:
     */

    public PhysicsObject2D() {
        super();
    }

    public PhysicsObject2D(final Modifier[] modifiers) {
        super(modifiers);
    }

    @Override
    public void instantiate(final GameObject parent,
                            final Object... args) {
        try {
            super.instantiate(parent, args);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArgumentContext[] getArgumentContexts() { // TODO: setup better way to do this, to name the variables you're passing in
        return new ArgumentContext[] {
                new ArgumentContext(
                        new ModifierInstantiateParameter<>(
                                "mass", Number.class,
                                (Number num) -> this.mass = num.longValue()),
                        new ModifierInstantiateParameter<>(
                                "color", Color.class, this::setColor)
                ),

                new ArgumentContext(
                        new ModifierInstantiateParameter<>(
                                "mass", Number.class,
                                (Number num) -> this.mass = num.longValue()),
                        new ModifierInstantiateParameter<>(
                                "color", Color.class, this::setColor),
                        new ModifierInstantiateParameter<>(
                                "velocity", Vector2D.class, this::setVelocity)
                ),

                new ArgumentContext(
                        new ModifierInstantiateParameter<>(
                                "mass", Number.class,
                                (Number num) -> this.mass = num.longValue()),
                        new ModifierInstantiateParameter<>(
                                "color", Color.class, this::setColor),
                        new ModifierInstantiateParameter<>(
                                "active", Boolean.class, this::setActive)
                ),

                new ArgumentContext(
                        new ModifierInstantiateParameter<>(
                                "mass", Number.class,
                                (Number num) -> this.mass = num.longValue()),
                        new ModifierInstantiateParameter<>(
                                "color", Color.class, this::setColor),
                        new ModifierInstantiateParameter<>(
                                "velocity", Vector2D.class, this::setVelocity),
                        new ModifierInstantiateParameter<>(
                                "active", Boolean.class, this::setActive)
                ),

                new ArgumentContext(
                        new ModifierInstantiateParameter<>(
                                "mass", Number.class,
                                (Number num) -> this.mass = num.longValue()),
                        new ModifierInstantiateParameter<>(
                                "color", Color.class, this::setColor),
                        new ModifierInstantiateParameter<>(
                                "active", Boolean.class, this::setActive),
                        new ModifierInstantiateParameter<>(
                                "hasGravity", Boolean.class, this::setHasGravity)
                ),

                new ArgumentContext(
                        new ModifierInstantiateParameter<>(
                                "mass", Number.class,
                                (Number num) -> this.mass = num.longValue()),
                        new ModifierInstantiateParameter<>(
                                "color", Color.class, this::setColor),
                        new ModifierInstantiateParameter<>(
                                "velocity", Vector2D.class, this::setVelocity),
                        new ModifierInstantiateParameter<>(
                                "active", Boolean.class, this::setActive),
                        new ModifierInstantiateParameter<>(
                                "hasGravity", Boolean.class, this::setHasGravity)
                )
        };
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane.class);
                add(LayerCollider2D.class);
            }
        };
    }


    /*
     * Behaviour:
     */

    @Override
    public void paint(final GraphicsContext gc) {
//        gc.setFill(getColor());
        getFromParent(Collider2D.class).paint(gc, getColor());
//        for (LayerCollider2D.Row row : getFromParent(LayerCollider2D.class).getRows()) {
//            LayerCollider2D.Row adjustedRow = row.at(getLocation());
//            adjustedRow.paint(gc);
//        }
        if (renderVelocityVector) {
//            gc.setFill(getColor().darker());
            gc.setLineWidth(1);
            gc.setStroke(getColor().darker());
            gc.strokeLine(getX(), getY(), getX() + getVelocity().getX(),
                    getY() + getVelocity().getY());
//            gc.moveTo(getX(), getY());
//            gc.lineTo(getX() + getVelocity().getX() * 10,
//                    getY() + getVelocity().getY() * 10);
//            System.out.println((getX() + getVelocity().getX() * 10) +", " +
//                    (getY() + getVelocity().getY() * 10));
        }
    }

    public Vector2D forceOfGravity(final PhysicsObject2D po1) {
        if (po1.hasGravity()) {
            double scalarForce = G * getMass() * po1.getMass()
                    / Math.pow(getLocation().distance(po1.getLocation()) * 10, 2);
            return Vector2D
                    .displacement(po1.getLocation(), this.getLocation())
                    .unitVector()
                    .scalarMultiply(scalarForce);
        }
        return new Vector2D(0);
    }

    public void applyForce(final Vector2D force) {
        forces.add(force);
    }

    public void applyForces(final Collection<Vector2D> newForces) {
        forces.addAll(newForces);
    }

    public void updateGravityForces(final PhysicsObject2D[] objects) {
//        Arrays.stream(objects)
//                .filter(object -> object != this)
//                .forEach(object -> applyForce(forceOfGravity(object)));
        for (PhysicsObject2D object : objects) {
            if (object != this) {
                applyForce(forceOfGravity(object));
            }
        }
    }

    public void evaluateForces(final double frameRate) {
//        Vector2D netForce = Vector2D.empty();
//
//        for (Vector2D force : forces) {
//            netForce = netForce.add(force);
//        }

//        Vector2D resultantVelocity =
//                netForce
//                        .scalarDivide(getMass())
//                        .scalarDivide(frameRate);

        Vector2D resultantVelocity = Vector
                .sum(new Vector2D(0), forces)
                .scalarDivide(getMass())
                .scalarDivide(frameRate);

        setVelocity(getVelocity().add(resultantVelocity));
        forces.clear();
    }

    public void updatePosition(double frameRate) {
        evaluateForces(frameRate);
        move(getVelocity().scalarDivide(frameRate));
    }

//    public void updatePosition(PhysicsObject2D[] objects, int frameRate) {
//        move(getVelocity().scalarDivide(frameRate));
//        LayerCollider2D collider = null;
//        for (PhysicsObject2D pObj : objects) {
//            if (pObj != this && getCollider().isColliding(pObj)) {
//                move(getVelocity().scalarDivide(-frameRate * 2));
//                collider = pObj.getCollider();
//                return true;
//            }
//        }
//        if (collider != null) {
//            if (getCollider().getHandler() instanceof PhysicsCollisionHandler
//                    && collider.getHandler() instanceof PhysicsCollisionHandler) {
//                ((PhysicsCollisionHandler) collider.getHandler())
//                        .updateVelocity(this, collider.getFromParent(PhysicsObject2D.class));
////                        .handleOneMover(this.getCollider(), collider,
////                                Arrays.stream(objects)
////                                        .map(PhysicsObject2D::getCollider)
////                                        .toList().toArray(new LayerCollider2D[0]),
////                                true);
//            }
////            while (!getCollider().isColliding(collider)) {
////                move(getVelocity().scalarDivide(frameRate * 10));
////            }
////            move(getVelocity().scalarDivide(frameRate * -20));
//        }
//    }

    public void move(Vector2D translation) {
        if (isActive()) {
            getLocation().setLocation(translation.add(getLocation()).toPoint());
        }
    }


    /*
     * Utilities:
     */

    public LayerCollider2D getCollider() {
        return getFromParent(LayerCollider2D.class);
    }

    public Point2D.Double getLocation() { // TODO take in a location in instantiate?
        return getFromParent(InPlane.class).getLocation();
    }

    private double getX() {
        return getFromParent(InPlane.class).getX();
    }

    private double getY() {
        return getFromParent(InPlane.class).getY();
    }

    public double getMass() {
        return mass;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Vector2D> getForces() {
        return Collections.unmodifiableList(forces);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean hasGravity() {
        return hasGravity;
    }

    public void setHasGravity(boolean hasGravity) {
        this.hasGravity = hasGravity;
    }

    public boolean isRenderVelocityVector() {
        return renderVelocityVector;
    }

    public void setRenderVelocityVector(boolean renderVelocityVector) {
        this.renderVelocityVector = renderVelocityVector;
    }
}
