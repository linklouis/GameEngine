package gameengine.twod;

import gameengine.vectormath.Vector2D;

public record Rect(Vector2D min, Vector2D max) {
    public Rect(Vector2D min, Vector2D max, boolean correctValues) {
        this(new Vector2D(Math.min(min.getX(), max.getX()), Math.min(min.getY(), max.getY())),
                new Vector2D(Math.max(min.getX(), max.getX()), Math.max(min.getY(), max.getY())));
    }

    public boolean contains(Vector2D point) {
        return min.getX() < point.getX() && max.getX() > point.getX()
                && min.getY() < point.getY() && max.getY() > point.getY();
    }
}
