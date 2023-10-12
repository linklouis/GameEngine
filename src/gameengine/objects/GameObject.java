package gameengine.objects;

import java.util.ArrayList;
import java.util.List;

public abstract class GameObject {
    private final Modifier[] modifiers;

    public GameObject() {
        modifiers = new Modifier[0];
    }


//    public gameengine.objects.GameObject(gameengine.objects.Modifier[] modifiers) {
//        this.modifiers = modifiers;
//    }

    public GameObject(Modifier... modifiers) {
        this.modifiers = modifiers;
    }

    public Modifier[] getModifiers() {
        return modifiers;
    }

    public <T extends Modifier> T get(Class<T> modifierClass) {
        for (Modifier modifier : getModifiers()) {
            if (modifierClass.isAssignableFrom(modifier.getClass())) {
                return (T) modifier;
            }
        }
        ensureDependencies();
        return null;
    }

    public List<Class<? extends Modifier>> getDependencies() {
        List<Class<? extends Modifier>> dependantClasses = new ArrayList<>();

        for (Modifier modifier : getModifiers()) {
            dependantClasses.addAll(modifier.getDependencies());
        }

        List<Class<? extends Modifier>> noRepeats = new ArrayList<>();
        for (Class<? extends Modifier> modifierClass : dependantClasses) {
//            if (!noRepeats.contains(modifierClass)) {
//                noRepeats.add(modifierClass);
//            }
            boolean contains = false;
            for (Class<? extends Modifier> modifierClass1 : noRepeats) {
                if (modifierClass.equals(modifierClass1)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                noRepeats.add(modifierClass);
            }
        }

        return noRepeats;
    }

    protected boolean hasAllDependancies() {
        for (Class<? extends Modifier> modifierClass : getDependencies()) {
            if (!containsModifier(modifierClass)) {
                return false;
            }
        }
        return true;
    }

    protected void ensureDependencies() {
        for (Class<? extends Modifier> modifierClass : getDependencies()) {
            if (!containsModifier(modifierClass)) {
                throw new RuntimeException(this.getClass()
                        + " requires a Modifier " + modifierClass + " but none was found.");
            }
        }
    }

    public <T extends Modifier> boolean containsModifier(Class<T> modifier) {
        for (Modifier modifier1 : getModifiers()) {
            if (modifier.isAssignableFrom(modifier1.getClass())) {
                return true;
            }
        }
        return false;
    }
}
