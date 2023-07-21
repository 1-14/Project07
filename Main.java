import hashChains.*;
import merkle.*;

public class Main {
    public static void main(String[] args) {
        HashChains hashChains = new HashChains();
        MerkleTree newNode = hashChains.setup();

        newNode.preorder();
    }
}