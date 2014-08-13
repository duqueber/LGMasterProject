/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LGPackage;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import sun.java2d.Surface;

public class BoardGUI extends JPanel {
 
//private final int frameheight;
//private final int framewidth;
private final int squaresheight;
private final int squareswidth;
public  static int squaresize;
//public int panelwidth ;
//public int panelheight;

//private final int horizontalgap;
//private final int verticalgap;
//private final int location_x;
//private final int location_y;

private BufferedImage dark;
private BufferedImage light;
private TexturePaint darktp;
private TexturePaint lighttp;

/*
Graphics bufferGraphics;
// The image that will contain everything that has been drawn on
// bufferGraphics.
Image offscreen;
// To get the width and height of the applet.
Dimension dim;*/

    public BoardGUI( int sh, int sw) {   

       squaresheight = 20;
       squareswidth  = 20; 
             
       /* panelwidth = (3*framewidth)/5;
        panelheight = frameheight-80; 
        calculate_squaresize();
        
        //recalculates size of panes so it is divisible by the number of squares
        horizontalgap =(abs(panelwidth - (squaresize* squareswidth))/2) ;
       
        verticalgap = (abs(frameheight - (squaresize* squaresheight))/2);
        int panelwidth_init = panelwidth;
        
        int panelheight_init = panelheight;
        panelwidth = (squareswidth * squaresize)+2*horizontalgap;
        panelheight = (squaresheight * squaresize)+2*verticalgap;

      */  
          int size = 721;
          squaresize = (size-1)/20;

        setPreferredSize( new Dimension (size+40, size+40));
        
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

    /*private void calculate_squaresize(){
    
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
        
    }*/

    private void doDrawing(Graphics g) {

       Graphics2D g2d = (Graphics2D) g;
  
       darktp = new TexturePaint(dark, new Rectangle(0, 0, squaresize, squaresize));
       lighttp = new TexturePaint(light, new Rectangle(0, 0, squaresize, squaresize));
       
       for (int y = 0; y< squaresheight; y++)        
       {
          for (int x = y%2; x< squareswidth ; x+=2) {
           
           if (y%2==0){
                g2d.setPaint(darktp);
                g2d.fillRect(squaresize*x+20, 
                        squaresize*y+20, squaresize, squaresize);  
            }
           
           else{
                g2d.setPaint(darktp);
                g2d.fillRect((squaresize*x+20),
                        squaresize*y+20, squaresize, squaresize);                    
            } 
          }    
        }  

       for (int y = 0; y< squaresheight; y++)        
       {
          for (int x = (y+1)%2; x< squareswidth ; x+=2) {
           
           if (y%2==1){
                g2d.setPaint(lighttp);
                g2d.fillRect(squaresize*x+20, 
                        squaresize*y+20, squaresize, squaresize);  
            }          
           else{
                g2d.setPaint(lighttp);
                g2d.fillRect((squaresize*x)+20,
                         squaresize*y+20, squaresize, squaresize);                     
            } 
          }    
        }   
    }
    
    
/*@Override
    public void paint (Graphics g){
        
        offscreen = createImage(getWidth(), getHeight());
        bufferGraphics = offscreen.getGraphics();
        paintComponent (bufferGraphics);
       
        
        g.drawImage(offscreen, 0,0, this);
    }*/
    
@Override
     public void paintComponent(Graphics g) {
              
        super.paintComponent(g);
        loadImages(); 
        doDrawing(g); // draws board       
    }
}



