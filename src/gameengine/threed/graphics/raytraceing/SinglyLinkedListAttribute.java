package gameengine.threed.graphics.raytraceing;

import gameengine.threed.prebuilt.objectmovement.collisions.Collider3D;

public class SinglyLinkedListAttribute {
    /**
     * A reference to the first Element in the list
     */
    private Element head;
    /**
     * An empty Element always pointing to head to help with creating loops
     */
    private final Element pointer = new Element(null, head);

    public SinglyLinkedListAttribute() {
        head = null;
    }

    public SinglyLinkedListAttribute(Collider3D<?>[] items) {
        for (Collider3D<?> item : items) {
            add(item);
        }
    }

    public SinglyLinkedListAttribute(SinglyLinkedListAttribute items) {
        Element element = items.getHead();
        while (element != null) {
            add(element.getValue());
            element = element.getNext();
        }
    }

    public SinglyLinkedListAttribute(SinglyLinkedList<Collider3D<?>> items) {
        SinglyLinkedList<Collider3D<?>>.Element element = items.getHead();
        while (element != null) {
            add(element.getValue());
            element = element.getNext();
        }
    }

    public class Element {
        private Element next;
        private final Collider3D<?> value;
//        private boolean isInRange = false;

        public Element(Collider3D<?> value, Element next) {
            this.value = value;
            this.next = next;
        }

        public Element(Collider3D<?> value) {
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

        public Collider3D<?> getValue() {
            return value;
        }

//        public boolean isInRange() {
//            return isInRange;
//        }
//
//        public void setInRange(boolean inRange) {
//            isInRange = inRange;
//        }
    }

    public void add(Collider3D<?> value) {
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

        for (Element element = pointer; element != null; element = element.getNext()) {
            output.append(element.value);
        }

        return output.toString();
    }
}
    
