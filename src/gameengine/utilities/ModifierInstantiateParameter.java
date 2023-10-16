package gameengine.utilities;

import java.util.function.Consumer;

public class ModifierInstantiateParameter<T> {
    /**
     * The name of the value the parameter represents.
     */
    private final String name;

    /**
     * The object type the parameter accepts.
     */
    private final Class<T> type;

    /**
     * The function to preform when an argument is accepted.
     */
    private final Consumer<T> handler;

    /**
     * Keeps track of whether or not the parameter
     * has already accepted an argument.
     */
    private boolean used = false;

    public ModifierInstantiateParameter(
            String name, Class<T> type, Consumer<T> handler) {
        this.name = name;
        this.type = type;
        this.handler = handler;
    }

    public ModifierInstantiateParameter(String name, Class<T> type, Object self) {
        this.name = name;
        this.type = type;
        this.handler = (T value) -> {
            try {
                self.getClass().getDeclaredField(name).set(self, value);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public void acceptValue(Object argument) {
        if (used) {
            throw new RuntimeException(); // TODO
        }

        handler.accept((T) argument);

        used = false;
    }

    public boolean validArg(Object argument) {
        return getType().isAssignableFrom(argument.getClass());
    }

    public Class<T> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ModifierInstantiateArgument " + getName() + " " + getType();
    }
}
