package com.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * * @title: ListToTree
 * * @author 159930
 * * @date 2020/10/9 19:56
 */
public class ListToTree {
    private String id;
    private String parentId;
    private String value;

    private List<ListToTree> childTree = new ArrayList<ListToTree>();

    public static void main(String[] args) {
        List<ListToTree> list = new ArrayList<ListToTree>();
        ListToTree listToTree1 = new ListToTree();
        listToTree1.setId("d1");
        listToTree1.setParentId("");
        listToTree1.setValue("a");
        list.add(listToTree1);
        ListToTree listToTree2 = new ListToTree();
        listToTree2.setId("d2");
        listToTree2.setParentId("d1");
        listToTree2.setValue("aa");
        list.add(listToTree2);
        ListToTree listToTree3 = new ListToTree();
        listToTree3.setId("d3");
        listToTree3.setParentId("d1");
        listToTree3.setValue("ab");
        list.add(listToTree3);
        ListToTree listToTree4 = new ListToTree();
        listToTree4.setId("d4");
        listToTree4.setParentId("d2");
        listToTree4.setValue("aaa");
        list.add(listToTree4);
        ListToTree listToTree5 = new ListToTree();
        listToTree5.setId("d5");
        listToTree5.setParentId("d3");
        listToTree5.setValue("aba");
        list.add(listToTree5);

        ListToTree listToTree6 = new ListToTree();
        listToTree6.setId("d6");
        listToTree6.setParentId("d5");
        listToTree6.setValue("abaa");
        list.add(listToTree6);

        System.out.println(changeTree(list));
    }

    private static ListToTree changeTree(List<ListToTree> list) {
        ListToTree p_Tree = new ListToTree();
        // 父节点
        for (ListToTree listToTree : list) {
            if ("".equals(listToTree.getParentId())){
                p_Tree = listToTree;
            }
        }
        // 二级节点
        for (ListToTree listToTree : list) {
            if (p_Tree.getId().equals(listToTree.getParentId())){
                p_Tree.getChildTree().add(listToTree);
            }
        }
        getListToTree(p_Tree.getChildTree(),list);
        return p_Tree;
    }

    private static void getListToTree(List<ListToTree> c_List,List<ListToTree> list){
        if (c_List == null || c_List.size() < 1){
            return;
        }
        for (ListToTree toTree : list) {
            for (ListToTree l : c_List){
                if (l.getId().equals(toTree.getParentId())){
                    l.getChildTree().add(toTree);
                    getListToTree(l.getChildTree(),list);
                }
            }
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }



    public List<ListToTree> getChildTree() {
        return childTree;
    }

    public void setChildTree(List<ListToTree> childTree) {
        this.childTree = childTree;
    }

    @Override
    public String toString() {
        return "ListToTree{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", value='" + value + '\'' +
                ", childTree=" + childTree +
                '}';
    }
}
