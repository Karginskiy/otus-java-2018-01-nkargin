package ru.otus.homework_3;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

/**
 * Created by nkargin on 15.02.2018.
 * hei@spark-mail.ru
 */
public class SimpleArrayList<T> implements List<T> {

    private static final int START_LENGTH = 10;
    private int size;
    private T[] data;

    @SuppressWarnings("unchecked")
    public SimpleArrayList() {
        this.data = (T[]) new Object[START_LENGTH];
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (size == 0) {
            return false;
        }

        for (T element : data) {
            if (element == null ? o == null : element.equals(o)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new SimpleIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] objects = (Object[]) Array.newInstance(data.getClass().getComponentType(), size);
        System.arraycopy(data, 0, objects, 0, size);
        return objects;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T1> T1[] toArray(T1[] a) {
        if (a.length >= data.length) {
            System.arraycopy(data, 0, a, 0, size);
            return a;
        } else {
            Class<?> targetArrayType = a.getClass().getComponentType();
            Class<?> srcArrayType = data.getClass().getComponentType();
            if (!srcArrayType.isAssignableFrom(targetArrayType)) {
                throw new ArrayStoreException();
            }

            T1[] resultArray = (T1[]) Array.newInstance(targetArrayType, size);
            for (int i = 0; i < resultArray.length; i++) {
                resultArray[i] = (T1) data[i];
            }

            return resultArray;
        }
    }

    @Override
    public boolean add(T t) {
        if (size == data.length) {
            extendData();
        }
        data[size++] = t;

        return true;
    }

    @SuppressWarnings("unchecked")
    private void extendData() {
        Class<?> componentType = data.getClass().getComponentType();
        T[] newData = (T[]) Array.newInstance(componentType, getNextSize());
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
    }

    private int getNextSize() {
        if (data.length == 0) {
            return 1;
        }
        return (data.length * 3) / 2 + 1;
    }

    @Override
    public boolean remove(Object o) {
        if (!data.getClass().getComponentType().isAssignableFrom(o.getClass())) {
            return false;
        }

        if (size == 0) {
            return false;
        }

        Iterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (next == null ? o == null : next.equals(o)) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        c.forEach(this::add);
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean addAll(int index, Collection<? extends T> c) {
        Objects.requireNonNull(c);

        if (index > data.length) {
            throw new ArrayIndexOutOfBoundsException();
        }

        Class<?> srcComponentType = data.getClass().getComponentType();

        T[] leftPart = (T[]) Array.newInstance(srcComponentType, index);
        System.arraycopy(data, 0, leftPart, 0, index);

        T[] newElements = c.toArray((T[]) Array.newInstance(srcComponentType));

        T[] rightShiftedPart =  (T[]) Array.newInstance(srcComponentType, data.length - index - 1);
        System.arraycopy(data, index, rightShiftedPart, 0, rightShiftedPart.length);

        Stream<T> part = Stream.concat(Arrays.stream(leftPart), Arrays.stream(newElements));
        Stream<T> full = Stream.concat(part, Arrays.stream(rightShiftedPart));

        data = full.collect(toList()).toArray((T[]) Array.newInstance(srcComponentType));

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        c.forEach(this::remove);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        removeIf(element -> !contains(element));
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        data = (T[]) Array.newInstance(data.getClass().getComponentType(), START_LENGTH);
        size = 0;
    }

    @Override
    public T get(int index) {
        return data[index];
    }

    @Override
    public T set(int index, T element) {
        if (index >= data.length) {
            throw new ArrayIndexOutOfBoundsException();
        }

        T toReturn = get(index);
        data[index] = element;

        return toReturn;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void add(int index, T element) {
        if (size == data.length) {
            extendData();
        }

        T[] prevData = (T[]) Array.newInstance(data.getClass().getComponentType(), data.length);
        System.arraycopy(data, 0, prevData, 0, data.length);

        System.arraycopy(prevData, 0, data, 0, index);
        prevData[index] = element;
        System.arraycopy(prevData, index, data, index + 1, prevData.length - index);
    }

    @Override
    public T remove(int index) {
        T previous = get(index);
        if (previous != null) {
            System.arraycopy(data, index + 1, data, index, size - index);
            size--;
        }
        return previous;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < data.length; i++) {
            if (o.equals(data[i])) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = data.length - 1; i >= 0; i++) {
            if (o.equals(data[i])) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ListItr();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ListItr();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return range(fromIndex, toIndex).mapToObj(this::get).collect(toList());
    }

    private class SimpleIterator implements Iterator<T> {

        int nextPosition;
        int indexOfLastReturned;

        @Override
        public boolean hasNext() {
            return nextPosition != size;
        }

        @Override
        public T next() {
            if (nextPosition >= size) {
                throw new NoSuchElementException();
            }
            T el = data[nextPosition];
            indexOfLastReturned = nextPosition;
            nextPosition++;
            return el;
        }

        @Override
        public void remove() {
            SimpleArrayList.this.remove(indexOfLastReturned);
            indexOfLastReturned = -1;
        }
    }

    private class ListItr extends SimpleIterator implements java.util.ListIterator<T> {

        private int previousPosition;

        @Override
        public boolean hasNext() {
            return super.hasNext();
        }

        @Override
        public T next() {
            return super.next();
        }

        @Override
        public boolean hasPrevious() {
            return previousPosition >= 0;
        }

        @Override
        public T previous() {
            if (previousPosition < 0) {
                throw new NoSuchElementException();
            }

            indexOfLastReturned = previousPosition;
            T datum = data[previousPosition];
            previousPosition--;
            return datum;
        }

        @Override
        public int nextIndex() {
            return nextPosition;
        }

        @Override
        public int previousIndex() {
            return previousPosition;
        }

        @Override
        public void remove() {
            super.remove();
        }

        @Override
        public void set(T t) {
            data[indexOfLastReturned] = t;
        }

        @Override
        public void add(T t) {
            SimpleArrayList.this.add(indexOfLastReturned, t);
        }
    }
}
