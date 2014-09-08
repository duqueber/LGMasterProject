/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package supportpackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author nati
 */
public class Tree<T> {
    
    private Node<T> root;
    private Stack  s = new Stack ();
    private List<Node<T>> leaves = new ArrayList();
    
    public Tree(){
        super();
    }
    
    public void setRoot (Node<T> r){
        this.root = r;
    }
    
    public void addLeaf (Node<T> l){
        this.leaves.add(l);
    }
        
    public List<Node<T>> getLeaves ()
    {
        if (leaves == null)
           return new ArrayList<Node<T>>(); 
        return this.leaves;
    }
    
    public void pathToRoot (Node<T> node, ArrayList<Node<T>> a){
        if (node == root){
            a.add(node);
            return;
        }    
        pathToRoot (node.father, a);
        a.add(node);
    }
    
    public void printTreeRelations ()
    {
        s.add (root);
            
        while (!s.empty()){
            
            Node<Coordinates> check = (Node<Coordinates>)s.pop();
            if (check.hasChildren())
            {
                System.out.println("dad:" + check.getData().x + ", " + check.getData().y);
                List<Node<Coordinates>> children = check.getChildren();
                System.out.println("Children: ");
                for (Node<Coordinates> child: children){
                    System.out.println(child.getData().x + ", " + child.getData().y);
                    s.add(child);
                }
            }
        }
    }//End PrintTreeRelations
    
    public class TreeNode extends Tree<Coordinates>{
        
        private List<Node<Coordinates>> leavesCoor = new ArrayList();
    
        
        @Override
        public void addLeaf (Node<Coordinates> n){
        this.leavesCoor.add(n);
        }
        
        @Override
        public List<Node<Coordinates>> getLeaves ()
        {
            if (leaves == null)
                return new ArrayList<Node<Coordinates>>();
            for (Node<Coordinates> leaveCoor: leavesCoor)
                System.out.println ("leaf: " + leaveCoor.getData().x + ", " + leaveCoor.getData().y); 
            return this.leavesCoor;
        }
    }
    
}
