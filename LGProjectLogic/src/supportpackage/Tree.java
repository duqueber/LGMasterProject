/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package supportpackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import lggrammars.Zones;

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
    
    public Node<T> getRoot (){
        return this.root;
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
            
            Node<Zones.Trajectory> check = (Node<Zones.Trajectory>)s.pop();
            if (check.hasChildren())
            {
                System.out.println("dad:" );
                check.getData().printTrajectory();
                List<Node<Zones.Trajectory>> children = check.getChildren();
                System.out.println("Children: ");
                for (Node<Zones.Trajectory> child: children){
                    child.getData().printTrajectory();
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
    
    public static void main(String args[]) {
    
        Tree<Integer> tree = new Tree <>();
        Node<Integer> r = new Node<> (5);
        tree.setRoot(r);
        

        Node<Integer> rc = new Node <> (4);
        Node<Integer> rc1 = new Node <> (3);
        ArrayList <Node<Integer>> list0 = new ArrayList();
        list0.add(rc);
        list0.add(rc1);
        r.setChildren(list0);
        
        Node<Integer> rcc = new Node <> (10);
        rc.addFirstChild(rcc);
        
        Node<Integer> rccc1 = new Node <> (20);
        Node<Integer> rccc2 = new Node <> (30);
        ArrayList <Node<Integer>> list1 = new ArrayList();
        list1.add(rccc1);
        list1.add (rccc2);
        rcc.setChildren(list1);
        
         Node<Integer> a1 = new Node <> (2);
        Node<Integer> a2 = new Node <> (1);
         Node<Integer> a3 = new Node <> (10);

         ArrayList <Node<Integer>> list2 = new ArrayList();
         list2.add(a1);
         list2.add(a2);
         list2.add(a3);
        rc1.setChildren(list2);
        
        if (r.hasChildren())
        System.out.println ("hello");
        
        tree.printTreeRelations();
    }
    
}
