package gameengine.experimental;

import gameengine.skeletons.Modifier;

import java.util.Arrays;
import java.util.Map;

/**
 * Thrown to indicate that a {@link Modifier} has been passed an illegal or
 * inappropriate argument in its instantiate method.
 *
 * @since   1.0
 * @author Louis Link
 */
public class IllegalModifierInstantiationArgumentException
        extends IllegalArgumentException {
    /**
     * Constructs an IllegalModifierInstantiationArgumentException where a given
     * parameter of a {@link Modifier}'s instantiate(Object...) method has been
     * passed an argument of an illegal type, with a detail message generated
     * from information about the illegal situation.
     *
     * @param argumentNumber The argument of the {@link Modifier}'s instantiate
     *                       method that was passed an illegal argument.
     * @param given The illegal object that was passed into the
     *              {@link Modifier}'s instantiate method at the given index.
     * @param modifierClass The class type of {@link Modifier} that the illegal
     *                      instantiate call occurred on.
     * @param validOptions An array of Maps of Strings (the name of the variable
     *                     the argument represents for the given type context) and
     *                     object types (the class type that the given context
     *                     expects) that represent contexts that can be legally
     *                     passed to the{@link Modifier}'s instantiate method.
     */
    public IllegalModifierInstantiationArgumentException(
            final int argumentNumber, final Object given,
            final Class<? extends Modifier> modifierClass,
            final Map<String, Class<?>>[] validOptions) {
        super(createMessage(
                argumentNumber, given, modifierClass, validOptions)
        );
    }

    /**
     * Constructs an IllegalModifierInstantiationArgumentException where a
     * {@link Modifier}'s instantiate(Object...) method has been passed an
     * illegal number of arguments, with a detail message generated from
     * information about the illegal situation.
     *
     * @param argumentsGiven An array of the illegal arguments passed into
     *                       the {@link Modifier}'s instantiate method.
     * @param modifierClass The class type of {@link Modifier} that the illegal
     *                      instantiate call occurred on.
     * @param validOptions An array of possible legal numbers of arguments that
     *                     could the passed into the {@link Modifier}'s
     *                     instantiate method.
     */
    public IllegalModifierInstantiationArgumentException(
            final Object[] argumentsGiven,
            final Class<? extends Modifier> modifierClass,
            final int[] validOptions) {
        super(createMessage(
                argumentsGiven, modifierClass, validOptions)
        );
    }

    /**
     * Generates a detail message for the
     * {@link IllegalModifierInstantiationArgumentException}, when a given
     * parameter of a {@link Modifier}'s instantiate(Object...) method has been
     * passed an argument of an illegal type, based on the passed information
     * about the illegal situation.
     *
     * @param argumentNumber The argument of the {@link Modifier}'s
     *                       instantiate(Object...) method that was passed an
     *                       illegal argument.
     * @param given The illegal object that was passed into the
     *              {@link Modifier}'s instantiate method at the given index.
     * @param modifierClass The class type of {@link Modifier} that
     *                      the illegal instantiate call occurred on
     * @param validOptions An array of Maps of Strings (the name of the variable
     *                     the argument represents for the given type context) and
     *                     object types (the class type that the given context
     *                     expects) that represent contexts that can be legally
     *                     passed to the{@link Modifier}'s instantiate method.
     * @return A detail message about the illegal argument for the
     *         {@link IllegalModifierInstantiationArgumentException}
     *         based on the given information.
     */
    private static String createMessage(
            final int argumentNumber, final Object given,
            final Class<? extends Modifier> modifierClass,
            final Map<String, Class<?>>[] validOptions) {
        StringBuilder message = new StringBuilder("Illegal type ").append(given)
                .append(" for argument ").append(argumentNumber).append(" of ")
                .append(modifierClass).append(". Valid argument values are: ");

//        int index = 0;
//        for (Map.Entry<String, Class<?>> entry : validOptions.entrySet()) {
//            if (entry.getValue() == null) {
//                message.append("no value given");
//            } else {
//                message.append(entry);
//            }
//
//            if (index < validOptions.size() - 1) {
//                message.append(", ");
//            } else {
//                message.append(".");
//            }
//        }
        message.append(Arrays.toString(validOptions));

        return message.toString();
    }


    /**
     * Generates a detail message for the
     * {@link IllegalModifierInstantiationArgumentException}, when the
     * {@link Modifier}'s instantiate(Object...) method has been passed an
     * illegal number of arguments, based on the passed information
     * about the illegal situation.
     *
     * @param argumentsGiven An array of the illegal arguments passed into
     *                       the {@link Modifier}'s instantiate method.
     * @param modifierClass The class type of {@link Modifier} that the illegal
     *                      instantiate call occurred on.
     * @param validOptions An array of possible legal numbers of arguments that
     *                     could the passed into the {@link Modifier}'s
     *                     instantiate method.
     * @return A detail message about the illegal arguments for the
     *         {@link IllegalModifierInstantiationArgumentException}
     *         based on the given information.
     */
    private static String createMessage(
            final Object[] argumentsGiven,
            final Class<? extends Modifier> modifierClass,
            final int[] validOptions) {
        StringBuilder message = new StringBuilder(
                "Illegal number of arguments ")
                .append(argumentsGiven.length).append(" for ")
                .append(modifierClass).append(". Valid numbers")
                .append("of arguments are: ");

        for (int i = 0; i < validOptions.length - 1; i++) {
            message.append(validOptions[i]).append(", ");
        }
        message.append(validOptions[validOptions.length - 1]).append(".");

        message.append(" Arguments given: ")
                .append(Arrays.toString(argumentsGiven));

        return message.toString();
    }
}
