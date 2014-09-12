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
    private PiecesLogic pieceStart;
    private  ArrayList<ArrayList<Node<Coordinates>>> mainPaths;
    
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
            this.v = new Table (rows, columns, 0);
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
        while (this.uParam.xInt != this.sizeOfBoard-1 || this.uParam.yInt != this.sizeOfBoard-1 )
        {
            u temp = f (this.uParam);
            this.uParam = temp;
            Q_4(t);
        }   
        
        System.out.println ("hello");
        //test        
        List<Node<Trajectory>> nodes = t.getChildren();
        for (Node<Trajectory> node : nodes){
            node.getData().printTrajectory();
        } 
        SetNextTime (t);
        
        if (!tableEqualToZero (this.w))
             Q_5(t);
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
                ArrayList<ArrayList<Node<Coordinates>>> negPaths = negTrajectories.getShortestTrajectories();
                ArrayList<Node<Coordinates>> mainPath = t.getData().shortestPath;
                
                for (ArrayList<Node<Coordinates>> negPath: negPaths ){
                    if (!ShareCoordinates(negPath, mainPath)){
                        Trajectory newNegT = new Trajectory (p.NAME, negPath, 
                                this.time.getIntValue(uParam.yInt));
                        Node<Trajectory> child = new Node<> (newNegT);
                        if (t.hasChildren())
                            t.getChildren().add(child);
                        else 
                            t.addFirstChild (child);
                    }
                }   
            }
        }

    }
    
    private void Q_5(Node<Zones.Trajectory> t){

        List<Node<Trajectory>> nodes = t.getChildren();
        for (Node<Zones.Trajectory> n : nodes){
                this.pieceStart= this.board.getPiece(n.getData().shortestPath.get(0).data);
                this.time = new Table (this.nextTime);
                u uTemp = new u (0,0,0);
                this.uParam = uTemp;
                SetWwithNext(this.nextTime);
                this.v = new Table (this.w);
                this.w = new Table(this.rows, this.columns, 0); 
                Q_3(n);
         }
        
    }
    
    private boolean tableEqualToZero (Table t){
        for (int i =0; i < t.columns*t.rows; i++){
            if (t.getIntValue(i) != 0)
                return false;
        }
        return true;
    }
    
    private boolean ShareCoordinates (ArrayList<Node<Coordinates>> path1, ArrayList<Node<Coordinates>> path2){
    
       Node<Coordinates> tempRemove = path1.remove (path1.size()-1);
        
        for (Node<Coordinates> coor: path1){
            for (Node<Coordinates> mainCoor: path2)
                if (coor.getData().equals(mainCoor.getData()))
                        return true;
        }
        path1.add(tempRemove);
        return false;
    }

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
                Coordinates it = negIt.next().getData();
                Coordinates itNext = negNextIt.next().getData();
                
                while (mainNextIt.hasNext()){
                    Coordinates itMain = mainIt.next().getData();
                    Coordinates itMainNext = mainNextIt.next().getData();
                    if (it.equals(itMain) && itNext.equals(itMainNext))
                        return true;
                }
            }
        
        }
        return false;
    }
            
            
    private u f (u oldU){
        
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
    
    private void SetNextTime (Node<Zones.Trajectory> t){
    // Alpha;
        List<Node<Trajectory>> nodes = t.getChildren();
        
        for (Node<Trajectory> node : nodes){
            ArrayList<Node<Coordinates>> shortestP = node.getData().shortestPath;
            Coordinates lastElem = shortestP.get(shortestP.size()-1).getData();
            int timeValue = this.time.getIntValue(lastElem.getInteger(this.columns));
            int k = timeValue - (shortestP.size()-1) + 1;
            
            for (int i = 1; i< shortestP.size()-1; i++ ){
                    Coordinates c = shortestP.get(i).getData();
                    if (this.nextTime.getIntValue(c.getInteger(this.columns)) < k || 
                            this.nextTime.getIntValue(c.getInteger(this.columns)) == 2* this.sizeOfBoard){
                        this.nextTime.changeValue(c,k); 
                        this.w.changeValue(c, 1);
                    }    
            }               
        }    
        
        //test
        System.out.println ("Next time");
        this.nextTime.PrintBoardInt();
        
        System.out.println ("w");
        this.w.PrintBoardInt();
        //test
    }
    
    private void SetWwithNext (Table ntTbl){
        this.w = new Table (this.rows, this.columns, 0);
        for (int i = 0 ; i < this.sizeOfBoard; i++)
            if (ntTbl.getIntValue (i) != 2*this.sizeOfBoard)
                this.w.changeValue(Coordinates.getCoordinates(i, this.rows), ntTbl.getIntValue (i)); 
        
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
        
        void printTrajectory (){   
            System.out.print ("Trajectory: " + pieceName + ", " );
            Print.PrintArray(shortestPath);
            System.out.println (", " + lt);
        }
    }
    
}
