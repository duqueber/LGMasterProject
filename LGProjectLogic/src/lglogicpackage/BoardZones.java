/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lglogicpackage;

import java.util.ArrayList;
import lggrammars.Zones;
import supportpackage.Tree;

/**
 *
 * @author nati
 */
public class BoardZones {
    
    private ArrayList <Tree <Zones.Trajectory>> bTree, wTree;
    private Gateways startGW;
    
    BoardZones(Board2D board, Strategies.Teams team ){
        Tree <Zones.Trajectory> mainBTraj, mainWTraj;
        
        if (board.getPieceFromName("B-Bomber")!=null && 
                board.getPieceFromName("W-Target")!= null){
            Zones BlackZone = new Zones (board, board.getPieceFromName("B-Bomber"), 
            board.getPieceFromName("W-Target"));
            BlackZone.GenerateZones();
            this.bTree= BlackZone.getZonesTree();
            mainBTraj = this.bTree.get(0);
        }
        else{
            this.bTree = null;
            mainBTraj = null;
        }
        
        if (board.getPieceFromName("W-Bomber")!=null && 
                board.getPieceFromName("B-Target")!= null){
            Zones whiteZone =new Zones (board,board.getPieceFromName("W-Bomber"),
            board.getPieceFromName("B-Target"));
            whiteZone.GenerateZones();
            this.wTree= whiteZone.getZonesTree();
            mainWTraj = this.wTree.get(0);
        }
        else {
            this.wTree = null;
            mainWTraj = null;
        }
        this.startGW = new Gateways (board, mainBTraj, mainWTraj, team);
        
    }
    
    public ArrayList <Tree <Zones.Trajectory>> getBlackZones (){
        return this.bTree;
    }
    
    public ArrayList <Tree <Zones.Trajectory>> getWhiteZones (){
        return this.wTree;
    }
    
    public Gateways getGateways (){
        return this.startGW;
    }
    
}
