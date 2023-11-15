package gameengine.threed.graphics.raytraceing.objectgraphics;


import gameengine.threed.geometry.RayIntersectable;
import gameengine.vectormath.Vector3D;

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
    private final Vector3D relativeTo;

    public RayIntersectableList(final RayIntersectable[] items, final Vector3D pointRelativeTo) {
        relativeTo = pointRelativeTo;
        for (int i = items.length - 1; i >=0; i--) {
            add(items[i]);
        }
    }

    public record Element(RayIntersectable value, Element next, double dist) {
        public boolean hasNext() {
            return next != null;
        }

        public boolean isLast() {
            return next == null;
        }
    }

    /**
     * Adds a new item at the head of the list.
     *
     * @param value the new Collider3D to be added to the list
     */
    private void add(final RayIntersectable value) {
        head = new Element(value, head, value.closestDistTo(relativeTo));
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
