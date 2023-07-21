# Project7

## 简介

Generalizing Hash Chains是一种将哈希链（Hash Chains）扩展和泛化的技术。哈希链是一种简单而有效的密码学技术，用于保证数据完整性和防止篡改。它通过在数据上多次应用哈希函数来创建一个链接的哈希值序列。
每个哈希值都依赖于前一个哈希值，形成一个连续的链条。当数据需要验证时，可以通过重新计算哈希链的最终哈希值，并与预期的哈希值进行比较，从而确定数据是否被篡改。

以下是一些Generalizing Hash Chains可能的变化和扩展：  

多级哈希链：引入多级哈希链，其中每个级别的哈希链都依赖于上一级的哈希链。这样可以创建更复杂的数据结构，以满足更严格的安全需求。  

分层哈希链：将数据划分为不同的层次，每个层次都有一个相应的哈希链。这样可以提供更细粒度的数据验证和访问控制。  

跨链验证：将多个哈希链链接在一起，形成更大的数据结构。这样可以提供更强大的数据完整性和安全性。  

公共/私有哈希链：引入公共和私有哈希链，其中公共哈希链可由所有人验证，而私有哈希链只能由特定授权的实体验证。  

哈希链配置：将哈希链的参数进行配置，如哈希函数的选择、哈希次数、初始种子等。这样可以根据具体需求进行优化和调整。  

时间戳和溯源：在哈希链中引入时间戳，记录数据的变化和更新历史，从而可以追溯数据的变更和来源。  

## 方案结构

![image](https://github.com/1-14/Project7/blob/main/1.png)

## 实现思路

按照上图中的结构，我们需要分别实现哈希链和Merkle tree，首先设计哈希链结构，之后定义哈希函数（SHA256），然后构建多级哈希链和分层哈希链；哈希链部分完成后构建Merkle Tree，
为每个Merkle Tree节点附加Generalizing Hash Chains的特性。可以在每个节点上构建Generalizing Hash Chains，形成多级哈希链。

## 关键代码展示

### HashChains初始化

```
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
```

### 创建Merkle tree

具体创建代码见MerkleTree.java

```
public MerkleTree setup(){
        calHash();
        calChecksum();
        shuffle();
        return createMerkle();
    }
```

### 计算哈希链

```
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
```

### 计算校验和

```
public void calChecksum() {
        SHA256 sha256 = new SHA256();
        checksum = seed_D;
        for (int i = 0; i < 9; i++)
            checksum = sha256.hash(checksum);
    }
```

### shuffle重新排列节点

```
public void shuffle() {
        merkleNodes.add(B);
        merkleNodes.add(A);
        merkleNodes.add(C);
        merkleNodes.add(checksum);
    }
```

### Main函数生成Generalizing Hash Chains

```
public class Main {
    public static void main(String[] args) {
        HashChains hashChains = new HashChains();
        MerkleTree newNode = hashChains.setup();

        newNode.preorder();
    }
}
```












