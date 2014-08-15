/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LGPackage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;


public class PieceSpecificationGUI extends JFrame {
    
  public JPanel LocPanel;  
  public static SelectionScreen s;
  public static JButton Locate;
  public static Boolean locateJeepActive;
  
  public PieceSpecificationGUI(SelectionScreen s) throws IOException
  {
    super ("pieces"); 
    this.s = s;
    int dimw = Toolkit.getDefaultToolkit().getScreenSize().width;       

    Container c = getContentPane();
    c.setLayout( new BorderLayout() );
    
    // Add Panel that displays the piece in PAGESTART
    Point3d UserPos = new Point3d(6,5,6);
    Point3d LookAt = new Point3d(0,0,0);
    PieceMovementPanel p = new PieceMovementPanel(s, 3, 3, 3);
    p.initUserPosition(UserPos, LookAt);
    p.loadModel("copter.obj", new Vector3d (1.5,1.5,1.5));

    p.addLine(new Point3f(0,0,0), new Point3f(1,1,1));
    p.addBranchGroup();
    c.add(p, BorderLayout.PAGE_START);

    // Add 2 Panels. One displays coordinates, the second one the number of the piece.
    // at LINESTART and CENTER
    LocationPanel slp = new LocationPanel ();
    slp.setLocationPanel(c); 
    
    // Add Panel that displays the button at PAGEEND
    LocationButtonPanel jp = new LocationButtonPanel (c);
    locateJeepActive = true;

    setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
    pack();
    setResizable(false);    // fixed size display
    setVisible(true);
    setLocation(dimw/2, 40);
    
  } 

    public class LocationPanel implements ItemListener{

        JPanel CardPanel;

        public void setLocationPanel( Container l){

            //ComboPanel
            JPanel ComboPanel = new JPanel ();

            String [] list = new String [s.getJeepQty()];

            for (int i = 0; i < list.length; i++){
                  list[i] = Integer.toString(i+1);
                  System.out.println ( "list [i] " + list[i]);
            }

            JComboBox PieceNumber = new JComboBox (list);
            PieceNumber.setEditable(false);
            PieceNumber.addItemListener(this);
            ComboPanel.add(PieceNumber);


            //Panel xyz
            JPanel[] xyz = new JPanel[s.getJeepQty()];

            JPanel xyzPanel = new JPanel ();


            xyzPanel.setBorder (new EmptyBorder (10,10,10,10));
            JLabel x= new JLabel ("x");
            JLabel y = new JLabel ("y");
            JLabel z = new JLabel ("z");

            xyzPanel.add(x);
            xyzPanel.add(y);
            xyzPanel.add(z);

            for (int i=0; i < xyz.length; i++){ 
                xyz[i] = xyzPanel;
            }


            //CardPanel
            CardPanel= new JPanel(new CardLayout());

            for (int i = 0; i< s.getJeepQty(); i++){
                    CardPanel.add(xyz[0], list [0]);
            }

          l.add (CardPanel, BorderLayout.LINE_START);
          l.add (ComboPanel, BorderLayout.CENTER);

        }

        @Override
        public void itemStateChanged(ItemEvent evt) {
            CardLayout cl = (CardLayout)(CardPanel.getLayout());
        cl.show(CardPanel, (String)evt.getItem());
    }

      }// END LocationPanel Class


    public class LocationButtonPanel{

        LocationButtonPanel( Container c) {

            JPanel LocButPanel = new JPanel ();
            LocButPanel.setBorder (new EmptyBorder (10,10,10,10));

            //Create Locate Button 
            Locate = new JButton ("Locate in Board");
            LocButPanel.add(Locate);

            c.add (LocButPanel, BorderLayout.LINE_END);

            Locate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    try {
                            LocateButtonActionPerformed(evt);
                    } 
                    catch (IOException ex) {
                        Logger.getLogger(PieceSpecificationGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }                       
                }   
            });
        }

        private void LocateButtonActionPerformed(java.awt.event.ActionEvent evt) throws IOException {

           s.toFront();
           Locate.setText ("Click Board");
           Locate.setEnabled(false);
           locateJeepActive = false; 
           BoardGUI.pieceset = false;
        }
    }//END LocationButtonPanel
  
  public static void main(String args[]) throws IOException{
      
      SelectionScreen s = new SelectionScreen (20, 20);
      new PieceSpecificationGUI (s);
  }

}
