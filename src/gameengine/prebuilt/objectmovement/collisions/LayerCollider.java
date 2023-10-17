package gameengine.prebuilt.objectmovement.collisions;

import gameengine.objects.GameObject;
import gameengine.objects.Modifier;
import gameengine.prebuilt.objectmovement.InPlane;
import gameengine.prebuilt.objectmovement.physics.PhysicsObject;
import gameengine.utilities.ArgumentContext;
import gameengine.utilities.ModifierInstantiateParameter;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public final class LayerCollider extends Collider<LayerCollider> {
    /**
     * Number of rows per 1 vertical unit
     */
    public static final int RESOLUTION = 1;
    public static final double ROW_HEIGHT = 1.0 / RESOLUTION;
    private Row[] rows;


    public LayerCollider() {
        super();
    }
    public LayerCollider(Modifier... modifiers) {
        super(modifiers);
    }

//    @Override
//    public void instantiate(GameObject parent, Object... args) {
//        super.instantiate(parent, args);
//        //        if (args[0] instanceof Row[] && args[1] instanceof CollisionHandler) {
////            rows = (Row[]) args[0];
////            handler = (CollisionHandler) args[1];
////        } else {
////            throw new IllegalArgumentException(); // TODO
////        }
//    }

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[] {
                new ArgumentContext(
                    new ModifierInstantiateParameter<>(
                            "rows", Row[].class,
                            this::setRows),
                    new ModifierInstantiateParameter<>(
                                "collisionHandler", CollisionHandler.class,
                                this::setHandler)
                )
        };
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        List<Class<? extends Modifier>> modifiers = new ArrayList<>();
        modifiers.add(InPlane.class);
        if (getHandler() instanceof PhysicsCollisionHandler) {
            modifiers.add(PhysicsObject.class);
        }
        return modifiers;
    }

    public Point2D.Double getLocation() { // TODO take in a location in instantiate?
        return getFromParent(InPlane.class).getLocation();
    }

    public Point2D.Double getCenter() {
        double centerX = 0;
        for (Row row : getRows()) {
            centerX += row.getMaxX() + row.getMinX();
        }
        centerX /= getRows().length;
        return new Point2D.Double(getX() + centerX, getY() + (maxY() + minY()) / 2.0);
    }

    private double getX() {
        return getFromParent(InPlane.class).getX();
    }

    private double getY() {
        return getFromParent(InPlane.class).getY();
    }

    @Override
    public boolean isColliding(LayerCollider coll) {
//        double thisMaxX = maxX();
//        double thisMinX = minX();
        if (inRange(coll)) {
            for (double y = Math.max(minY(), coll.minY()); y < Math.min(maxY(), coll.maxY()); y += ROW_HEIGHT) {
                if (rowAt(y).atX(getX()).isColliding(coll.rowAt(y).atX(coll.getX()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public Row getColliding(GameObject gObj) { // TODO
        assert gObj.containsModifier(LayerCollider.class);
        LayerCollider coll = gObj.get(LayerCollider.class);
        if (inRange(coll)) {
            for (double y = Math.max(minY(), coll.minY()); y < Math.min(maxY(), coll.maxY()); y += ROW_HEIGHT) {
                if (rowAt(y).atX(getX()).isColliding(coll.rowAt(y).atX(coll.getX()))) {
                    return coll.rowAt(y);
                }
            }
        }
        return null;
    }

    @Override
    public double minX() {
        double min = get(0).getMinX();
        for (int i = 0; java.lang.Double.isNaN(min) && i < getRows().length; i++) {
            min = get(i).getMinX();
        }
        for (Row row : rows) {
            if (row.getMinX() < min) {
                min = row.getMinX();
            }
        }
        return getX() + min;
    }

    public double minX(int index) {
        return getX() + get(index).getMinX();
    }

    @Override
    public double maxX() {
        double max = get(0).getMinX();
        for (int i = 0; java.lang.Double.isNaN(max) && i < getRows().length; i++) {
            max = get(i).getMaxX();
        }
        for (Row row : rows) {
            if (row.getMaxX() > max) {
                max = row.getMaxX();
            }
        }
        return getX() + max;
    }

    public double maxX(int index) {
        return getY() + get(index).getMaxX();
    }

    @Override
    public double maxY() {
        return getY() + get(getRows().length - 1).maxY();
    }

    @Override
    public double minY() {
        return getY() + get(0).minY();
    }

    @Override
    public Class<LayerCollider> getColliderClass() {
        return LayerCollider.class;
    }

    public double height() {
        return getRows().length * ROW_HEIGHT;
    }

    @Override
    public void paint(GraphicsContext gc, Color color) {
        gc.setFill(color);
        for (LayerCollider.Row row : getFromParent(LayerCollider.class).getRows()) {
            LayerCollider.Row adjustedRow = row.at(getLocation());
            adjustedRow.paint(gc);
        }
    }




    /*
     * Layer Functionality:
     */

    public Row[] getRows() {
        return rows;
    }

    public Row rowAtHeight(int index) {
        return get(index).atHeight(getY());
    }

    public Row get(int index) {
        return getRows()[index];
    }

    public void setRows(Row[] rows) {
        this.rows = rows;
    }

    public void setRow(int index, Row row) {
        getRows()[index] = row;
    }

    public Row rowAt(double y) {
        if (y > maxY() || y < minY()) {
            throw new IllegalArgumentException("y value of "
                    + y + " out of bounds for gameengine.objects.GameObject of height "
                    + height() + " at (" + getX() + ", " + getY() + ")"
            );
        }

        return get((int) ((y - minY()) / ROW_HEIGHT));//(getRows().length / 2.0 + (y - centerY()) / ROW_HEIGHT + 0.5));
    }

    public void newRow(int index, double minX, double maxX) {
        double y = (index - getRows().length / 2.0) * ROW_HEIGHT;
        setRow(index, new Row(y, ROW_HEIGHT, minX, maxX));
    }

    public static class Row {
        private final double y, height;
        private final double minX, maxX;

        private Row(double y, double height, double left, double right) {
            this.y = y;
            this.height = height;
            this.minX = left;
            this.maxX = right;
        }

        public double getY() {
            return y;
        }

        public double getHeight() {
            return height;
        }

        public double getMinX() {
            return minX;
        }

        public double getMaxX() {
            return maxX;
        }

        public double minY() {
            return getY() - getHeight() / 2.0;
        }

        public double maxY() {
            return getY() + getHeight() / 2.0;
        }

        public double width() {
            return getMaxX() - getMinX();
        }

        public Point2D.Double topLeft() {
            return new Point2D.Double(getMinX(), minY());
        }

        public Point2D.Double topRight() {
            return new Point2D.Double(getMaxX(), minY());
        }

        public Point2D.Double bottomLeft() {
            return new Point2D.Double(getMinX(), maxY());
        }

        public Point2D.Double bottomRight() {
            return new Point2D.Double(getMaxX(), maxY());
        }

        public Row atHeight(double height) {
            return new Row(getY() + height, getHeight(), getMinX(), getMaxX());
        }

        public Row atX(double x) {
            return new Row(getY(), getHeight(), getMinX() + x, getMaxX() + x);
        }

        public Row at(Point2D.Double location) {
            return new Row(getY() + location.getY(), getHeight(), getMinX() + location.getX(), getMaxX() + location.getX());
        }

        /**
         * Assumes the two {@link Row}s (the object the method was called on
         * and {@link @param row}) are at the same y.
         *
         * @param row The {@link Row} to check for collision with
         * @return true if the two rows have a 1D collision.
         */
        public boolean isColliding(Row row) {
            return (getMinX() < row.getMaxX() && getMaxX() > row.getMinX())
                    || (row.getMinX() < getMaxX() && row.getMaxX() > getMinX());
        }

        public void paint(GraphicsContext gc) {
            gc.fillRect(getMinX(), minY(), width(), getHeight());
        }
    }
}
