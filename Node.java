package merkle;

import javax.print.DocFlavor;

public class Node {
    private String data;
    private String hashValue;
    private Node left;
    private Node right;
    private Node parent;
    private SHA256 sha256;

    public Node() {
        sha256 = new SHA256();
        this.data = null;
        this.hashValue = null;
        left = null;
        right = null;
        parent = null;
    }

    public Node(String newData) {
        sha256 = new SHA256();
        this.data = newData;
        this.hashValue = sha256.hash(newData);
        left = null;
        right = null;
        parent = null;
    }

    public Node(String newData1, String newData2) {
        sha256 = new SHA256();
        this.data = newData1 + newData2;
        String hashValue1 = sha256.hash(newData1);
        String hashValue2 = sha256.hash(newData2);
        this.hashValue = sha256.hash(hashValue1 + hashValue2);
        left = null;
        right = null;
        parent = null;
    }

    public void preorder(Node node) {
        if (node != null) {
            System.out.println("Data: " + node.data + " HashValue: " + node.hashValue);
            preorder(node.left);
            preorder(node.right);
        }
    }

    public void setData(String newData) {
        this.data = newData;
        this.hashValue = sha256.hash(newData);
    }

    public void setHashValue(String newHashValue) {
        this.hashValue = newHashValue;
    }

    public void setLeft(Node node) {
        this.left = node;
    }

    public void setRight(Node node) {
        this.right = node;
    }

    public void setParent(Node node) {
        this.parent = node;
    }

    public String getData() {
        return this.data;
    }

    public String getHashValue() {
        return this.hashValue;
    }

    public Node getLeft() {
        return this.left;
    }

    public Node getRight() {
        return this.right;
    }

    public Node getParent() {
        return this.parent;
    }

}
