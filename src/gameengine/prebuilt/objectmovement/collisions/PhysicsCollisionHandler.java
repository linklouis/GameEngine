package gameengine.prebuilt.objectmovement.collisions;

import gameengine.prebuilt.objectmovement.physics.PhysicsObject;
import gameengine.utilities.ModifierInstantiateParameter;
import gameengine.vectormath.Vector2D;

import java.util.*;

public class PhysicsCollisionHandler extends CollisionHandler<PhysicsCollision> {
    // TODO Change so that instead of moving between frames, applies a force away from center of intersecting objects??
    //  Would always be able to use velocity to determine amount to move, and would be able to use rest of physics engine rather than weird rules and math.
    //  Would also be less expensive bc no iterating!!!

    private final double dampening;
    private static final double DEFAULT_DAMPENING = 1;
    private final int iterations;
    private static final int DEFAULT_ITERATIONS = 2;

//    public PhysicsCollisionHandler(Modifier... modifiers) {
//        super(modifiers);
//    }

    public PhysicsCollisionHandler() {
        super();
        this.dampening = DEFAULT_DAMPENING;
        this.iterations = DEFAULT_ITERATIONS;
    }

    public PhysicsCollisionHandler(double dampening) {
        super();
        this.dampening = dampening;
        this.iterations = DEFAULT_ITERATIONS;
    }

    public PhysicsCollisionHandler(double dampening, int iterations) {
        super();
        this.dampening = dampening;
        this.iterations = iterations;
    }

    public PhysicsCollisionHandler(int iterations) {
        super();
        this.dampening = DEFAULT_DAMPENING;
        this.iterations = iterations;
    }

    public PhysicsCollisionHandler(Collection<PhysicsCollision> collisions) {
        super(collisions);
        this.dampening = DEFAULT_DAMPENING;
        this.iterations = DEFAULT_ITERATIONS;
    }

    public PhysicsCollisionHandler(double dampening, Collection<PhysicsCollision> collisions) {
        super(collisions);
        this.dampening = dampening;
        this.iterations = DEFAULT_ITERATIONS;
    }

    public PhysicsCollisionHandler(double dampening, int iterations, Collection<PhysicsCollision> collisions) {
        super(collisions);
        this.dampening = dampening;
        this.iterations = iterations;
    }

    public PhysicsCollisionHandler(int iterations, Collection<PhysicsCollision> collisions) {
        super(collisions);
        this.dampening = DEFAULT_DAMPENING;
        this.iterations = iterations;
    }

    @Override
    protected void handle(PhysicsCollision collision, LayerCollider[] otherCollidersA) {
//        List<LayerCollider> otherList = new ArrayList<>(List.of(otherCollidersA));
//        otherList.remove(collision.getObj1());
//        otherList.remove(collision.getObj2());
//        LayerCollider[] otherColliders = otherList.toArray(new LayerCollider[0]);

        PhysicsObject pObj1 = collision.getObj1().getFromParent(PhysicsObject.class);
        PhysicsObject pObj2 = collision.getObj2().getFromParent(PhysicsObject.class);

//        if (collision.occurring()) {
//            while (collision.occurring()) {
//                pObj1.updatePosition(80);
//                pObj2.updatePosition(80);
//            }
//            inDifferentDirections(collision, otherCollidersA);
//            inSameDirection(collision, otherCollidersA);
            if (pObj1.getVelocity().dotProduct(pObj2.getVelocity()) < 0){//pObj1.getVelocity().magnitude() / 6) {
                updateVelocity(pObj1, pObj2);
//                pObj1.updatePosition(2000);
//                pObj2.updatePosition(2000);
//                inDifferentDirections(collision, otherCollidersA);
            }
//            else {
//                pObj1.updatePosition(80);
//                pObj2.updatePosition(80);
//                System.out.println("a");
//            }
//            else {
//                inSameDirection(collision, otherCollidersA);
//            }
//        }
    }

