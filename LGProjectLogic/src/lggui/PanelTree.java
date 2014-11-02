/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lggui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
    static final int NODE_SIZE=40; 
    Stack<TreeNode> treeStack = new Stack<>();
    private boolean RadioPressed = false;
    private TreeNode currentTreeNode = null;
    static int repaintCallsCounter = 0;

    
    PanelTree (){
        super ();
        Dimension d = new Dimension (GUIFrame.PWIDTH- GUIFrame.BHEIGHT, 3*(GUIFrame.PHEIGHT/4));
        this.setPreferredSize(d);
        this.SIZE = this.getPreferredSize();
        this.SQUARE =  this.SIZE.width/14;
        repaintCallsCounter = 0;
    }
    
            
    void setTreeStartStack (Node<Moves> root){
        PanelTree.repaintCallsCounter = 0;
        this.treeStack = new Stack <> ();     
    //treeNode (x, y, Line2D line1,Color color, String data)    
        int startAt = this.SIZE.width/2 - this.SQUARE/2;
        addChildrenTreeStack(root, new TreeNode (startAt, -40,0,0, Color.BLACK, "77"));
    }
    
    
    void addChildrenTreeStack(Node<Moves> n, TreeNode tn){
        List <Node <Moves>>  children = n.getChildren();
        Color color;
        if (tn.color == null || tn.color.equals(Color.BLACK) )
            color= Color.WHITE;
        else
            color = Color.BLACK;
        
        String label0, label1;
        Coordinates c0, c1;
        
        int yCoor = tn.y + 2*(this.NODE_SIZE);
        if (children.isEmpty())
            return;
        
        if (children.size()> 2)
            throw new IllegalArgumentException ("Tree is binary");
        
        c0= children.get(0).getData().getStep();
        label0 = "" + c0.x + c0.y;
        if (children.get(0).getDepth() == 1 || children.get(0).getDepth()==2){

            if (children.size() == 1 && children.get(0).getDepth()==1){
                this.treeStack.add (new TreeNode (tn.x, yCoor, tn.x, tn.y, color, label0));  
                repaintCallsCounter--;
            }
            else
             if (children.size() == 1 && children.get(0).getDepth()==2){
                this.treeStack.add (new TreeNode (tn.x, yCoor, tn.x, tn.y, color, label0));  
                repaintCallsCounter++;
            }
            if (children.size () == 2){
                c1= children.get(1).getData().getStep();
                label1 = "" + c1.x + c1.y;
                if (repaintCallsCounter == 0){
                    this.treeStack.add (new TreeNode (tn.x+ 3* (this.NODE_SIZE/2), yCoor+40, tn.x, tn.y+40, color, label0)); 
                    this.treeStack.add (new TreeNode (tn.x- 3* (this.NODE_SIZE/2), yCoor+40, tn.x, tn.y+40, color, label1));  
                }    
                else{  
                    this.treeStack.add (new TreeNode (tn.x+ 3* (this.NODE_SIZE/2), yCoor, tn.x, tn.y, color, label0)); 
                    this.treeStack.add (new TreeNode (tn.x- 3* (this.NODE_SIZE/2), yCoor, tn.x, tn.y, color, label1));
                }  
                repaintCallsCounter++;
            }
        }
        else{        
            if (children.size() == 2){
                c1= children.get(1).getData().getStep();
                label1 = "" + c1.x + c1.y;
                if (tn.x > this.SIZE.width/2)
                    this.treeStack.add (new TreeNode (tn.x+ 3* (this.NODE_SIZE/2), yCoor, tn.x, tn.y, color, label0)); 
                else 
                    this.treeStack.add (new TreeNode (tn.x- 3* (this.NODE_SIZE/2), yCoor, tn.x, tn.y, color, label0)); 
                this.treeStack.add (new TreeNode (tn.x, yCoor, tn.x, tn.y, color, label1));
             }
            else
                this.treeStack.add (new TreeNode (tn.x, yCoor, tn.x, tn.y, color, label0));
        }

    }

    public void callRepaint (Node<Moves> m){
        this.currentTreeNode = this.treeStack.pop();
        repaint();     
        addChildrenTreeStack(m, this.currentTreeNode);
        
    }
    @Override
    public void paintComponent(Graphics gComp) {
        if (this.RadioPressed){
            super.paintComponent(gComp);
            setRadioPressed (false);
        }
        else{
            Graphics2D g = (Graphics2D) gComp;
            System.out.println ("repaint called");
            if (this.treeStack.isEmpty())
               System.out.println ("stack is empty");

            if (this.currentTreeNode != null){    

               repaintCallsCounter++;
                //g.fill3DRect(this.currentTreeNode.x, this.currentTreeNode.y, this.NODE_SIZE, this.NODE_SIZE, true);
                g.setColor (this.currentTreeNode.color);
                g.fillOval(this.currentTreeNode.x, this.currentTreeNode.y, this.NODE_SIZE, this.NODE_SIZE);


                g.setColor (Color.BLACK);

                if (repaintCallsCounter> 1){
                    Line2D line = this.currentTreeNode.line1;
                    g.setStroke(new BasicStroke(2));
                    g.draw (line);
                } 

                if (this.currentTreeNode.color.equals (Color.WHITE))
                    g.setColor (Color.BLACK);
                else
                    g.setColor (Color.WHITE);
                int fontSize = 18;
                g.setFont (new Font ("TimesRoman", Font.BOLD, fontSize));
                int xString = this.currentTreeNode.x + this.NODE_SIZE/2- fontSize/2-2; 
                int yString = this.currentTreeNode.y+this.NODE_SIZE/2+ fontSize/2;
                g.drawString(this.currentTreeNode.data, xString , yString );

            }    
        }    
    }      
    
    public void setRadioPressed (boolean b){
        this.RadioPressed = b;
    }    
    class TreeNode {
        
        int x, y;
        Line2D line1;
        Color color;
        String data;
        
        TreeNode (int x, int y, int xdad, int ydad, Color color, String data){
            this.x = x;
            this.y = y;
            double halfNode = PanelTree.NODE_SIZE/2;
            this.line1 = new Line2D.Double (xdad+ halfNode, ydad+ PanelTree.NODE_SIZE, x+halfNode, y);
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
