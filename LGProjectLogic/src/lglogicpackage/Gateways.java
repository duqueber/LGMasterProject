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

    private Board2D board;
    private ArrayList<Coordinates> whiteGatewaysP, whiteGatewaysI;
    private ArrayList<Coordinates> blackGatewaysP, blackGatewaysI;
    private Tree<Zones.Trajectory> blackZone, whiteZone;

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

        ArrayList<Node<Coordinates>> stBlack = this.blackZone.getRoot().getData().
                getShortestPath();
        ArrayList<Node<Coordinates>> stWhite = this.whiteZone.getRoot().getData().
                getShortestPath();

        String blackZoneType = Zones.getZoneType(black);
        String whiteZoneType = Zones.getZoneType(white);

        //WhiteIntercept, BlackIntercept, WhiteProtect, Black Protect
        int whitePDist = Zones.getShortestDistFirstNeg(white);
        int blackPDist = Zones.getShortestDistFirstNeg(black);
        ZoneTypes zt = new ZoneTypes(blackZoneType, whiteZoneType);

        if (zt.isWhiteWin()) {
            calculateGateways(bFighter, stWhite, this.blackGatewaysI, 2);
            calculateGateways(bFighter, stBlack, this.blackGatewaysP, blackPDist);
        } else if (zt.isBlackWin()) {
            calculateGateways(wFighter, stWhite, this.whiteGatewaysP, whitePDist);
            calculateGateways(wFighter, stBlack, this.whiteGatewaysI, 2);
        } else if (zt.isBothIntercept()) {
            calculateGateways(wFighter, stWhite, this.whiteGatewaysP, whitePDist);
            calculateGateways(bFighter, stBlack, this.blackGatewaysP, blackPDist);
        } else if (zt.isBothProtect()) {
            calculateGateways(wFighter, stBlack, this.whiteGatewaysI, 2);
            calculateGateways(bFighter, stWhite, this.blackGatewaysI, 2);
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
