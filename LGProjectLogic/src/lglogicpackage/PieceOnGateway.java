/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lglogicpackage;

import java.util.ArrayList;
import java.util.List;
import lggrammars.Zones;
import supportpackage.Coordinates;
import supportpackage.Moves;
import supportpackage.Node;
import supportpackage.Tree;

/**
 *
 * @author nati
 */
public class PieceOnGateway implements Tactics{

    private Strategies strategy;
    private ArrayList<Node<Moves>> nextSteps;
    private Strategies.Types typeGW;
    private Strategies.Teams team;
    private Tree<Zones.Trajectory> attackZone, protectZone;

    
    public PieceOnGateway (Strategies strategy,  Strategies.Types type, Strategies.Teams team){
        this.strategy = strategy;
        this.team = team;
        this.typeGW = type;
        this.attackZone = null;
        this.protectZone = null;
        this.nextSteps = new ArrayList <> ();
    }
    
    @Override
    public void developTactic() {
        
        if (team.equals(Strategies.Teams.WHITE)){
            if (this.typeGW.equals(Strategies.Types.INTERCEPT))
                this.attackZone = this.strategy.bTree.get(0);
            else
                this.protectZone = this.strategy.wTree.get(0);
        }               
        else{
            if (this.typeGW.equals(Strategies.Types.INTERCEPT))
                this.attackZone = this.strategy.wTree.get(0);
            else
                this.protectZone = this.strategy.bTree.get(0);
        }    
        calculateNextMoves();
    }

    @Override
    public void calculateNextMoves() {
        
        Coordinates step;
        PiecesLogic piece;
        if (this.attackZone != null){
            step = this.attackZone.getRoot().getData().getShortestPath().get(1).getData();
            piece =  this.strategy.board.getPieceFromName(this.attackZone.getRoot().getData().getPieceName());
            this.nextSteps.add(new Node(new Moves(piece, step)));
        }
        else{
            List<Node<Zones.Trajectory>> childrenRoot = this.attackZone.getRoot().getChildren();

            if (!childrenRoot.isEmpty())
                for (Node<Zones.Trajectory> firstNeg: childrenRoot )
                    if (!firstNeg.hasChildren()){
                        step= firstNeg.getData().getShortestPath().get(1).getData();
                        piece =this.strategy.board.getPieceFromName(firstNeg.getData().getPieceName());
                        if (!Gateways.inArrayNoFirst(this.attackZone.getRoot().getData().getShortestPath(), step))
                            this.nextSteps.add(new Node (new Moves (piece, step)));
                    }
            
        }
    }    
    @Override
    public ArrayList<Node<Moves>> getNextMoves() {
        return this.nextSteps;
    } 

    @Override
    public boolean possible() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean notPossible() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
