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
        MoveStruct m = new MoveStruct (this.moves.getRoot(), this.board);
        createTree  (m);
    }
    
    public void createTree(MoveStruct m ){
        
        this.board = new Board2D (m.getBoard());
        
        if (sufficientConditionMet (m.getMove())){
            return;
        }
        
        if (!m.getMove().isRoot())
            makeStrategyMove (m.getMove().getData());
            
        this.nextSteps = getPreferedSteps(generateNextSteps (m.getMove()));    

        if (!this.nextSteps.isEmpty())
          //  if ((!wSufficientConditionMet () && !bSufficientConditionMet()))
                m.getMove().setChildren(this.nextSteps);                    

        ArrayList<MoveStruct> arrayS = new ArrayList <>();
        for (Node<Moves> step: this.nextSteps) { 
            MoveStruct s = new MoveStruct (step, this.board);
            arrayS.add(s);
        }    
        
        for (MoveStruct mS : arrayS)
            createTree (mS);
    }
    //WhiteIntercept, BlackIntercept, WhiteProtect, Black Protect
    private ArrayList<Node<Moves>> generateNextSteps (Node<Moves> m){
  
        
        MixedDraw WTactic;
        BlackWins BTactic;
        ArrayList<Coordinates> intercept= new ArrayList<>();
        ArrayList<Coordinates> protect= new ArrayList<> ();
        ArrayList<Node<Moves>> temp;
        boolean inAttackGW, inProtectGW;
        
        inAttackGW=false;
        inProtectGW = false;
         
        if (this.startGw !=null){
            intercept = this.startGw.getBlackGatewaysIntercept();
            for (Coordinates i: intercept)
                if (i.equals(m.getData().getPiece().getCoordinates()))
                    inAttackGW = true;
   
           /* protect = this.startGw.getWhiteGatewaysProtect();
            for (Coordinates a: protect){
                if (a.equals(m.getData().getPiece().getCoordinates()))
                    inProtectGW = true;
            }*/
        }
        
        this.setZones();
        
        /*if (inAttackGW || inProtectGW)
            if (m.getData().getPiece().getTeam()== 1)
                return stepsGW(inAttackGW, inProtectGW, Strategies.Teams.WHITE);
            else 
                return stepsGW(inAttackGW, inProtectGW, Strategies.Teams.BLACK);
        */
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
            BTactic = new BlackWins (this.board, inAttackGW, Teams.BLACK);            
            temp = BTactic.generateNextSteps(m);
            if (!temp.isEmpty())
                return temp;
            else
                return drawArray (Teams.BLACK, m);
        }                   
    }

    private  ArrayList<Node<Moves>> stepsGW (boolean attack, boolean protect, 
            Strategies.Teams team){
        
        PieceOnGateway gwTactic;
        ArrayList<Node<Moves>> temp = new ArrayList<>(); 
        if (attack){
            gwTactic = new PieceOnGateway (this, Strategies.Types.INTERCEPT,
                        team);
            gwTactic.developTactic();
            temp.addAll(gwTactic.getNextMoves());
        }    
        if (protect){
            gwTactic = new PieceOnGateway (this, Strategies.Types.PROTECT,
                        team);
            gwTactic.developTactic();
            temp.addAll(gwTactic.getNextMoves());
        }
        return temp;    
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
                if (i.equals(move.getPiece().getCoordinates()))
                    return true;
            
            protect = this.startGw.getWhiteGatewaysProtect();
            for (Coordinates i : protect){
                if (i.equals(move.getStep()))
                    return true;                    
            }
        }    
       return false; 
    }
    
    private class MoveStruct {
        
        private Node <Moves> move;
        private Board2D board;
        
        MoveStruct (Node <Moves> move, Board2D board){
            this.move = move;
            this.board = board;
        }
        
        final Node <Moves> getMove (){
            return this.move;
        } 
        
        final Board2D getBoard (){
            return this.board;
        }
    }
}
