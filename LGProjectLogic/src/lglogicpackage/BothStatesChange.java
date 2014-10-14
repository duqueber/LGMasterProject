/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lglogicpackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lggrammars.ShortestTrajectory;
import supportpackage.Coordinates;
import supportpackage.Moves;
import supportpackage.Node;
import supportpackage.ZoneTypes;

/**
 *
 * @author nati
 */
public class BothStatesChange  implements Tactics  {
    
    private static ArrayList<Boolean> shared = new ArrayList<>(); 
    private static ArrayList<Integer> closestArray = new ArrayList<>(); 
    private Strategies strategy;
    private ArrayList<Coordinates> sharedLocation;
    private ArrayList<Node<Moves>> nextSteps = new ArrayList<>();
    PiecesLogic fighter;
    private StructGW closestGWRandom;
    private boolean reachedGW;
    private ArrayList <Coordinates>  gwPointPro, gwPointInt;
    
    BothStatesChange (Strategies strategy){
        this.strategy= strategy;
        this.reachedGW = false;
        this.gwPointPro = new ArrayList<>();
        this.gwPointInt = new ArrayList<>();
    }

    @Override
    public void developTactic() {
        
        ArrayList<ArrayList <Node<Coordinates>>> gwZonesProtect, gwZonesIntercept;
        gwZonesProtect = new ArrayList<> ();
        gwZonesIntercept = new ArrayList<>();

        ZoneTypes type = new ZoneTypes (this.strategy.startBlackZoneType, strategy.startWhiteZoneType);
        if (type.isBlackWin()){
            this.fighter = this.strategy.board.getPieceFromName("W-Fighter");
            gwZonesProtect = this.strategy.startGw.generateGatewaysZones(Gateways.Teams.WHITE, 
                    Gateways.Types.PROTECT);
            gwZonesIntercept = this.strategy.startGw.generateGatewaysZones(Gateways.Teams.BLACK, 
                    Gateways.Types.INTERCEPT);
            gwPointInt = this.strategy.startGw.getBlackGatewaysIntercept();
            gwPointPro = this.strategy.startGw.getWhiteGatewaysProtect();
        }    
        else
        if (type.isWhiteWin()){
            this.fighter = this.strategy.board.getPieceFromName("B-Fighter");
            gwZonesProtect = this.strategy.startGw.generateGatewaysZones(Gateways.Teams.BLACK, 
                    Gateways.Types.PROTECT);
            gwZonesIntercept = this.strategy.startGw.generateGatewaysZones(Gateways.Teams.WHITE, 
                    Gateways.Types.INTERCEPT);
            gwPointInt = this.strategy.startGw.getWhiteGatewaysIntercept();
            gwPointPro = this.strategy.startGw.getBlackGatewaysProtect();
        }    
       
        this.sharedLocation = sharedLocationGW(gwPointPro, gwZonesProtect, 
                gwPointInt, gwZonesIntercept);
        
        OnlyGateways(); // just choose shared locations that are GW if they exist. Prune the other ones.
        
        if (!this.sharedLocation.isEmpty())
            shared.add(Boolean.TRUE);
        else 
            shared.add(Boolean.FALSE);
        
        calculateNextMoves();
    }// End changeBoth
    
    //WhiteIntercept, BlackIntercept, WhiteProtect, Black Protect
    
    private ArrayList<Coordinates> sharedLocationGW ( ArrayList <Coordinates> gwPtPro,
             ArrayList<ArrayList <Node<Coordinates>>> gwZonePro,
             ArrayList <Coordinates> gwPtInt, ArrayList<ArrayList <Node<Coordinates>>> gwZoneInt){
 
        ArrayList<Coordinates> sharedLocations = new ArrayList <>();
        ShortestTrajectory st;
        PiecesLogic fighterDummy;
        int[][] steps;
        int closest=100;
        Map<StructGW, Integer> m = new HashMap<StructGW, Integer>();
        
        if (gwPtPro.isEmpty() || gwPtInt.isEmpty())
            return new ArrayList<Coordinates>();
        else
            for (Coordinates gwPro :  gwPtPro){
                fighterDummy = new FighterLogic("Fighter", gwPro.x, gwPro.y, 99);
                st = new ShortestTrajectory (this.strategy.board, fighterDummy, 
                      new Coordinates (0, 0));
                steps = st.getSTPieceBegin();
                for (Coordinates gwInt : gwPtInt){
                    m.put(new StructGW (gwPro, gwInt), (Integer)steps[gwInt.x][gwInt.y]);
                    if (closest > steps[gwInt.x][gwInt.y])
                        closest = steps[gwInt.x][gwInt.y];                
                }    
            }   
        
        closestArray.add((Integer)closest);
        
        Map<StructGW, Integer> mCheck = new HashMap<StructGW, Integer>();
        for (Map.Entry<StructGW, Integer> e : m.entrySet()) // eliminate no closest
            if (e.getValue().equals((Integer)(closest)))
                mCheck.put(e.getKey(), e.getValue());
        
        //loops the closest shortest first negation trajectories from gateways to 
        //main path to search for shared locations
        for (ArrayList <Node<Coordinates>> st1: gwZonePro)
            for (ArrayList <Node<Coordinates>> st2: gwZoneInt){  
                this.closestGWRandom = new StructGW (st1.get(0).getData(), 
                        st2.get(0).getData());
                if (containsKey(mCheck,new StructGW (st1.get(0).getData(), 
                        st2.get(0).getData()))){
                    for (Node<Coordinates> n1: st1)
                        for (Node<Coordinates>n2: st2)
                            if (n1.getData().equals(n2.getData()) && 
                                    !Gateways.IsInArray(sharedLocations, n1.getData()))
                                sharedLocations.add(n1.getData());
                }
            }
        
 
       //test
       System.out.println ("Shared Locations"); 
       for (Coordinates c: sharedLocations)
           c.PrintCoor();
       //test
       return sharedLocations;             
        //say closest is one, for all of the gateways separated by one, check if 
        //the paths share trajectory.
    }
    
