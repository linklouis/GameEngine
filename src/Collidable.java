import javafx.scene.canvas.GraphicsContext;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Collidable extends Modifier {
    /**
     * Number of rows per 1 vertical unit
     */
    public static final int RESOLUTION = 1;

    public static final double ROW_HEIGHT = 1.0 / RESOLUTION;

    private Row[] rows;

    public Collidable() {
        super();
    }

    public Collidable(Modifier[] modifiers) {
        super(modifiers);
    }

    @Override
    public void instantiate(GameObject parent, Object... args) {
        super.instantiate(parent);
        if (args[0] instanceof Row[]) {
            rows = (Row[]) args[0];
        }
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane.class);
            }
        };
    }

    protected Point2D.Double getLocation() { // TODO take in a location in instantiate?
        return getParent().get(InPlane.class).getLocation();
    }

    private double getX() {
        return getParent().get(InPlane.class).getX();
    }

    private double getY() {
        return getParent().get(InPlane.class).getY();
    }

    public boolean isColliding(GameObject gObj) {
        assert gObj.containsModifier(Collidable.class);
        Collidable coll = gObj.get(Collidable.class);
        return isColliding(coll);
    }

    public boolean isColliding(Collidable coll) {
//        double thisMaxX = maxX();
//        double thisMinX = minX();
        if (
                (
                        maxY() > coll.minY() && minY() < coll.maxY() ||
                                coll.maxY() > minY() && coll.minY() < maxY()
                ) && (
                        maxX() > coll.minX() && minX() < coll.maxX() ||
                                coll.maxX() > minX() && coll.minX() < maxX()
                )
        ) {
            for (double y = Math.max(minY(), coll.minY()); y < Math.min(maxY(), coll.maxY()); y += ROW_HEIGHT) {
                if (rowAt(y).atX(getX()).isColliding(coll.rowAt(y).atX(coll.getX()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public Row getColliding(GameObject gObj) { // TODO
        assert gObj.containsModifier(Collidable.class);
        Collidable coll = gObj.get(Collidable.class);
        if (
                (
                        maxY() > coll.minY() && minY() < coll.maxY() ||
                                coll.maxY() > minY() && coll.minY() < maxY()
                ) && (
                        maxX() > coll.minX() && minX() < coll.maxX() ||
                                coll.maxX() > minX() && coll.minX() < maxX()
                )
        ) {
            for (double y = Math.max(minY(), coll.minY()); y < Math.min(maxY(), coll.maxY()); y += ROW_HEIGHT) {
                if (rowAt(y).atX(getX()).isColliding(coll.rowAt(y).atX(coll.getX()))) {
                    return coll.rowAt(y);
                }
            }
        }
        return null;
    }

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

    public double maxY() {
        return getY() + get(getRows().length - 1).maxY();
    }

    public double minY() {
        return getY() + get(0).minY();
    }

    public double height() {
        return getRows().length * ROW_HEIGHT;
    }

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
                    + y + " out of bounds for GameObject of height "
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
