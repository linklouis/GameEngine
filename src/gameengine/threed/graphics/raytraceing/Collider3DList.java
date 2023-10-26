package gameengine.threed.graphics.raytraceing;

import gameengine.threed.prebuilt.objectmovement.collisions.Collider3D;

/**
 * A very lightweight and specific use-case singly linked list implementation
 * to optimize referencing Collider3Ds when ray tracing. Objects can only be
 * added at the head, and no {@code get()} method is provided, the only
 * external access to the list is through the head, so uses must loop through
 * themselves.
 *
 * @author Louis Link
 * @since 1.0
 */
public final class Collider3DList {
    /**
     * A reference to the first Element in the list.
     */
    private Element head;

    /**
     * Creates a new {@code Collider3DList} with no elements and a null
     * {@code head}.
     */
    public Collider3DList() {
        head = null;
    }

    /**
     * Creates a new {@code Collider3DList} with matching elements to the
     * passed array.
     *
     * @param items An array of {@link Collider3D} to initialize the values in
     *              the list.
     */
    public Collider3DList(final Collider3D<?>[] items) {
        for (Collider3D<?> item : items) {
            add(item);
        }
    }

    public record Element(Collider3D<?> value, Collider3DList.Element next) { }

    /**
     * Adds a new item at the head of the list.
     *
     * @param value the new Collider3D to be added to the list
     */
    private void add(final Collider3D<?> value) {
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
