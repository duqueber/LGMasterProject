/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LGPackage;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.io.IOException;
import javax.swing.JFrame;


public class PieceSpecificationGUI extends JFrame {
  
  public PieceSpecificationGUI(SelectionScreen s) throws IOException
  {
    super ("pieces"); 
    int dimw = Toolkit.getDefaultToolkit().getScreenSize().width;       
    Container c = getContentPane();
    c.setLayout( new BorderLayout() );
    
    PieceMovementPanel p = new PieceMovementPanel(s);
    c.add(p, BorderLayout.CENTER);

    setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
    pack();
    setResizable(false);    // fixed size display
    setVisible(true);
    //setLocation(s.RPanel.getLocation().x, 0);
    
  } 

}
