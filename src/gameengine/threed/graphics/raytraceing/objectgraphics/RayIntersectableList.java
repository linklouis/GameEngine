package gameengine.threed.graphics.raytraceing.objectgraphics;


import gameengine.threed.geometry.RayIntersectable;

/**
 * A very lightweight and specific use-case singly linked list implementation
 * to optimize referencing RayIntersectables when ray tracing. Objects can only be
 * added at the head, and no {@code get()} method is provided, the only
 * external access to the list is through the head, so uses must loop through
 * themselves.
 *
 * @author Louis Link
 * @since 1.0
 */
public final class RayIntersectableList {
    /**
     * A reference to the first Element in the list.
     */
    private Element head;

    /**
     * Creates a new {@code RayIntersectableList} with no elements and a null
     * {@code head}.
     */
    public RayIntersectableList() {
        head = null;
    }

    /**
     * Creates a new {@code RayIntersectableList} with matching elements to the
     * passed array.
     *
     * @param items An array of {@link RayIntersectable} to initialize the values in
     *              the list.
     */
    public RayIntersectableList(final RayIntersectable[] items) {
        for (int i = items.length - 1; i >=0; i--) {
            add(items[i]);
        }
    }

    public record Element(RayIntersectable value, RayIntersectableList.Element next) { }

    /**
     * Adds a new item at the head of the list.
     *
     * @param value the new Collider3D to be added to the list
     */
    private void add(final RayIntersectable value) {
        head = new Element(value, head);
    }

    /**
     * @return True if {@link #head} is null, otherwise false.
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * The only access point provided to items in the list.
     * @return the first element in the list.
     */
    public Element getHead() {
        return head;
    }
}
