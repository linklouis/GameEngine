import java.util.List;

public abstract class Modifier extends GameObject {
    private final GameObject parent;

    public Modifier(GameObject parent) {
        super();
        this.parent = parent;
    }

    public Modifier(GameObject parent, Modifier[] modifiers) {
        super(modifiers);
        this.parent = parent;
    }

    public GameObject getParent() {
        return parent;
    }

    public abstract List<Class<? extends Modifier>> getDependencies();
}
