/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lggui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import lggrammars.Zones;
import lglogicpackage.Board2D;
import lglogicpackage.PiecesLogic;
import supportpackage.ZoneTypes;

/**
 *
 * @author nati
 */
public class SpaceChart extends JPanel{
    
    private final int SIZE = 500;
    ZoneTypes zt = null;
    private final PanelButtons pb; 
    
    SpaceChart(String blackType, String whiteType, PanelButtons pb){
        super ();
        JFrame chart = new JFrame ();        
        Container c = chart.getContentPane();
        c.setLayout( new BorderLayout() );
        chart.add(this, BorderLayout.CENTER);
        setPreferredSize (new Dimension (SIZE,SIZE));
        
        GUIFrame.showOnScreen(1, chart, true);
        
        chart.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
        chart.pack();
        chart.setResizable(false); 
        chart.setVisible(true);
        this.pb = pb;
        this.zt = new ZoneTypes (blackType, whiteType);
        
    }
    

    @Override
    public void paintComponent(Graphics gComp) {
        super.paintComponent(gComp);
        Graphics2D g = (Graphics2D) gComp;
        
        g.setColor (Color.BLACK);
        g.fillRect(0, 0, SIZE/2, SIZE/2);
        
        g.setColor (Color.WHITE);
        g.fillRect(SIZE/2, SIZE/2, SIZE/2, SIZE/2);
        
        g.setColor (Color.GRAY);
        g.fillRect(SIZE/2, 0, SIZE/2, SIZE/2);
        
        g.setColor (Color.GRAY);
        g.fillRect(0, SIZE/2, SIZE/2, SIZE/2);
        
        Color purple = new Color (0.148f,0f,0.211f);
        g.setColor (purple);
        g.setStroke(new BasicStroke(2));
        g.drawOval(SIZE/3, SIZE/3, SIZE/3, SIZE/3);
        
        g.setColor  (new Color(1.0f, 0.271f, 0.0f));
        if (this.pb.blackWins.isSelected())
            g.fillOval(SIZE/2-25, SIZE/2-25, 20, 20);
        
        else 
         if (this.pb.drawIntercept.isSelected())
            g.fillOval(SIZE/2+5, SIZE/2-25, 20, 20); 
        
        else 
        if (this.pb.drawProtect.isSelected())
            g.fillOval(SIZE/2-25, SIZE/2+5, 20, 20);          
  
        else 
        if (this.pb.whiteWins.isSelected())
            g.fillOval(SIZE/2+5, SIZE/2+5, 20, 20);   
          
        else 
        if (this.pb.mixedDraw.isSelected()) {
             g.fillOval(SIZE/2+5, SIZE/2-25, 20, 20); 
             g.fillOval(SIZE/2-25, SIZE/2+5, 20, 20);      
        }
        
        int fontSize = 12;
        g.setFont (new Font ("TimesRoman", Font.BOLD, fontSize));
        
        g.setColor (Color.WHITE);
        int x1String = SIZE/3+SIZE/12;
        int y1String = SIZE/3+SIZE/12;
        g.drawString("Black", x1String , y1String );
        g.drawString("Wins", x1String , y1String+fontSize+5 );
        
        
        g.setColor (purple);
        int x2String = SIZE/2+10;
        g.drawString("DRAW", x2String , y1String );
        
        int y3String = SIZE/2 +20;
        g.drawString("DRAW", x1String-20, y3String);
        
        g.setColor (Color.BLACK);
        g.drawString("White", x2String, y3String);
        g.drawString("Wins", x2String, y3String+fontSize+5);
        
        fontSize = 18;
        
        Font font = (new Font ("TimesRoman", Font.BOLD, fontSize));
        g.setFont(font);
        g.setColor (Color.WHITE);
        drawAttributed (g, "WB-InterceptW-Zone", font, 0, 12, 30,70);
        drawAttributed (g, "& BB-ProtectB-Zone", font, 0, 12, 30 , 70+fontSize+5);
        
        g.setColor (purple);
        drawAttributed (g, "WB-InterceptW-Zone", font, 0, 12, 320,70);
        drawAttributed (g, "& BB-InterceptB-Zone", font, 0, 14, 320 , 70+fontSize+5);
        
        drawAttributed (g, "WB-ProtectW-Zone", font, 0, 10, 30,320);
        drawAttributed (g, "& BB-ProtectB-Zone", font, 0, 12, 30 , 320+fontSize+5);

        g.setColor (Color.BLACK);
        drawAttributed (g, "WB-ProtectW-Zone", font, 0, 10, 320,320);
        drawAttributed (g, "& BB-InterceptB-Zone", font, 0, 14, 320 , 320+fontSize+5);
        
        
       if (this.zt.isBlackWin()){
               g.setColor (Color.WHITE);
               g.fillOval(SIZE/4, SIZE/4, 20,20);    
        }    
        
        if (this.zt.isWhiteWin()){
            g.setColor (Color.BLACK);
            g.fillOval(SIZE- SIZE/4, SIZE- SIZE/4, 20,20);
        }
        
        if (this.zt.isBothIntercept()){
            if (PanelButtons.endOfBranch && pb.solution.isSelected()){
                g.setColor  (new Color(1.0f, 0.271f, 0.0f));
                g.fillOval(SIZE/2+5, SIZE/2-25, 20, 20);  
           }
           else{
                g.setColor(Color.BLACK);
                g.fillOval(SIZE- SIZE/4, SIZE/4, 20,20);
            }
        }    
         
        if (this.zt.isBothProtect()){
            if (PanelButtons.endOfBranch && pb.solution.isSelected()){
                g.setColor  (new Color(1.0f, 0.271f, 0.0f));
                g.fillOval(SIZE/2-25, SIZE/2+5, 20, 20);    
            }
            else{
                g.setColor(Color.BLACK);
                g.fillOval(SIZE/4, SIZE- SIZE/4, 20,20);
            }        
        }    
            
    }
    
    private void drawAttributed (Graphics2D g, String s,
            Font font, int xSub, int ySub, int x, int y){
               
        AttributedString as = new AttributedString (s);
        as.addAttribute(TextAttribute.FONT, font, xSub, ySub);
        as.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB,ySub, ySub+6 );
        g.drawString (as.getIterator(), x , y);
    }


    public static void main(String[] args) { 
        
    }
}