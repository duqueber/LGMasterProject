/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lglogicpackage;

import java.util.ArrayList;
import lggrammars.ShortestTrajectory;
import supportpackage.Coordinates;
import supportpackage.Moves;
import supportpackage.Node;

/**
 *
 * @author nati
 */
public class ChangeOneState implements Tactics {
    
    private Strategies strategy;
    private String desiredChange;
    private PiecesLogic fighter;
    private ArrayList<Coordinates> GWpoints;
    private ArrayList<Node<Moves>> nextSteps = new ArrayList<>();
    
    ChangeOneState (Strategies strategy, String desiredChange){
        this.desiredChange = desiredChange;
        this.strategy = strategy;
    }

    //WhiteIntercept, BlackIntercept, WhiteProtect, Black Protect
    @Override
    public void developTactic() {
        switch (this.desiredChange){
            case "0_1_": // change to white protect
                this.fighter = this.strategy.board.getPieceFromName("W-Fighter");
                this.GWpoints = this.strategy.startGw.getWhiteGatewaysProtect();
                break;
            case "1_0_": // change to white intercept
                this.fighter = this.strategy.board.getPieceFromName("B-Fighter");
                this.GWpoints = this.strategy.startGw.getWhiteGatewaysIntercept();
                break;    
            case "_0_1": // change to white protect
                this.fighter = this.strategy.board.getPieceFromName("B-Fighter");
                this.GWpoints = this.strategy.startGw.getBlackGatewaysProtect();
                break;       
            default:
                case "_1_0": // change to black intercept
                this.fighter = this.strategy.board.getPieceFromName("W-Fighter");
                this.GWpoints = this.strategy.startGw.getBlackGatewaysIntercept();
                break;    
        }
            
        calculateNextMoves ();
    }

    @Override
    public void calculateNextMoves() {
        ArrayList<ArrayList<Node<Coordinates>>> sts = new ArrayList<>();
        ShortestTrajectory stObj;
        ArrayList<Coordinates> temp =  new ArrayList <>();
        
        for (Coordinates gwCoor: this.GWpoints){
            stObj = new ShortestTrajectory (strategy.board, this.fighter, gwCoor);
            stObj.GenerateShortestTrajectory();
            sts = stObj.getShortestTrajectories();
            
            for (ArrayList<Node<Coordinates>>  st: sts)
                if (Gateways.IsInArray(temp, st.get(1).getData())){
                
                    temp.add(st.get(1).getData());
            }
        } 
        
     //  this.nextSteps.add (new Node(new Moves (this.fighter,st.get(1).getData())));
    }

    @Override
    public ArrayList<Node<Moves>> getNextMoves() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean possible() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean notPossible() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
