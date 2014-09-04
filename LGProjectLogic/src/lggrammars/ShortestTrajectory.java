/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lggrammars;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;
import lglogicpackage.Board2D;
import lglogicpackage.PiecesLogic;
import supportpackage.Coordinates;
import supportpackage.Node;
import supportpackage.Print;
import supportpackage.Tree;

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
    private Coordinates currentX, currentY;
    private int currentl;
    private Vector sumArray;
    private Tree tree;
    private Stack doMoves;
    
    public ShortestTrajectory (Board2D board, PiecesLogic pieceBegin, Coordinates posEnd) {
        
        this.board = board;
        pieceStart = pieceBegin;
        posTarget = posEnd;
        rows = board.rows;
        columns=board.columns;
        doMoves = new Stack ();
        
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
        /*Print.PrintTable(STPieceBegin, rows, columns);
        System.out.println (" ");
        Print.PrintTable(STPosEnd, rows, columns);
        System.out.println (" ");
        Print.PrintTable(sumBoard, rows, columns);
        */     
        //test

    }
    
    public void GenerateShortestTrajectory (){
        
        if (l > 0){
            Gt_Q1();           
        } 
        else
            return;
        
        tree= new Tree ();
        Node<Coordinates> root = new Node (new Coordinates (pieceStart.positionX, pieceStart.positionY));
        tree.setRoot(root);
            
        Gt_Q2(root,posTarget, l);
        
       
    }
    
    private void Gt_Q1 (){
        currentX= new Coordinates (pieceStart.positionX, pieceStart.positionY);
        currentY = posTarget;
        currentl =l;
    }
    
    private void Gt_Q2( Node<Coordinates> begin, Coordinates target, int lparam){

        
        //while (lparam >= 1){
          int templ = l-lparam+1;  
          setMoves (begin, templ);  
        //}
    
        
    }
    
    private void setMoves (Node<Coordinates> begin, int templ){
        
        Vector intersection= new Vector();
        Vector STOne = new Vector();
        List children = new ArrayList ();

        Coordinates [] STOneMoves = pieceStart.PossibleMoves(begin.getData().x,
                begin.getData().y);
        for (Coordinates STOneMove : STOneMoves) {
                if (STOneMove.x < columns && STOneMove.y < rows && STOneMove.x >-1 
                        && STOneMove.y >-1) {
                   STOne.add(STOneMove);
                }
        }        
        
        for (Object sumElement : sumArray){
            for (Object STOneElement :STOne){
                Coordinates sE = (Coordinates)sumElement;
                Coordinates sO = (Coordinates)STOneElement;
                if (sE.x == sO.x && sE.y == sO.y)
                    intersection.add (sumElement);
            }
        }
        
        for (Object intersectionElement : intersection){
            Coordinates intElem = (Coordinates) intersectionElement;
        
            if (STPieceBegin [intElem.x][intElem.y] == templ){
                doMoves.add (intElem);
                children.add(new Node<Coordinates> (intElem));
                begin.setChildren(children);
            }    
        }
        
        
        //test
        for (Object elem: doMoves){
            Coordinates cd = (Coordinates)elem;
            cd.PrintCoor();
        }    
    //test
        
    }
    private void setSum (){
        
        sumArray = new Vector();
        for (int i = rows-1; i >=0; i-- ){
            for (int j = 0; j < columns; j++){
                
                if (STPieceBegin[j][i]+ STPosEnd[j][i] == l){
                    sumBoard [j][i] = l;
                    sumArray.addElement(new Coordinates (j,i));
                }    
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
                    //initMove.PrintCoor();
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
