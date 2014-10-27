/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lglogicpackage;

import java.util.ArrayList;
import java.util.List;
import lggrammars.Zones.Trajectory;
import supportpackage.Coordinates;
import supportpackage.Moves;
import supportpackage.Node;
import supportpackage.Tree;
import supportpackage.ZoneTypes;

/**
 *
 * @author nati
 */
public class KeepBothStates implements Tactics{

    private Strategies strategy;
    private ArrayList<Node<Moves>> nextSteps;
    private PiecesLogic fighter, bomber;
    private Tree<Trajectory> attackZone, protectZone;
    private boolean protectShrink, attackShrinks;
    private ArrayList<Integer> attackSd, protectSd; 
    
    KeepBothStates (Strategies strategy){
        this.strategy = strategy;
        this.nextSteps = new ArrayList<>();
        this.attackSd = new ArrayList <>();
        this.protectSd = new ArrayList<>();
        this.attackShrinks = false;
        this.protectShrink = false;
    }
    
    @Override
    public void developTactic() {

        ZoneTypes type = new ZoneTypes (strategy.startBlackZoneType, strategy.startWhiteZoneType);
        if (type.isBlackWin()){
            this.fighter = this.strategy.board.getPieceFromName("B-Fighter");
            this.bomber = this.strategy.board.getPieceFromName("B-Bomber");
            this.attackZone = this.strategy.wTree.get(0);
            this.protectZone = this.strategy.bTree.get(0);
            this.attackSd = Strategies.sdBlackInt;
            this.protectSd = Strategies.sdWhitePro;
        }    
        else
        if (type.isWhiteWin()){
            this.fighter = this.strategy.board.getPieceFromName("W-Fighter");
            this.bomber = this.strategy.board.getPieceFromName("W-Bomber");
            this.attackZone = this.strategy.bTree.get(0);
            this.protectZone = this.strategy.wTree.get(0);
            this.attackSd = Strategies.sdWhiteInt;
            this.protectSd = Strategies.sdBlackPro;
        }    
        
       calculateNextMoves();
    }

    @Override
    public void calculateNextMoves() {
        
        Coordinates protectStep;
        boolean protectSdShrank, attackSdShrank;
        protectSdShrank = false;
        attackSdShrank = false;
                
        if (this.protectSd.size() > 1)
            protectSdShrank = this.protectSd.get(this.protectSd.size()-1) < 
                    this.protectSd.get(this.protectSd.size()-2); 
        
        if (this.attackSd.size() > 1)
            attackSdShrank = this.attackSd.get(this.attackSd.size()-1) < 
                    this.attackSd.get(this.attackSd.size()-2); 
        
        //there exists only ONE step with an open trajectory for the bomber.
        if (this.protectSd.size()==1 || attackSdShrank ){
            protectStep = this.protectZone.getRoot().getData().getShortestPath().get(1).getData();
            this.nextSteps.add(new Node (new Moves (this.bomber, protectStep)));
        }
        
        // there exists at least one step with open trajectory for the fighter but I need 
        // to look for it. There might be steps without open trajectory
        List<Node<Trajectory>> childrenRoot = this.attackZone.getRoot().getChildren();
        Coordinates attackStep;
        if (this.attackSd.size()== 1 || protectSdShrank){

            if (!childrenRoot.isEmpty()){
                for (Node<Trajectory> firstNeg: childrenRoot )
                    if (!firstNeg.hasChildren()){
                        attackStep= firstNeg.getData().getShortestPath().get(1).getData();
                        if (!Gateways.inArrayNoFirst(this.attackZone.getRoot().getData().getShortestPath(), attackStep))
                            this.nextSteps.add(new Node (new Moves (this.fighter, attackStep)));
                    }            
            }
        }
        validateNextMoves ();
 
    }

    void validateNextMoves (){
        Board2D boardTemp;
        Moves move;
        BoardZones bz;
        boardTemp=new Board2D (this.strategy.board);
        for (Node<Moves> nMove: this.nextSteps){
            move = nMove.getData();
            boardTemp.makeMove (move);
        }
        
        bz = new BoardZones (boardTemp, null);
            
        int currentAttackSd, currentProSd;

        if (this.bomber.NAME =="B-Bomber"){    
                currentAttackSd = bz.getGateways().blackIspaceDist;
                currentProSd = bz.getGateways().whitePspaceDist;
        }    
        else {
                currentAttackSd = bz.getGateways().whiteIspaceDist;
                currentProSd = bz.getGateways().blackPspaceDist;
        }
        
        int oldAttackSd, oldProtectSd;
        if (this.attackSd.size() == 1)
            oldAttackSd= this.attackSd.get(0)+1;
        else
            oldAttackSd = this.attackSd.get(this.attackSd.size()-2);
        
        if (this.protectSd.size() == 1)
            oldProtectSd= this.protectSd.get(0)+1;
        else
            oldProtectSd = this.protectSd.get(this.protectSd.size()-2);
        
        if (currentAttackSd == oldAttackSd)
            this.attackShrinks = true;
        if (currentProSd == oldProtectSd)
            this.protectShrink = true;
    }
    
    @Override
    public ArrayList<Node<Moves>> getNextMoves() {
        return this.nextSteps;
    }

    @Override
    public boolean possible() {
        //Take into account when start state is win. Both gateways types need to shrink
        // if bomber is B-bomber Is black win
        return (this.attackShrinks && this.protectShrink);
    }

    @Override
    public boolean notPossible() {
        return false;
    }

    
}
