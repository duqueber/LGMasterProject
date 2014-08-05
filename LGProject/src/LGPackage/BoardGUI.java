/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LGPackage;
import java.awt.*;
import java.awt.BorderLayout;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.abs;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import sun.java2d.Surface;


/**
 *
 * @author nati
 */


public class BoardGUI extends JPanel {
 
private final int frameheight;
private final int framewidth;
private final int squaresheight;
private final int squareswidth;
public static int squaresize;
public final int panelwidth ;
public final int panelheight;
private final int sizepanel2;
private int horizontalgap;
private int verticalgap;

private BufferedImage dark;
private BufferedImage light;
private TexturePaint darktp;
private TexturePaint lighttp;

    
    public BoardGUI( int fh, int fw, int sh, int sw) {   

        frameheight = fh;
        framewidth = fw;
        squaresheight = sh;
        squareswidth  = sw; 
        panelwidth = (3*framewidth)/5;
        panelheight = frameheight-70; 
        sizepanel2= framewidth-panelwidth;

        setSize(panelwidth,panelheight);
        setLocation(0,0);
        setBackground(Color.WHITE);
        /* this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(panelwidth, frameheight));
        emptyPanel.setMaximumSize(new Dimension(panelwidth, frameheight));
        emptyPanel.setMinimumSize(new Dimension(panelwidth, frameheight));
        emptyPanel.setBackground(new Color(0,0,0,65));
  

        JPanel RightPanel = new JPanel ();
        


        RightPanel.add(new JButton ("hello"));
        RightPanel.add(new JButton ("hello2"));
        RightPanel.setBackground (new Color(0,0,0,145));
        
  
                
        this.add(emptyPanel);
        this.add(RightPanel);

*/
    
    }
    
    private void loadImages() {

        try {

            dark = ImageIO.read(new File("cementdark.jpeg"));
            light = ImageIO.read(new File("cementlight.jpeg"));

            System.out.println("It loaded");
        } catch (IOException ex) {

            Logger.getLogger(Surface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        //this.setBounds (20,20,panelwidth, panelheight);
        if (squaresheight > squareswidth){
            if (panelheight < panelwidth)
                    squaresize = panelheight/squaresheight;
            else
                    squaresize = panelwidth/squaresheight;
        }
        
        else{
            if (panelheight < panelwidth)
                    squaresize = panelheight/squareswidth;
            else
                    squaresize = panelwidth/squareswidth;
        }
       
        System.out.println("frame width and height" + frameheight  + " and " + framewidth);
        System.out.println("width and height" + panelheight  + " and " + panelwidth);
        
       horizontalgap = abs(panelwidth - (squaresize* squareswidth))/2;
       
       verticalgap = abs(panelheight - (squaresize* squaresheight))/2+20;
       
       darktp = new TexturePaint(dark, new Rectangle(0, 0, squaresize, squaresize));
       lighttp = new TexturePaint(light, new Rectangle(0, 0, squaresize, squaresize));
       
       System.out.println("squaresize " + squaresize);
       
       System.out.println("vertical and horizontal " + verticalgap + " and " + horizontalgap);
       
       for (int y = 0; y< squaresheight; y++)        
       {
          for (int x = y%2; x< squareswidth ; x+=2) {
           
           if (y%2==0){
                g2d.setPaint(darktp);
                g2d.fillRect(horizontalgap +squaresize*x, 
                        verticalgap + squaresize*y, squaresize, squaresize);  
            }
           
           else{
                g2d.setPaint(darktp);
                g2d.fillRect((horizontalgap+squaresize*x),
                        verticalgap + squaresize*y, squaresize, squaresize);      
                
            } 
          }    
        }  

       for (int y = 0; y< squaresheight; y++)        
       {
          for (int x = (y+1)%2; x< squareswidth ; x+=2) {
           
           if (y%2==1){
                g2d.setPaint(lighttp);
                g2d.fillRect(horizontalgap +squaresize*x, 
                        verticalgap + squaresize*y, squaresize, squaresize);  
            }          
           else{
                g2d.setPaint(lighttp);
                g2d.fillRect((horizontalgap+squaresize*x),
                        verticalgap + squaresize*y, squaresize, squaresize);                     
            } 
          }    
        }   
    }
    
    
@Override
     public void paintComponent(Graphics g) {
              
        super.paintComponent(g);
       // int panelwidth = (3*framewidth)/5;
        //int panelheight = frameheight-70;
        

        loadImages(); 
        doDrawing(g); // draws board  

              
    }
}



