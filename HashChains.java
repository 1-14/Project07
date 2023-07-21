package hashChains;

import merkle.MerkleTree;

import java.security.*;
import java.util.ArrayList;
import java.util.List;

public class HashChains {
    public HashChains() {
        salt_A = genRandom();
        salt_B = genRandom();
        salt_C = genRandom();

        seed_main = genRandom();
        seed_D = genRandom();
        shuffle_seed = genRandom();

        s_3 = genRandom();
        s_2 = genRandom();
        s_1 = genRandom();

        h_3 = new ArrayList<>();
        h_2 = new ArrayList<>();
        h_1 = new ArrayList<>();

        merkleNodes = new ArrayList<>();
    }
    public MerkleTree setup(){
        calHash();
        calChecksum();
        shuffle();
        return createMerkle();
    }

    public void calHash() {
        SHA256 sha256 = new SHA256();
        String t3, t2, t1;
        t3 = s_3;
        t2 = s_2;
        t1 = s_1;

        h_3.add(t3);
        h_2.add(t2);
        h_1.add(t1);

        for (int i = 0; i < 3; i++) {
            t3 = sha256.hash(t3);
            t2 = sha256.hash(t3);
            t1 = sha256.hash(t3);

            h_3.add(t3);
            h_2.add(t2);
            h_1.add(t1);
        }

        a = h_3.get(3) + h_2.get(1) + h_1.get(2);
        b = h_3.get(3) + h_2.get(0) + h_1.get(3);
        c = h_3.get(2) + h_2.get(3) + h_1.get(3);

        A = sha256.hash(salt_A + a);
        B = sha256.hash(salt_B + b);
        C = sha256.hash(salt_C + c);
    }

    public void calChecksum() {
        SHA256 sha256 = new SHA256();
        checksum = seed_D;
        for (int i = 0; i < 9; i++)
            checksum = sha256.hash(checksum);
    }

    public void shuffle() {
        merkleNodes.add(B);
        merkleNodes.add(A);
        merkleNodes.add(C);
        merkleNodes.add(checksum);
    }

    public MerkleTree createMerkle() {
        return new MerkleTree(merkleNodes);
    }

    private String genRandom() {
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[16];

        random.nextBytes(randomBytes);

        StringBuilder sb = new StringBuilder();
        for (byte b : randomBytes)
            sb.append(String.format("%02x", b));

        return sb.toString();
    }

    private String salt_A, salt_B, salt_C;
    private String seed_main, seed_D, shuffle_seed;
    private String s_3, s_2, s_1;
    private List<String> h_3, h_2, h_1;
    private String a, b, c;
    private String A, B, C;
    private String checksum;
    private List<String> merkleNodes;
}
