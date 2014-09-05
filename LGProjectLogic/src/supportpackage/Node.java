/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package supportpackage;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nati
 */
public class Node<T> {
    
    public T data;
    public List<Node<T>> children;
    public Node<T> father;
        
    public Node(){
        super();
    }
    
    public Node (T data){
        this.data = data;
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
    }
    
    public void setFather (Node<T> dad){
        this.father = dad;
    }
    
    public boolean hasChildren (){
        if (this.children== null)
            return false;
        return true;
    }
    

}
