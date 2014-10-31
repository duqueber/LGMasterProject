/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lggui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.JPanel;
import supportpackage.Coordinates;
import supportpackage.Moves;
import supportpackage.Node;
import supportpackage.Tree;

/**
 *
 * @author nati
 */
public class PanelTree extends JPanel {
    
    private Graphics2D g2d;
    private final int SQUARE;
    private final Dimension SIZE;
    private final int NODE_SIZE; 
    Stack<TreeNode> treeStack = new Stack<>();
    private boolean NextPressed = false;
    private TreeNode currentTreeNode = null;

    
    PanelTree (){
        super ();
        Dimension d = new Dimension (GUIFrame.PWIDTH- GUIFrame.BHEIGHT, 3*(GUIFrame.PHEIGHT/4));
        this.setPreferredSize(d);
        this.SIZE = this.getPreferredSize();
        this.SQUARE =  this.SIZE.width/14;
        this.NODE_SIZE = 30;
    }
    
            
    void setTreeStartStack (Node<Moves> root){
        this.treeStack = new Stack <> ();     
    //treeNode (x, y, Line2D line1,Color color, String data)    
        int startAt = this.SIZE.width/2 - this.SQUARE/2;
        addChildrenTreeStack(root, new TreeNode (startAt, 0,null,Color.BLACK, "77"));
    }
    
    
    void addChildrenTreeStack(Node<Moves> n, TreeNode tn){
        List <Node <Moves>>  children = n.getChildren();
        Color color;
        if (tn.color == null || tn.color.equals(Color.BLACK) )
            color= Color.WHITE;
        else
            color = Color.BLACK;
        
        String label1, label2;
        Coordinates c1, c2;
        
        int yCoor = tn.y + 2*(this.NODE_SIZE);
        if (children.isEmpty())
            return;
        
        if (children.size()> 2)
            throw new IllegalArgumentException ("Tree is binary");
        
        c1= children.get(0).getData().getStep();
        label1 = "" + c1.x + c1.y;
        if (children.get(0).getDepth() == 1 || children.get(0).getDepth()==2){

            if (children.size() == 1)
                this.treeStack.add (new TreeNode (tn.x, yCoor, null, color, label1));          
             
            if (children.size () == 2){
                c2= children.get(1).getData().getStep();
                label2 = "" + c2.x + c2.y;
                this.treeStack.add (new TreeNode (tn.x+ 3* (this.NODE_SIZE/2), yCoor, null, color, label2)); 
                this.treeStack.add (new TreeNode (tn.x- 3* (this.NODE_SIZE/2), yCoor, null, color, label1));  
            }
        }
        else{        
            if (children.size() == 2){
                c2= children.get(1).getData().getStep();
                label2 = "" + c2.x + c2.y;
                if (tn.x > this.SIZE.width/2)
                    this.treeStack.add (new TreeNode (tn.x+ 3* (this.NODE_SIZE/2), yCoor, null, color, label2)); 
                else 
                    this.treeStack.add (new TreeNode (tn.x- 3* (this.NODE_SIZE/2), yCoor, null, color, label2)); 
             }
            this.treeStack.add (new TreeNode (tn.x, yCoor, null, color, label1));
        }

    }

    public void callRepaint (Node<Moves> m){
        this.currentTreeNode = this.treeStack.pop();
        repaint();     
        addChildrenTreeStack(m, this.currentTreeNode);
        
    }
    @Override
        public void paintComponent(Graphics g) {

           // super.paintComponent(g);
           System.out.println ("repaint called");
            //int startAt = this.SIZE.width/2 - this.SQUARE/2;
           if (this.treeStack.isEmpty())
               System.out.println ("stack is empty");
            if (this.NextPressed && !this.treeStack.isEmpty ()){
               System.out.println (this.currentTreeNode.data);
                if (this.currentTreeNode.data.equals (String.valueOf(45)))
                    g.setColor (Color.BLUE);
                else
                g.setColor (this.currentTreeNode.color);
                
                g.fill3DRect(this.currentTreeNode.x, this.currentTreeNode.y, this.NODE_SIZE, this.NODE_SIZE, true);
                
                
                /*   g.setColor (this.);             
                g.fill3DRect(startAt, this.SQUARE, this.NODE_SIZE, this.NODE_SIZE , true);
                
                g.fill3DRect(startAt, 3*this.SQUARE, this.NODE_SIZE, this.NODE_SIZE, true);
                
                g.fill3DRect(startAt- 3* (this.SQUARE/2), 3*this.SQUARE, this.NODE_SIZE, this.NODE_SIZE, true);
                g.fill3DRect(startAt+ 3*(this.SQUARE/2), 3*this.SQUARE, this.NODE_SIZE, this.NODE_SIZE, true);
                */this.NextPressed =false;
            }    
            

    }      
    
    public void setNextPressed (boolean b){
        this.NextPressed = b;
    }    
    class TreeNode {
        
        int x, y;
        Line2D line1;
        Color color;
        String data;
        TreeNode (int x, int y, Line2D line1, Color color, String data){
            this.x = x;
            this.y = y;
            this.line1 = line1;
            this.color = color;
            this.data = data;
        }
        
    }    
    /*public void doDrawing() {

        g2d.setPaint (new Paint ())
        g2d.fillOval (this.SIZE.width/2, this.SQUARE, 1, 1 );
        loadImages(); 
        image = new BufferedImage(size+40,size+40, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D)image.getGraphics();
        darktp = new TexturePaint(dark, new Rectangle(0, 0, squaresize, squaresize));
        lighttp = new TexturePaint(light, new Rectangle(0, 0, squaresize, squaresize));

        for (int y = 0; y< squaresheight; y++)        
        {
            for (int x = y%2; x< squareswidth ; x+=2) {
                if (y%2==0){
                    g2d.setPaint(darktp);
                    g2d.fillRect(squaresize*x+borderGap, 
                            squaresize*y+borderGap, squaresize, squaresize);  
                }
                else{
                    g2d.setPaint(darktp);
                    g2d.fillRect((squaresize*x+borderGap),
                            squaresize*y+borderGap, squaresize, squaresize);                    
                } 
            }    
        }  

        for (int y = 0; y< squaresheight; y++)        
        {
            for (int x = (y+1)%2; x< squareswidth ; x+=2) {
                if (y%2==1){
                    g2d.setPaint(lighttp);
                    g2d.fillRect(squaresize*x+borderGap, 
                            squaresize*y+borderGap, squaresize, squaresize);  
                }          
                else{
                    g2d.setPaint(lighttp);
                    g2d.fillRect((squaresize*x)+borderGap,
                             squaresize*y+borderGap, squaresize, squaresize);                     
                } 
            }    
        }  

    }//End of doDrawing()*/
    
}
