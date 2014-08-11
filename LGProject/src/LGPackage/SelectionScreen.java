package LGPackage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import static javax.swing.SwingConstants.VERTICAL;


/**
 *
 * @author nati
 */
public class SelectionScreen extends JFrame{
    
    public static final int Maxwidth = Toolkit.getDefaultToolkit().getScreenSize().width - 40;
    public static final int Maxheight = Toolkit.getDefaultToolkit().getScreenSize().height -40;
    public static int fwidth;
    public static int fheight;

    
    
    public SelectionScreen (int h, int w){
    
     Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
     JFrame f=new JFrame("Movement & Target");
     f.setLayout(null);
     
     System.out.println( "heigth " + h + " and w " + w  );
     
     if (h <=4 && w <=4){
         fwidth = (Maxwidth*2)/3;
         fheight = (Maxheight*2)/3;
         
        f.setSize (fwidth ,fheight);
     }
     
     else{ 
         
         f.setSize (Maxwidth, Maxheight);
         fwidth = Maxwidth;
         fheight = Maxheight;
    }
     

    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setLocation(dim.width/2-f.getSize().width/2, dim.height/2-f.getSize().height/2);

    BoardGUI board = new BoardGUI(fheight, fwidth, h, w);
    

    f.getContentPane().add(board);
    
    final int widthofRPanel = fwidth - (board.panelwidth+40) ;
    final int locAfterBoard = board.panelwidth+10;
    final int heightofRPanel = board.panelheight;
    

    f.getContentPane().add(RightPanel ( widthofRPanel, locAfterBoard, heightofRPanel));
    f.setVisible(true);
    
    System.out.println("f");
     
    
    }
    
    
    private JPanel RightPanel (int widthofRPanel, int locAfterBoard, int heightofRPanel){
    
        JPanel RPanel = new JPanel ();
        RPanel.setSize(widthofRPanel, heightofRPanel);
        RPanel.setLocation(locAfterBoard, 20);
        RPanel.setBackground(Color.BLUE);
       // RPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        RPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 0.2; 
        c.weightx = 0.2;
        
        JLabel Team = new JLabel ("Team");
        c.gridx = 0;
        c.gridy =0;

        RPanel.add (Team, c);
        
        JComboBox TeamNumber = new JComboBox();
        TeamNumber.setModel(new DefaultComboBoxModel(new String[] {"1", "2"}));
        c.gridx= 1;
        c.gridy = 0;
       
        RPanel.add(TeamNumber,c);

     //   c.insets = new Insets(0,10,0,0);  //top padding
     //   c.fill = GridBagConstraints.HORIZONTAL;
        

        JButton Jeep = new JButton("Jeep");
        JButton Tank= new JButton("Tank");
        JButton Jet= new JButton("Jet"); 
        JButton Helicopter= new JButton("Helicopter"); 
        JButton Custom= new JButton("Custom"); 
        JButton Obstacles= new JButton("Obstacles");
        JButton Preview= new JButton("Preview");
        
        JButton[] buttonList1 = new JButton[]{ Jeep, Tank, Jet, Helicopter, Custom,
        Obstacles,Preview};
        
        for (int i = 0; i < buttonList1.length; i++){
            c.gridx = 0;
            c.gridy = i+1;
            RPanel.add(buttonList1[i],c);
        }

        Jeep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JeepButtonActionPerformed(evt);
            }
                });
        
        Integer value = 0;
        Integer min = 0;
        Integer max = 10;
        Integer step = 1;
        
        SpinnerNumberModel model0 = new SpinnerNumberModel(value, min, max, step);
        SpinnerNumberModel model1 = new SpinnerNumberModel(value, min, max, step);
        SpinnerNumberModel model2 = new SpinnerNumberModel(value, min, max, step);
        SpinnerNumberModel model3 = new SpinnerNumberModel(value, min, max, step);
        SpinnerNumberModel model4 = new SpinnerNumberModel(value, min, max, step);
        SpinnerNumberModel model5 = new SpinnerNumberModel(value, min, max, step);
      
        JSpinner JeepQty = new JSpinner(model0);
        JSpinner TankQty = new JSpinner(model1);
        JSpinner JetQty = new JSpinner(model2);
        JSpinner HelicopterQty = new JSpinner(model3);
        JSpinner CustomQty = new JSpinner(model4);
        JSpinner ObstaclesQty = new JSpinner(model5);
        JSpinner l = new JSpinner(model0);
    
        JSpinner[] listOfQty = new JSpinner[]{JeepQty, TankQty, JetQty,
        HelicopterQty, CustomQty, ObstaclesQty, l };
        
        for (int i = 0; i < listOfQty.length; i++){
            c.gridx = 1;
            c.gridy = i+1;
         //   RPanel.add(listOfQty[i],c);
        }
        
        return RPanel;
    } 
    
      private void JeepButtonActionPerformed(java.awt.event.ActionEvent evt) {
          
        PieceSpecificationGUI p = new PieceSpecificationGUI();
          
      
  } 
    
}

