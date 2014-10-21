/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lglogicpackage;

import java.util.ArrayList;
import supportpackage.Coordinates;
import supportpackage.Moves;
import supportpackage.Node;

/**
 *
 * @author nati
 */
public class BWinWMixed extends Strategies{
    
        
    ArrayList<Node<Moves>> nextSteps;
    Strategies.Teams teamName;

    BWinWMixed (Board2D board, Strategies.Teams teamName){
        super (board);
        this.nextSteps = new ArrayList<>();
        this.teamName = teamName;
    }
    public void createTree (){
        createTree  (this.moves.getRoot());
    }
    
    public void createTree(Node<Moves> m ){
        
        if (sufficientConditionMet (m)){
            return;
        }
        if (!m.isRoot())
            makeStrategyMove (m.getData());
            
        this.nextSteps = getPreferedSteps(generateNextSteps (m));    

        if (!this.nextSteps.isEmpty())
          //  if ((!wSufficientConditionMet () && !bSufficientConditionMet()))
                m.setChildren(this.nextSteps);                    

        for (Node<Moves> step: this.nextSteps)       
            createTree (step);
    }
    //WhiteIntercept, BlackIntercept, WhiteProtect, Black Protect
    private ArrayList<Node<Moves>> generateNextSteps (Node<Moves> m){
        
        this.setZones();
        
        MixedDraw WTactic;
        BlackWins BTactic;
        
        ArrayList<Node<Moves>> temp;
        
        if (m.getData().getPiece().getTeam() == 2 || m.isRoot() ){
            WTactic = new MixedDraw (this.board, Teams.WHITE);// choose intercept or protect.
            //both will return a mixed strategy if it exists.
            temp =WTactic.generateNextSteps(m);
            if (!temp.isEmpty()) // a mixed strategy does not exist.
                return temp;
            else
                return drawArray (Teams.WHITE, m);              
        }
        
        else {
            BTactic = new BlackWins (this.board);            
            temp = BTactic.generateNextSteps(m);
            if (!temp.isEmpty())
                return temp;
            else
                return drawArray (Teams.BLACK, m);
        }                   
    }

    private ArrayList<Node<Moves>> drawArray (Teams team, Node<Moves> m){
        
        ArrayList<Node<Moves>> temp;
        DrawIntercept tacticII;
        DrawProtect tacticIII;
        tacticII = new DrawIntercept (this.board, team); 
        temp= tacticII.generateNextSteps(m);
                
        tacticIII = new DrawProtect (this.board, team);
        temp.addAll(tacticIII.generateNextSteps(m));
                
        return temp;
        
    }
    
    private ArrayList<Node<Moves>> getPreferedSteps (ArrayList<Node<Moves>> mArray){
     
        ArrayList<Node<Moves>> temp = new ArrayList<>();
        ArrayList <Coordinates> preferred;
        preferred =  this.startGw.getprefered();
        
        if (preferred.isEmpty())
            return mArray;
        for (Coordinates c : preferred)
            for (Node<Moves> node: mArray)
                if (node.getData().getStep().equals(c))
                    temp.add(node);
        if (temp.isEmpty())
            return mArray;
        else
            return temp;
    }
    private boolean sufficientConditionMet(Node<Moves> m){
        
        if (this.startGw == null)
            return false;
        
        ArrayList<Coordinates> intercept= new ArrayList<>();
        ArrayList<Coordinates> protect= new ArrayList<> ();
        Moves move = m.getData();
        if (move.getPiece().getTeam()== 2){
            if (this.board.hasPiece(move.getStep()))
                return true;
        }
        else{
            intercept = this.startGw.getBlackGatewaysIntercept();
            for (Coordinates i : intercept)
                if (i.equals(move.getStep()))
                    return true;
            
            protect = this.startGw.getWhiteGatewaysProtect();
            for (Coordinates i : protect){
                if (i.equals(move.getStep()))
                    return true;                    
            }
        }    
       return false; 
    }  
}
