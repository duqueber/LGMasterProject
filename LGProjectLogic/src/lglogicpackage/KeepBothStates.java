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
    private int loserProtectDist, loserInterceptDist;
    private boolean protectShrink, attackShrinks;
    
    KeepBothStates (Strategies strategy){
        this.strategy = strategy;
        this.nextSteps = new ArrayList<>();
        this.attackShrinks = false;
        this.protectShrink = false;
    }
    
    @Override
    public void developTactic() {

        ZoneTypes type = new ZoneTypes (strategy.startBlackZoneType, strategy.startWhiteZoneType);
        if (type.isBlackWin()){
            this.fighter = strategy.board.getPieceFromName("B-Fighter");
            this.bomber = strategy.board.getPieceFromName("B-Bomber");
            this.attackZone = strategy.wTree.get(0);
            this.protectZone = strategy.bTree.get(0);
        }    
        else
        if (type.isWhiteWin()){
            this.fighter = strategy.board.getPieceFromName("W-Fighter");
            this.bomber = strategy.board.getPieceFromName("W-Bomber");
            this.attackZone = strategy.bTree.get(0);
            this.protectZone = strategy.wTree.get(0);
        }    
        
        this.loserProtectDist = this.attackZone.getRoot().getChildren().get(0).
                getData().getShortestPath().size()-1;              
        this.loserInterceptDist= this.protectZone.getRoot().getData().getLen ();
        
       calculateNextMoves();
    }

    @Override
    public void calculateNextMoves() {
        
        //there exists only ONE step with an open trajectory for the bomber.
        Coordinates protectStep = this.protectZone.getRoot().getData().getShortestPath().get(1).getData();
        this.nextSteps.add(new Node (new Moves (this.bomber, protectStep)));
        
        // there exists at least one step with open trajectory for the fighter but I need 
        // to look for it. There might be steps without open trajectory
        List<Node<Trajectory>> childrenRoot = this.attackZone.getRoot().getChildren();
        Coordinates attackStep;
        
        if (!childrenRoot.isEmpty()){
            for (Node<Trajectory> firstNeg: childrenRoot )
                if (!firstNeg.hasChildren()){
                    attackStep= firstNeg.getData().getShortestPath().get(1).getData();
                    this.nextSteps.add(new Node (new Moves (this.fighter, attackStep)));
                }            
        }
 
    }

    void simulateNextMoves (){
        Board2D boardTemp;
        Moves move;
        BoardZones bz;
        int newLoserProDist, newLoserIntDist;
        newLoserProDist = 999;
        newLoserIntDist = 999;
        
        
        for (Node<Moves> nMove: this.nextSteps){
            boardTemp=new Board2D (this.strategy.board);
            move = nMove.getData();

            if ((move.getPiece().NAME.equals("W-Bomber") ||
                    move.getPiece().NAME.equals("B-Bomber")) && 
                    newLoserIntDist > this.loserInterceptDist){
                boardTemp.makeMove (move);
                bz = new BoardZones (boardTemp);
                if (move.getPiece().NAME.equals("W-Bomber")) 
                    newLoserProDist = bz.getWhiteZones().get(0).getRoot().getData().getLen ();
                else 
                    newLoserIntDist = bz.getBlackZones().get(0).getRoot().getData().getLen ();
            }    
            else{
                if ((move.getPiece().NAME.equals("W-Fighter") ||
                    move.getPiece().NAME.equals("B-Fighter")) && 
                    newLoserProDist > this.loserProtectDist){
                    boardTemp.makeMove (move);
                    bz = new BoardZones (boardTemp);
                        if (move.getPiece().NAME.equals("W-Fighter")) 
                            newLoserProDist = bz.getBlackZones().get(0).getRoot().getChildren().
                            get(0).getData().getShortestPath().size()-1; 
                        else 
                            newLoserProDist = bz.getWhiteZones().get(0).getRoot().getChildren().
                            get(0).getData().getShortestPath().size()-1; 
                }         
            }
        }
        
     
        if (newLoserProDist< this.loserProtectDist)
            this.protectShrink = true;
        if (newLoserIntDist < this.loserInterceptDist)
            this.attackShrinks = true;
    }
    
    @Override
    public ArrayList<Node<Moves>> getNextMoves() {
        return this.nextSteps;
    }

    @Override
    public boolean possible() {
        return (this.attackShrinks && this.protectShrink);
    }

    @Override
    public boolean notPossible() {
        return !possible();
    }

    
}
