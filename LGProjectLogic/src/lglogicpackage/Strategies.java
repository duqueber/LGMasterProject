/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lglogicpackage;

import java.util.ArrayList;
import lggrammars.ShortestTrajectory;
import lggrammars.Zones;
import supportpackage.Coordinates;
import supportpackage.Node;
import supportpackage.Tree;
import supportpackage.ZoneTypes;

/**
 *
 * @author nati
 */
public abstract class Strategies {
    private Board2D board;
    private Gateways startGw;
    private Zones whiteZoneStart, BlackZoneStart;
    private String startBlackZoneType, startWhiteZoneType, desiredBlackZoneType, 
            desiredWhiteZoneType ;
    
    Strategies (Board2D board){
        this.board = board;   
      
        this.BlackZoneStart= new Zones (this.board,board.getPieceFromName("B-Bomber"),
                board.getPieceFromName("W-Target"));
        this.BlackZoneStart.GenerateZones();
        
        this.whiteZoneStart= new Zones (this.board,board.getPieceFromName("W-Bomber"),
                board.getPieceFromName("B-Target"));
        this.whiteZoneStart.GenerateZones();

        ArrayList <Tree <Zones.Trajectory>> wTree= this.whiteZoneStart.getZonesTree();
        ArrayList <Tree <Zones.Trajectory>> bTree= this.BlackZoneStart.getZonesTree();
        
        this.startBlackZoneType = Zones.getZoneType(bTree.get(0));
        this.startWhiteZoneType = Zones.getZoneType(wTree.get(0));
        
        Gateways gw = new Gateways (this.board, bTree.get(0), wTree.get(0));

    }
   
    public void chooseMethod (String desiredBlack, String desiredWhite){
        this.desiredBlackZoneType= desiredBlack;
        this.desiredWhiteZoneType = desiredWhite;
        if (this.desiredBlackZoneType.equals(this.startBlackZoneType)&&
                this.desiredWhiteZoneType.equals(this.startWhiteZoneType))
            keepBoth ();
        else 
        // either white or black are not desired 
        if (this.desiredBlackZoneType.equals(this.startBlackZoneType) ||
                    desiredWhite.equals(this.desiredWhiteZoneType)) 
            changeOne ();        
        else
            // both different
            changeBoth();       
    }
    
    private void changeBoth (){
        PiecesLogic fighter;
        ArrayList<ArrayList <Node<Coordinates>>> gwZonesProtect, gwZonesIntercept;
        gwZonesProtect = new ArrayList<> ();
        gwZonesIntercept = new ArrayList<>();
        ArrayList <Coordinates>  gwPointPro, gwPointInt;
        gwPointPro = new ArrayList<>();
        gwPointInt = new ArrayList<>();
        
        ZoneTypes type = new ZoneTypes (this.startBlackZoneType, this.startWhiteZoneType);
        if (type.isBlackWin()){
            fighter = this.board.getPieceFromName("W-Fighter");
            gwZonesProtect = this.startGw.generateGatewaysZones(Gateways.Teams.WHITE, 
                    Gateways.Types.PROTECT);
            gwZonesIntercept = this.startGw.generateGatewaysZones(Gateways.Teams.BLACK, 
                    Gateways.Types.INTERCEPT);
            gwPointInt = this.startGw.getBlackGatewaysIntercept();
            gwPointPro = this.startGw.getWhiteGatewaysProtect();
        }    
        else
        if (type.isWhiteWin()){
            fighter = this.board.getPieceFromName("B-Fighter");
            gwZonesProtect = this.startGw.generateGatewaysZones(Gateways.Teams.BLACK, 
                    Gateways.Types.PROTECT);
            gwZonesIntercept = this.startGw.generateGatewaysZones(Gateways.Teams.WHITE, 
                    Gateways.Types.INTERCEPT);
            gwPointInt = this.startGw.getWhiteGatewaysIntercept();
            gwPointPro = this.startGw.getBlackGatewaysProtect();
        }    
       
        ArrayList<Coordinates> sharedLocation = sharedLocationGW(gwPointPro, gwZonesProtect, 
                gwPointInt, gwZonesIntercept);
    }
    //WhiteIntercept, BlackIntercept, WhiteProtect, Black Protect
    
    private ArrayList<Coordinates> sharedLocationGW ( ArrayList <Coordinates> gwPtPro,
             ArrayList<ArrayList <Node<Coordinates>>> gwZonePro,
             ArrayList <Coordinates> gwPtInt, ArrayList<ArrayList <Node<Coordinates>>> gwZoneInt){
 
        ShortestTrajectory st;
        PiecesLogic fighterDummy;
        int [][] steps;
        int closest=100;
        if (gwPtPro.isEmpty() || gwPtInt.isEmpty())
            return new ArrayList<Coordinates>();
        else
            for (Coordinates gwPro :  gwPtPro){
                fighterDummy = new FighterLogic("Fighter", gwPro.x, gwPro.y, 99);
                st = new ShortestTrajectory (this.board, fighterDummy, 
                      new Coordinates (0, 0));
                steps = st.getSTPieceBegin();
                for (Coordinates gwInt : gwPtInt){
                    if (closest > steps[gwInt.x][gwInt.y])
                        closest = steps[gwInt.x][gwInt.y];
                }    
            }   
        
        //say closest is one, for all of the gateways separated by one, check if 
        //the paths share trajectory.
    }
    private void keepBoth (){
        
    }
    
    private void changeOne (){
        
    }
    

}
