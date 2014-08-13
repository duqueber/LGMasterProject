package LGPackage;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class SelectionScreen extends JFrame{
    
   // public static final int Maxwidth = Toolkit.getDefaultToolkit().getScreenSize().width - 40;
   // public static final int Maxheight = Toolkit.getDefaultToolkit().getScreenSize().height -40;
   //public static int fwidth;
   // public static int fheight;
    public static JPanel RPanel;
    public JPanel border;
     BoardGUI board;
     int h;
     int w;
    
    public SelectionScreen (int h, int w){
    super ("Location");
     
     this.h =h;
     this.w=w;
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

     System.out.println( "screen heigth " + dim.height  );
     /*
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
     
    */
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    setLayout(new BorderLayout());
    Container c = getContentPane();

    createBoard();
    c.add(board, BorderLayout.WEST);
    
  /*  final int widthofRPanel = this.getWidth() - (board.getWidth()) -10 ;
    final int locAfterBoard = board.getWidth()+10;
    final int heightofRPanel = this.getHeight()-1;
    System.out.println("w of Rpanels, loc after Board, height of panel" + widthofRPanel + ", " 
    + locAfterBoard + " and " + heightofRPanel);*/
    c.add(RightPanel ( ), BorderLayout.CENTER);

    setResizable(false);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);

    }
    
    private void createBoard(){
    border = new JPanel();
    board = new BoardGUI(h,w);
    border.add(board);
  }

    private JPanel RightPanel (){
    
        RPanel = new JPanel ();
       // RPanel.setSize(widthofRPanel, heightofRPanel);
       // RPanel.setLocation(locAfterBoard, 1);
      //  RPanel.setLayout(null);
      //  RPanel.setBackground(Color.BLUE);
       // RPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        RPanel.setLayout(new GridBagLayout());

        RPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 0.2; 
        c.weightx = 0.2;

        JLabel Team = new JLabel ("Team");
        c.gridx = 0;
        c.gridy =0;
     
        RPanel.add (Team);
        
        JComboBox TeamNumber = new JComboBox();
        TeamNumber.setModel(new DefaultComboBoxModel(new String[] {"1", "2"}));
        c.gridx= 1;
        c.gridy = 0;
       
        RPanel.add(TeamNumber,c);

        c.insets = new Insets(0,10,0,0);  //top padding
        c.fill = GridBagConstraints.HORIZONTAL;
        

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
                try {
                    JeepButtonActionPerformed(evt);
                } catch (IOException ex) {
                    Logger.getLogger(SelectionScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
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
            RPanel.add(listOfQty[i],c);
        }
        
        pack();
        return RPanel;
    } 
    
      private void JeepButtonActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
          
        PieceSpecificationGUI p = new PieceSpecificationGUI(this);
          
      
  } 

          public static void main(String args[]) {
    /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SelectionScreen(0,0).setVisible(true);
            }
        });
    }
    
}

