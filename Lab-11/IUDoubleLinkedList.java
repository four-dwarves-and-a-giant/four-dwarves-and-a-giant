import java.util.ConcurrentModificationException;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.NoSuchElementException;

public class IUDoubleLinkedList<E> implements IndexedUnsortedList<E> {

    private enum ListIteratorState {
        NEXT, PREVIOUS, NEITHER
    }
    private BidirectionalNode<E> front, rear;
    private int count;
    private int modCount;

    public IUDoubleLinkedList() {
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
            addBetween(new BidirectionalNode<>(element), null, null);
        } else {
            addBetween(new BidirectionalNode<>(element), rear, null);
        }
    }

    @Override
    public void add(E element) {
        addToRear(element);
    }

    @Override
    public void addAfter(E element, E target) {
        BidirectionalNode<E> newNode = new BidirectionalNode<E>(element);
        BidirectionalNode<E> previous = findNode(target);
        if (previous != null) {
            BidirectionalNode<E> next = previous.getNext();
            addBetween(newNode, previous, next);
        } else {
            throw new NoSuchElementException();
        }

    }

    @Override
    public void add(int index, E element) {
        if ((index < 0) || (index > size())) {
            throw new IndexOutOfBoundsException();
        }
        BidirectionalNode<E> newNode = new BidirectionalNode<E>(element);
        if (index == 0) {
            addBetween(newNode, null, front);
        } else if (index == size()) {
            addBetween(newNode, rear, null);
        } else {
            BidirectionalNode<E> previous = findNode(index - 1);
            BidirectionalNode<E> next = previous.getNext();
            addBetween(newNode, previous, next);
        }
    }

    public void addBetween(BidirectionalNode<E> newNode, BidirectionalNode<E> previous, BidirectionalNode<E> next) {
        if (previous == null && next == null) {
            front = rear = newNode;
        } else if (previous == null) {
            newNode.setNext(front);
            front.setPrevious(newNode);
            front = newNode;
        } else if (next == null) {
            newNode.setPrevious(rear);
            rear.setNext(newNode);
            rear = newNode;
        } else {
            previous.setNext(newNode);
            newNode.setPrevious(previous);
            next.setPrevious(newNode);
            newNode.setNext(next);
        }
        count++;
        modCount++;

    }

    @Override
    public E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return removeNode(front);
    }

    @Override
    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return removeNode(rear);
    }

    @Override
    public E remove(E element) {
        return removeNode(findNode(element));
    }

    @Override
    public E remove(int index) {
        if ((index < 0) || (index >= size())) {
            throw new IndexOutOfBoundsException();
        }

        BidirectionalNode<E> target = findNode(index);
        E result = target.getElement();
        removeNode(target);
        return result;
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

    @Override
    public int indexOf(E element) {
        BidirectionalNode<E> temp = front;
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

    private E removeNode(BidirectionalNode<E> node) {
        if (node == null) {
            throw new NoSuchElementException();
        }
        E retVal = node.getElement();
        if (node.getNext() == null && node.getPrevious() == null) {
            front = rear = null;
        } else if (node.getNext() == null) {
            rear = node.getPrevious();
            rear.setNext(null);
        } else if (node.getPrevious() == null) {
            front = node.getNext();
            front.setPrevious(null);
        } else {
            node = node.getPrevious();
            node.setNext(node.getNext().getNext());
            node.getNext().setPrevious(node);
        }
        count--;
        modCount++;
        return retVal;
    }

    private BidirectionalNode<E> findNode(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        BidirectionalNode<E> temp;
        temp = front;
        for (int i = 0; i < index; i++) {
            temp = temp.getNext();
        }

        return temp;
    }

    public BidirectionalNode<E> findNode(E element) {
        BidirectionalNode<E> temp = front;
        while (temp != null) {
            if (temp.getElement().equals(element)) {
                return temp;
            }
            temp = temp.getNext();
        }
        throw new NoSuchElementException();
    }

    @Override
    public Iterator<E> iterator() {
        return new DoubleLinkedListIterator();
    }

    private class DoubleLinkedListIterator implements Iterator<E> {

        private BidirectionalNode<E> previous;
        private BidirectionalNode<E> current;
        private BidirectionalNode<E> next;
        private int iterModCount;

        public DoubleLinkedListIterator() {
            previous = null;
            current = null;
            next = front;
            iterModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) { throw new ConcurrentModificationException(); }
            return next != null;
        }

        @Override
        public E next() {
            if (!hasNext()) { throw new NoSuchElementException(); }

            if (current != null) {
                previous = current;
            }
            current = next;
            next = next.getNext();
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
            count--;
            modCount++;
            iterModCount++;
        }
    }

    @Override
    public ListIterator<E> listIterator() {
        return new DoubleLinkedListListIterator();
    }

    @Override
    public ListIterator<E> listIterator(int startingIndex) {
        return new DoubleLinkedListListIterator(startingIndex);
    }

    private class LinkedCursor {

        private int virtualIndex;

        public LinkedCursor(int virtualIndex) {
            if (virtualIndex < -1 || virtualIndex > count) {
                throw new IndexOutOfBoundsException();
            }
            this.virtualIndex = virtualIndex - 1;
        }

        public int getPreviousIndex() {
            return virtualIndex;
        }

        public int getNextIndex() {
            return virtualIndex + 1;
        }

        public int previousIndex() {
            int retVal = virtualIndex;
            virtualIndex--;
            return retVal;
        }

        public int nextIndex() {
            return virtualIndex++;
        }
    }

    private class DoubleLinkedListListIterator implements ListIterator<E> {

        private LinkedCursor cursor;
        private int listIterModCount;
        private BidirectionalNode<E> current;
        private ListIteratorState lastReturned;

        public DoubleLinkedListListIterator() {
            this(0);
        }

        public DoubleLinkedListListIterator(int nextVirtualIndex) {
            cursor = new LinkedCursor(nextVirtualIndex);
            listIterModCount = modCount;
            findIndex(nextVirtualIndex);
            lastReturned = ListIteratorState.NEITHER;

        }

        private void findIndex(int i) {
            if (i < 0 || i > size()) {
                throw new IndexOutOfBoundsException(i);
            }
            if (i == 0) {
                return;
            }
            i--;
            if (i <= (size() / 2)) {
                current = front;
                for (int j = 0; i > j; j++) {
                    current = current.getNext();
                }
            } else {
                current = rear;
                for (int j = size() - 1; i < j; j--) {
                    current = current.getPrevious();
                }
            }
        }

        @Override
        public boolean hasNext() {
            if (listIterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return cursor.getNextIndex() < size();
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (current == null) {
                current = front;
            } else {
                current = current.getNext();
            }
            E item = current.getElement();
            lastReturned = ListIteratorState.NEXT;
            cursor.nextIndex();
            return item;
        }

        @Override
        public boolean hasPrevious() {
            if (listIterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return cursor.getPreviousIndex() >= 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            switch (lastReturned) {
                case PREVIOUS:
                    current = current.getPrevious();
                    break;
            }
            E retVal = current.getElement();

            lastReturned = ListIteratorState.PREVIOUS;
            cursor.previousIndex();
            return retVal;
        }

        @Override
        public int nextIndex() {
            if (listIterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return cursor.getNextIndex();
        }

        @Override
        public int previousIndex() {
            if (listIterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return cursor.getPreviousIndex();
        }

        @Override
        public void remove() {
            if (listIterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            switch (lastReturned) {
                case NEITHER:
                    throw new IllegalStateException();
            }
            removeNode(current);
            lastReturned = ListIteratorState.NEITHER;
            current = current.getPrevious();
            cursor.previousIndex();
            listIterModCount++;
        }

        @Override
        public void set(E e) {
            if (listIterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            switch (lastReturned) {
                case NEITHER:
                    throw new IllegalStateException();
                default:
                    current.setElement(e);
            }
            modCount++;
            listIterModCount++;
        }

        @Override
        public void add(E e) {
            if (listIterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            BidirectionalNode<E> newNode = new BidirectionalNode<E>(e);
            switch (lastReturned) {
                case NEXT:
                    break;
                case PREVIOUS:
                    current = current.getPrevious();
                    break;
                case NEITHER:
                    break;
                default:
                    throw new AssertionError();
            }
            addBetween(newNode, current, (current == null) ? null : current.getNext());
            cursor.getPreviousIndex();
            listIterModCount++;
            lastReturned = ListIteratorState.NEITHER;
        }

    }
}
