/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lggrammars;

import java.util.LinkedList;
import java.util.Queue;
import lglogicpackage.Board2D;
import lglogicpackage.PiecesLogic;
import supportpackage.Coordinates;
import supportpackage.Print;

/**
 *
 * @author nati
 */
public class ShortestTrajectory {
    
    private final Board2D board;
    private final PiecesLogic pieceStart;
    private final Coordinates posTarget;
    private Coordinates[] ShortestPath;
    
    private final int rows, columns; 
    private int [][] sumBoard, STPieceBegin; 

    
    public ShortestTrajectory (Board2D board, PiecesLogic pieceBegin, Coordinates posEnd) {
        
        this.board = board;
        pieceStart = pieceBegin;
        posTarget = posEnd;
        rows = board.rows;
        columns=board.columns;
        sumBoard = new int [columns][rows];
        STPieceBegin = new int [columns][rows];
        STPieceBegin[pieceStart.positionX][pieceStart.positionY] = -1;
        //test
        Print t = new Print ();
        setSTPieceBegin ();
        t.PrintTable(STPieceBegin, rows, columns);
        //test
    }
    
    public void GenerateShortestTrajectory (){
        
    }
    
    private void Gt_Q1 (){
        
        
    }
    
    private void setSum (){
        
        
        
    }
    
    private void setSTPieceBegin(){
        
        int counter = 0;
        int counterLimit = rows * columns;
        Queue nextMoves = new LinkedList ();
        Coordinates [] initMoves = pieceStart.PossibleMoves(pieceStart.positionX,
                pieceStart.positionY);
        
        if (initMoves.length != 0){
            
            for (Coordinates initMove : initMoves) {
                if (initMove.x < columns && initMove.y < rows && initMove.x >-1 
                        && initMove.y >-1) {
                    nextMoves.add(initMove);
                    STPieceBegin[initMove.x][initMove.y]= 1;
                    counter++;
                    initMove.PrintCoor();
                }                           
            }
        }            
        else
            return;
        

        while (!nextMoves.isEmpty() && counter< counterLimit){
            int i =0;
            Coordinates Coors = (Coordinates)nextMoves.poll();
            Coordinates [] tempNextMoves = pieceStart.PossibleMoves(Coors.x, Coors.y);
            
            int j = STPieceBegin [Coors.x][Coors.y];
            if (j >0)
                i=j+1;
            
            if (tempNextMoves.length != 0){     
                       
                for (Coordinates tempNextMove:tempNextMoves) {
                    
                    if (tempNextMove.x < columns && tempNextMove.y < rows 
                            && tempNextMove.x> -1 && tempNextMove.y > -1){
                        if (STPieceBegin[tempNextMove.x][tempNextMove.y] == 0 || 
                            STPieceBegin[tempNextMove.x][tempNextMove.y] > i ){
                            STPieceBegin[tempNextMove.x][tempNextMove.y] = i;
                            counter++;
                        }        
                        nextMoves.add(tempNextMove);                       
                    }
                }    
            }
        }
    }//END of setSTPieceBegin    
    
    
}
