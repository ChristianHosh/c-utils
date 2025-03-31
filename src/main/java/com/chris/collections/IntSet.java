package com.chris.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class IntSet {
  private Node root;
  private int size = 0;

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public boolean contains(int value) {
    return search(root, value) != null;
  }

  public boolean add(int value) {
    if (contains(value)) return false;
    root = insert(root, value);
    size++;
    return true;
  }

  public boolean remove(int value) {
    if (!contains(value)) return false;
    root = delete(root, value);
    size--;
    return true;
  }

  public void clear() {
    root = null;
    size = 0;
  }

  public int[] toArray() {
    List<Integer> list = new ArrayList<>();
    inOrder(root, list);
    return list.stream().mapToInt(i -> i).toArray();
  }

  public Iterator<Integer> iterator() {
    List<Integer> list = new ArrayList<>();
    inOrder(root, list);
    return list.iterator();
  }

  public Integer first() {
    if (root == null) throw new NoSuchElementException();
    Node node = root;
    while (node.left != null) node = node.left;
    return node.value;
  }

  public Integer last() {
    if (root == null) throw new NoSuchElementException();
    Node node = root;
    while (node.right != null) node = node.right;
    return node.value;
  }

  public Integer higher(int value) {
    return findHigher(root, value, null);
  }

  public Integer lower(int value) {
    return findLower(root, value, null);
  }

  private Integer findHigher(Node node, int value, Integer candidate) {
    if (node == null) return candidate;
    if (node.value > value) return findHigher(node.left, value, node.value);
    return findHigher(node.right, value, candidate);
  }

  private Integer findLower(Node node, int value, Integer candidate) {
    if (node == null) return candidate;
    if (node.value < value) return findLower(node.right, value, node.value);
    return findLower(node.left, value, candidate);
  }

  private Node search(Node node, int value) {
    if (node == null || node.value == value) return node;
    return (value < node.value) ? search(node.left, value) : search(node.right, value);
  }

  private Node insert(Node node, int value) {
    if (node == null) return new Node(value);
    if (value < node.value) {
      node.left = insert(node.left, value);
    } else if (value > node.value) {
      node.right = insert(node.right, value);
    }
    node.height = 1 + Math.max(height(node.left), height(node.right));
    return balance(node, value);
  }

  private Node delete(Node node, int value) {
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

  private Node balance(Node node, int value) {
    int balanceFactor = getBalance(node);

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

  private void inOrder(Node node, List<Integer> list) {
    if (node != null) {
      inOrder(node.left, list);
      list.add(node.value);
      inOrder(node.right, list);
    }
  }

  private static class Node {
    int value;
    Node left, right;
    int height;

    Node(int value) {
      this.value = value;
      this.height = 1;
    }
  }
}
