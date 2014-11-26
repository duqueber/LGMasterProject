/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lggui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import lglogicpackage.Board2D;


public class GUIFrame extends JFrame {

    Board2D board;
    static Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize(); 
    static final int PWIDTH = screenDim.width-80;   
    static final int PHEIGHT = screenDim.height-50; 
    static final int BHEIGHT = PHEIGHT-50;
    
    
    public GUIFrame(Board2D board) throws IOException {
        super("LG States");
        this.board = board;
        Container c = getContentPane();
        c.setLayout( new BorderLayout() );
        BoardScene bs = new BoardScene(board);     //
        c.add(bs, BorderLayout.LINE_START);
        
        JPanel panelRight = new JPanel();        
        c.add (panelRight, BorderLayout.CENTER);
    
       // panelRight.setPreferredSize (new Dimension (this.PWIDTH-this.BHEIGHT,this.PHEIGHT));
        
        panelRight.setLayout(new BorderLayout());
        
        PanelButtons panelButtons = new PanelButtons (bs, this);
        panelRight.add (panelButtons.getPanelButtons(), BorderLayout.CENTER);
        
        JPanel panelTree = panelButtons.getPanelTree();
        panelRight.add (panelTree, BorderLayout.NORTH);
        
      

        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setState(this.ICONIFIED);
        this.setExtendedState(MAXIMIZED_BOTH);
        showOnScreen (1, this, false);
        setResizable(false); 
        pack();
        setVisible(true);
    }
    
    public static void showOnScreen( int screen, JFrame frame , boolean mid) {
        int x;
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();
        if( screen > -1 && screen < gd.length ) {
            Rectangle r = gd[screen].getDefaultConfiguration().getBounds();
            if (mid)
               frame.setLocation(r.x + r.width/2, frame.getY());
            else
                frame.setLocation(r.x + r.width/2, frame.getY());
            
        } else if( gd.length > 0 ) {
            frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x, frame.getY());
        } else {
            throw new RuntimeException( "No Screens Found" );
    }
   }

    public static void main(String[] args) throws IOException{ 
        //new GUIFrame(); 
    }
} 
