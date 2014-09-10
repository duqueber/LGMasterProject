/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lggrammars;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lglogicpackage.Board2D;
import lglogicpackage.PiecesLogic;
import supportpackage.Coordinates;
import supportpackage.Node;
import supportpackage.Print;
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
    private  ArrayList<ArrayList<Node<Coordinates>>> negPaths;
    
    private  final int rows, columns; 
    private  final int lInitial;
    
    private Table v, w, time, nextTime, distance;
    private final int sizeOfBoard;
    private u uParam;
    private final PiecesLogic pieceTarget;
    private ArrayList <Tree <Zones.Trajectory>> zonesTrees; // Array of all zones
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
        this.zonesTrees= new ArrayList<>();
        
        this.time = new Table (rows, columns, 2*this.sizeOfBoard);
        this.nextTime = new Table (rows, columns, 2*this.sizeOfBoard);
        this.distance = new Table (rows, columns, 2*this.sizeOfBoard);
        
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
 
            Trajectory t = new Trajectory (this.pieceStart.NAME, mainPath, uParam.lu+1);
            this.zonesTrajectories= new Tree<>();
            Node<Zones.Trajectory> root = new Node <> (t);
            zonesTrajectories.setRoot(root);
            zonesTrees.add(zonesTrajectories);
        }
        
        for (Tree <Zones.Trajectory> treez : zonesTrees ){
            this.uParam = new u (0,0,0);
            this.w = new Table (rows, columns, 0);
            this.v = w;
            setDistance (treez.getRoot().getData().shortestPath);
            this.time = this.distance;
            
            //test
            System.out.println("Distance and vq2 and time" );
            this.distance.PrintBoardInt();
            this.v.PrintBoardInt();    
            this.distance.PrintBoardInt();
            
            //test   
  
            Q_3 (treez.getRoot()); 
        }

    }
    
    private void Q_3 ( Node<Zones.Trajectory> t){
        if (this.uParam.xInt != this.sizeOfBoard-1 ||this.uParam.yInt != this.sizeOfBoard-1 ){
            u temp = f (this.uParam);
            this.uParam = temp;
            Q_4(t);
        }
        else 
            Q_5();
        //test
        
        List<Node<Trajectory>> nodes = t.getChildren();
        for (Node<Trajectory> node : nodes){
            Print.PrintArray(node.getData().shortestPath);
        }
        
        //test

    }
     
    private void Q_4 (Node<Zones.Trajectory> t){
        PiecesLogic p = board.getPiece(uParam.getXCoor());
        
        if (  p != null && this.pieceStart.NAME != p.NAME && uParam.lu>0 && 
                uParam.getXCoor() != this.pieceStart.getCoordinates()){
            boolean against = Oppose(this.pieceStart, p);
            
            ShortestTrajectory negTrajectories = new ShortestTrajectory (this.board, p, uParam.getYCoor());
            int mapXtoY = negTrajectories.Map(); 
                            
            if (mapXtoY !=0 && ((!against && mapXtoY == 1) ||
                    (against && mapXtoY <=uParam.lu))){  
                
                negTrajectories.GenerateShortestTrajectory();
                this.negPaths = negTrajectories.getShortestTrajectories();
                ArrayList<Node<Coordinates>> mainPath = t.getData().shortestPath;
                
                for (ArrayList<Node<Coordinates>> negPath: negPaths ){
                    
                    if (!CheckOverlap(negPath, mainPath)){
                        Trajectory newNegT = new Trajectory (p.NAME, negPath, 
                                this.time.getIntValue(uParam.yInt));
                        Node<Trajectory> child = new Node<> (newNegT);
                        if (t.hasChildren())
                            t.getChildren().add(child);
                        else 
                            t.addFirstChild (child);
                    }

                }   
            Q_3(t);    
            }
            else Q_3(t);  
        }
        else
            Q_3(t);
    }
    
    private void Q_5(){}
    
    private boolean CheckOverlap (ArrayList<Node<Coordinates>> path1, ArrayList<Node<Coordinates>> path2)
    {
        Iterator <Node<Coordinates>> negIt, negNextIt, mainIt, mainNextIt;
        
        negNextIt = path1.iterator();
        negIt = path1.iterator();
        mainNextIt = path2.iterator();
        
        if (negNextIt.hasNext() && mainNextIt.hasNext()){
            negNextIt.next();
            
            
            while (negNextIt.hasNext()){

                mainNextIt = mainIt = path2.iterator();
                mainNextIt.next();
                Node<Coordinates> it = negIt.next();
                Node<Coordinates> itNext = negNextIt.next();
                
                while (mainNextIt.hasNext()){

                    if (it == mainIt.next() && itNext == mainNextIt.next())
                        return true;
                }
            }
        
        }
        return false;
    }
            
            
    private u f (u oldU){
        //test
        int check = this.time.getIntValue(oldU.yInt+1);
        int check2 = this.v.getIntValue(oldU.yInt+1);
        //test
        
        if ((oldU.xInt != this.sizeOfBoard-1 && oldU.lu>0) ||
                (oldU.yInt == this.sizeOfBoard -1 && oldU.lu <= 0))
                return new u (oldU.xInt+1, oldU.yInt, oldU.lu);
        else // x=n OR (l<=0 && y !=n)
            return new u (0, oldU.yInt + 1, 
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
            this.lu = lu;
        }
        
        Coordinates getXCoor (){
            return Coordinates.getCoordinates(this.xInt, Zones.this.rows);
        }
        
        Coordinates getYCoor (){
            return Coordinates.getCoordinates(this.yInt, Zones.this.rows);
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
