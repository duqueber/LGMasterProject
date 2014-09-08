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
import supportpackage.Table;
import supportpackage.Tree;

/**
 *
 * @author nati
 */
public class Zones  {

    private final Board2D board;
    private  final PiecesLogic pieceStart;
    private  ArrayList<ArrayList<Node<Coordinates>>> mainPaths;
    
    private  final int rows, columns; 
    private  final int lInitial;
    
    private Table v, w, time, nextTime, distance;
    private final int sizeOfBoard;
    private u uParam;
    private final PiecesLogic pieceTarget;
    private ArrayList <ArrayList <Zones.Trajectory>> zonesArray; // Array of all zones
    private Tree <Zones.Trajectory> zonesTrajectories;// zone starting with each main trajectory

    
    public Zones (Board2D board, PiecesLogic pieceBegin, PiecesLogic pieceTarget){
    
        this.board = board;
        columns = this.board.columns;
        rows = this.board.rows;
        sizeOfBoard = rows*columns;
        
        this.pieceStart = pieceBegin;
        this.pieceTarget = pieceTarget;
        this.lInitial = new ShortestTrajectory(board,this.pieceStart,
                new Coordinates (this.pieceTarget.positionX, this.pieceTarget.positionY)).Map();
        this.zonesArray= new ArrayList<>();
        
        time = new Table (rows, columns, 2*this.sizeOfBoard);
        nextTime = new Table (rows, columns, 2*this.sizeOfBoard);
  
        
        uParam = new u (this.pieceStart.getCoordinates().getInteger(columns), 
                this.pieceTarget.getCoordinates().getInteger(columns), this.lInitial);
        
    }

    public void GenerateZones (){
        
        //Q1 condition
        if (On(pieceStart, Coordinates.getCoordinates(uParam.xInt, rows)) && 
                On (pieceTarget, Coordinates.getCoordinates(uParam.yInt, rows)) 
                && Oppose(pieceStart, pieceTarget) )
            Q2 ();
        else return;
        

    }
    
    private void Q2 (){
        
        ShortestTrajectory Tracks = new ShortestTrajectory (board, this.pieceStart,
                this.pieceTarget.getCoordinates());
        Tracks.GenerateShortestTrajectory();
        this.mainPaths = Tracks.getShortestTrajectories();
        
        int [] wTemp;
        
        for (ArrayList<Node<Coordinates>> mainPath : mainPaths){

            this.distance = new Table (rows, columns, 2*this.sizeOfBoard);  
            this.uParam = new u (0,0,0);
            this.w = new Table (rows, columns, 0);
            this.v = w;
            
            Trajectory t = new Trajectory (this.pieceStart.NAME, mainPath, uParam.lu+1);
            this.zonesTrajectories= new Tree<>();
            Node<Zones.Trajectory> root = new Node <> (t);
            zonesTrajectories.setRoot(root);
            
            setDistance (mainPath);
            this.time = this.distance;
            
            Q_3 ();
        }
        
        //test
        System.out.println("Distance and vq2 and time" );
        this.distance.PrintBoardInt();
        this.v.PrintBoardInt();    
        this.distance.PrintBoardInt();
         //test   
    }
    
    private void Q_3 (){
        if (this.uParam.xInt != this.sizeOfBoard-1 ||this.uParam.xInt != this.sizeOfBoard-1 ){
            u temp = f (this.uParam);
            this.uParam = temp;
            Q_4();
        }
        else 
            Q_5();
    }
    
    
    private void Q_4 (){
        if 
    }
    
    private void Q_5(){}
    
    private u f (u oldU){
        if ((oldU.xInt != this.sizeOfBoard-1 && oldU.lu>0) ||
                (oldU.yInt == this.sizeOfBoard -1 && oldU.lu <= 0))
                return new u (oldU.xInt+1, oldU.yInt, oldU.yInt);
        else // x=n OR (l<=0 && y !=n)
            return new u (1, oldU.yInt + 1, 
                    (this.time.getIntValue(oldU.yInt+1)* this.v.getIntValue(oldU.yInt+1)));
    }
    
    private void setDistance (ArrayList<Node<Coordinates>> Coors ){
        
        int i=0;
        for (Node<Coordinates> coor: Coors ){
            if (i>0){
                Coordinates pos = coor.getData();
                this.distance.changeValue(pos, i+1);
                this.v.changeValue(pos, 1);
            }
            i++;
        }
    }
    
    private Boolean On (PiecesLogic piece, Coordinates c){
        if (piece.getCoordinates().equals(c) )
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
        int lu;
        int xInt, yInt;

        u (int x, int y, int lu){
            this.xInt = x;
            this.yInt = y;
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
