
import java.util.Comparator;
import java.util.Iterator;

/**
 * Class for sorting lists that implement the IndexedUnsortedList interface,
 * using ordering defined by class of objects in list or a Comparator. As
 * written uses Quicksort algorithm.
 *
 * @author CPSC 221 Instructors
 */
public class Sort {

    /**
     * Returns a new list that implements the IndexedUnsortedList interface. As
     * configured, uses WrappedDLL. Must be changed if using your own
     * IUDoubleLinkedList class.
     *
     * @return a new list that implements the IndexedUnsortedList interface
     */
    private static <E> IndexedUnsortedList<E> newList() {
        return new WrappedDLL<>();
    }

    /**
     * Sorts a list that implements the IndexedUnsortedList interface using
     * compareTo() method defined by class of objects in list. DO NOT MODIFY
     * THIS METHOD
     *
     * @param <E> The class of elements in the list, must extend Comparable
     * @param list The list to be sorted, implements IndexedUnsortedList
     * interface
     * @see IndexedUnsortedList
     */
    public static <E extends Comparable<E>> void sort(IndexedUnsortedList<E> list) {
        quicksort(list);
    }

    /**
     * Sorts a list that implements the IndexedUnsortedList interface using
     * given Comparator. DO NOT MODIFY THIS METHOD
     *
     * @param <E> The class of elements in the list
     * @param list The list to be sorted, implements IndexedUnsortedList
     * interface
     * @param c The Comparator used
     * @see IndexedUnsortedList
     */
    public static <E> void sort(IndexedUnsortedList<E> list, Comparator<E> c) {
        quicksort(list, c);
    }

    /**
     * Quicksort algorithm to sort objects in a list that implements the
     * IndexedUnsortedList interface, using compareTo() method defined by class
     * of objects in list. DO NOT MODIFY THIS METHOD SIGNATURE
     *
     * @param <E> The class of elements in the list, must extend Comparable
     * @param list The list to be sorted, implements IndexedUnsortedList
     * interface
     */
    private static <E extends Comparable<E>> void quicksort(IndexedUnsortedList<E> list) {
        if (list.size() > 1) {
            E pivot = list.last();
            list.removeLast();
            Iterator<E> it = list.iterator();
            WrappedDLL<E> leftDLL = new WrappedDLL<>();
            WrappedDLL<E> rightDLL = new WrappedDLL<>();
            while (it.hasNext()) {
                E elementE = it.next();
                it.remove();
                if (pivot.compareTo(elementE) >= 0) {
                    leftDLL.add(elementE);
                } else {
                    rightDLL.add(elementE);
                }
            }
            quicksort(leftDLL);
            quicksort(rightDLL);
            Iterator<E> leftIterator = leftDLL.iterator();
            Iterator<E> rightIterator = rightDLL.iterator();
            while (leftIterator.hasNext()) {
                list.add(leftIterator.next());
            }
            list.add(pivot);
            while (rightIterator.hasNext()) {
                list.add(rightIterator.next());
            }
        }
    }

    /**
     * Quicksort algorithm to sort objects in a list that implements the
     * IndexedUnsortedList interface, using the given Comparator. DO NOT MODIFY
     * THIS METHOD SIGNATURE
     *
     * @param <E> The class of elements in the list
     * @param list The list to be sorted, implements IndexedUnsortedList
     * interface
     * @param c The Comparator used
     */
    private static <E> void quicksort(IndexedUnsortedList<E> list, Comparator<E> c) {
        if (list.size() > 1) {
            E pivot = list.last();
            list.removeLast();
            Iterator<E> it = list.iterator();
            WrappedDLL<E> leftDLL = new WrappedDLL<>();
            WrappedDLL<E> rightDLL = new WrappedDLL<>();
            while (it.hasNext()) {
                E elementE = it.next();
                it.remove();
                if (c.compare(pivot, elementE) >= 0) {
                    leftDLL.add(elementE);
                } else {
                    rightDLL.add(elementE);
                }
            }
            quicksort(leftDLL, c);
            quicksort(rightDLL, c);
            Iterator<E> leftIterator = leftDLL.iterator();
            Iterator<E> rightIterator = rightDLL.iterator();
            while (leftIterator.hasNext()) {
                list.add(leftIterator.next());
            }
            list.add(pivot);
            while (rightIterator.hasNext()) {
                list.add(rightIterator.next());
            }
        }

    }
}
