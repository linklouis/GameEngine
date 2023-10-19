package gameengine.skeletons;

public abstract class Visual<GraphicsObjectType extends GraphicsObject> extends Modifier {
    private GraphicsObjectType appearance; // TODO name better

    /*
     * Construction:
     */

    public Visual() {
        super();
    }

    public Visual(Modifier... modifiers) {
        super(modifiers);
    }

    /*
     * Utilities:
     */

    public GraphicsObjectType getAppearance() {
        return appearance;
    }

    public void setAppearance(GraphicsObjectType appearance) {
        this.appearance = appearance;
    }
}
