package com.chris.collections;

import java.util.*;
import java.util.function.Function;

public class IntMap<V> implements Iterable<Map.Entry<Integer, V>> {

  private Node root;
  private int size = 0; // Keep track of size

  @Override
  public Iterator<Map.Entry<Integer, V>> iterator() {
    return entrySet().iterator();
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public boolean containsKey(int key) {
    return search(root, key) != null;
  }

  public boolean containsValue(V value) {
    return containsValueRecursive(root, value);
  }

  private boolean containsValueRecursive(Node node, V value) {
    if (node == null) return false;
    if (Objects.equals(node.value, value)) return true;
    return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
  }

  public V put(int key, V value) {
    Node existing = search(root, key);
    if (existing != null) {
      V oldValue = existing.value;
      existing.value = value;
      return oldValue;
    }
    root = insert(root, key, value);
    size++;
    return null;
  }

  public V putIfAbsent(int key, V value) {
    if (!containsKey(key)) {
      put(key, value);
      return null;
    }
    return get(key);
  }

  public V computeIfAbsent(int key, Function<Integer, V> mappingFunction) {
    if (!containsKey(key)) {
      V newValue = mappingFunction.apply(key);
      put(key, newValue);
      return newValue;
    }
    return get(key);
  }

  public V get(int key) {
    Node node = search(root, key);
    return (node == null) ? null : node.value;
  }

  public V remove(int key) {
    if (!containsKey(key)) return null;
    V oldValue = get(key);
    root = delete(root, key);
    size--;
    return oldValue;
  }

  public void clear() {
    root = null;
    size = 0;
  }

  public IntSet keySet() {
    IntSet keys = new IntSet();
    inOrderKeys(root, keys);
    return keys;
  }

  private void inOrderKeys(Node node, IntSet keys) {
    if (node != null) {
      inOrderKeys(node.left, keys);
      keys.add(node.key);
      inOrderKeys(node.right, keys);
    }
  }

  public Collection<V> values() {
    List<V> values = new ArrayList<>(this.size);
    inOrderValues(root, values);
    return values;
  }

  private void inOrderValues(Node node, List<V> values) {
    if (node != null) {
      inOrderValues(node.left, values);
      values.add(node.value);
      inOrderValues(node.right, values);
    }
  }

  public Set<Map.Entry<Integer, V>> entrySet() {
    Set<Map.Entry<Integer, V>> entries = new TreeSet<>(Comparator.comparingInt(Map.Entry::getKey));
    inOrderEntries(root, entries);
    return entries;
  }

  private void inOrderEntries(Node node, Set<Map.Entry<Integer, V>> entries) {
    if (node != null) {
      inOrderEntries(node.left, entries);
      entries.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
      inOrderEntries(node.right, entries);
    }
  }

  // Internal helper methods (AVL tree balancing, insert, delete, search)
  private Node search(Node node, int key) {
    if (node == null || node.key == key) return node;
    return (key < node.key) ? search(node.left, key) : search(node.right, key);
  }

  private Node insert(Node node, int key, V value) {
    if (node == null) return new Node(key, value);

    if (key < node.key) {
      node.left = insert(node.left, key, value);
    } else if (key > node.key) {
      node.right = insert(node.right, key, value);
    } else {
      node.value = value;
      return node;
    }

    node.height = 1 + Math.max(height(node.left), height(node.right));
    return balance(node, key);
  }

  private Node delete(Node node, int key) {
    if (node == null) return null;

    if (key < node.key) {
      node.left = delete(node.left, key);
    } else if (key > node.key) {
      node.right = delete(node.right, key);
    } else {
      if (node.left == null || node.right == null) {
        node = (node.left != null) ? node.left : node.right;
      } else {
        Node temp = minValueNode(node.right);
        node.key = temp.key;
        node.value = temp.value;
        node.right = delete(node.right, temp.key);
      }
    }

    if (node == null) return null;

    node.height = Math.max(height(node.left), height(node.right)) + 1;
    return balance(node, key);
  }

  private Node balance(Node node, int key) {
    int balanceFactor = getBalance(node);

    if (balanceFactor > 1 && key < node.left.key) return rotateRight(node);
    if (balanceFactor < -1 && key > node.right.key) return rotateLeft(node);
    if (balanceFactor > 1 && key > node.left.key) {
      node.left = rotateLeft(node.left);
      return rotateRight(node);
    }
    if (balanceFactor < -1 && key < node.right.key) {
      node.right = rotateRight(node.right);
      return rotateLeft(node);
    }

    return node;
  }

  private Node rotateRight(Node node) {
    Node left = node.left;
    Node T2 = left.right;

    left.right = node;
    node.left = T2;

    node.height = Math.max(height(node.left), height(node.right)) + 1;
    left.height = Math.max(height(left.left), height(left.right)) + 1;

    return left;
  }

  private Node rotateLeft(Node node) {
    Node right = node.right;
    Node T2 = right.left;

    right.left = node;
    node.right = T2;

    node.height = Math.max(height(node.left), height(node.right)) + 1;
    right.height = Math.max(height(right.left), height(right.right)) + 1;

    return right;
  }

  private int height(Node node) {
    return (node == null) ? 0 : node.height;
  }

  private int getBalance(Node node) {
    return (node == null) ? 0 : height(node.left) - height(node.right);
  }

  private Node minValueNode(Node node) {
    while (node.left != null) node = node.left;
    return node;
  }

  private class Node {
    int key;
    V value;
    Node left, right;
    int height;

    Node(int key, V value) {
      this.key = key;
      this.value = value;
      this.height = 1;
    }
  }
}