    private boolean containsKey (Map <StructGW, Integer> m, StructGW s){

        for (Map.Entry<StructGW, Integer> e : m.entrySet()) 
            if (e.getKey().equals(s))
                return true;
        return false;
    } 
    
    private boolean IsGateway ( Coordinates c){
        for (Coordinates gInt: this.gwPointInt)
            if (gInt.equals(c))
                return true;
        for (Coordinates gPro: this.gwPointPro)
            if (gPro.equals(c))
                return true;
        return false;
    }
    
    private boolean OnlyGateways (){
        
        ArrayList<Coordinates> tempshared = new ArrayList<>();
        
        for (Coordinates n: this.sharedLocation)
            for (Coordinates gInt: this.gwPointInt)
                if (gInt.equals(n))
                    for (Coordinates gPro: this.gwPointPro)
                        if (gInt.equals(gPro))
                            tempshared.add(gPro);
        
        if (!tempshared.isEmpty()){
            this.sharedLocation = tempshared;
            return true;
        }
            
        return false;
    }


    
    private class StructGW{
        Coordinates gwPro, gwInt;
        StructGW (Coordinates gwPro, Coordinates gwInt){
            this.gwInt = gwInt;
            this.gwPro = gwPro;
        }
        
        
        @Override
        public boolean equals (Object c){
            if (c == null)
                return false;
   
            if (this.getClass() != c.getClass())
                return false;
  
            return ((StructGW)c).gwInt.equals(this.gwInt) && 
                    ((StructGW)c).gwPro.equals(this.gwPro);
        }
        
    }

    @Override
    public void calculateNextMoves() {
        
        ArrayList<ArrayList<Node<Coordinates>>> stArray = new ArrayList<>();
        ShortestTrajectory st = null;
        
        if (!this.sharedLocation.isEmpty()){
         for (Coordinates c: this.sharedLocation)
             st= new ShortestTrajectory (this.strategy.board, this.fighter, c );
             st.GenerateShortestTrajectory();
             stArray = st.getShortestTrajectories();
             for (ArrayList<Node<Coordinates>> firstStep: stArray)
                 this.nextSteps.add(new Node(new Moves(this.fighter, 
                         firstStep.get(1).getData())));             
        }
        else{
            
            st= new ShortestTrajectory (this.strategy.board, this.fighter, this.closestGWRandom.gwInt );
            st.GenerateShortestTrajectory();
            stArray = st.getShortestTrajectories();
            for (ArrayList<Node<Coordinates>> firstStep: stArray){
                this.nextSteps.add(new Node(new Moves(this.fighter, 
                        firstStep.get(1).getData())));
            }    
        } 
    }

    @Override
    public ArrayList<Node<Moves>> getNextMoves() {
        return this.nextSteps;
    }


    @Override
    public boolean possible() {

        if (shared.size() > 1)     
            if (shared.get(shared.size()-1).equals (Boolean.TRUE) )
                return true; // There is a shared locations
        return false;
    }
    
    
    @Override
    public boolean notPossible() {
        
        if (shared.size() > 1)
            if (shared.get(shared.size()-2).equals(Boolean.TRUE) &&
                    shared.get(shared.size()-1).equals(Boolean.FALSE))
            return true;
        
        if (closestArray.size()>1)
            return closestArray.get(closestArray.size()-1)> closestArray.get(closestArray.size()-2);
        return false;
    }
         
}
