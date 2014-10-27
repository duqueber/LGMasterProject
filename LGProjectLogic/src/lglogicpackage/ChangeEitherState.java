/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lglogicpackage;

import java.util.ArrayList;
import lggrammars.ShortestTrajectory;
import supportpackage.Coordinates;
import supportpackage.Moves;
import supportpackage.Node;

/**
 *
 * @author nati
 */
public class ChangeEitherState implements Tactics {
    
    private Strategies strategy;
    private String desiredChange;
    private PiecesLogic fighter;
    private ArrayList<Coordinates> GWpoints;
    private ArrayList<Node<Moves>> nextSteps = new ArrayList<>();
    private ArrayList<Integer> attackSd, protectSd; 
    private boolean sumSdMantained;
    private boolean sumSdShrank;
    
    ChangeEitherState (Strategies strategy, String desiredChange){
        this.desiredChange = desiredChange;
        this.strategy = strategy;
        this.attackSd = new ArrayList<>();
        this.protectSd = new ArrayList<>();
        this.sumSdMantained = false;
        this.sumSdShrank = false;
    }

     ChangeEitherState (ChangeEitherState c){
        this.desiredChange = c.desiredChange;
        this.strategy = c.strategy;
        this.attackSd = c.attackSd;
        this.protectSd = c.protectSd;
        this.sumSdMantained = c.sumSdMantained;
        this.sumSdShrank = c.sumSdShrank;
    }
    //WhiteIntercept, BlackIntercept, WhiteProtect, Black Protect
    @Override
    public void developTactic() {
        switch (this.desiredChange){
            case "0_1_": // change to white protect
                this.fighter = this.strategy.board.getPieceFromName("W-Fighter");
                this.GWpoints = this.strategy.startGw.getWhiteGatewaysProtect();
                this.protectSd = Strategies.sdWhitePro;
                this.attackSd = Strategies.sdBlackInt;
                break;
            case "1_0_": // change to white intercept
                this.fighter = this.strategy.board.getPieceFromName("B-Fighter");
                this.GWpoints = this.strategy.startGw.getWhiteGatewaysIntercept();
                this.protectSd = Strategies.sdBlackPro;
                this.attackSd = Strategies.sdWhiteInt;
                break;    
            case "_0_1": // change to black protect
                this.fighter = this.strategy.board.getPieceFromName("B-Fighter");
                this.GWpoints = this.strategy.startGw.getBlackGatewaysProtect();
                this.protectSd = Strategies.sdBlackPro;
                this.attackSd = Strategies.sdWhiteInt;
                break;       
            default:
                case "_1_0": // change to black intercept
                this.fighter = this.strategy.board.getPieceFromName("W-Fighter");
                this.GWpoints = this.strategy.startGw.getBlackGatewaysIntercept();
                this.protectSd = Strategies.sdWhitePro;
                this.attackSd = Strategies.sdBlackInt;
                break;    
        }
        int oldSumSd, newSumSd,attackSize, protectSize;
        oldSumSd= 0;   
        newSumSd = 0;   
        attackSize = this.attackSd.size();
        protectSize = this.protectSd.size();
                       
        if (attackSize > 2 && protectSize > 2){
            oldSumSd= this.attackSd.get(attackSize-3)+ this.protectSd.get(protectSize-3); 
            newSumSd= this.attackSd.get(attackSize-1)+ this.protectSd.get(protectSize -1); 
        }
        
        
        if (oldSumSd == 0 && newSumSd == 0)
                calculateNextMoves ();
        else
        if (oldSumSd <= newSumSd) {       
            this.sumSdMantained = true;
            calculateNextMoves ();   
        }        
        else {
            this.sumSdShrank = true;
            calculateNextMoves (); 
        }    
        
    }

    @Override
    public void calculateNextMoves() {
        ArrayList<ArrayList<Node<Coordinates>>> sts = new ArrayList<>();
        ShortestTrajectory stObj;
        ArrayList<Coordinates> temp =  new ArrayList <>();
        
        for (Coordinates gwCoor: this.GWpoints){
            stObj = new ShortestTrajectory (strategy.board, this.fighter, gwCoor);
            stObj.GenerateShortestTrajectory();
            sts = stObj.getShortestTrajectories();
            
            for (ArrayList<Node<Coordinates>>  st: sts)
                if (!Gateways.IsInArray(temp, st.get(1).getData())){
                    // test
                    System.out.println ("added to moves " );
                    st.get(1).getData().PrintCoor();
                    //test
                    temp.add(st.get(1).getData());
            }            
        }
        for (Coordinates c: temp){
            if (validNextMove (new Moves (this.fighter, c)))
                this.nextSteps.add (new Node<> (new Moves (this.fighter, c)));
        }    
    }
    
    private boolean validNextMove ( Moves move){
        
        Board2D boardTemp;
        BoardZones bz;
        boardTemp=new Board2D (this.strategy.board);
        boardTemp.makeMove (move);

        bz = new BoardZones (boardTemp, null);

        int currentAttackSd, currentProSd;

        if (this.fighter.NAME =="B-Fighter"){    
                currentAttackSd = bz.getGateways().whiteIspaceDist;
                currentProSd = bz.getGateways().blackPspaceDist;
        }    
        else {
                currentAttackSd = bz.getGateways().blackIspaceDist;
                currentProSd = bz.getGateways().whitePspaceDist;
        }
        
        boolean mixedStrategy = false;
        int attackSize = this.attackSd.size();
        int protectSize = this.protectSd.size();

        mixedStrategy = currentAttackSd < this.attackSd.get (attackSize-1)&& 
            currentProSd < this.protectSd.get (protectSize-1);

        if (strategy instanceof DrawIntercept || strategy instanceof DrawProtect) 
            return !mixedStrategy;
               
        if (strategy instanceof MixedDraw)         
            return mixedStrategy;
        else 
            return true;
    }    
    

    @Override
    public ArrayList<Node<Moves>> getNextMoves() {
        return this.nextSteps;
    }

    @Override
    public boolean possible() {
        return this.sumSdShrank;
    }

    @Override
    public boolean notPossible() {

        return this.sumSdMantained;
    }
    
}
