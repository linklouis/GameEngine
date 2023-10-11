package vectormath;

import jdk.jfr.Experimental;

@Experimental
public class SizableVector extends Vector<SizableVector> {

    public SizableVector(final double[] components) {
        super(components);
    }

    @Override
    public int size() {
        return getComponents().length;
    }

    @Override
    public SizableVector newVector(final double... comps) {
        return new SizableVector(comps);
    }
}
