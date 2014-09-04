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
    private final int [][] sumBoard, STPieceBegin, STPosEnd; 
    private final int l; 
    
    public ShortestTrajectory (Board2D board, PiecesLogic pieceBegin, Coordinates posEnd) {
        
        this.board = board;
        pieceStart = pieceBegin;
        posTarget = posEnd;
        rows = board.rows;
        columns=board.columns;
        sumBoard = new int [columns][rows];
        STPieceBegin = new int [columns][rows];
        STPosEnd = new int [columns][rows];
        
        STPieceBegin[pieceStart.positionX][pieceStart.positionY] = -1;
        STPosEnd [posEnd.x][posEnd.y] = -1;
        
        setCompleteST (new Coordinates (pieceStart.positionX, pieceStart.positionY), STPieceBegin);
        l=STPieceBegin[posEnd.x][posEnd.y];
        
        pieceStart.setDirection (-(pieceStart.getDirection()));
        setCompleteST (posEnd, STPosEnd);
        pieceStart.setDirection (-(pieceStart.getDirection()));
        
        STPieceBegin[pieceStart.positionX][pieceStart.positionY] = 0;
        STPosEnd [posEnd.x][posEnd.y] = 0;
        
        setSum();
        //test
        Print.PrintTable(STPieceBegin, rows, columns);
        System.out.println (" ");
        Print.PrintTable(STPosEnd, rows, columns);
        System.out.println (" ");
        Print.PrintTable(sumBoard, rows, columns);
             
        //test

    }
    
    public void GenerateShortestTrajectory (){
        
    }
    
    private void Gt_Q1 (){
        
        
    }
    
    private void setSum (){
        
        for (int i = rows-1; i >=0; i-- ){
            for (int j = 0; j < columns; j++){
                
                if (STPieceBegin[j][i]+ STPosEnd[j][i] == l)
                    sumBoard [j][i] = l;
            }
        }               
    }//End of setSum
    
    private void setCompleteST(Coordinates startPos, int[][] table){
        
        Queue nextMoves = new LinkedList ();
        Coordinates [] initMoves = pieceStart.PossibleMoves(startPos.x,
                startPos.y);
        
        if (initMoves.length != 0){
            
            for (Coordinates initMove : initMoves) {
                if (initMove.x < columns && initMove.y < rows && initMove.x >-1 
                        && initMove.y >-1) {
                    nextMoves.add(initMove);
                    table[initMove.x][initMove.y]= 1;
                    initMove.PrintCoor();
                }                           
            }
        }            
        else
            return;
        

        while (!nextMoves.isEmpty()){
            int i =0;
            Coordinates Coors = (Coordinates)nextMoves.poll();
            Coordinates [] tempNextMoves = pieceStart.PossibleMoves(Coors.x, Coors.y);
            
            i = table[Coors.x][Coors.y]+1;
            
            if (tempNextMoves.length != 0){     
                       
                for (Coordinates tempNextMove:tempNextMoves) {
                    
                    if (tempNextMove.x < columns && tempNextMove.y < rows 
                            && tempNextMove.x> -1 && tempNextMove.y > -1){
                        if (table[tempNextMove.x][tempNextMove.y] == 0 ){
                            table[tempNextMove.x][tempNextMove.y] = i;
                            nextMoves.add(tempNextMove);
                        }        
                                               
                    }
                }    
            }
        }
    }//END of setCompleteST    
    
    
}
