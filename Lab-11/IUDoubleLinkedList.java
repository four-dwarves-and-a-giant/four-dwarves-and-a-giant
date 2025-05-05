import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUDoubleLinkedList<E> implements IndexedUnsortedList<E> {

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
            front = rear = new BidirectionalNode<>(element);
        } else {
            rear.setNext(new BidirectionalNode<>(element));
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
        if ((index < 0) || (index > size())) {
            throw new IndexOutOfBoundsException();
        }
        BidirectionalNode<E> newNode = new BidirectionalNode<E>(element);
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            BidirectionalNode<E> current = front;
            BidirectionalNode<E> previous = null;
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
        if ((index < 0) || (index >= size())) {
            throw new IndexOutOfBoundsException();
        }

        BidirectionalNode<E> target = findNode(index);
        E result = target.getElement();

        if (index == 0) {
            front = target.getNext();
        } else if (target.getNext() != null){
            target.getPrevious().setNext(target.getNext());
            target.getNext().setPrevious(target.getPrevious());
        } else {
            rear = target.getPrevious();
        }
        count--;
        modCount++;
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

    public BidirectionalNode<E> findNode(int index) {
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
        private int virtualNextIndex;

        public LinkedCursor(int nextVirtualIndex) {
            if (nextVirtualIndex < 0 || nextVirtualIndex > count) {
                throw new IndexOutOfBoundsException();
            }
            this.virtualNextIndex = nextVirtualIndex;
        }

        public int getVirtualNextIndex() {
            return virtualNextIndex;
        }

        public int getVirtualPreviousIndex() {
            return virtualNextIndex - 1;
        }

        public void rightShift() {
            if (virtualNextIndex < count) {
                virtualNextIndex++;
            }
        }

        public void leftShift() {
            if (virtualNextIndex > 0) {
                virtualNextIndex--;
            }
        }

    }

    private class DoubleLinkedListListIterator implements ListIterator<E> {
        private LinkedCursor cursor;
        private int listIterModCount;
        private BidirectionalNode<E> current;
        private BidirectionalNode<E> lastReturned;

        private DoubleLinkedListListIterator() {
            this(0);
        }

        public DoubleLinkedListListIterator(int nextVirtualIndex) {
            cursor = new LinkedCursor(nextVirtualIndex);
            listIterModCount = modCount;
            // current = // How to get current?
            lastReturned = null;
        }

        @Override
        public boolean hasNext() {
            if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
            return cursor.getVirtualNextIndex() < 0;
        }

        @Override
        public E next() {
            
            if (!hasNext()) { throw new NoSuchElementException(); }
            E item =  current.getElement();
            lastReturned = current;
            current = current.getNext();
            cursor.rightShift();
            return item;
        }

        @Override
        public boolean hasPrevious() {
            if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
            return cursor.getVirtualPreviousIndex() > -1;
        }

        @Override
        public E previous() {
            if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
            if (!hasPrevious()) { throw new NoSuchElementException(); }
            current = lastReturned = current.getPrevious();
            E item = current.getElement();
            cursor.leftShift();
            return item;
        }

        @Override
        public int nextIndex() {
            return cursor.getVirtualNextIndex();
        }

        @Override
        public int previousIndex() {
            return cursor.getVirtualPreviousIndex();
        }

        @Override
        public void remove() {
            if (listIterModCount != modCount) throw new ConcurrentModificationException();
            if (lastReturned == null) throw new IllegalStateException();

            BidirectionalNode<E> previousNode = lastReturned.getPrevious();
            BidirectionalNode<E> nextNode = lastReturned.getNext();

            if (previousNode != null) {previousNode.setNext(nextNode); } else { front = nextNode;}
            if (nextNode != null) { nextNode.setPrevious(previousNode); } else { rear = previousNode; }
            if (lastReturned == current) {
                current = nextNode;
            }

            lastReturned = null;
            count--;
            modCount++;
            listIterModCount++;
        }

        @Override
        public void set(E e) {
            if (listIterModCount != modCount ) { throw new ConcurrentModificationException(); }
            if ( lastReturned == null ) { throw new IllegalStateException(); }
            BidirectionalNode<E> newNode = new BidirectionalNode<>(e);
            lastReturned = newNode;
        }

        @Override
        public void add(E e) {
            if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
            BidirectionalNode<E> newNode = new BidirectionalNode<>(e);
            if (front == null) {
                front = rear = newNode;
            } else if (current == front) {
                newNode.setNext(front);
                front.setPrevious(newNode);
                front = newNode;
            } else if (current == null) {
                newNode.setPrevious(rear);
                rear.setNext(newNode);
                rear = newNode;
            } else {
                BidirectionalNode<E> prev = current.getPrevious();
                newNode.setPrevious(prev);
                newNode.setNext(current);
                if (prev != null) { prev.setNext(newNode); }
                current.setPrevious(newNode);
            }
            count++;
            modCount++;
            listIterModCount++;
            cursor.rightShift();
            lastReturned = null;
        }
    }
}