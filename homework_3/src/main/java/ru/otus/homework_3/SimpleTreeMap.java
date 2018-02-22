package ru.otus.homework_3;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by nkargin on 16.02.2018.
 *
 * Simple implementation of tree map with marking remove.
 *
 * hei@spark-mail.ru
 */
@SuppressWarnings("unchecked")
public class SimpleTreeMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {

    private Comparator<K> keyComparator;
    private int size;

    private TreeNode<K, V> root;
    private Set<Entry<K, V>> entrySet = new HashSet<>();

    public SimpleTreeMap(Comparator<K> comparator) {
        this.keyComparator = comparator;
        this.size = 0;
    }

    public SimpleTreeMap() {
        this(null);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return this.keySet().contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public V get(Object key) {
        TreeNode<K, V> current = root;

        while (current != null) {
            if (current.getKey().equals(key) && !current.isRemoved()) {
                return current.getValue();
            }

            int compare = compare(current.getKey(), (K) key);

            current = compare == 1 ? current.getRight() : current.getLeft();
        }

        return null;
    }

    @Override
    public V put(K key, V value) {
        TreeNode<K, V> target = new TreeNode<>(key, value);

        if (root == null) {
            root = target;
            add(target, null);
            return null;
        }

        TreeNode<K, V> current = root;
        while (current != null) {
            if (current.getKey().equals(target.getKey())) {
                return current.setValue(target.getValue());
            }

            int compare = compare(current.getKey(), target.getKey());
            if (compare == 0) {
                compare = 1;
            }

            if (compare == -1) {
                if (current.getLeft() == null) {
                    current.setLeft(target);
                    add(target, current);
                    break;
                }
                current = current.getLeft();
            } else {
                if (current.getRight() == null) {
                    current.setRight(target);
                    add(target, current);
                    break;
                }
                current = current.getRight();
            }
        }

        return null;
    }

    private void add(TreeNode<K, V> target, TreeNode<K, V> parent) {
        target.setParent(parent);
        entrySet.add(target);
        size++;
    }

    private int compare(K current, K target) {
        return keyComparator != null ? keyComparator.compare(current, target) :
                current instanceof Comparable ? ((Comparable) current).compareTo(target) : 0;
    }

    @Override
    public V remove(Object key) {
        TreeNode<K, V> current = root;

        if (root == null) {
            return null;
        }

        if (current.getKey().equals(key)) {
            V val = current.getValue();
            root.setRemoved(true);
            size--;
            return val;
        }

        while (current != null) {

            int compare = compare(current.getKey(), (K) key);

            if (compare == -1) {
                TreeNode<K, V> left = current.getLeft();
                if (left != null) {
                    if (left.getKey().equals(key)) {
                        V val = left.getValue();
                        current.getLeft().setRemoved(true);
                        size--;
                        return val;
                    }
                    current = left;
                }
            } else {
                TreeNode<K, V> right = current.getRight();
                if (right != null) {
                    if (right.getKey().equals(key)) {
                        V val = right.getValue();
                        current.getRight().setRemoved(true);
                        size--;
                        return val;
                    }
                    current = right;
                }
            }
        }

        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        return entrySet.stream().map(Entry::getKey).collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        return entrySet.stream().map(Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.entrySet;
    }

    public static class TreeNode<K, V> implements Map.Entry<K, V> {

        private K key;
        private V value;

        private TreeNode<K, V> parent;
        private TreeNode<K, V> left;
        private TreeNode<K, V> right;
        private boolean isRemoved;

        public TreeNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public boolean isRemoved() {
            return isRemoved;
        }

        public void setRemoved(boolean removed) {
            isRemoved = removed;
        }

        public TreeNode<K, V> getParent() {
            return parent;
        }

        public void setParent(TreeNode<K, V> parent) {
            this.parent = parent;
        }

        public TreeNode<K, V> getLeft() {
            return left;
        }

        public void setLeft(TreeNode<K, V> left) {
            this.left = left;
        }

        public TreeNode<K, V> getRight() {
            return right;
        }

        public void setRight(TreeNode<K, V> right) {
            this.right = right;
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
    }

}
