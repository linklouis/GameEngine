package gameengine.prebuilt.physics;

import gameengine.objects.Modifier;
import gameengine.vectormath.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class PhysicsCollisionHandler extends CollisionHandler {

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

    @Override
    public void handle(Collision collision, Collidable[] otherCollidersA) {
//        List<Collidable> otherList = new ArrayList<>(List.of(otherCollidersA));
//        otherList.remove(collision.getObj1());
//        otherList.remove(collision.getObj2());
//        Collidable[] otherColliders = otherList.toArray(new Collidable[0]);

        PhysicsObject pObj1 = collision.getObj1().getParent().get(PhysicsObject.class);
        PhysicsObject pObj2 = collision.getObj2().getParent().get(PhysicsObject.class);

        if (collision.occurring()) {
//            inDifferentDirections(collision, otherCollidersA);
            inSameDirection(collision, otherCollidersA);
//            if (pObj1.getVelocity().dotProduct(pObj2.getVelocity()) < pObj1.getVelocity().magnitude() / 6) {
//                inDifferentDirections(collision, otherCollidersA);
//            } else {
//                inSameDirection(collision, otherCollidersA);
//            }
        }
    }

    private void inDifferentDirections(Collision collision, Collidable[] otherCollidersA) {
        List<Collidable> otherList = new ArrayList<>(List.of(otherCollidersA));
        otherList.remove(collision.getObj1());
        otherList.remove(collision.getObj2());
        Collidable[] otherColliders = otherList.toArray(new Collidable[0]);

        PhysicsObject pObj1 = collision.getObj1().getParent().get(PhysicsObject.class);
        PhysicsObject pObj2 = collision.getObj2().getParent().get(PhysicsObject.class);


        for (int iterationFactor = 2; iterationFactor < iterations * 2; iterationFactor += 2) {
            while (collision.occurring()) {
                pObj1.move(pObj1.getVelocity().unitVector().scalarDivide(-iterationFactor));
                pObj2.move(pObj2.getVelocity().unitVector().scalarDivide(-iterationFactor));
            }
            pObj1.move(pObj1.getVelocity().unitVector().scalarDivide(iterationFactor));
            pObj2.move(pObj2.getVelocity().unitVector().scalarDivide(iterationFactor));
            for (Collidable collidable : otherColliders) {
                PhysicsObject pObj = collidable.getParent().get(PhysicsObject.class);
                if (collision.getObj1().isColliding(collidable)) {
                    handle(collidable, collision.getObj1(), otherCollidersA);
//                        if (pObj.getVelocity().dotProduct(Vector2D.displacement(pObj.getLocation(), pObj1.getLocation())) < 0) {
//                            pObj.updatePosition(60);
//                        } else {
//                            while (collision.getObj1().isColliding(collidable)) {
//                                pObj.move(pObj.getVelocity().scalarDivide(-60));
//                            }
//                        }
                }
                if (collision.getObj2().isColliding(collidable)) {
                    handle(collidable, collision.getObj2(), otherCollidersA);
//                        if (pObj.getVelocity().dotProduct(Vector2D.displacement(pObj.getLocation(), pObj2.getLocation())) < 0) {
//                            pObj.updatePosition(60);
//                        } else {
//                            while (collision.getObj2().isColliding(collidable)) {
//                                pObj.move(pObj.getVelocity().scalarDivide(-60));
//                            }
//                        }
                }
            }
        }
        pObj1.move(pObj1.getVelocity().unitVector().scalarDivide(-iterations * 2));
        pObj2.move(pObj2.getVelocity().unitVector().scalarDivide(-iterations * 2));
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

    private void inSameDirection(Collision collision, Collidable[] otherCollidersA) {
        List<Collidable> otherList = new ArrayList<>(List.of(otherCollidersA));
        otherList.remove(collision.getObj1());
        otherList.remove(collision.getObj2());
        Collidable[] otherColliders = otherList.toArray(new Collidable[0]);

        PhysicsObject pObj1 = collision.getObj1().getParent().get(PhysicsObject.class);
        PhysicsObject pObj2 = collision.getObj2().getParent().get(PhysicsObject.class);

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

//        for (Collidable collidable : otherColliders) {
//            PhysicsObject pObj = collidable.getParent().get(PhysicsObject.class);
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


    public void handle(Collidable obj1, Collidable obj2, Collidable[] otherCollidersA) {
        List<Collidable> otherList = new ArrayList<>(List.of(otherCollidersA));
        otherList.remove(obj1);
        otherList.remove(obj2);
        Collidable[] otherColliders = otherList.toArray(new Collidable[0]);

        PhysicsObject mover = obj1.getParent().get(PhysicsObject.class);
        PhysicsObject other = obj2.getParent().get(PhysicsObject.class);

        if (obj1.isColliding(obj2)) {
//            for (int iterationFactor = 60; iterationFactor < iterations * 60; iterationFactor += 60) {
//                while (obj1.isColliding(obj2)) {
//                    mover.move(mover.getVelocity().unitVector().scalarDivide(-iterationFactor));
//                }
//                mover.move(mover.getVelocity().unitVector().scalarDivide(iterationFactor));
//                for (Collidable collidable : otherColliders) {
//                    PhysicsObject pObj = collidable.getParent().get(PhysicsObject.class);
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
            for (Collidable collidable : otherColliders) {
                PhysicsObject pObj = collidable.getParent().get(PhysicsObject.class);
                if (mover.getCollider().isColliding(collidable)) {
                    handle(mover.getCollider(), collidable, otherCollidersA);
                }
            }

//            mover.move(mover.getVelocity().unitVector().scalarDivide(-iterations * 60));
//            Vector2D displacement = Vector2D.displacement(other.getLocation(), mover.getLocation()) .unitVector();
//            mover.setVelocity(
//                    mover.getVelocity()
//                            .subtract(
//                                    displacement.unitVector().scalarMultiply(
//                                            mover.getVelocity().dotProduct(displacement)
//                                    ).scalarMultiply(2)
//                            )
//                            .scalarMultiply(getDampening())
//            );
        }
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>();
    }


    public double getDampening() {
        return dampening;
    }
}
