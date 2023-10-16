package gameengine.objects;

import gameengine.utilities.ArgumentContext;

import java.util.Arrays;
import java.util.List;

public abstract class Modifier extends GameObject {
    private GameObject parent;
    private boolean instantiated = false;

    public Modifier() {
        super();
    }

    public Modifier(Modifier... modifiers) {
        super(modifiers);
    }

    @Override
    public <T extends Modifier> T get(Class<T> modifierClass) {
        for (Modifier modifier : getModifiers()) {
            if (modifierClass.isAssignableFrom(modifier.getClass())) {
                return (T) modifier;
            }
        }
        getParent().ensureDependencies();
        return null;
    }

    public void instantiate(GameObject parent, Object... args) throws NoSuchFieldException, IllegalAccessException {
        if (!instantiated) {
            this.parent = parent;
            instantiated = true;
            parent.ensureDependencies();

            for (ArgumentContext context : getArgumentContexts()) {
                if (context.instantiateWith(args)) {
                    return;
                }
            }
            throw new IllegalArgumentException("Given: " + Arrays.toString(Arrays.stream(args).map(arg -> arg.getClass() + ": " + arg).toArray()) + " Valid options: " + Arrays.deepToString(getArgumentContexts()));//IllegalModifierInstantiationArgumentException();
        }
    }
    public GameObject getParent() {
        return parent;
    }

    public abstract List<Class<? extends Modifier>> getDependencies();

    public abstract ArgumentContext[] getArgumentContexts();
}
