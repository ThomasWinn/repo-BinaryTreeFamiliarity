// Thomas Nguyen nguy3817@umn.edu
// Brandon Chan  chanx024@umn.edu


import java.util.*;

public class BinaryTree<K extends Comparable<K>, V> {
    private Node<K, V> root;

    public BinaryTree(Node root) {
        this.root = root;
    }

    public Node<K, V> getRoot() {
        return this.root;
    }

    public void add(K key, V value) {
        Node<K, V> newNode = new Node<K, V>(key, value);
        Node <K, V> current = root;
        Node <K, V> previous = null;

        //if theres nothing in the tree
        if(root == null) {
            root = newNode;
        }

        //add new node
        else {
            while(current != null) {
                //if root current is less than right leaf key...
                if(newNode.getKey().compareTo(current.getKey()) < 0) {
                    //will go down left side
                    previous = current;
                    current = current.getLeft();
                }
                else if(current.getKey().compareTo(newNode.getKey()) == 0) {
                    current.setValue(newNode.getValue());
                    return;
                }


                else {
                    //will go down right side
                    previous = current;
                    current = current.getRight();
                }
            }
            // Once its at the end, add node at the bottom
            if(newNode.getKey().compareTo(previous.getKey()) < 0) {
                previous.setLeft(newNode);
            }
            else {
                previous.setRight(newNode);
            }
        }
    }

    //BINARY SEARCH
    // KEY = 4
    public V find(K key) {

        return findHelper(root,key);
    }

    //Binary Search helper compare values
    public V findHelper(Node<K,V> hi, K key) {

        if(hi == null) {
            return null;
        }

        else if(key.compareTo(hi.getKey()) == 0) {
            return (V) hi.getValue();

        }
        else if(key.compareTo(hi.getKey()) < 0) {
            return findHelper(hi.getLeft(),key);
        }
        else if(key.compareTo(hi.getKey()) > 0) {
            return findHelper(hi.getRight(), key);
        }

        else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public V[] flatten() {
        List<V> flatten = new LinkedList<V>();
        //creates the flattened list
        flatten = flattenAssistanceTool(root, flatten);
        V[] flattenedArray = (V[]) new Object[flatten.size()];
        //creates and adds the flattened tree to an array
        for (int i = 0; i < flatten.size(); i++) {
            flattenedArray[i] = flatten.get(i);
        }
        return flattenedArray;
    }

    public List<V> flattenAssistanceTool(Node<K, V> root, List<V> flatten) {
        //If the tree is empty just return an empty list
        if(root == null) {
            return flatten;
        } else {
            //return each subtree and continuously add the subtree's root's value
            //to a list
            flattenAssistanceTool(root.getLeft(), flatten);
            flatten.add(root.getValue());
            flattenAssistanceTool(root.getRight(), flatten);
            return flatten;
        }
    }

    //finds number of nodes in tree
    public int numberOfNodes(Node roooot) {
        int number = 0;
        if(roooot != null) {
            return numberOfNodes(roooot.getLeft()) + numberOfNodes(roooot.getRight()) + 1;
        }
        return 0;
    }

    // finds the 20 to allow inorder traversal to happen
    public K findLoowestKey(Node prev, Node curr) {

        if(curr.getLeft() == null) {
            return (K) prev.getKey();
        } else {
            return findLoowestKey(curr, curr.getLeft());
        }
    }

    public void remove(K key) {

        Node<K,V> current = root;
        Node<K,V> previous = null;

        // when removing root... make right side the new root
        while(current != null) {

            // if target key is found and current.next isnt null
            if(current.getKey() == key && (current.getLeft() != null || current.getRight() != null)) {

                //if wanting to remove root make lowest left side key the new root
                if(current.getKey().compareTo(root.getKey()) == 0) {
                    root = smallestNode(current.getRight());
                    break;
                }
                // left side
                else if(current.getKey().compareTo(previous.getKey()) < 0) {
                    previous.setLeft(current.getLeft());
                    break;
                    //right side
                } else {
                    previous.setRight(current.getRight());
                    break;
                }
            }

            else if(current.getKey() == key && (current.getRight() == null || current.getLeft() == null)) {
                // left side
                if(current.getKey().compareTo(previous.getKey()) < 0) {
                    previous.setLeft(null);
                    break;
                }
                //right side
                else {
                    previous.setRight(null);
                    break;
                }
            }

            // if key is less than the current node go down left side
            else if(key.compareTo(current.getKey()) < 0) {
                previous = current;
                current = current.getLeft();
            }
            // goes down right side
            else if (key.compareTo(current.getKey()) > 0){
                previous = current;
                current = current.getRight();
            }
        }
    }

    // Returns smallest node and makes the node before set the smallest node's spot to null
    public Node<K,V> smallestNode(Node<K,V> nodeee) {

        // no child nodes on node
        if(nodeee.getLeft() == null && nodeee.getRight() == null) {
            Node<K,V> noode = nodeee;
            root.setRight(null);
            return noode;
        }
        else if(nodeee.getLeft() == null && nodeee.getRight() != null) {
            Node<K,V> noddde = noLeftNodeCase(nodeee);
        }
        else {

            Node<K, V> after = nodeee.getLeft();

            if (after.getLeft() != null) {
                nodeee = after;
                return smallestNode(nodeee);
            }
            nodeee.setLeft(null);
            return after;
        }
        return nodeee;
    }

    // If left side is null, traverse right side
    public Node<K,V> noLeftNodeCase(Node node) {
        Node<K,V> after = node.getRight();
        if(after.getLeft()!=null) {
            node = after;
            return noLeftNodeCase(node);
        }
        node.setRight(null);
        return node;
        }



    // Passes in the subtree
    public boolean containsSubtree(BinaryTree<K, V> other) {

        Node<K,V> current = root;

        if(other == null || other.getRoot() == null) {
            return true;
        }

        while(current != null) {
            // if current finds the subtree root
            if(current.getKey().compareTo(other.getRoot().getKey()) == 0) {
                if(isIdentical(current, other.getRoot())) {
                    return true;
                } else {
                    return false;
                }
            }

            //iterate current root

            //goes to left side if subtree root is less than the current root
            if(other.getRoot().getKey().compareTo(current.getKey()) < 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }

            // if subtreeRoot is not found, it will exit loop and return false
        }
        return false;
    }

    //returns true if both the left key and right key are the same of both the subtree root and current root
    //another condition?
    public static boolean isIdentical(Node current, Node subTreeRoot) {


        if(current == null && subTreeRoot == null) {
            return true;
        }
        else if(current == null && subTreeRoot != null) {
            return false;
        }
        else if(current != null && subTreeRoot == null) {
            return false;
        }
        else if(current.getKey().compareTo(subTreeRoot.getKey()) == 0 && current.getValue().equals(subTreeRoot.getValue())) {
            return (isIdentical(current.getLeft(), subTreeRoot.getLeft()) && isIdentical(current.getRight(), subTreeRoot.getRight()));
        } else {
            return false;
        }
    }
}