package com.chris.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class LongSet implements Iterable<Long>{

  private Node root;
  private int size = 0;

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public boolean contains(long value) {
    return search(root, value) != null;
  }

  public boolean add(long value) {
    if (contains(value)) return false;
    root = insert(root, value);
    size++;
    return true;
  }

  public boolean remove(long value) {
    if (!contains(value)) return false;
    root = delete(root, value);
    size--;
    return true;
  }

  public void clear() {
    root = null;
    size = 0;
  }

  public long[] toArray() {
    List<Long> list = new ArrayList<>();
    inOrder(root, list);
    return list.stream().mapToLong(i -> i).toArray();
  }

  @Override
  public Iterator<Long> iterator() {
    List<Long> list = new ArrayList<>();
    inOrder(root, list);
    return list.iterator();
  }

  public Long first() {
    if (root == null) throw new NoSuchElementException();
    Node node = root;
    while (node.left != null) node = node.left;
    return node.value;
  }

  public Long last() {
    if (root == null) throw new NoSuchElementException();
    Node node = root;
    while (node.right != null) node = node.right;
    return node.value;
  }

  public Long higher(long value) {
    return findHigher(root, value, null);
  }

  public Long lower(long value) {
    return findLower(root, value, null);
  }

  private Long findHigher(Node node, long value, Long candidate) {
    if (node == null) return candidate;
    if (node.value > value) return findHigher(node.left, value, node.value);
    return findHigher(node.right, value, candidate);
  }

  private Long findLower(Node node, long value, Long candidate) {
    if (node == null) return candidate;
    if (node.value < value) return findLower(node.right, value, node.value);
    return findLower(node.left, value, candidate);
  }

  private Node search(Node node, long value) {
    if (node == null || node.value == value) return node;
    return (value < node.value) ? search(node.left, value) : search(node.right, value);
  }

  private Node insert(Node node, long value) {
    if (node == null) return new Node(value);
    if (value < node.value) {
      node.left = insert(node.left, value);
    } else if (value > node.value) {
      node.right = insert(node.right, value);
    }
    node.height = 1 + Math.max(height(node.left), height(node.right));
    return balance(node, value);
  }

  private Node delete(Node node, long value) {
    if (node == null) return null;
    if (value < node.value) {
      node.left = delete(node.left, value);
    } else if (value > node.value) {
      node.right = delete(node.right, value);
    } else {
      if (node.left == null || node.right == null) {
        return (node.left != null) ? node.left : node.right;
      } else {
        Node temp = minValueNode(node.right);
        node.value = temp.value;
        node.right = delete(node.right, temp.value);
      }
    }
    node.height = Math.max(height(node.left), height(node.right)) + 1;
    return balance(node, value);
  }

  private Node balance(Node node, long value) {
    long balanceFactor = getBalance(node);

    if (balanceFactor > 1 && value < node.left.value) return rotateRight(node);
    if (balanceFactor < -1 && value > node.right.value) return rotateLeft(node);
    if (balanceFactor > 1 && value > node.left.value) {
      node.left = rotateLeft(node.left);
      return rotateRight(node);
    }
    if (balanceFactor < -1 && value < node.right.value) {
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

  private void inOrder(LongSet.Node node, List<Long> list) {
    if (node != null) {
      inOrder(node.left, list);
      list.add(node.value);
      inOrder(node.right, list);
    }
  }

  private static class Node {
    long value;
    Node left, right;
    int height;

    Node(long value) {
      this.value = value;
      this.height = 1;
    }
  }
  
}
