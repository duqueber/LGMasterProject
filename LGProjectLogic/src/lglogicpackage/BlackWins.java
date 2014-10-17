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
public class BlackWins extends Strategies {

    private Tactics WTactic, BTactic;
    ArrayList<Node<Moves>> nextSteps;
    
    BlackWins (Board2D board){
        super (board);
        this.nextSteps = new ArrayList<>();
        this.WTactic = null;
        this.BTactic = null;
    }
    public void evaluateBlackWins (){
        evaluateBlackWins (this.moves.getRoot());
    }
    
    private void evaluateBlackWins (Node<Moves> m ){
        
        if (necessaryConditionMet () || necessaryConditionNotMet())
            return;
        
        if (!m.isRoot())
            makeStrategyMove (m.getData());
            
        this.nextSteps = generateNextSteps (m);    

        if (!this.nextSteps.isEmpty())
         if (!necessaryConditionMet () && !necessaryConditionNotMet() ||
                 this.BTactic instanceof KeepBothStates)
            m.setChildren(this.nextSteps);                    

        for (Node<Moves> step: this.nextSteps)       
            evaluateBlackWins(step);
    }
    //WhiteIntercept, BlackIntercept, WhiteProtect, Black Protect
    ArrayList<Node<Moves>> generateNextSteps (Node<Moves> m){
        
        if (m.getData().getPiece().getTeam() == 2  ){
            this.WTactic = chooseTactic ("_1_0","0_1_" );
            this.WTactic.developTactic();
            return this.WTactic.getNextMoves();
        }
        else {
            this.BTactic = chooseTactic ("_0_1","1_0_"  );
            this.BTactic.developTactic();
            return this.BTactic.getNextMoves();
        }    
            
    }
    
    private boolean necessaryConditionMet(){
         if (this.BTactic == null)
             return false;
         else
             return this.BTactic.possible();
    }
    
    private boolean necessaryConditionNotMet(){
         if (this.BTactic == null)
             return false;
          else
             return this.BTactic.notPossible();
    }
    

}
