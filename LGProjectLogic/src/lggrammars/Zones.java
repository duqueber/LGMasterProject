/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lggrammars;

import java.util.ArrayList;
import lglogicpackage.Board2D;
import lglogicpackage.PiecesLogic;
import supportpackage.Coordinates;
import supportpackage.Node;

/**
 *
 * @author nati
 */
public class Zones  {
    
    //variables from ShortestTrajectory
    private final Board2D board;
    private  final PiecesLogic pieceStart;
    private  ArrayList<ArrayList<Node<Coordinates>>> ShortestPaths;
    
    private  final int rows, columns; 
    private  final int lInitial;
    
    private int[] v, w, time, nextTime;
    private final int sizeOfBoard;
    private u uParam;
    private final PiecesLogic pieceTarget;
    ArrayList <Zones.Trajectory> ZonesArray;

    
    public Zones (Board2D board, PiecesLogic pieceBegin, PiecesLogic pieceTarget){
    
        this.board = board;
        columns = this.board.columns;
        rows = this.board.rows;
        sizeOfBoard = rows*columns;
        
        this.pieceStart = pieceBegin;
        this.pieceTarget = pieceTarget;
        this.lInitial = new ShortestTrajectory(board,this.pieceStart,
                new Coordinates (this.pieceTarget.positionX, this.pieceTarget.positionY)).Map();
        
        v = new int [sizeOfBoard];
        w = new int [sizeOfBoard];
        time = new int [sizeOfBoard];
        nextTime = new int [sizeOfBoard];

        uParam = new u (this.pieceStart, this.pieceTarget, this.lInitial);
        
        for (int slot: time){
            slot = 2*sizeOfBoard;
        }

        for (int nextSlot: nextTime){
            nextSlot = 2*sizeOfBoard;
        }
    }

    public void GenerateZones (){
        
        //Q1 condition
        if (On(pieceStart) && On (pieceTarget) && Oppose(pieceStart, pieceTarget) )
            Q2 ();
        else return;
    }
    
    private void Q2 (){
        
        
    }
    
    private Boolean On (PiecesLogic piece){
        if (piece.getCoordinates().equals(uParam.pieceX.getCoordinates()) )
            return true;
        return false;
    }
    
    private Boolean Oppose (PiecesLogic piece1, PiecesLogic piece2){
        if (piece1.getTeam() == piece2.getTeam())
            return false;
        else
            return true;
    }
    
    private class u{
        PiecesLogic pieceX, pieceY;
        int lu;
        u (PiecesLogic x, PiecesLogic y, int lu){
            this.pieceX =x;
            this.pieceY=y;
            this.lu=lu;
        }
    }
    
    private class Trajectory{
        String pieceName;
        ArrayList<Node<Coordinates>> shortestPath;
        int lt;
        
        Trajectory (String pieceName, ArrayList<Node<Coordinates>> shortestPath, int lt){
            this.pieceName = pieceName;
            this.shortestPath = shortestPath;
            this.lt = lt;
        }
    }
    
}
