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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
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
    private Map<Coordinates, String> cutReasons = new HashMap<>(); 
    public static CutReason currentReason;
    private boolean changingBranch;
    private int startAt = 200;
    
    PanelTree (){
        super ();
        Dimension d = new Dimension (GUIFrame.PWIDTH- GUIFrame.BHEIGHT, (GUIFrame.PHEIGHT-120));
        this.setPreferredSize(d);
        this.SIZE = this.getPreferredSize();
        this.SQUARE =  22;
        setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10,
                BoardScene.backgroundColor));
        this.changingBranch = false;
        repaintCallsCounter = 0;
    }
    
            
    void setTreeStartStack (Node<Moves> root, Map<Coordinates, String> cutReasons){
        PanelTree.repaintCallsCounter = 0;
        this.treeStack = new Stack <> ();     
    //treeNode (x, y, Line2D line1,Color color, String data)    
        
                //this.getPreferredSize().width/2 - this.SQUARE/2;
        addChildrenTreeStack(root, new TreeNode (startAt, -40,startAt,0, Color.BLACK, "77"));
        this.cutReasons = cutReasons;
    }
    
    
    void addChildrenTreeStack(Node<Moves> n, TreeNode tn){
        List <Node <Moves>>  children = n.getChildren();
        Color color;
        if (!children.isEmpty() && (children.get(0).getData().getStep().equals(new Coordinates (1,5))||
                children.get(0).getData().getStep().equals(new Coordinates (7,3))))
            color= Color.BLACK;
        else 
        
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
                if (color.equals (Color.BLACK)){
                    this.treeStack.add (new TreeNode (tn.x, yCoor+40, tn.x, tn.y+40, color, label0));  
                    repaintCallsCounter++;
                }    
                else{
                    this.treeStack.add (new TreeNode (tn.x, yCoor, tn.x, tn.y, color, label0));  
                    repaintCallsCounter--;
                }
            }
            else
             if (children.size() == 1 && children.get(0).getDepth()==2){
                this.treeStack.add (new TreeNode (tn.x, yCoor, tn.x, tn.y, color, label0));  
                repaintCallsCounter++;
            }
            if (children.size () == 2){
                c1= children.get(1).getData().getStep();//
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
                if (tn.x > startAt)
                    this.treeStack.add (new TreeNode (tn.x+ 3* (this.NODE_SIZE/2), yCoor, tn.x, tn.y, color, label0)); 
                else 
                    this.treeStack.add (new TreeNode (tn.x- 3* (this.NODE_SIZE/2), yCoor, tn.x, tn.y, color, label0)); 
                this.treeStack.add (new TreeNode (tn.x, yCoor, tn.x, tn.y, color, label1));
             }
            else
                this.treeStack.add (new TreeNode (tn.x, yCoor, tn.x, tn.y, color, label0));
        }

    }

    public void changeBranch (){
        this.changingBranch = true;
        this.currentTreeNode =  this.treeStack.pop();
        repaint();
        this.treeStack.add (this.currentTreeNode);
        
    }
    public void callRepaint (Node<Moves> m){
        this.currentTreeNode = this.treeStack.pop();
        repaint();     
        addChildrenTreeStack(m, this.currentTreeNode);
        
    }
    
    @Override
    public void paintComponent(Graphics gComp) {
      
        Graphics2D g = (Graphics2D) gComp;
        if (this.RadioPressed){
                super.paintComponent(gComp);
                setRadioPressed (false);
        }
        else{
            if (!this.changingBranch){  

                System.out.println ("repaint called");
                if (this.treeStack.isEmpty())
                   System.out.println ("stack is empty");

                if (this.currentTreeNode != null){    

                   repaintCallsCounter++;

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

                    Coordinates c = Coordinates.parseString(this.currentTreeNode.data);

                    g.drawString(Coordinates.convertToChess(this.currentTreeNode.data), xString , yString );

                    int nodeMidX = this.getLocationOnScreen().x +this.currentTreeNode.x + this.NODE_SIZE/2;
                    int nodeY = this.getLocationOnScreen().y +this.currentTreeNode.y +this.NODE_SIZE;

                    if (!this.cutReasons.isEmpty()){
                        for (Map.Entry<Coordinates, String> e : this.cutReasons.entrySet()){
                            if (e.getKey ().equals(c)){
                                currentReason = new CutReason (e.getValue(), new Point (nodeMidX, nodeY));
                                g.setStroke(new BasicStroke(7));
                                g.setColor (BoardScene.backgroundColor);
                                g.drawOval(this.currentTreeNode.x-3, this.currentTreeNode.y-3, 
                                        this.NODE_SIZE+6, this.NODE_SIZE+6);
                            }    
                        }  
                    }    
                }    
            }   
            else {
                System.out.println (this.changingBranch);
                g.setColor (new Color(1.0f, 0.271f, 0.0f));
                g.setStroke(new BasicStroke(6));
                g.drawOval(this.currentTreeNode.xdad-3, this.currentTreeNode.ydad-3, 
                this.NODE_SIZE+6, this.NODE_SIZE+6); 
                this.changingBranch = false;                        
            }
        }    
              
    }      
    
    public void setRadioPressed (boolean b){
        this.RadioPressed = b;
    }    
    class TreeNode {
        
        int x, y, xdad, ydad;
        Line2D line1;
        Color color;
        String data;
        
        TreeNode (int x, int y, int xdad, int ydad, Color color, String data){
            this.xdad = xdad;
            this.ydad = ydad;
            this.x = x;
            this.y = y;
            double halfNode = PanelTree.NODE_SIZE/2;
            this.line1 = new Line2D.Double (xdad+ halfNode, ydad+ PanelTree.NODE_SIZE, x+halfNode, y);
            this.color = color;
            this.data = data;

        }
        
    }    
    
}
