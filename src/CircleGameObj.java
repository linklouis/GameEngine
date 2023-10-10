import java.awt.*;

public class CircleGameObj {//} extends GameObject {
    private final double R;

    public CircleGameObj(double r) {
        R = r;
    }

    public double getR() {
        return R;
    }

//    @Override
//    public boolean isColliding(GameObject gObj) {
//        try {
//            return distance(gObj) <= ((CircleGameObj) gObj).getR() + getR();
//        } catch (Exception e) {
//            throw new IllegalArgumentException("CircleGameObjects can"
//                    + "only detect collisions with other CircleGameObjects." + e);
//        }
//    }
//
//    @Override
//    public double left() {
//        return ;
//    }
//
//    @Override
//    public double right() {
//        return 0;
//    }
//
//    @Override
//    public double yMax() {
//        return 0;
//    }
//
//    @Override
//    public double yMin() {
//        return 0;
//    }
//
//    @Override
//    public Point center() {
//        return null;
//    }
}
