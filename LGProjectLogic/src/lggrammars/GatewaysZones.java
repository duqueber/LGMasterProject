/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lggrammars;

import java.util.ArrayList;
import lglogicpackage.Board2D;
import lglogicpackage.FighterLogic;
import lglogicpackage.PiecesLogic;
import supportpackage.Coordinates;
import supportpackage.Node;

/**
 *
 * @author nati
 */
public class GatewaysZones {
    
    private Board2D board;
    private PiecesLogic fighter;
    private ArrayList <Node<Coordinates>> goToPoints;
    private ArrayList<Coordinates> gateways;
    private ArrayList<ArrayList <Node<Coordinates>>> finalArray;
    
    public GatewaysZones (Board2D board, ArrayList <Node<Coordinates>> goToPoints,
            ArrayList<Coordinates> gateways){
        this.board = board;
        this.gateways = gateways;
        this.goToPoints= goToPoints;
        this.finalArray = new ArrayList();
        
    }
          
    public void generateGatewayZones (){
      
        ArrayList <ArrayList<supportpackage.Node<Coordinates>>> gwShortestArray;
        ShortestTrajectory gwShortestObj;
        if (!this.gateways.isEmpty()){
            for (Coordinates wG : this.gateways){
                for (int i = 1; i < this.goToPoints.size(); i++){
                    gwShortestObj = new ShortestTrajectory (this.board, 
                            new FighterLogic ("W-Fighter", wG.x, wG.y, 1), 
                            this.goToPoints.get(i).getData());
                    gwShortestObj.GenerateShortestTrajectory(); 
                    gwShortestArray = gwShortestObj.getShortestTrajectories();
                    
                    for (ArrayList <Node<Coordinates>> gwShortestPath: gwShortestArray){
                        if (!Zones.ShareCoordinates(gwShortestPath, goToPoints))
                            this.finalArray.add(gwShortestPath);
                    }
                    
                }
            } 
        }    
    }// endo of generateZones

    public ArrayList<ArrayList <Node<Coordinates>>> getGatewaysZones (){
        return this.finalArray;
    }
    
}

