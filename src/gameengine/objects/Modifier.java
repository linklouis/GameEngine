package gameengine.objects;

import java.util.List;
import java.util.Map;

public abstract class Modifier extends GameObject {
    private GameObject parent;
    private boolean instantiated = false;

    public Modifier() {
        super();
    }

    public Modifier(Modifier... modifiers) {
        super(modifiers);
    }

    public void instantiate(GameObject parent, Object... args) {
        if (!instantiated) {
            this.parent = parent;
            instantiated = true;
            parent.ensureDependencies();
        }
    }
    public GameObject getParent() {
        return parent;
    }

    public abstract List<Class<? extends Modifier>> getDependencies();

    public abstract Map<String, Class<?>>[] getValidArguments();
}