    public ModifierInstantiateParameter<?>[][] getValidArguments() throws NoSuchFieldException {
        return new ModifierInstantiateParameter<?>[0][0];
    }

    private void inDifferentDirections(Collision collision, LayerCollider[] otherCollidersA) {
        List<LayerCollider> otherList = new ArrayList<>(List.of(otherCollidersA));
        otherList.remove(collision.getObj1());
        otherList.remove(collision.getObj2());
        LayerCollider[] otherColliders = otherList.toArray(new LayerCollider[0]);

        PhysicsObject pObj1 = collision.getObj1().getFromParent(PhysicsObject.class);
        PhysicsObject pObj2 = collision.getObj2().getFromParent(PhysicsObject.class);


        for (int iterationFactor = 2; iterationFactor < iterations * 2; iterationFactor += 2) {
            while (collision.occurring()) {
                pObj1.move(pObj1.getVelocity().unitVector().scalarDivide(-iterationFactor));
                pObj2.move(pObj2.getVelocity().unitVector().scalarDivide(-iterationFactor));
            }
            pObj1.move(pObj1.getVelocity().unitVector().scalarDivide(iterationFactor));
            pObj2.move(pObj2.getVelocity().unitVector().scalarDivide(iterationFactor));
            for (LayerCollider layerCollider : otherColliders) {
                PhysicsObject pObj = layerCollider.getFromParent(PhysicsObject.class);
                if (collision.getObj1().isColliding(layerCollider)) {
                    handleOneMover(layerCollider, collision.getObj1(), otherCollidersA, false);
//                        if (pObj.getVelocity().dotProduct(Vector2D.displacement(pObj.getLocation(), pObj1.getLocation())) < 0) {
//                            pObj.updatePosition(60);
//                        } else {
//                            while (collision.getObj1().isColliding(layerCollider)) {
//                                pObj.move(pObj.getVelocity().scalarDivide(-60));
//                            }
//                        }
                }
                if (collision.getObj2().isColliding(layerCollider)) {
                    handleOneMover(layerCollider, collision.getObj2(), otherCollidersA, false);
//                        if (pObj.getVelocity().dotProduct(Vector2D.displacement(pObj.getLocation(), pObj2.getLocation())) < 0) {
//                            pObj.updatePosition(60);
//                        } else {
//                            while (collision.getObj2().isColliding(layerCollider)) {
//                                pObj.move(pObj.getVelocity().scalarDivide(-60));
//                            }
//                        }
                }
            }
        }
        pObj1.move(pObj1.getVelocity().unitVector().scalarDivide(-iterations * 2));
        pObj2.move(pObj2.getVelocity().unitVector().scalarDivide(-iterations * 2));
        updateVelocity(pObj1, pObj2);
    }

