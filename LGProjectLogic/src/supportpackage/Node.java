/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package supportpackage;

import java.util.ArrayList;
import java.util.List;
import lggrammars.Zones;

/**
 *
 * @author nati
 */
public class Node<T> {
    
    T data;
    List<Node<T>> children;
    Node<T> father;
    boolean isRoot;
    
    public Node(){
        super();
    }
    
    public Node (T data){
        this.data = data;
        this.isRoot = false;
    }
    
    public T getData (){    
        return this.data;
    }
    
    public List<Node<T>> getChildren() {
        if (children == null)
            return new ArrayList<Node<T>>();
        
        return this.children;
    }
    
    public void setChildren(List<Node<T>> children) {
        this.children = children;
        for (Node<T>child: children)
            child.setFather(this);
    }
    
    public void deleteChild (Node<T> child){
        for (Node<T> oldChild : this.children){
            if (oldChild.data.equals(child.data))
                this.children.remove(oldChild);
        }
    }
    
    public void setFather (Node<T> dad){
        this.father = dad;
    }
    
    public boolean hasChildren (){
        if (this.children== null)
            return false;
        return true;
    }
    public boolean isRoot (){
        return this.isRoot;
    }

    public void addFirstChild(Node<T> child) {
        List<Node<T>> c = new ArrayList<>();
        c.add(child);
        this.setChildren(c);
        child.setFather(this);
    }
    
    public void delete (){
        deleteNodeasChild();
        this.children = null;
        this.data = null;
        this.isRoot = false;
    }
    
    private void deleteNodeasChild (){
        List<Node<T>> temp = new ArrayList <>();
        for (Node<T>child:  this.father.children)
            if (!child.equals(this))
                temp.add(child);                    
        this.father.children = temp;
    }
    
    public int getDepth (){
        if (this.father != null)
            return getDepth(this);
        else
            return 0;
    }
    
    private int getDepth (Node<T> n){
        if (n.father == null)
            return 0;
        return (getDepth (n.father)+ 1);
    }
    public Node<T> getFather (){
        return this.father;
    }
}
