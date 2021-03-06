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
public class DrawIntercept extends Strategies {
    
    private Tactics WTactic, BTactic;
    ArrayList<Node<Moves>> nextSteps;
    Teams teamName;
    
    public DrawIntercept (Board2D board, Teams teamName){
        super (board);
        this.nextSteps = new ArrayList<>();
        this.WTactic = null;
        this.BTactic = null;
        this.teamName = teamName;
    }
    public void evaluateDrawIntercept (){
        evaluateDrawIntercept (this.moves.getRoot());
    }
    
    public void evaluateDrawIntercept (Node<Moves> m ){
        
        if (necessaryConditionMet () || necessaryConditionNotMet()){
            addToCutReason (m.getData().getStep(), "Sd Maintained");
            return;
        }
        if (!m.isRoot())
            makeStrategyMove (m.getData());
            
        this.nextSteps = generateNextSteps (m);    

        if (!this.nextSteps.isEmpty()){
            if (!necessaryConditionMet () && !necessaryConditionNotMet()||
                 this.WTactic instanceof KeepBothStates)
                m.setChildren(this.nextSteps); 
            else 
                if (necessaryConditionMet () || necessaryConditionNotMet())
                    addToCutReason (m.getData().getStep(), "Sd Maintained");
        }

        for (Node<Moves> step: this.nextSteps)       
            evaluateDrawIntercept(step);
    }
    //WhiteIntercept, BlackIntercept, WhiteProtect, Black Protect
    ArrayList<Node<Moves>> generateNextSteps (Node<Moves> m){
        
        if (m.getData().getPiece().getTeam() == 2 || m.isRoot() ){
            if (Teams.WHITE.equals(this.teamName))
                this.WTactic = chooseTactic ("_1_0","1_0_" );
            else 
                this.WTactic = chooseTactic ("_1_0","0_1_" );
            
            this.WTactic.developTactic();
            return this.WTactic.getNextMoves();
        }
        else {
            if (Teams.BLACK.equals(this.teamName))
                this.BTactic = chooseTactic ("_1_0","1_0_");
            else
                this.BTactic = chooseTactic  ("_0_1","1_0_");
            
            this.BTactic.developTactic();
            return this.BTactic.getNextMoves();
        }                
    }
    
    private boolean necessaryConditionMet(){

        if (Teams.WHITE.equals(this.teamName)){
            if (this.WTactic == null)
                return false;
            else
                return this.WTactic.possible();
        }
        else {
            if (this.BTactic == null)
                return false;
            else
                return this.BTactic.possible();            
        }        
    }
    
    private boolean necessaryConditionNotMet(){
       
        if (Teams.WHITE.equals(this.teamName)){
            if (this.WTactic == null)
                return false;
            else
                return this.WTactic.notPossible();
        }    
        
        else {
            if (this.BTactic == null)
                return false;
            else
                return this.BTactic.notPossible();            
        }  
    }
    

}
