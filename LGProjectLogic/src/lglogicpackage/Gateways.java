/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lglogicpackage;

import java.util.ArrayList;
import java.util.Iterator;
import lggrammars.ShortestTrajectory;
import lggrammars.Zones;
import supportpackage.Coordinates;
import supportpackage.Node;
import supportpackage.Tree;
import supportpackage.ZoneTypes;

public class Gateways {
    
    public enum Teams {WHITE, BLACK};
    public enum Types {PROTECT, INTERCEPT};

    private Board2D board;
    private ArrayList<Coordinates> whiteGatewaysP, whiteGatewaysI;
    private ArrayList<Coordinates> blackGatewaysP, blackGatewaysI;
    private ArrayList<Coordinates> preferredGateways;
    private Tree<Zones.Trajectory> blackZone, whiteZone;
    public int whitePspaceDist, blackIspaceDist, blackPspaceDist,whiteIspaceDist;
    private ArrayList<Node<Coordinates>> stBlack, stWhite;
    private int whitePDist, blackPDist;

    public Gateways(Board2D board, Tree<Zones.Trajectory> black,
            Tree<Zones.Trajectory> white, Strategies.Teams team) {

        this.board = board;
        this.whiteGatewaysP = new ArrayList<>();
        this.blackGatewaysP = new ArrayList<>();
        this.whiteGatewaysI = new ArrayList<>();
        this.blackGatewaysI = new ArrayList<>();
        this.preferredGateways = new ArrayList<>();
        this.blackIspaceDist = 0;
        this.blackPspaceDist = 0;
        this.whiteIspaceDist = 0;
        this.whitePspaceDist = 0;
        this.blackZone = black;
        this.whiteZone = white;

        PiecesLogic wFighter = this.board.getPieceFromName("W-Fighter");
        PiecesLogic bFighter = this.board.getPieceFromName("B-Fighter");

        if (this.blackZone != null){
            this.stBlack = this.blackZone.getRoot().getData().getShortestPath();
            this.blackPDist = Zones.getShortestDistFirstNeg(this.blackZone);
        }    
        else {
            this.stBlack = null;
            this.blackPDist = 0;
        }    
        
        if (this.whiteZone != null){
            this.stWhite = this.whiteZone.getRoot().getData().getShortestPath();
            this.whitePDist = Zones.getShortestDistFirstNeg(this.whiteZone);
        }    
        else {
            this.stWhite= null;
            this.whitePDist = 0;
        }
        String blackZoneType;
        String whiteZoneType; 
        
         
        blackZoneType = Zones.getZoneType(this.blackZone, team);
        whiteZoneType = Zones.getZoneType(this.whiteZone, team);

        ZoneTypes zt = new ZoneTypes(blackZoneType, whiteZoneType);

     //   if (zt.isWhiteWin()) {
       //     this.whiteIspaceDist = calculateGateways(bFighter, stWhite, this.whiteGatewaysI, 0);
         //   this.blackPspaceDist = calculateGateways(bFighter, stBlack, this.blackGatewaysP, blackPDist);
       // } else if (zt.isBlackWin()) {
             this.whitePspaceDist = calculateGateways(wFighter, stWhite, this.whiteGatewaysP, whitePDist);
             if (team == null)
                this.blackIspaceDist = calculateGateways(wFighter, stBlack, this.blackGatewaysI, 0);
             else
                 this.blackIspaceDist = -1;
        //} else if (zt.isBothIntercept()) {
          //  this.whitePspaceDist= calculateGateways(wFighter, stWhite, this.whiteGatewaysP, whitePDist);
           // this.blackPspaceDist = calculateGateways(bFighter, stBlack, this.blackGatewaysP, blackPDist);
        //} else if (zt.isBothProtect()) {
          //  this.blackIspaceDist = calculateGateways(wFighter, stBlack, this.blackGatewaysI, 0);
            //this.whiteIspaceDist= calculateGateways(bFighter, stWhite, this.whiteGatewaysI, 0);
       // }
    }

    private int calculateGateways(PiecesLogic start, ArrayList<Node<Coordinates>> st,
            ArrayList<Coordinates> array, int dist) {
        if (st == null)
            return 0;
        int sd = 0;
        ArrayList<ArrayList<Node<Coordinates>>> stsFirstStep;
        ShortestTrajectory stToMain;
        Coordinates c;
        int distTemp;
        int starti = 99;
        int counter = 0;
        while (starti==99 && counter<= st.size()){
            if (st.get(counter).hasChildren())
                starti = counter;
            counter++;
        }
        
        if (st.size() > 1){
            for (int i=starti; i < st.size(); i++){
                stToMain = new ShortestTrajectory(this.board, start,
                    st.get(i).getData());
                stToMain.GenerateShortestTrajectory();
                stsFirstStep = stToMain.getShortestTrajectories();
                if (dist == 0)
                    distTemp = i+1;
                else 
                    distTemp = dist;
                for (ArrayList<Node<Coordinates>> stFirstStep : stsFirstStep) {
                    c= stFirstStep.get(stFirstStep.size() - 1 - distTemp).getData();
                    if (!IsInArray ( array, c))
                        array.add(c);
                    if (i+1 >distTemp)
                            if (!IsInArray (this.preferredGateways, c))
                                this.preferredGateways.add(c);
                    if (sd == 0 || sd> stFirstStep.size() - 1 - distTemp )
                        sd = stFirstStep.size() - 1 - distTemp;
                }
            }        
        }
        return sd;
    }
    