    private void inSameDirection(Collision collision, LayerCollider[] otherCollidersA) {
        List<LayerCollider> otherList = new ArrayList<>(List.of(otherCollidersA));
        otherList.remove(collision.getObj1());
        otherList.remove(collision.getObj2());
        LayerCollider[] otherColliders = otherList.toArray(new LayerCollider[0]);

        PhysicsObject pObj1 = collision.getObj1().getFromParent(PhysicsObject.class);
        PhysicsObject pObj2 = collision.getObj2().getFromParent(PhysicsObject.class);

        for (int iterationFactor = 2; iterationFactor < iterations * 2; iterationFactor += 2) {
            while (collision.occurring()) {
                pObj1.move(Vector2D.displacement(pObj1.getLocation(), pObj2.getLocation())
                        .unitVector()
                        .scalarDivide(iterationFactor));
                pObj2.move(Vector2D.displacement(pObj1.getLocation(), pObj2.getLocation())
                        .unitVector()
                        .scalarDivide(-iterationFactor));
            }
            pObj1.move(Vector2D.displacement(pObj1.getLocation(), pObj2.getLocation())
                    .unitVector()
                    .scalarDivide(-iterationFactor));
            pObj2.move(Vector2D.displacement(pObj2.getLocation(), pObj1.getLocation())
                    .unitVector()
                    .scalarDivide(iterationFactor));
        }
        pObj1.move(Vector2D.displacement(pObj1.getLocation(), pObj2.getLocation())
                .unitVector()
                .scalarDivide(iterations * 2));
        pObj2.move(Vector2D.displacement(pObj2.getLocation(), pObj1.getLocation())
                .unitVector()
                .scalarDivide(-iterations * 2));

//        for (LayerCollider collidable : otherColliders) {
//            PhysicsObject pObj = collidable.getFromParent(PhysicsObject.class);
//            if (collision.getObj1().isColliding(collidable)) {
//                if (pObj.getVelocity().dotProduct(Vector2D.displacement(pObj.getLocation(), collision.getObj1().getLocation())) < 0) {
//                    pObj.updatePosition(60);
//                } else {
//                    while (collision.getObj1().isColliding(collidable)) {
//                        pObj.move(pObj.getVelocity().scalarDivide(-60));
//                    }
//                }
//                handle(collidable, collision.getObj1(), otherCollidersA);
//            }
//            if (collision.getObj2().isColliding(collidable)) {
////                if (pObj.getVelocity().dotProduct(Vector2D.displacement(pObj.getLocation(), collision.getObj2().getLocation())) < 0) {
////                    pObj.updatePosition(60);
////                } else {
////                    while (collision.getObj2().isColliding(collidable)) {
////                        pObj.move(pObj.getVelocity().scalarDivide(-60));
////                    }
////                }
//                handle(collidable, collision.getObj2(), otherCollidersA);
//            }
//        }

        updateVelocity(pObj1, pObj2);
    }

    public void updateVelocity(PhysicsObject pObj1, PhysicsObject pObj2) {
        Vector2D displacement = Vector2D.displacement(pObj2.getLocation(), pObj1.getLocation()).unitVector();
//            pObj1.setVelocity(
//                    displacement.scalarMultiply(
//                            pObj1.getVelocity().dotProduct(displacement.scalarMultiply(-1))
//                    ).scalarMultiply(2)
//            );
//
//            displacement = Vector2D.displacement(pObj1.getLocation(), pObj2.getLocation()).unitVector();
//            pObj2.setVelocity(
//                    displacement.scalarMultiply(
//                            pObj2.getVelocity().dotProduct(displacement.scalarMultiply(-1))
//                    ).scalarMultiply(2)
//            );
        pObj1.setVelocity(
                pObj1.getVelocity()
                        .subtract(
                                displacement.unitVector().scalarMultiply(
                                        pObj1.getVelocity().dotProduct(displacement)
                                ).scalarMultiply(2)
                        )
                        .scalarMultiply(getDampening())
        );

        displacement = Vector2D.displacement(pObj1.getLocation(), pObj2.getLocation()).unitVector();
        pObj2.setVelocity(
                pObj2.getVelocity()
                        .subtract(
                                displacement.scalarMultiply(
                                        pObj2.getVelocity().dotProduct(displacement)
                                ).scalarMultiply(2)
                        )
                        .scalarMultiply(getDampening())
        );
    }


