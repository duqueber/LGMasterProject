/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lglogicpackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lggrammars.ShortestTrajectory;
import lggrammars.Zones;
import supportpackage.Coordinates;
import supportpackage.Moves;
import supportpackage.Node;
import supportpackage.Tree;
import supportpackage.ZoneTypes;

/**
 *
 * @author nati
 */
public abstract class Strategies {
    Board2D board;
    Gateways startGw;
    String startBlackZoneType, startWhiteZoneType, desiredBlackZoneType, 
            desiredWhiteZoneType ;
    static Tree<Moves> moves = new Tree<>();
    ArrayList <Tree <Zones.Trajectory>> bTree, wTree;
    
    public enum Teams {WHITE, BLACK};
    public enum Types {PROTECT, INTERCEPT};
    
    Strategies (Board2D board){
        this.board = board;
        this.moves.setRoot(new Node (new Moves(this.board.getPieceFromName("W-Fighter"))));
    }
   
    public Tactics chooseTactic (String desiredBlack, String desiredWhite){
        Tactics tac;
        this.desiredBlackZoneType= desiredBlack;
        this.desiredWhiteZoneType = desiredWhite;
        
        BoardZones bz = new BoardZones (this.board);
        this.bTree = bz.getBlackZones();
        this.wTree = bz.getWhiteZones();
        this.startGw = bz.getGateways();
        
        this.startBlackZoneType = Zones.getZoneType(bTree.get(0));
        this.startWhiteZoneType = Zones.getZoneType(wTree.get(0));
        
        
        if (this.desiredBlackZoneType.equals(this.startBlackZoneType)&&
                this.desiredWhiteZoneType.equals(this.startWhiteZoneType)){
            System.out.println("Chose keep both");
            return tac = new KeepBothStates(this);
                    //new keepBoth ();
           
        }
        else 
        // either white or black are not desired 
        if (this.desiredBlackZoneType.equals(this.startBlackZoneType))
            return tac= new ChangeOneState (this, this.desiredWhiteZoneType);
        else 
        if (this.desiredWhiteZoneType.equals(this.startWhiteZoneType)) 
            return tac= new ChangeOneState (this, this.desiredBlackZoneType);
        
        else{
            // both different
            System.out.println("Chose change both");
            return tac = new BothStatesChange(this);   
        }    
     }    
   
    boolean makeStrategyMove (Moves m){
            return this.board.makeMove(m);
    }

    
    private void keepBoth (){
        
    }
    
    private void changeOne (){
        
    }
    
   
   
}
