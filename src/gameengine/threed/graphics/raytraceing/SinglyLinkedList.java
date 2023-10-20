package gameengine.threed.graphics.raytraceing;

import gameengine.threed.prebuilt.objectmovement.collisions.Collider3D;

public class SinglyLinkedList<E> {
    /**
     * A reference to the first Element in the list
     */
    private Element head;
    /**
     * An empty Element always pointing to head to help with creating loops
     */
    private final Element pointer = new Element(null, head);

    public SinglyLinkedList() {
        head = null;
    }

    public SinglyLinkedList(E[] items) {
        for (E item : items) {
            add(item);
        }
    }

    public class Element {
        Element next;
        E value;

        public Element(E value, Element next) {
            this.value = value;
            this.next = next;
        }

        public Element(E value) {
            this.value = value;
            this.next = null;
        }

        public void removeNext() {
            if (next != null) {
                next = next.next;
                if (this == pointer) {
                    head = next;
                }
            }
        }

        public Element getNext() {
            return next;
        }

        public boolean hasNext() {
            return next != null;
        }

        public E getValue() {
            return value;
        }
    }

    public void add(E value) {
        setHead(new Element(value, head));
    }

    public boolean isEmpty() {
        return head == null;
    }

    public Element getHead() {
        return head;
    }

    private void setHead(Element head) {
        this.head = head;
        pointer.next = head;
    }

    public Element getPointer() {
        return pointer;
    }

    public String toString() {
        StringBuilder output = new StringBuilder("SinglyLinkedList: ");

        for (Element element = head; element.hasNext(); element = element.getNext()) {
            output.append(element.value);
        }

        return output.toString();
    }
}
