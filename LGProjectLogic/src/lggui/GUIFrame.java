/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lggui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
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
        
        PanelButtons panelButtons = new PanelButtons (bs);
        panelRight.add (panelButtons.getPanelButtons(), BorderLayout.CENTER);
        
        JPanel panelTree = panelButtons.getPanelTree();
        panelRight.add (panelTree, BorderLayout.NORTH);
        
      

        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setState(this.ICONIFIED);
        this.setExtendedState(MAXIMIZED_BOTH);
        pack();
        setResizable(false); 
        setVisible(true);
    } 

    public static void main(String[] args) throws IOException{ 
        //new GUIFrame(); 
    }
} 
