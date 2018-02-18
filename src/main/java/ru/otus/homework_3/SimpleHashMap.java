package ru.otus.homework_3;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import static java.lang.Math.abs;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Created by nkargin on 16.02.2018.
 *
 * My simple hash map implementation. No Treefying and not good collision evasion.
 *
 * hei@spark-mail.ru
 */
@SuppressWarnings("unchecked")
public class SimpleHashMap<K, V> implements Map<K, V> {

    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int MAX_SIZE = 1 << 30;
    private static final int DEFAULT_SIZE = 1 << 4;

    private int size;
    private Node<K, V>[] data;
    private double loadFactor;
    private Set<Entry<K, V>> entrySet = new HashSet<>();

    public SimpleHashMap() {
        this(DEFAULT_SIZE);
    }

    public SimpleHashMap(int capacity) {
        int nextPowerOfTwo = getNextPowerOfTwo(capacity);
        this.data = (Node<K, V>[]) new Node[capacity];
        this.size = 0;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    public SimpleHashMap(double loadFactor, int capacity) {
        this(capacity);
        if (loadFactor < 0 && loadFactor > 1) throw new IllegalArgumentException("Incorrect load factor");
        this.loadFactor = loadFactor;
    }

    private int getNextPowerOfTwo(int num) {
        num = num - 1;
        num = num | (num >> 1);
        num = num | (num >> 2);
        num = num | (num >> 4);
        num = num | (num >> 8);
        num = num | (num >> 16);
        return num + 1;
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
    public boolean containsKey(Object key) {
        if (key == null) {
            return containsKey(getNodeByKeyHash(0, data), key);
        }

        return containsKey(getNodeByKeyHash(key.hashCode(), data), key);
    }

    private boolean containsKey(final Node<K, V> node, Object key) {
        return node != null && (key.equals(node.getKey()) || containsKey(node.getNext(), key));
    }

    private Node<K, V> getNodeByKeyHash(int i, Node<K, V>[] data) {
        return data[getIndexByHash(i, data.length)];
    }

    private int getIndexByHash(int hash, int length) {
        return abs(hash % length);
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public V get(Object key) {
        if (key == null) {
            Node<K, V> nodeByKeyHash = getNodeByKeyHash(0, data);
            if (nodeByKeyHash == null) {
                return null;
            }

            return nodeByKeyHash.getKey() == null ? nodeByKeyHash.getValue() : null;
        }

        int indexByHash = getIndexByHash(key.hashCode(), data.length);
        Node<K, V> current = getNodeByKeyHash(indexByHash, data);

        while (current != null) {
            if (current.getKey().equals(key)) {
                return current.getValue();
            }

            current = current.getNext();
        }

        return null;
    }

    @Override
    public V put(K key, V value) {
        if (data.length == 0 || isOnThreshold()) {
            resizeAndRehash();
        }

        Node<K, V> targetNode = new Node<>();
        targetNode.setKey(key);
        targetNode.setValue(value);
        targetNode.setHash(key.hashCode());

        if (key == null) {
            return putNullValue(targetNode);
        } else {
            int indexByHash = getIndexByHash(key.hashCode(), data.length);
            Node<K, V> nodeByKeyHash = getNodeByKeyHash(indexByHash, data);
            if (nodeByKeyHash == null) {
                data[indexByHash] = targetNode;
                size++;
                entrySet.add(targetNode);
                return null;
            } else {
                Node<K, V> current = nodeByKeyHash;
                while (current != null) {
                    if (key.equals(current.getKey())) {
                        return current.setValue(value);
                    }

                    current = current.getNext();
                }

                putFirstToChain(nodeByKeyHash, targetNode);
                data[indexByHash] = targetNode;
                entrySet.add(targetNode);
                size++;
            }
        }

        return null;
    }

    /*
        Naive implementation! Throws runtime exception on resize if no more place exists.
     */
    private void resizeAndRehash() {
        int newLength, prevLength = data.length;
        Class<?> componentType = data.getClass().getComponentType();
        newLength = prevLength << 1;

        if (newLength >= MAX_SIZE) {
            throw new RuntimeException("No more place in hashmap.");
        }

        Node<K, V>[] newInstance = (Node<K, V>[]) Array.newInstance(componentType, newLength);

        for (int i = 0; i < prevLength; i++) {
            Node<K, V> oldTabElement = data[i];
            if (oldTabElement == null) {
                continue;
            }

            if (!oldTabElement.hasNext()) {
                newInstance[getIndexByHash(oldTabElement.getHash(), newLength)] = oldTabElement;
                continue;
            }

            traverseCollisionsListAndRehash(newLength, newInstance, oldTabElement);
        }

        data = newInstance;
    }

    private void traverseCollisionsListAndRehash(int newLength, Node<K, V>[] newInstance, Node<K, V> oldTabElement) {

        /*
         * Reverse list to fight list cycles.
         */

        Node<K, V> last = oldTabElement;
        while (last.getNext() != null) {
            last = last.getNext();
        }

        Node<K, V> current = last;

        /*
         * Update hashes for all the chain elements.
         */

        while (current != null) {
            int indexByHash = getIndexByHash(current.getKey().hashCode(), newLength);
            Node<K, V> nodeByKeyHash = getNodeByKeyHash(indexByHash, newInstance);
            if (nodeByKeyHash == null) {
                newInstance[indexByHash] = current;
            } else {
                putFirstToChain(nodeByKeyHash, current);
                newInstance[indexByHash] = current;
            }

            current = current.getPrevious();
        }
    }

    private boolean isOnThreshold() {
        return ((data.length << 1) * loadFactor) <= size;
    }

    private V putNullValue(Node<K, V> targetNode) {
        int indexByHash = getIndexByHash(0, data.length);

        Node<K, V> nodeByKeyHash = getNodeByKeyHash(indexByHash, data);
        if (nodeByKeyHash == null) {
            data[indexByHash] = targetNode;
            size++;
            entrySet.add(targetNode);
            return null;
        }

        return null;
    }

    private void putFirstToChain(Node<K, V> node, Node<K, V> newNode) {
        node.setPrevious(newNode);
        newNode.setNext(node);
        newNode.setPrevious(null);
    }

    @Override
    public V remove(Object key) {
        if (key == null) {
            Node<K, V> nodeByKeyHash = getNodeByKeyHash(0, data);
            if (nodeByKeyHash != null) {
                if (nodeByKeyHash.getKey() == null) {
                    data[0] = null;
                    size--;
                    return nodeByKeyHash.getValue();
                }
            }

            return null;
        }

        int indexByHash = getIndexByHash(key.hashCode(), data.length);
        Node<K, V> current = getNodeByKeyHash(indexByHash, data);

        while (current != null) {
            if (current.getKey().equals(key)) {
                V value = current.getValue();

                if (current.getPrevious() == null && current.getNext() == null) {
                    data[indexByHash] = null;
                    size--;
                    return value;
                } else {
                    updateChain(current);
                    size--;
                    return value;
                }
            }

            current = current.getNext();
        }

        return null;
    }

    private void updateChain(Node<K, V> current) {
        Node<K, V> previous = current.getPrevious();
        Node<K, V> next = current.getNext();

        if (previous != null) {
            previous.setNext(next);
        }
        if (next != null) {
            next.setPrevious(previous);
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        size = 0;
        IntStream.range(0, data.length).forEach(index -> data[index] = null);
    }

    @Override
    public Set<K> keySet() {
        return entrySet.stream().map(Entry::getKey).collect(toSet());
    }

    @Override
    public Collection<V> values() {
        return entrySet.stream().map(Entry::getValue).collect(toList());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.entrySet;
    }

    private static class Node<K, V> implements Map.Entry<K, V> {
        private Node<K, V> previous;
        private Node<K, V> next;
        private K key;
        private V value;
        private int hash;

        public boolean hasNext() {
            return next != null;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V prev = this.value;
            this.value = value;
            return prev;
        }

        public Node<K, V> getPrevious() {
            return previous;
        }

        public void setPrevious(Node<K, V> previous) {
            this.previous = previous;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public int getHash() {
            return hash;
        }

        public void setHash(int hash) {
            this.hash = hash;
        }
    }
}
