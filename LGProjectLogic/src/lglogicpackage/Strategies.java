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
    Zones whiteZoneStart, BlackZoneStart;
    String startBlackZoneType, startWhiteZoneType, desiredBlackZoneType, 
            desiredWhiteZoneType ;
    Tree<Moves> moves;
    
    Strategies (Board2D board){
        this.board = board;
        this.moves = new Tree<> ();
        this.moves.setRoot(new Node (new Moves(this.board.getPieceFromName("W-Fighter"))));
        
        this.BlackZoneStart= new Zones (this.board,this.board.getPieceFromName("B-Bomber"),
                board.getPieceFromName("W-Target"));
        this.BlackZoneStart.GenerateZones();
        
        this.whiteZoneStart= new Zones (this.board,this.board.getPieceFromName("W-Bomber"),
                board.getPieceFromName("B-Target"));
        this.whiteZoneStart.GenerateZones();

        ArrayList <Tree <Zones.Trajectory>> wTree= this.whiteZoneStart.getZonesTree();
        ArrayList <Tree <Zones.Trajectory>> bTree= this.BlackZoneStart.getZonesTree();
        
        this.startBlackZoneType = Zones.getZoneType(bTree.get(0));
        this.startWhiteZoneType = Zones.getZoneType(wTree.get(0));
        
        this.startGw = new Gateways (this.board, bTree.get(0), wTree.get(0));
    }
   
    public Tactics chooseTactic (String desiredBlack, String desiredWhite){
        Tactics tac;
        this.desiredBlackZoneType= desiredBlack;
        this.desiredWhiteZoneType = desiredWhite;
        if (this.desiredBlackZoneType.equals(this.startBlackZoneType)&&
                this.desiredWhiteZoneType.equals(this.startWhiteZoneType)){
            System.out.println("Chose keep both");
            return tac = null;
                    //new keepBoth ();
           
        }
        else 
        // either white or black are not desired 
        if (this.desiredBlackZoneType.equals(this.startBlackZoneType) ||
                    this.desiredWhiteZoneType.equals(this.startWhiteZoneType)) {
           // changeOne (); 
            System.out.println("Chose change one");
            return tac= null;
            
        }
        else{
            // both different
            System.out.println("Chose change both");
            return tac = new BothStatesChange(this);   
        }    
        
    }
    
    
    
    private void keepBoth (){
        
    }
    
    private void changeOne (){
        
    }
    
   
   
}
