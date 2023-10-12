package gameengine.utilities;

import java.util.function.Consumer;

public class ModifierInstantiateParameter<T> {
//    private final Field field;
    private final String name;
    private final Class<T> type;
//    private final Object self;
    private final Consumer<T> handler;
    private boolean used = false;

    public ModifierInstantiateParameter(String name, Class<T> type, Consumer<T> handler) {//(Field field, String name, Object self, ArgumentHandler handler) {
//        this.field = field;
        this.name = name;
//        this.self = self;
        this.type = type;
        this.handler = handler;
    }

    public ModifierInstantiateParameter(String name, Class<T> type, Object self) {//(Field field, String name, Object self, ArgumentHandler handler) {
//        this.field = field;
        this.name = name;
//        this.self = self;
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

        handler.accept((T) argument);//, self);

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
