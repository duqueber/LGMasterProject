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
public class BWinWMixed extends Strategies{
    
        
    private MixedDraw WTactic; 
    private BlackWins BTactic;
    ArrayList<Node<Moves>> nextSteps;
    Strategies.Teams teamName;

    BWinWMixed (Board2D board, Strategies.Teams teamName){
        super (board);
        this.nextSteps = new ArrayList<>();
        this.WTactic = null;
        this.BTactic = null;
        this.teamName = teamName;
    }
    public void createTree (){
        createTree  (this.moves.getRoot());
    }
    
    public void createTree(Node<Moves> m ){
        
        if (wSufficientConditionMet () || bSufficientConditionMet()){
            return;
        }
        if (!m.isRoot())
            makeStrategyMove (m.getData());
            
        this.nextSteps = generateNextSteps (m);    

        if (!this.nextSteps.isEmpty())
            if ((!wSufficientConditionMet () && !bSufficientConditionMet()))
                m.setChildren(this.nextSteps);                    

        for (Node<Moves> step: this.nextSteps)       
            createTree (step);
    }
    //WhiteIntercept, BlackIntercept, WhiteProtect, Black Protect
    private ArrayList<Node<Moves>> generateNextSteps (Node<Moves> m){
        
        ArrayList<Node<Moves>> temp;
        if (m.getData().getPiece().getTeam() == 2 || m.isRoot() ){
            this.WTactic = new MixedDraw (this.board, Teams.WHITE);// choose intercept or protect.
            //both will return a mixed strategy if it exists.
            temp =this.WTactic.generateNextSteps(m);
            if (!temp.isEmpty()) // a mixed strategy does not exist.
                return temp;
            else
              if (isInterceptDraw(Teams.WHITE) && isProtectDraw(Teams.WHITE))   
                  //calculate next steps for both types of draw and return both.
                // if only one calculate next steps for that one . 
                  // for black same think except that I want to calculate first
                  //black win
        else {
       //     this.BTactic = chooseTactic  ("_0_1","1_0_");            
        //    this.BTactic.developTactic();
           // return this.BTactic.getNextMoves();
        }                
         }
    }
    private boolean isInterceptDraw (Teams t){
        return this.startGw.blackIspaceDist <= this.startGw.whitePspaceDist;
    } 
    
    private boolean isProtectDraw (Teams t){
        return this.startGw.blackIspaceDist <= this.startGw.whitePspaceDist;
    } 
    
    private boolean wSufficientConditionMet(){

        if (this.WTactic == null)
            return false;
        else
            return this.WTactic.possible();
    }
    
    private boolean bSufficientConditionMet(){
       
        if (this.BTactic == null)
            return false;
        else
            return this.BTactic.notPossible();            
     }         
}
