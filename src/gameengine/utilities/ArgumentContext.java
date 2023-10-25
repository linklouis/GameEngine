package gameengine.utilities;

import java.util.Arrays;

public class ArgumentContext {

    private static final EmptyInstantiateAction EMPTY_ACTION = new EmptyInstantiateAction() {
        @Override
        public void run() {

        }
    };

    private final ModifierInstantiateParameter<?>[] parameters;
    private final EmptyInstantiateAction otherSetup;


    /*
     * Construction:
     */

    public ArgumentContext(ModifierInstantiateParameter<?>... parameters) {
        this.parameters = parameters;
        this.otherSetup = EMPTY_ACTION;
    }

    public ArgumentContext(EmptyInstantiateAction otherSetup, ModifierInstantiateParameter<?>... parameters) {
        this.parameters = parameters;
        this.otherSetup = otherSetup;
    }


    /*
     * Functionality:
     */

    public boolean instantiateWith(Object[] args) {
        if (argumentsValidForContext(args)) {
            instantiateWithArgs(args);
            return true;
        }
        return false;
    }

    private void instantiateWithArgs(Object[] args) {
        for (int index = 0; index < args.length; index++) {
            parameters[index].acceptValue(args[index]);
        }
        getOtherSetup().run();
    }

    public boolean argumentsValidForContext(Object[] args) {
        if (parameters.length != args.length) {
            return false;
        }
        for (int index = 0; index < args.length; index++) {
            if (!parameters[index].validArg(args[index])) {
                return false;
            }
        }
        return true;
    }


    /*
     * Utilities:
     */

    public ModifierInstantiateParameter<?>[] getParameters() {
        return parameters;
    }

    public EmptyInstantiateAction getOtherSetup() {
        return otherSetup;
    }

    public String toString() {
        return Arrays.toString(getParameters());
    }
}
