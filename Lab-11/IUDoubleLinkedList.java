import java.util.ConcurrentModificationException;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUDoubleLinkedList<E> {
    private BidirectionalNode<E> front, rear;

    private int count;
    private int modCount;

    private enum ListIteratorState { NEXT, PREVIOUS, NEITHER }

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

        public int getNextIndex() {
            return getActualIndexFromVirtual(virtualNextIndex);
        }

        public int getPreviousIndex() {
            return getActualIndexFromVirtual(getVirtualPreviousIndex());
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

        private int getActualIndexFromVirtual(int virtualIndex) {
            return virtualIndex % count + 1;
        }
    }

    private class DoubleLinkedListListIterator<E> implements ListIterator<E> {
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

        public boolean hasNext() {
            if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
            return cursor.getVirtualNextIndex() < 0;
        }

        public E next() {
            if (!hasNext()) { throw new NoSuchElementException(); }
            E item =  current.getElement();
            lastReturned = current;
            current = current.getNext();
            cursor.rightShift();
            return item;
        }

        public boolean hasPrevious() {
            if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
            return cursor.getPreviousIndex() > -1;
        }

        public E previous() {
            if (!hasPrevious()) { throw new NoSuchElementException(); }
            current = lastReturned = current.getPrevious();
            E item = current.getElement();
            cursor.leftShift();
            return item;
        }

        public int nextIndex() {
            return cursor.getNextIndex();
        }

        public int previousIndex() {
            return cursor.getPreviousIndex();
        }

        @Override
        public void remove() {
            if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }    
            if (lastReturned == current) {
                current = lastReturned.getNext();

            } 
            lastReturned.getPrevious().setNext(lastReturned.getNext());
        }

        @Override
        public void set(E e) {
            BidirectionalNode<E> newNode = new BidirectionalNode<E>(e);
            lastReturned = newNode;
        }

        @Override
        public void add(E e) {
            BidirectionalNode<E> newNode = new BidirectionalNode<E>(e);
            if (lastReturned != current) {
                BidirectionalNode<E> temp = current.getNext();
                newNode.setNext(temp);
                newNode.setPrevious(current);
            } else {
                newNode.setNext(lastReturned);
                newNode.setPrevious(lastReturned.getNext());
            }
        }

    }
}
