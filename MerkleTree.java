package merkle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MerkleTree {
    private Node rootNode;
    private int leafNodeNum;
    private SHA256 sha256;

    public MerkleTree(List<String> dataList) {
        sha256 = new SHA256();
        leafNodeNum = dataList.size();
        rootNode = createMerkleTree(dataList);
    }

    public void insert(String newData) {
        Node newNode = new Node(newData);
        insertNode(newNode);
    }

    public void preorder() {
        rootNode.preorder(rootNode);
    }

    public int getLeafNodeNum() {
        return this.leafNodeNum;
    }

    private Node createMerkleTree(List<String> dataList) {
        final int range = dataList.size();
        if (range == 1) {
            Node root = new Node(dataList.get(0));
            return root;
        } else {
            List<Node> nodeList = new ArrayList<Node>();
            for (String data : dataList) {
                Node newNode = new Node(data);
                nodeList.add(newNode);
            }
            return createMerkleNode(nodeList);
        }
    }

    private Node createMerkleNode(List<Node> nodeList) {
        final int range = nodeList.size();
        if (range == 1) {
            return nodeList.get(0);
        }
        List<Node> newRootList = new ArrayList<Node>();
        for (int i = 0; i < range; i += 2) {
            Node left = nodeList.get(i);
            Node right = i + 1 < range ? nodeList.get(i + 1) : new Node();
            Node root = new Node(left.getData(), right.getData());

            left.setParent(root);
            right.setParent(root);

            root.setLeft(left);
            root.setRight(right);
            newRootList.add(root);
        }
        return createMerkleNode(newRootList);
    }

    public Node findLeaf() {
        if (rootNode == null || rootNode.getLeft() == null || rootNode.getRight() == null) {
            return null;
        }

        Queue<Node> queue = new LinkedList<Node>();
        queue.offer(rootNode);
        Node leaf = null;

        while (!queue.isEmpty()) {
            int size = queue.size();
            boolean flag = false;

            for (int i = 0; i < size; i++) {
                Node tmp = queue.poll();
                if (tmp.getLeft() == null && tmp.getRight() == null) {
                    leaf = tmp;
                    flag = true;
                    break;
                }
                if (tmp.getLeft() != null)
                    queue.offer(tmp.getLeft());
                if (tmp.getRight() != null)
                    queue.offer(tmp.getRight());
            }
            if (flag) break;
        }
        return leaf;
    }

    private void insertNode(Node newNode) {

        Node leaf = findLeaf();

        Node tmp = new Node(leaf.getData());
        leaf.setLeft(tmp);
        leaf.setRight(newNode);

        newNode.setParent(leaf);

        Node root = leaf;
        while (root != null) {
            root.setData(root.getLeft().getData() + root.getRight().getData());
            String leftHash = root.getLeft().getHashValue();
            String rightHash = root.getRight().getHashValue();
            root.setHashValue(sha256.hash(leftHash + rightHash));
            root = root.getParent();
        }

    }
}

