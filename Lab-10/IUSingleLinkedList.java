
import java.util.*;

/**
 * Single-linked node implementation of IndexedUnsortedList. An Iterator with
 * working remove() method is implemented, but ListIterator is unsupported.
 *
 * @author
 *
 * @param <E> type to store
 */
public class IUSingleLinkedList<E> implements IndexedUnsortedList<E> {

    private LinearNode<E> front, rear;
    private int count;
    private int modCount;

    /**
     * Creates an empty list
     */
    public IUSingleLinkedList() {
        front = rear = null;
        count = 0;
        modCount = 0;
    }

    @Override
    public void addToFront(E element) {
        add(0, element);
    }

    @Override
    public void addToRear(E element) {
        if (isEmpty()) {
            front = rear = new LinearNode<>(element);
        } else {
            rear.setNext(new LinearNode<>(element));
            rear = rear.getNext();
        }
        count++;
        modCount++;
    }

    @Override
    public void add(E element) {
        addToRear(element);
    }

    @Override
    public void addAfter(E element, E target) {
        int i = indexOf(target);
        if (i < 0) {
            throw new NoSuchElementException();
        }
        add(i + 1, element);
    }

    @Override
    public void add(int index, E element) {
        if ((index<0) || (index > size())) {
            throw new IndexOutOfBoundsException();
        }
        LinearNode<E> newNode = new LinearNode<>(element);
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            LinearNode<E> current = front;
            LinearNode<E> previous = null;
            for (int i = 0; i < index; i++) {
                if (current == null) {
                    throw new InternalError();
                }
                previous = current;
                current = current.getNext();
            }
            newNode.setNext(current);
            if (previous == null) {
                front = newNode;
            } else {
                previous.setNext(newNode);
            }
            if (current == null){
                rear = newNode;
            }
        }
        count++;
        modCount++;
    }

    @Override
    public E removeFirst() {
        E first = first();
        front = front.getNext();
        count--;
        modCount++;
        return first;
    }

    @Override
    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return remove(count - 1);
    }

    @Override
    public E remove(E element) {
        int i = indexOf(element);
        if (i < 0) {
            throw new NoSuchElementException();
        }
        return remove(i);
    }

    @Override
    public E remove(int index) {
        if ((index<0) || (index >= size())) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            return removeElement(null, findNode(index));
        }
        LinearNode<E> previousNode = findNode(index - 1);
        return removeElement(previousNode, previousNode.getNext());
    }

    @Override
    public void set(int index, E element) {
        findNode(index).setElement(element);
        modCount++;
    }

    @Override
    public E get(int index) {
        return findNode(index).getElement();
    }

    private LinearNode<E> findNode(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        LinearNode<E> temp = front;
        for (int i = 0; i < index; i++) {
            temp = temp.getNext();
        }
        return temp;
    }

    @Override
    public int indexOf(E element) {
        LinearNode<E> temp = front;
        for (int i = 0; i < size(); i++) {
            if (temp.getElement().equals(element)) {
                return i;
            }
            temp = temp.getNext();
        }
        return -1;
    }

    @Override
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return front.getElement();
    }

    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return rear.getElement();
    }

    @Override
    public boolean contains(E target) {
        return indexOf(target) >= 0;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public String toString() {
        String result = "[";
        for (int i = 0; i < count; i++) {
            result += get(i);
            if (i < count - 1) {
                result += ", ";
            }
        }
        return result + "]";
    }

    private E removeElement(LinearNode<E> previous, LinearNode<E> currentent) {
        // Grab element
        E result = currentent.getElement();
        // If not the first element in the list
        if (previous != null) {
            previous.setNext(currentent.getNext());
        } else { // If the first element in the list
            front = currentent.getNext();
        }
        // If the last element in the list
        if (currentent.getNext() == null) {
            rear = previous;
        }
        count--;
        modCount++;

        return result;
    }

    @Override
    public Iterator<E> iterator() {
        return new SLLIterator();
    }

    /**
     * Iterator for IUSingleLinkedList
     */
    private class SLLIterator implements Iterator<E> {

        private LinearNode<E> previous;
        private LinearNode<E> current;
        private LinearNode<E> next;
        private int iterModCount;
        private boolean canRemove;

        /**
         * Creates a new iterator for the list
         */
        public SLLIterator() {
            previous = null;
            current = null;
            next = front;
            iterModCount = modCount;
            canRemove = false;
        }

        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return next != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (current != null) {
                previous = current;
            }
            current = next;
            next = next.getNext();
            canRemove = true;
            return current.getElement();
        }

        @Override
        public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (current == null) {
                throw new IllegalStateException();
            }
            if (previous == null) {
                front = next;
            } else {
                previous.setNext(next);
            }
            if (current == rear) {
                rear = previous;
            }
            current = null;
            canRemove = false;
            count--;
            modCount++;
            iterModCount++;
        }
    }

    // IGNORE THE FOLLOWING CODE
    // DON'T DELETE ME, HOWEVER!!!
    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator(int startingIndex) {
        throw new UnsupportedOperationException();
    }
}
