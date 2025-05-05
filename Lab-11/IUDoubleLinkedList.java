
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
            if (current == null) {
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
        } else if (target.getNext() != null) {
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
    public Iterator<E> iterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public ListIterator<E> listIterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    @Override
    public ListIterator<E> listIterator(int startingIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

}
