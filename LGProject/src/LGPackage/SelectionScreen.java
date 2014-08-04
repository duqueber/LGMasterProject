package LGPackage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;
import static javax.swing.SwingConstants.VERTICAL;


/**
 *
 * @author nati
 */
public class SelectionScreen extends JFrame{
    
    public static int Maxwidth = Toolkit.getDefaultToolkit().getScreenSize().width - 40;
    public static int Maxheight = Toolkit.getDefaultToolkit().getScreenSize().height -40;
    public static int fwidth;
    public static int fheight;
    public static int slidex;
    public static int slidey;
    
    
    public SelectionScreen (int h, int w){
    
     Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
     JFrame f=new JFrame("Movement & Target");
     Container contentPane = f.getContentPane();
     //contentPane.setLayout(null);

     System.out.println( "heigth " + h + " and w " + w  );
     
   /*  if (h <=4 && w <=4){
         fwidth = (Maxwidth*2)/3;
         fheight = (Maxheight*2)/3;
         
         f.setSize (fwidth ,fheight);
     }
     
     else{ */
         
         f.setSize (Maxwidth, Maxheight);
         fwidth = Maxwidth;
         fheight = Maxheight;
     //}
     

    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setLocation(dim.width/2-f.getSize().width/2, dim.height/2-f.getSize().height/2);
     
    BoardGUI board = new BoardGUI(fheight, fwidth, h, w);

    f.add(board);

    f.setVisible(true);
    System.out.println("f");
     
    
    }
    
}
