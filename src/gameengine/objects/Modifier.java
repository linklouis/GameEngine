package gameengine.objects;

import gameengine.utilities.ModifierInstantiateParameter;

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

            for (ModifierInstantiateParameter<?>[] params : getValidArguments()) {
                if (argumentsValidForContext(args, params)) {
                    for (int index = 0; index < args.length; index++) {
//                        fields[index].setAccessible(true);
                        params[index].acceptValue(args[index]);//.set(this, args[index]);
                    }
                    return;
                }
            }
            throw new IllegalArgumentException("Given: " + Arrays.toString(Arrays.stream(args).map(arg -> arg.getClass() + ": " + arg).toArray()) + " Valid options: " + Arrays.deepToString(getValidArguments()));//IllegalModifierInstantiationArgumentException();
        }
    }
    public GameObject getParent() {
        return parent;
    }

    private static boolean argumentsValidForContext(Object[] args, ModifierInstantiateParameter<?>[] params) {
        if (params.length != args.length) {
            return false;
        }
        for (int index = 0; index < args.length; index++) {
            if (!params[index].validArg(args[index])) {
                return false;
            }
        }
        return true;
    }

    public abstract List<Class<? extends Modifier>> getDependencies();

    public abstract ModifierInstantiateParameter<?>[][] getValidArguments() throws NoSuchFieldException;
}
