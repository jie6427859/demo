package com.test;

import java.util.Arrays;

/**
 * * @title: MyMap
 * * @author 159930
 * * @date 2020/10/9 14:41
 */
public class MyMap<A,B> {

    private MyNode[] myNodes;
    private int length;
    public MyMap(){
        myNodes = new MyNode[8];
    }
    public MyMap(int i){
        myNodes = new MyNode[i];
        this.length = i;
    }
    public void put(A key, B value){
        // 计算hash值
        int hash = key.hashCode();
        boolean isAdd = true;
        for (int i = 0; i < myNodes.length; i++) {
            if (myNodes[i] != null){
                // 通过hash 找到数组
                if(myNodes[i].getHash() == hash){
                    isAdd = false;
                    MyNode nextNode = myNodes[i].getNext();
                    MyNode currMyNode = myNodes[i];
                    while (true){
                        if (currMyNode != null){
                            if (key.equals(currMyNode.getKey())){
                                myNodes[i] = new MyNode();
                                myNodes[i].setHash(hash);
                                myNodes[i].setValue(value);
                                myNodes[i].setKey(key);
                            }
                            return;
                        }

                        if (nextNode == null){
                            MyNode temp = new MyNode();
                            temp.setHash(hash);
                            temp.setValue(value);
                            temp.setKey(key);
                            currMyNode.setNext(temp);
                            return;
                        }
                        nextNode = currMyNode.getNext();
                        if (nextNode != null){
                            currMyNode = nextNode;
                        }
                    }
                }
            }
            // 头结点为空
            if (myNodes[i] == null){
                isAdd = false;
                myNodes[i] = new MyNode();
                myNodes[i].setHash(hash);
                myNodes[i].setValue(value);
                myNodes[i].setKey(key);
                break;
            }
        }
        // 是否需要扩容
        if (isAdd){
            myNodes = Arrays.copyOf(myNodes,myNodes.length *2);
            for (int i = length; i < myNodes.length; i++){
                if (myNodes[i] == null){
                    myNodes[i] = new MyNode();
                    myNodes[i].setHash(hash);
                    myNodes[i].setValue(value);
                    myNodes[i].setKey(key);
                    break;
                }
            }
            length = myNodes.length;
        }
    }

    public B get(A key){
        // 计算hash值
        int hash = key.hashCode();
        for (int i = 0; i < myNodes.length; i++){
            MyNode myNode = myNodes[i];
            if (myNode != null){
                if (myNode.getHash() == hash){
                    MyNode nextNode = myNode.getNext();
                    if (myNode.getKey().equals(key)){
                        return (B) myNode.getValue();
                    }
                    // 遍历链表
                    while (true){
                        if (nextNode == null){
                            return null;
                        }
                        if (nextNode.getKey().equals(key)){
                            return (B) nextNode.getValue();
                        }
                        nextNode = nextNode.getNext();
                    }
                }
            }
        }
        return null;
    }

    public void remove(A key){
        // 计算hash值
        int hash = key.hashCode();
        for (int i = 0; i < myNodes.length; i++){
            MyNode myNode = myNodes[i];
            if (myNode.getHash() == hash){
                MyNode nextNode = myNode.getNext();
                // 首节点
                if (myNode.getKey().equals(key)){
                    myNodes[i] = nextNode;
                    return;
                }
                // 遍历链表
                while (true){
                    MyNode preNode = null;
                    if (nextNode == null){
                        return ;
                    }
                    if (nextNode.getKey().equals(key)){
                        preNode.setNext(nextNode.getNext());
                        return;
                    }
                    preNode = nextNode;
                    nextNode = nextNode.getNext();
                }
            }
        }
    }

    public void clear(){
        myNodes = null;
    }

    public int size(){
        return myNodes.length;
    }

    @Override
    public String toString() {
        return "MyMap{" +
                "myNodes=" + Arrays.toString(myNodes) +
                ", length=" + length +
                '}';
    }

    public MyNode[] getMyNodes() {
        return myNodes;
    }

    public void setMyNodes(MyNode[] myNodes) {
        this.myNodes = myNodes;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
