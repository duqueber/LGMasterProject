/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package supportpackage;

/**
 *
 * @author nati
 */
public class Tree<T> {
    
    private Node<T> root;
    
    public Tree(){
        super();
    }
    
    public void setRoot (Node<T> r){
        this.root = r;
    }
    
}
