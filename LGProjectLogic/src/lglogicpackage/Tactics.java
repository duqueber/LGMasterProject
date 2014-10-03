/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lglogicpackage;

import java.util.ArrayList;
import supportpackage.Moves;
import supportpackage.Node;

/**
 *
 * @author nati
 */
public interface Tactics {
    
    
    
    void developTactic ();
    void calculateNextMoves ();
    ArrayList<Node<Moves>> getNextMoves();
    boolean win();
    boolean fail ();
}
