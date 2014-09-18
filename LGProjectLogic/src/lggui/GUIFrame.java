/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lggui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.IOException;
import javax.swing.JFrame;


public class GUIFrame extends JFrame {

    public GUIFrame() throws IOException {
        super("LG States");
        Container c = getContentPane();
        c.setLayout( new BorderLayout() );
        BoardScene bs = new BoardScene();     //
        c.add(bs, BorderLayout.CENTER);

        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        pack();
        setResizable(false); 
        setVisible(true);
    } 

    public static void main(String[] args) throws IOException{ 
        new GUIFrame(); 
    }
} 
