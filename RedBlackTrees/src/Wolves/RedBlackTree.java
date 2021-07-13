package Wolves;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

;


class Node {
    String data;
    Node parent;
    Node left;
    Node right;
    int color;
}

public class RedBlackTree {
    private Node root;
    private Node TNULL;

    // Balance the tree after deletion of a node
    private void fixDelete(Node x) {
        Node s;
        while (x != root && x.color == 0) {
            if (x == x.parent.left) {
                s = x.parent.right;
                if (s.color == 1) {
                    s.color = 0;
                    x.parent.color = 1;
                    leftRotate(x.parent);
                    s = x.parent.right;
                }

                if (s.left.color == 0 && s.right.color == 0) {
                    s.color = 1;
                    x = x.parent;
                } else {
                    if (s.right.color == 0) {
                        s.left.color = 0;
                        s.color = 1;
                        rightRotate(s);
                        s = x.parent.right;
                    }

                    s.color = x.parent.color;
                    x.parent.color = 0;
                    s.right.color = 0;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                s = x.parent.left;
                if (s.color == 1) {
                    s.color = 0;
                    x.parent.color = 1;
                    rightRotate(x.parent);
                    s = x.parent.left;
                }

                if (s.right.color == 0) {
                    s.color = 1;
                    x = x.parent;
                } else {
                    if (s.left.color == 0) {
                        s.right.color = 0;
                        s.color = 1;
                        leftRotate(s);
                        s = x.parent.left;
                    }

                    s.color = x.parent.color;
                    x.parent.color = 0;
                    s.left.color = 0;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = 0;
    }

    private void rbTransplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private void deleteNodeHelper(Node node, String key) {
        Node z = TNULL;
        Node x, y;
        while (node != TNULL) {
            if (node.data == key) {
                z = node;
            }

            if (key.compareToIgnoreCase(node.data) >= 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        }

        if (z == TNULL) {
            System.out.println("Couldn't find key in the tree");
            return;
        }

        y = z;
        int yOriginalColor = y.color;
        if (z.left == TNULL) {
            x = z.right;
            rbTransplant(z, z.right);
        } else if (z.right == TNULL) {
            x = z.left;
            rbTransplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                rbTransplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }

            rbTransplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if (yOriginalColor == 0) {
            fixDelete(x);
        }
    }

    // Balance the node after insertion
    private void fixInsert(Node k) {
        Node u;
        while (k.parent.color == 1) {
            if (k.parent == k.parent.parent.right) {
                u = k.parent.parent.left;
                if (u.color == 1) {
                    u.color = 0;
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rightRotate(k);
                    }
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    leftRotate(k.parent.parent);
                }
            } else {
                u = k.parent.parent.right;

                if (u.color == 1) {
                    u.color = 0;
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        leftRotate(k);
                    }
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    rightRotate(k.parent.parent);
                }
            }
            if (k == root) {
                break;
            }
        }
        root.color = 0;
    }

       private void printInorder( Node node ,FileWriter mywriter) throws IOException {
            if (node == null)
                return;

            /* first recur on left child */
           if (node.data!=null) {
               printInorder(node.left, mywriter);

               /* then print the data of node */
               String newline = System.getProperty("line.separator");
               mywriter.write(node.data + newline);
               /* now recur on right child */
               printInorder(node.right, mywriter);
           }
        }


    static int getfullCount(Node root) {
        if (root == null)
            return 0;

        int res = 0;
        if (root.left != null && root.right != null)
            res++;

        res += (getfullCount(root.left) + getfullCount(root.right));
        return res;
    }

    private void iterativeSearch(Node root, String key) {
        // Traverse untill root reaches to dead end
        while (root != TNULL) {
            // pass right subtree as new tree
            if (root.data != null) {
                if (key.compareTo(root.data) > 0)
                    root = root.right;

                    // pass left subtree as new tree
                else if (key.compareTo(root.data) < 0)
                    root = root.left;
                else if (key.equalsIgnoreCase(root.data)) {
                    System.out.println("found");
                    return;
                }
            } else
                return;
        }
        System.out.println("Not found");
    }


    private void printHelper(Node root, String indent, boolean last) {
        if (root != TNULL) {
            System.out.print(indent);
            if (last) {
                System.out.print("R----");
                indent += "   ";
            } else {
                System.out.print("L----");
                indent += "|  ";
            }

            String sColor = root.color == 1 ? "RED" : "BLACK";
            System.out.println(root.data + "(" + sColor + ")");
            printHelper(root.left, indent, false);
            printHelper(root.right, indent, true );
        }
    }

    public RedBlackTree() {
        TNULL = new Node();
        TNULL.color = 0;
        TNULL.left = null;
        TNULL.right = null;
        root = TNULL;
    }

    int maxDepth(Node node) {
        if (node == TNULL)
            return 0;
        else {
            /* compute the depth of each subtree */
            int lDepth = maxDepth(node.left);
            int rDepth = maxDepth(node.right);
            /* use the larger one */
            if (lDepth > rDepth)
                return (lDepth+1);
            else
                return (rDepth+1);
        }
    }

    public Node minimum(Node node) {
        while (node.left != TNULL) {
            node = node.left;
        }
        return node;
    }

    public Node maximum(Node node) {
        while (node.right != TNULL) {
            node = node.right;
        }
        return node;
    }

    public Node successor(Node x) {
        if (x.right != TNULL) {
            return minimum(x.right);
        }

        Node y = x.parent;
        while (y != TNULL && x == y.right) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    public Node predecessor(Node x) {
        if (x.left != TNULL) {
            return maximum(x.left);
        }

        Node y = x.parent;
        while (y != TNULL && x == y.left) {
            x = y;
            y = y.parent;
        }

        return y;
    }


    private Node search(Node root, String key)  {
        // Base Cases: root is null or key is present at root
        if (root == TNULL || root.data.equalsIgnoreCase(key))
            return root;
        // val is greater than root's key
        if (root.data.compareToIgnoreCase(key)>0)
            return search(root.left, key);
        // val is less than root's key
        return search(root.right, key);
    }



    public void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != TNULL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    public void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != TNULL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    public void insert(String key) {
        Node node = new Node();
        node.parent = null;
        node.data = key;
        node.left = TNULL;
        node.right = TNULL;
        node.color = 1;
        Node y = null;
        Node x = this.root;
        while (x != TNULL) {
            y = x;
            if (x.data.compareToIgnoreCase(node.data) > 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        node.parent = y;
        if (y == null) {
            root = node;
        } else if (y.data.compareToIgnoreCase(node.data) > 0) {
            y.left = node;
        } else {
            y.right = node;
        }
        if (node.parent == null) {
            node.color = 0;
            return;
        }
        if (node.parent.parent == null) {
            return;
        }

        fixInsert(node);
    }

    public Node getRoot() {
        return this.root;
    }

    public void deleteNode(String data) {
        deleteNodeHelper(this.root, data);
    }

    public void printTree() {
        printHelper(this.root, "", true);
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        RedBlackTree obj = new RedBlackTree();

        int x=0;
       while (x!=7)
       {
            System.out.println("\nSelect an operation:\n");
            System.out.println("1. Load Dictionary (File Path):");
            System.out.println("2. Insert a new word:");
            System.out.println("3. Lookup a word:");
            System.out.println("4. Print height of RBT:");
            System.out.println("5. Print Dictionary Size:");
            System.out.println("6. Print into a new file:");
            System.out.println("7.Exit:");



            int ch = scan.nextInt();
            switch (ch) {
                case 1:
                    System.out.println("Enter file path");
                    try {
                        File myObj = new File(scan.next());
                        Scanner myReader = new Scanner(myObj);
                        while (myReader.hasNextLine()) {
                            String data = myReader.nextLine();
                            obj.insert(data);
                        }
                        myReader.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    System.out.println("Enter element:");
                    String element = scan.next();
                    Node node1 = obj.search(obj.root, element);
                    if (node1.data!=null)
                    {
                        System.out.println("Word Already in Dictionary");
                    }else {
                        obj.insert(element);
                    }
                    break;
                case 3:
                    System.out.println("Enter element to search");
                    Node node = obj.search(obj.root, scan.next());
                    if (node.data!=null)
                    {
                        System.out.println("Found");
                    }else {
                        System.out.println("Not Found");
                    }
                    break;
                case 4:
                    System.out.println("Tree Height: " + (obj.maxDepth(obj.root)-1) );
                    break;
                case 5:
                    System.out.println("Number of nodes: " +  obj.maxDepth(obj.root));
                    int n = getfullCount(obj.root);
                    System.out.println(n);
                    break;
                case 6:
                    try {
                        FileWriter myWriter = new FileWriter("New Dictionary.txt");
                        obj.printInorder(obj.root,myWriter);
                        myWriter.close();
                        System.out.println("Successfully wrote to the file.");
                    } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    System.out.println("Exiting");
                    x=7;
                    break;
            }
        }
    }
}