/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LGPackage;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import sun.java2d.Surface;

        

public class BoardGUI extends JPanel implements MouseListener, MouseMotionListener  {
 
//private final int frameheight;
//private final int framewidth;
//public int panelwidth ;
//public int panelheight;
//private final int horizontalgap;
//private final int verticalgap;
//private final int location_x;
//private final int location_y;
    
    private final int squaresheight;
    private final int squareswidth;
    private final int borderGap =20;
    private final int size;
    public  static int squaresize;

    private BufferedImage dark;
    private BufferedImage light;
    private TexturePaint darktp;
    private TexturePaint lighttp;

    JPanel drawingPane;

    Graphics bufferGraphics;
    Image offscreen;
    Dimension dim;
    Graphics2D g2d;
    BufferedImage image;
    
    
    private int xMouse =-10;
    private int yMouse= -10;

    private static int ovalsize;
    public static Boolean pieceset;
    java.util.List<CoordStruct> JeepCoor = Collections.synchronizedList(new ArrayList<CoordStruct> ());
     
    public BoardGUI( int sh, int sw)  {  
    
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
       super (new BorderLayout()); 
       
       pieceset= false;
       squaresheight = 20;
       squareswidth  = 20; 
       size = 721;
       squaresize = (size-1)/20;
       ovalsize = squaresize-4; // has to be even
       doDrawing ();
       
       drawingPane = new DrawingPane();     
       drawingPane.addMouseListener(this);
       drawingPane.addMouseMotionListener(this);
            
       drawingPane.setPreferredSize( new Dimension (size+40, size+40));   
       add (drawingPane, BorderLayout.CENTER);    
       
     // CoordStruct hi =new CoordStruct(10,20,30); 
      //JeepCoor.add( hi );
      //System.out.println("coor "+ hi.xcoor + " " + hi.ycoor + " " + hi.zcoor);
    }
       
    private void loadImages() {

        try {
            dark = ImageIO.read(new File("cementdark.jpeg"));
            light = ImageIO.read(new File("cementlight.jpeg"));
            System.out.println("It loaded");
        } 
        catch (IOException ex) {
            Logger.getLogger(Surface.class.getName()).log(Level.SEVERE, null, ex);
        }
    } //End of loadImages()
    
    public void doDrawing() {

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

    }//End of doDrawing()
    
  ////////////////////////////////Mouse Events//////////////////////////////////
    @Override
    public void mouseReleased(MouseEvent e)
    {
        xMouse = e.getX();
        yMouse= e.getY();
        int squarex, squarey;
        
        //Make sure that mouse was clicked in board
        if ((xMouse> borderGap && xMouse < borderGap + squareswidth * squaresize) 
                    && yMouse > borderGap && yMouse < borderGap + squaresheight * squaresize){
            if (PieceSpecificationGUI.locateJeepActive== false){
                squarex = (xMouse-borderGap)/squaresize;
                squarey = (yMouse-borderGap)/squaresize;
                xMouse = borderGap + (squarex*squaresize)+2+ (ovalsize/2);
                yMouse= borderGap + (squarey*squaresize)+2+ (ovalsize/2);

                JeepCoor.add (new CoordStruct (xMouse, yMouse,0));

                drawingPane.repaint();

                pieceset = true;
                PieceSpecificationGUI.LocationPanel.setCurrentXPanelText(Integer.toString (squarex));
                PieceSpecificationGUI.LocationPanel.setCurrentYPanelText(Integer.toString (squarey));

                PieceSpecificationGUI.Locate.setEnabled(true);
                PieceSpecificationGUI.locateJeepActive = true;
                PieceSpecificationGUI.Locate.setText("Locate in Board");

            } 
           
        }
    }

    @Override
    public void mouseClicked(MouseEvent e){}
    @Override
    public void mouseEntered(MouseEvent e){}
    @Override
    public void mouseExited(MouseEvent e){}
    @Override
    public void mousePressed(MouseEvent e){}
    @Override
    public void mouseDragged(MouseEvent e){}

    @Override
    public void mouseMoved(MouseEvent e) { 
       
   
        xMouse= e.getX();
        yMouse = e.getY();
        
        if (pieceset == false && PieceSpecificationGUI.locateJeepActive == false){
            if ((xMouse-ovalsize/2 > borderGap && xMouse+ovalsize/2  < borderGap + squareswidth * squaresize) 
                    && yMouse-ovalsize/2 > borderGap && yMouse+ovalsize/2 < borderGap + squaresheight * squaresize){
                    drawingPane.repaint();

            }
        }    
    }
    
    private class CoordStruct {
        public int xcoor, ycoor, zcoor;
        
        CoordStruct (int x, int y, int z){
            xcoor = x;
            ycoor = y;
            zcoor= z;
        }
    }
    
    public class DrawingPane extends JPanel {
        
        //Area tankIcon = new Area();
        //Rectangle2D.Double rec = new Rectangle2D.Double(30, 30, 20, 10);
        
        
        @Override
        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            g.drawImage(image, 0,0, this);
            
            // Draw oval when x and y are positive, User needs to click
            
            for (CoordStruct oval: JeepCoor){
                g.setColor (Color.RED);             
                g.fillOval(oval.xcoor-ovalsize/2, oval.ycoor-ovalsize/2, ovalsize, ovalsize);    
            }
            
            if (xMouse>=0 && yMouse>=0){
                g.setColor(Color.RED);
                g.fillOval (xMouse-ovalsize/2, yMouse-ovalsize/2, ovalsize,ovalsize);

        }
          
         //   doDrawing(g); // draws board       
        }//End paintComponent ()
    }//End DrawingPane
}//End BoardGUI

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
    

       /* @Override
        public void paint (Graphics g){

            offscreen = createImage(getWidth(), getHeight());
            bufferGraphics = offscreen.getGraphics();
            paintComponent (bufferGraphics);
            //g.drawImage(offscreen, 0,0, this);
        }//End of paint()*/

 

