package lglogicpackage;

import java.io.IOException;
import lggrammars.ShortestTrajectory;
import lggrammars.Zones;
import lggui.GUIFrame;
import supportpackage.Coordinates;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nati
 */
public class Board2D {
    
    public PiecesLogic [][] board;
    public int columns, rows;
    public final PiecesLogic [] pieces;
    
    public Board2D (int rows, int columns, PiecesLogic [] players ){
        
        this.columns = columns;
        this.rows = rows;
        this.pieces = players;
        this.board = new PiecesLogic [columns][rows];
        
        //test
        /*for (int i = rows-1; i >=0; i-- ){
            for (int j = 0; j < columns; j++){
                
                board[j][i]= "No piece";
                System.out.print ( " "+j + i);
            }    
        
        System.out.println(' ');
        } */   
        //test
        for (int i= 0; i< players.length; i++){
            addPiece (players[i]);        
        }
        
      /*  //test
        for (int i = rows-1; i >=0; i-- ){
            for (int j = 0; j < columns; j++){                
                System.out.print ( board[j][i].NAME + "- "+ j +" "+i + "");
            }    
        
        System.out.println(' ');
        }  
        //test*/
    }    
    
    public Board2D (Board2D b){
        this.columns = b.columns;
        this.rows = b.rows;
        this.pieces = b.getListPieces();
        this.board = new PiecesLogic [columns][rows];
        
        for (int i= 0; i< this.pieces.length; i++){
            addPiece (this.pieces[i]);        
        }
    }
            
            
    public void addPiece (PiecesLogic piece){
        
        try{
            board[piece.positionX][piece.positionY] = piece;

         } catch (IndexOutOfBoundsException e) {
                System.err.println("Caught IndexOutOfBoundsException. Piece cannot"
                        + " be placed: "+  e.getMessage());

            }         
    }
    
    public void removePiece (PiecesLogic piece){
        try{
            board[piece.positionX][piece.positionY] = null;

         } catch (IndexOutOfBoundsException e) {
                System.err.println("Caught IndexOutOfBoundsException. Piece cannot"
                        + " be removed: "+  e.getMessage());

         }
    }
    
    public void replace (String repP, String withP){
        PiecesLogic p1, p2;
        p1=p2=null;
        for (PiecesLogic p: this.pieces ){
            if (p.NAME.equals(repP) ){
                p1=p;
                removePiece(p);
            }    
            if (p.NAME.equals(withP)){
                p2 = p;
                removePiece(p);
            }
        }
        Coordinates cp1 =p1.getCoordinates();
        p1.positionX = p2.positionX;
        p1.positionY = p2.positionY;
        p2.positionX = cp1.x;
        p2.positionY = cp1.y;
        this.addPiece(p1);
        this.addPiece(p2);
         
    }
    
    public PiecesLogic getPiece (Coordinates c){
        if (board [c.x][c.y] == null )
            return null;
        else 
            return board [c.x][c.y]; 
    }
    
    public PiecesLogic getPieceFromName (String Name){
        for (PiecesLogic p : this.pieces ){
            if (p.NAME == Name)
                return p;
        }    
        return null;   
    }
    
    public boolean hasPiece (Coordinates c){
        if (this.board[c.x][c.y]== null)
            return true;
        return false;
    }
    
    public PiecesLogic[] getListPieces (){
        return this.pieces;
    }
    public static void main(String args[]) throws IOException {
        
        PiecesLogic[] pieces = new PiecesLogic[6];
        pieces[0]= new FighterLogic ( "B-Fighter", 0, 5, 2);
        pieces[1]= new FighterLogic ("W-Fighter", 7, 7, 1);
        pieces[2]= new BomberLogic ("B-Bomber", 7, 4, 2, -1);
        pieces[3] = new BomberLogic ("W-Bomber", 2, 5, 1, 1);
        pieces[4] = new TargetLogic ("B-Target", 2, 7, 2);
        pieces[5] = new TargetLogic ("W-Target", 7, 0, 1);        
        
        Board2D hi = new Board2D(8,8, pieces);

        GUIFrame gui = new GUIFrame(hi);
        
        /*System.out.println ("B=Bomber to W- target" );
        
        Zones z = new Zones (hi, pieces[2],pieces[5]);
        z.GenerateZones();
        
       /*System.out.println ("W-Bomber to B-target" );
        Zones st2 = new Zones (hi,pieces[3], pieces[4]);
        st2.GenerateZones();*/
        
        /*System.out.println ("B-Fighter to W-Bomber" );
        ShortestTrajectory st3 = new ShortestTrajectory(hi,pieces[0], new Coordinates (7,7));
        st3.GenerateShortestTrajectory();
        Zones st = new Zones(hi,pieces[0], pieces[1]);
        st.GenerateZones();
        
        System.out.println ("W-Fighter to B-Bomber" );
        ShortestTrajectory st4 = new ShortestTrajectory(hi,pieces[1], new Coordinates (2,7));
        st4.GenerateShortestTrajectory();*/
    }        

}
