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
public class DrawProtect extends Strategies{
    
    private Tactics WTactic, BTactic;
    ArrayList<Node<Moves>> nextSteps;
    Teams teamName;
    
    DrawProtect (Board2D board, Teams teamName){
        super (board);
        this.nextSteps = new ArrayList<>();
        this.WTactic = null;
        this.BTactic = null;
        this.teamName = teamName;
    }
    
    public void evaluateDrawProtect(){
        evaluateDrawProtect (this.moves.getRoot());
    }
    
    public void evaluateDrawProtect (Node<Moves> m ){
        
        if (necessaryConditionMet () || necessaryConditionNotMet()){
           // m.delete();
            return;
        }
        if (!m.isRoot())
            makeStrategyMove (m.getData());
            
        this.nextSteps = generateNextSteps (m);    

        if (!this.nextSteps.isEmpty())
            if (!necessaryConditionMet () && !necessaryConditionNotMet()||
                 this.WTactic instanceof KeepBothStates)
                m.setChildren(this.nextSteps);                    

        for (Node<Moves> step: this.nextSteps)       
            evaluateDrawProtect(step);
    }
    //WhiteIntercept, BlackIntercept, WhiteProtect, Black Protect
    ArrayList<Node<Moves>> generateNextSteps (Node<Moves> m){
        
        if (m.getData().getPiece().getTeam() == 2 || m.isRoot() ){
            if (Teams.WHITE.equals(this.teamName))
                this.WTactic = chooseTactic ("_0_1","0_1_");
            else 
                this.WTactic = chooseTactic ("_1_0","0_1_");
            
            this.WTactic.developTactic();
            return this.WTactic.getNextMoves();
        }
        else {
            if (Teams.BLACK.equals(this.teamName))
                this.BTactic = chooseTactic ("_0_1","0_1_" );
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
