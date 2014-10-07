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
public class BoardZones extends Zones{
    
    private ArrayList <Tree <Zones.Trajectory>> bTree, wTree;
    private Gateways startGW;
    
    BoardZones(Board2D board ){
   
        super (board, board.getPieceFromName("B-Bomber"), 
        board.getPieceFromName("W-Target"));
        GenerateZones();
        this.bTree= getZonesTree();
        
        Zones whiteZone =new Zones (board,board.getPieceFromName("W-Bomber"),
            board.getPieceFromName("B-Target"));
        whiteZone.GenerateZones();
        this.wTree= whiteZone.getZonesTree();
        
        this.startGW = new Gateways (this.board, this.bTree.get(0), this.wTree.get(0));
        
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
