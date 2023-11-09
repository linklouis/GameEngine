package gameengine.utilities;


public class Random {
    /**
     * A reference to the first Element in the list.
     */
    private Element head;
    private Element pointer = head;
    private int size = 0;

    public Random(int size) {
        regenerate(size);
    }

    public void regenerate(int size) {
        this.size = size;
        regenerate();
    }

    public void regenerate() {
        if (size <= 0) {
            size = 10;
        }
        head = null;
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < size; i++) {
            add(rand.nextGaussian());
        }
    }

    public double next() {
        if (pointer == null) {
            if (isEmpty()) {
                regenerate();
            } else {
                pointer = head;
            }
        }

        double value = pointer.value();
        pointer = pointer.next();
        return value;
    }

    private record Element(double value, Element next) { }

    /**
     * Adds a new item at the head of the list.
     *
     * @param value the new Collider3D to be added to the list
     */
    private void add(final double value) {
        head = new Element(value, head);
    }

    /**
     * @return True if {@link #head} is null, otherwise false.
     */
    private boolean isEmpty() {
        return head == null;
    }
}