    public void handleOneMover(LayerCollider obj1,
                               LayerCollider obj2,
                               LayerCollider[] otherCollidersA,
                               boolean updateVelocity) {
        List<LayerCollider> otherList = new ArrayList<>(List.of(otherCollidersA));
        otherList.remove(obj1);
        otherList.remove(obj2);
        LayerCollider[] otherColliders = otherList.toArray(new LayerCollider[0]);

        PhysicsObject mover = obj1.getFromParent(PhysicsObject.class);
        PhysicsObject other = obj2.getFromParent(PhysicsObject.class);

        if (obj1.isColliding(obj2)) {
//            for (int iterationFactor = 60; iterationFactor < iterations * 60; iterationFactor += 60) {
//                while (obj1.isColliding(obj2)) {
//                    mover.move(mover.getVelocity().unitVector().scalarDivide(-iterationFactor));
//                }
//                mover.move(mover.getVelocity().unitVector().scalarDivide(iterationFactor));
//                for (LayerCollider collidable : otherColliders) {
//                    PhysicsObject pObj = collidable.getFromParent(PhysicsObject.class);
//                    if (mover.getCollider().isColliding(collidable)) {
//                        if (pObj.getVelocity().dotProduct(Vector2D.displacement(pObj.getLocation(), mover.getLocation())) < 0) {
//                            pObj.updatePosition(60);
//                        } else {
//                            while (mover.getCollider().isColliding(collidable)) {
//                                pObj.move(pObj.getVelocity().scalarDivide(-60));
//                            }
//                        }
//                    }
//                }
//            }

            for (int iterationFactor = 2; iterationFactor < iterations * 2; iterationFactor += 2) {
                while (obj1.isColliding(obj2)) {
                    mover.move(Vector2D.displacement(mover.getLocation(), mover.getLocation())
                            .unitVector()
                            .scalarDivide(iterationFactor));
                }
                mover.move(Vector2D.displacement(mover.getLocation(), mover.getLocation())
                        .unitVector()
                        .scalarDivide(-iterationFactor));
            }
            mover.move(Vector2D.displacement(mover.getLocation(), mover.getLocation())
                    .unitVector()
                    .scalarDivide(iterations * 2));
            for (LayerCollider layerCollider : otherColliders) {
                PhysicsObject pObj = layerCollider.getFromParent(PhysicsObject.class);
                if (mover.getCollider().isColliding(layerCollider)) {
                    handleOneMover(mover.getCollider(), layerCollider, otherCollidersA, false);
                }
            }

            if (updateVelocity) {
                updateVelocity(mover, other);
            }
        }
    }


    public boolean newCollision(final LayerCollider physObj1, final LayerCollider physObj2) {
        if (physObj1 == physObj2 || !physObj1.isColliding(physObj2)) {
            return false;
        }
        if (!(physObj1.getHandler() instanceof PhysicsCollisionHandler
                && physObj2.getHandler() instanceof PhysicsCollisionHandler)) {
            return false;
        }

        PhysicsCollision collision = new PhysicsCollision(physObj1, physObj2);
        for (PhysicsCollision coll : getCollisions()) {
            if (collision.compareTo(coll) == 0) {
                return false;
            }
        }
        getCollisions().add(collision);
        return true;
    }

    public  void getAndHandleAllCollisions(LayerCollider[] colliders,
                                           boolean recheck,
                                           LayerCollider collider,
                                           boolean active,
                                           double velocityScaleFactor) {
        Collision[] currentCollisions =
                getCollisions(collider, colliders)
                        .toArray(new Collision[0]);
        if (currentCollisions.length > 0 && active) {
            PhysicsObject pObj = collider.getFromParent(PhysicsObject.class);
            for (Collision collision : currentCollisions) {
                if (pObj.getVelocity()
                        .dotProduct(
                                Vector2D.displacement(
                                        collision.getObj2().getLocation(),
                                        collider.getLocation()
                                )) > 0) {
                    pObj.move(pObj.getVelocity().scalarDivide(-velocityScaleFactor));
                    updateVelocity(
                            collision.getObj1().getFromParent(PhysicsObject.class),
                            collision.getObj2().getFromParent(PhysicsObject.class));
                }

            }
            return;
        }
        findCollisions(collider, colliders);
        if (recheck) {
            while (!getCollisions().isEmpty()) {
                handleAllCollisions(colliders);
                findCollisions(collider, colliders);
            }
        } else {
            handleAllCollisions(colliders);
        }
    }

    public double getDampening() {
        return dampening;
    }
}
