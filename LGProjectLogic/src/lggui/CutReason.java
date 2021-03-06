/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lggui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import supportpackage.Coordinates;

/**
 *
 * @author nati
 */
public class CutReason extends JPanel{
    
    private final int HEIGHTREC = 30;
    private int WIDTHREC;
    private String reason;
    private JFrame chart;
    private Font font;
    
    public CutReason (String reason, Point point){
        
        super ();

        this.reason = reason;
        this.chart = new JFrame ();        
        Container c = chart.getContentPane();
        c.setLayout( new BorderLayout() );
        this.chart.add(this, BorderLayout.CENTER);
        
        int fontSize = 14;
        this.font = new Font ("TimesRoman", Font.BOLD, fontSize);
        
        this.WIDTHREC = (this.reason.toCharArray().length+5)*9; 

        setPreferredSize (new Dimension (this.WIDTHREC, this.HEIGHTREC));
        
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4,
                BoardScene.backgroundColor));
        this.chart.setUndecorated(true);
        this.chart.pack();
        this.chart.setLocation(new Point (point.x-WIDTHREC/2, point.y ) ); 
        this.chart.setResizable(false); 
        this.chart.setVisible(true);
        
    }    
    
    public static void closeCurrentReason (){
        JFrame j= new JFrame ();
        if (PanelTree.currentReason.isDisplayable()){
            j =(JFrame) SwingUtilities.getWindowAncestor(PanelTree.currentReason);
            j.setVisible(false);
        }            
    }
    
    @Override
    public void paintComponent(Graphics gComp) {
        
        super.paintComponent(gComp);
        Graphics2D g = (Graphics2D) gComp;
        
        g.setColor (Color.BLACK);
        
        g.setFont (this.font);
        FontMetrics fm = g.getFontMetrics();
        int startX = (this.WIDTHREC - fm.stringWidth(this.reason))/2 ;
        int startY = (this.HEIGHTREC )/2+5;
        g.drawString(this.reason,startX , startY );
        
    }    
    
   /*           if (this.RadioPressed){
            super.paintComponent(gComp);
            setRadioPressed (false);
        }
        else{
            
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
                g.drawString(this.currentTreeNode.data, xString , yString );

            }    
        }    
*/
    public static void main(String[] args) { 
        //new CutReason ("Enemy destroyed"); 
    }
}
