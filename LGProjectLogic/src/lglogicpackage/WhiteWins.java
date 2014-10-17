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
    ArrayList<Node<Moves>> nextSteps;
    private boolean whiteWinsChance;
    
    WhiteWins (Board2D board){
        super (board);
        this.nextSteps = new ArrayList<>();
        this.WTactic = null;
        this.BTactic = null;
        this.whiteWinsChance= false;
    }
    public void evaluateWhiteWins (){
        evaluateWhiteWins (this.moves.getRoot());

    }
    
    public void evaluateWhiteWins (Node<Moves> m ){
        
        if (necessaryConditionNotMet()){
           // m.delete();
            return;
        }
        if (!m.isRoot())
            makeStrategyMove (m.getData());
            
        this.nextSteps = generateNextSteps (m);    

        if (!this.nextSteps.isEmpty())
            if (!necessaryConditionNotMet()||
                 this.WTactic instanceof KeepBothStates)
                m.setChildren(this.nextSteps);                    

        for (Node<Moves> step: this.nextSteps)       
            evaluateWhiteWins(step);
    }
    //WhiteIntercept, BlackIntercept, WhiteProtect, Black Protect
    ArrayList<Node<Moves>> generateNextSteps (Node<Moves> m){
        
        if (m.getData().getPiece().getTeam() == 2 || m.isRoot() ){
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
         if (this.WTactic == null)
             return false;
         else
             return this.WTactic.possible();
    }
    
    private boolean necessaryConditionNotMet(){
         if (this.WTactic == null)
             return false;
          else
             return this.WTactic.notPossible();
    }
    

    
}