    public ArrayList<ArrayList <Node<Coordinates>>> generateGatewaysZones( Teams team,
            Types type){
        
        ArrayList <ArrayList<supportpackage.Node<Coordinates>>> gwShortestArray;
        ShortestTrajectory gwShortestObj;
        ArrayList<Coordinates> useGW;
        ArrayList <Node<Coordinates>> goToPoints;
        int maxDist = 0;
      
        if (type.equals(type.INTERCEPT)){
            if (team.equals(team.BLACK)){
                useGW = this.blackGatewaysI;
                goToPoints = this.stBlack;
            }    
            else {
                useGW= this.whiteGatewaysI;
                goToPoints = this.stWhite;
            }    
        } else{
            if (team.equals(team.BLACK)){
                useGW = this.blackGatewaysP;
                goToPoints = this.stBlack;
                maxDist = this.blackPDist;
            }    
            else {
                useGW= this.whiteGatewaysP;
                goToPoints = this.stWhite;
                maxDist = this.whitePDist;
            }    
        }
        
        return helpGenerateZones (useGW, goToPoints, maxDist);    
    }  
    
    private ArrayList<ArrayList <Node<Coordinates>>> helpGenerateZones 
        (ArrayList<Coordinates> useGW, ArrayList <Node<Coordinates>> goToPoints, int maxDist){
        
        ShortestTrajectory gwShortestObj;
        ArrayList<ArrayList <Node<Coordinates>>> gwShortestArray;
        ArrayList<ArrayList <Node<Coordinates>>> finalArray = new ArrayList <>();
            
        if (!useGW.isEmpty()){
            for (Coordinates wG : useGW){
                for (int i = 0; i < goToPoints.size(); i++){
                    gwShortestObj = new ShortestTrajectory (this.board, 
                            new FighterLogic ("W-Fighter", wG.x, wG.y, 1), 
                            goToPoints.get(i).getData());
                    gwShortestObj.GenerateShortestTrajectory(); 
                    gwShortestArray = gwShortestObj.getShortestTrajectories();
                    
                    for (ArrayList <Node<Coordinates>> gwShortestPath: gwShortestArray){
                        if (!Zones.ShareCoordinates(gwShortestPath, goToPoints))
                            if (maxDist == 0){
                                if (gwShortestPath.size()-1 <= i+1)
                                    finalArray.add(gwShortestPath);
                            }    
                            else {
                               if (gwShortestPath.size()-1 <= maxDist)
                                   finalArray.add(gwShortestPath);
                            }
                    }
                    
                }
            } 
        }
        return finalArray;
    }// endo of generateZones
    
    private void Print (ArrayList<Coordinates> gw){
        
        System.out.print("GW: ");
        for (Coordinates c: gw)
            c.PrintCoor();     
    }
    
    public ArrayList<Coordinates> getBlackGatewaysProtect() {
        Print(this.blackGatewaysP);
        return this.blackGatewaysP;
    }

    public ArrayList<Coordinates> getWhiteGatewaysIntercept() {
        Print (this.whiteGatewaysI);
        return this.whiteGatewaysI;
    }
    
    public ArrayList<Coordinates> getBlackGatewaysIntercept() {
        Print (this.blackGatewaysI);
        return this.blackGatewaysI;
    }

    public ArrayList<Coordinates> getWhiteGatewaysProtect() {
        Print (this.whiteGatewaysP);
        return this.whiteGatewaysP;
    }
    
     public ArrayList<Coordinates> getprefered() {
        Print (this.preferredGateways);
        return this.preferredGateways;
    }
    
    public static boolean IsInArray (ArrayList<Coordinates> a, Coordinates c){
        for (Coordinates coor : a)
            if (coor.equals(c))
                return true;
        return false;
    }
    
    public static boolean inArrayNoFirst (ArrayList<Node<Coordinates>> a, Coordinates c){
        
        if (a.size()>1){
            for (int i = 1; i<a.size(); i++)
                if (a.get(i).getData().equals(c))
                    return true;         
        }
        return false;
    }

}
