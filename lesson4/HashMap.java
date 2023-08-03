package ru.geekbrains.lesson4;

import java.util.Iterator;

public class HashMap<K, V> implements Iterable<HashMap.Entry<K, V>> {

    private static final int INIT_BUCKET_COUNT = 16;
    private static final double LOAD_FACTOR = 0.75;

    private int size;

    private Bucket<K, V>[] buckets;

    public HashMap() {
        buckets = (Bucket<K, V>[]) new Bucket[INIT_BUCKET_COUNT];
    }

    public HashMap(int initCount) {
        buckets = (Bucket<K, V>[]) new Bucket[initCount];
    }

    public int getSize() {
        return size;
    }

    @Override
    public Iterator<HashMap.Entry<K, V>> iterator() {
        return new HashMapIterator();
    }

    class HashMapIterator implements Iterator<HashMap.Entry<K, V>> {

        private int bucketIndex = 0;
        private Bucket<K, V>.Node currentNode = null;

        @Override
        public boolean hasNext() {
            if (currentNode != null && currentNode.next != null) {
                return true;
            }
            for (int i = bucketIndex + 1; i < buckets.length; i++) {
                if (buckets[i] != null && buckets[i].head != null) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public HashMap.Entry<K, V> next() {
            if (currentNode == null || currentNode.next == null) {
                while (bucketIndex < buckets.length) {
                    if (buckets[bucketIndex] != null && buckets[bucketIndex].head != null) {
                        currentNode = buckets[bucketIndex].head;
                        break;
                    }
                    bucketIndex++;
                }
            } else {
                currentNode = currentNode.next;
            }
            return currentNode.entry;
        }
    }

    public static class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    private int calculateBucketIndex(K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    private void recalculate() {
        size = 0;
        Bucket<K, V>[] old = buckets;
        buckets = (Bucket<K, V>[]) new Bucket[old.length * 2];
        for (int i = 0; i < old.length; i++) {
            Bucket<K, V> bucket = old[i];
            if (bucket != null) {
                Bucket<K, V>.Node node = bucket.head;
                while (node != null) {
                    put(node.entry.getKey(), node.entry.getValue());
                    node = node.next;
                }
            }
            old[i] = null;
        }
    }

    public V put(K key, V value) {
        if (buckets.length * LOAD_FACTOR <= size) {
            recalculate();
        }

        int index = calculateBucketIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new Bucket<>();
        }

        Bucket<K, V> bucket = buckets[index];
        Entry<K, V> entry = new Entry<>(key, value);
        V oldValue = bucket.add(entry);
        if (oldValue == null) {
            size++;
        }
        return oldValue;
    }

    public V get(K key) {
        int index = calculateBucketIndex(key);
        if (buckets[index] != null) {
            return buckets[index].get(key);
        }
        return null;
    }

    public V remove(K key) {
        int index = calculateBucketIndex(key);
        if (buckets[index] != null) {
            V oldValue = buckets[index].remove(key);
            if (oldValue != null) {
                size--;
            }
            return oldValue;
        }
        return null;
    }

    public boolean containsKey(K key) {
        int index = calculateBucketIndex(key);
        return buckets[index] != null && buckets[index].get(key) != null;
    }

    public boolean containsValue(V value) {
        for (Bucket<K, V> bucket : buckets) {
            if (bucket != null) {
                Bucket<K, V>.Node node = bucket.head;
                while (node != null) {
                    if (node.entry.value.equals(value)) {
                        return true;
                    }
                    node = node.next;
                }
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;
        buckets = (Bucket<K, V>[]) new Bucket[INIT_BUCKET_COUNT];
    }

    private class Bucket<K, V> {
        private Node head;

        private V add(Entry<K, V> entry) {
            Node node = new Node();
            node.entry = entry;

            if (head == null) {
                head = node;
                return null;
            }

            Node currentNode = head;
            while (true) {
                if (currentNode.entry.key.equals(entry.key)) {
                    V oldValue = currentNode.entry.value;
                    currentNode.entry.value = entry.value;
                    return oldValue;
                } else if (currentNode.next == null) {
                    currentNode.next = node;
                    break;
                }
                currentNode = currentNode.next;
            }
            return null;
        }

        private V get(K key) {
            Node currentNode = head;
            while (currentNode != null) {
                if (currentNode.entry.key.equals(key)) {
                    return currentNode.entry.value;
                }
                currentNode = currentNode.next;
            }
            return null;
        }

        private V remove(K key) {
            if (head == null) {
                return null;
            }

            if (head.entry.key.equals(key)) {
                V oldValue = head.entry.value;
                head = head.next;
                return oldValue;
            }

            Node currentNode = head;
            while (currentNode.next != null) {
                if (currentNode.next.entry.key.equals(key)) {
                    V oldValue = currentNode.next.entry.value;
                    currentNode.next = currentNode.next.next;
                    return oldValue;
                }
                currentNode = currentNode.next;
            }
            return null;
        }

        class Node {
            Entry<K, V> entry;
            Node next;
        }
    }
}