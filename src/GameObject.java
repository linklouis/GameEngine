import java.awt.*;

public abstract class GameObject extends Point {
    // somehow enforce that the GameObj is the same type as the one the method is called on?
    public abstract boolean isColliding(GameObject gObj);

    public abstract double left();

    public abstract double right();

    public abstract double top();

    public abstract double bottom();

    public abstract Point center();
}
