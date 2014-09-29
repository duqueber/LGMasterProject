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
    private Tree<Zones.Trajectory> blackZone, whiteZone;
    private ArrayList<Node<Coordinates>> stBlack, stWhite;
    private int whitePDist, blackPDist;

    public Gateways(Board2D board, Tree<Zones.Trajectory> black,
            Tree<Zones.Trajectory> white) {

        this.board = board;
        this.whiteGatewaysP = new ArrayList<>();
        this.blackGatewaysP = new ArrayList<>();
        this.whiteGatewaysI = new ArrayList<>();
        this.blackGatewaysI = new ArrayList<>();
        this.blackZone = black;
        this.whiteZone = white;

        PiecesLogic wFighter = this.board.getPieceFromName("W-Fighter");
        PiecesLogic bFighter = this.board.getPieceFromName("B-Fighter");

        this.stBlack = this.blackZone.getRoot().getData().getShortestPath();
        this.stWhite = this.whiteZone.getRoot().getData().getShortestPath();

        String blackZoneType = Zones.getZoneType(this.blackZone);
        String whiteZoneType = Zones.getZoneType(this.whiteZone);

        //WhiteIntercept, BlackIntercept, WhiteProtect, Black Protect
        this.whitePDist = Zones.getShortestDistFirstNeg(this.whiteZone);
        this.blackPDist = Zones.getShortestDistFirstNeg(this.blackZone);
        ZoneTypes zt = new ZoneTypes(blackZoneType, whiteZoneType);

        if (zt.isWhiteWin()) {
            calculateGateways(bFighter, stWhite, this.whiteGatewaysI, 2);
            calculateGateways(bFighter, stBlack, this.blackGatewaysP, blackPDist);
        } else if (zt.isBlackWin()) {
            calculateGateways(wFighter, stWhite, this.whiteGatewaysP, whitePDist);
            calculateGateways(wFighter, stBlack, this.blackGatewaysI, 2);
        } else if (zt.isBothIntercept()) {
            calculateGateways(wFighter, stWhite, this.whiteGatewaysP, whitePDist);
            calculateGateways(bFighter, stBlack, this.blackGatewaysP, blackPDist);
        } else if (zt.isBothProtect()) {
            calculateGateways(wFighter, stBlack, this.blackGatewaysI, 2);
            calculateGateways(bFighter, stWhite, this.whiteGatewaysI, 2);
        }
    }

    private void calculateGateways(PiecesLogic start, ArrayList<Node<Coordinates>> st,
            ArrayList<Coordinates> array, int dist) {

        ArrayList<ArrayList<Node<Coordinates>>> stsFirstStep;
        ShortestTrajectory stToMain;
        Coordinates c;
        //Gateways do not go to begining
        Iterator<Node<Coordinates>> it = st.iterator();
        if (it.hasNext()) {
            it.next();
        }

        if (it.hasNext()) {
            stToMain = new ShortestTrajectory(this.board, start,
                    it.next().getData());
            stToMain.GenerateShortestTrajectory();
            stsFirstStep = stToMain.getShortestTrajectories();

            for (ArrayList<Node<Coordinates>> stFirstStep : stsFirstStep) {
                c= stFirstStep.get(stFirstStep.size() - 1 - dist).getData();
                if (!IsInArray ( array, c))
                    array.add(c);
            }
        }
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
    
    
    public ArrayList<Coordinates> getBlackGatewaysProtect() {
        return this.blackGatewaysP;
    }

    public ArrayList<Coordinates> getWhiteGatewaysIntercept() {
        return this.whiteGatewaysI;
    }
    
    public ArrayList<Coordinates> getBlackGatewaysIntercept() {
        return this.blackGatewaysI;
    }

    public ArrayList<Coordinates> getWhiteGatewaysProtect() {
        return this.whiteGatewaysP;
    }
    
    private boolean IsInArray (ArrayList<Coordinates> a, Coordinates c){
        for (Coordinates coor : a)
            if (coor.equals(c))
                return true;
        return false;
    }

}
