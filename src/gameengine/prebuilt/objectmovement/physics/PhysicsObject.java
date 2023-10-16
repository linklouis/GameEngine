package gameengine.prebuilt.objectmovement.physics;

import gameengine.graphics.GraphicsObject;
import gameengine.objects.GameObject;
import gameengine.objects.Modifier;
import gameengine.prebuilt.objectmovement.InPlane;
import gameengine.prebuilt.objectmovement.collisions.Collidable;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PhysicsObject extends GraphicsObject {
    private static final double G = 6.67408 * Math.pow(10, -11);

    private final List<Vector2D> forces = new ArrayList<>();

    private boolean active;

    private long mass;
    private Vector2D velocity = Vector2D.empty();
    private Color color;


    /*
     * Construction:
     */

    public PhysicsObject() {
        super();
    }

    public PhysicsObject(final Modifier[] modifiers) {
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
    public ModifierInstantiateParameter<?>[][] getValidArguments() {
        return new ModifierInstantiateParameter[][] {
                {
                    new ModifierInstantiateParameter<>(
                        "mass", Number.class,
                        (Number num) -> this.mass = num.longValue()),
                    new ModifierInstantiateParameter<>(
                            "color", Color.class,
                            (Color col) -> this.color = col)
                },

                {
                    new ModifierInstantiateParameter<>(
                            "mass", Number.class,
                            (Number num) -> this.mass = num.longValue()),
                    new ModifierInstantiateParameter<>(
                            "color", Color.class,
                            (Color col) -> this.color = col),
                    new ModifierInstantiateParameter<>(
                            "velocity", Vector2D.class,
                            this::setVelocity) },

                {
                        new ModifierInstantiateParameter<>(
                                "mass", Number.class,
                                (Number num) -> this.mass = num.longValue()),
                        new ModifierInstantiateParameter<>(
                                "color", Color.class,
                                (Color col) -> this.color = col)
                },

                {
                        new ModifierInstantiateParameter<>(
                                "mass", Number.class,
                                (Number num) -> this.mass = num.longValue()),
                        new ModifierInstantiateParameter<>(
                                "color", Color.class,
                                (Color col) -> this.color = col),
                        new ModifierInstantiateParameter<>(
                                "velocity", Vector2D.class,
                                this::setVelocity) }
        };
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane.class);
                add(Collidable.class);
            }
        };
    }


    /*
     * Behaviour:
     */

    @Override
    public void paint(final GraphicsContext gc) {
        gc.setFill(getColor());
        for (Collidable.Row row : getParent().get(Collidable.class).getRows()) {
            Collidable.Row adjustedRow = row.at(getLocation());
            adjustedRow.paint(gc);
        }
    }

    public Vector2D forceOfGravity(final PhysicsObject po1) {
        double scalarForce = G * getMass() * po1.getMass()
                / Math.pow(getLocation().distance(po1.getLocation()) * 10, 2);
        return Vector2D
                .displacement(po1.getLocation(), this.getLocation())
                .unitVector()
                .scalarMultiply(scalarForce);
    }

    public void applyForce(final Vector2D force) {
        forces.add(force);
    }

    public void applyForces(final Collection<Vector2D> newForces) {
        forces.addAll(newForces);
    }

    public void updateGravityForces(final PhysicsObject[] objects) {
//        Arrays.stream(objects)
//                .filter(object -> object != this)
//                .forEach(object -> applyForce(forceOfGravity(object)));
        for (PhysicsObject object : objects) {
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

        Vector2D resultantVelocity = Vector2D
                .sum(Vector2D.empty(), forces)
                .scalarDivide(getMass())
                .scalarDivide(frameRate);

        setVelocity(getVelocity().add(resultantVelocity));
        forces.clear();
    }

    public void updatePosition(double frameRate) {
        evaluateForces(frameRate);
        move(getVelocity().scalarDivide(frameRate));
    }

//    public void updatePosition(PhysicsObject[] objects, int frameRate) {
//        move(getVelocity().scalarDivide(frameRate));
//        Collidable collider = null;
//        for (PhysicsObject pObj : objects) {
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
//                        .updateVelocity(this, collider.getParent().get(PhysicsObject.class));
////                        .handleOneMover(this.getCollider(), collider,
////                                Arrays.stream(objects)
////                                        .map(PhysicsObject::getCollider)
////                                        .toList().toArray(new Collidable[0]),
////                                true);
//            }
////            while (!getCollider().isColliding(collider)) {
////                move(getVelocity().scalarDivide(frameRate * 10));
////            }
////            move(getVelocity().scalarDivide(frameRate * -20));
//        }
//    }

    public void move(Vector2D translation) {
        getLocation().setLocation(translation.add(getLocation()).toPoint());
    }


    /*
     * Utilities:
     */

    public Collidable getCollider() {
        return getParent().get(Collidable.class);
    }

    public Point2D.Double getLocation() { // TODO take in a location in instantiate?
        return getParent().get(InPlane.class).getLocation();
    }

    private double getX() {
        return getParent().get(InPlane.class).getX();
    }

    private double getY() {
        return getParent().get(InPlane.class).getY();
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
}
