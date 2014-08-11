/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LGPackage;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import javax.swing.JFrame;


public class PieceSpecificationGUI extends JFrame {
    
    
  public PieceSpecificationGUI() 
  {
    super ("pieces"); 
    this.setSize(500, 500);
    Dimension dim2 = Toolkit.getDefaultToolkit().getScreenSize();
    this.setLocation(dim2.width-this.getSize().width, 20);
    
    
    //Container c = getContentPane();
   // c.setLayout( new BorderLayout() );
    
    
    //WrapCheckers3D w3d = new WrapCheckers3D();     // panel holding the 3D canvas
    //c.add(w3d, BorderLayout.CENTER);

    setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
    //pack();
    //setResizable(false);    // fixed size display
    setVisible(true);
  } // end of Checkers3D()


// -----------------------------------------

  public static void main(String[] args) throws IOException
  { new PieceSpecificationGUI(); }
    
}
