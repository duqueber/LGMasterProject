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
    Tree<Moves> moves = new Tree<>();
    ArrayList <Tree <Zones.Trajectory>> bTree, wTree;
    public static ArrayList<Integer> sdWhiteInt = new ArrayList<>();
    public static ArrayList<Integer>  sdBlackInt = new ArrayList<>();
    public static ArrayList<Integer>  sdWhitePro = new ArrayList<>();
    public static ArrayList<Integer>  sdBlackPro = new ArrayList<>();
    
    public enum Teams {WHITE, BLACK};
    public enum Types {PROTECT, INTERCEPT};
    
    Strategies (Board2D board){
        this.board = new Board2D( board);
        this.moves.setRoot(new Node (new Moves(this.board.getPieceFromName("W-Fighter"))));
    }
   
    public Tactics chooseTactic (String desiredBlack, String desiredWhite){
        Tactics tac;
        this.desiredBlackZoneType= desiredBlack;
        this.desiredWhiteZoneType = desiredWhite;
        
        setZones ();
        
        if (this.startGw.whiteIspaceDist != 0)
            sdWhiteInt.add(this.startGw.whiteIspaceDist);
        if (this.startGw.blackIspaceDist != 0)
            sdBlackInt.add(this.startGw.blackIspaceDist);
        if (this.startGw.whitePspaceDist != 0)
            sdWhitePro.add(this.startGw.whitePspaceDist);
        if (this.startGw.blackPspaceDist !=0)
            sdBlackPro.add (this.startGw.blackPspaceDist);
        
        if (this.desiredBlackZoneType.equals(this.startBlackZoneType)&&
                this.desiredWhiteZoneType.equals(this.startWhiteZoneType)){
            System.out.println("Chose keep both");
            return tac = new KeepBothStates(this);
        }
        else {
            
            ZoneTypes zt = new ZoneTypes (this.startBlackZoneType, this.startWhiteZoneType);
            // either white or black are not desired and the start state is a win. The team
            // can change either of the states
            if (zt.isBlackWin() || zt.isWhiteWin()){ 
                System.out.println("Chose change either");
                if (this.desiredBlackZoneType.equals(this.startBlackZoneType)) 
                    return tac= new ChangeEitherState (this, this.desiredWhiteZoneType);
                if (this.desiredWhiteZoneType.equals(this.startWhiteZoneType)) 
                    return tac= new ChangeEitherState (this, this.desiredBlackZoneType);
            }
        }    
       
        // else both different
        System.out.println("Chose change both");
        return tac = new BothStatesChange(this);   
            
     }    
   
    public void setZones (){
        BoardZones bz = new BoardZones (this.board);
        this.bTree = bz.getBlackZones();
        this.wTree = bz.getWhiteZones();
        this.startGw = bz.getGateways();
        
        this.startBlackZoneType = Zones.getZoneType(bTree.get(0));
        this.startWhiteZoneType = Zones.getZoneType(wTree.get(0));
    }
    
    boolean makeStrategyMove (Moves m){
            return this.board.makeMove(m);
    }
    
    public Tree<Moves> getTree (){
        return this.moves;
    }

    public static void restartSd (){
        sdWhiteInt = new ArrayList<>();
        sdBlackInt = new ArrayList<>();
        sdWhitePro = new ArrayList<>();
        sdBlackPro = new ArrayList<>();
    }
    
    private void keepBoth (){
        
    }
    
    private void changeOne (){
        
    }
    
   
   
}
