/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lglogicpackage;

import java.util.ArrayList;
import supportpackage.Moves;
import supportpackage.Node;
import supportpackage.Tree;

/**
 *
 * @author nati
 */
public class WhiteWins extends Strategies{

    private Tactics WTactic, BTactic;
    private boolean defWin, defFail;
    ArrayList<Node<Moves>> nextSteps;
    
    WhiteWins (Board2D board){
        super (board);
        this.defFail = false;
        this.defWin = false;
        this.nextSteps = new ArrayList<>();
    }
    public void evaluateWhiteWins (){
        evaluateWhiteWins (this.moves.getRoot());
    }
    
    public void evaluateWhiteWins (Node<Moves> m ){
        
        if (isDefWin() || isDefFail())
     
        //this.moves.setRoot(new Node(new Moves(this.board.getPieceFromName("W-Fighter"))));
        //while (!fail){
           // if (this.moves.isEmpty()){
                
            //}// add conditions to stop loop, change board in strategies (need to 
            // add a function for doing this.
            this.WTactic = chooseTactic ("_1_0","0_1_" );
            this.WTactic.developTactic();
            this.nextSteps = this.WTactic.getNextMoves();
            
            if (!this.nextSteps.isEmpty())
                m.setChildren(this.nextSteps);                    

          //  this.tac = chooseTactic ("_0_1","1_0_" );
        //}
    }
    //WhiteIntercept, BlackIntercept, WhiteProtect, Black Protect
    private boolean isDefFail(){
        
        return this.BTactic.win() || this.WTactic.fail();
    }
    
    private boolean isDefWin(){
        return this.WTactic.win();
    }
}
