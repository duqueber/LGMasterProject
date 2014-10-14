package lglogicpackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import lggrammars.ShortestTrajectory;
import lggrammars.Zones;
import lggui.GUIFrame;
import supportpackage.Coordinates;
import supportpackage.Moves;
import supportpackage.Node;

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
    public ArrayList <PiecesLogic> pieces;
    
    public Board2D (int rows, int columns, ArrayList <PiecesLogic> players ){
        
        this.columns = columns;
        this.rows = rows;
        this.pieces = new ArrayList<>();
        this.board = new PiecesLogic [columns][rows];
        
        for (int i=0; i< this.columns; i++)
            for (int j=0; j< this.rows; j++)
                this.board[i][j] = null;
        
        PiecesLogic p;
        for (PiecesLogic q : players){
            p = returnPieceObj (q);
            this.pieces.add(p);
            addPiece (p);        
        }
        //test
        /*for (int i = rows-1; i >=0; i-- ){
            for (int j = 0; j < columns; j++){
                
                board[j][i]= "No piece";
                System.out.print ( " "+j + i);
            }    
        
        System.out.println(' ');
        } */   
        //test
        
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
        this.pieces = new ArrayList<>();
        this.board = new PiecesLogic [b.columns][b.rows];
        
        for (int i=0; i<b.columns; i++)
            for (int j=0; j<b.rows; j++)
                this.board[i][j] = null;
        
        PiecesLogic p;
        for (PiecesLogic q : b.pieces){
            p= returnPieceObj (q);
            this.pieces.add(p);
            addPiece (p);        
        }
    }
            
            
    public void addPiece (PiecesLogic piece){
        
        try{
                this.board[piece.positionX][piece.positionY] = returnPieceObj (piece);
            
            for (PiecesLogic p: this.pieces)
                if (piece.NAME.equals(p.NAME)){
                    p.positionX = piece.positionX;
                    p.positionY = piece.positionY;
                }    

         } catch (IndexOutOfBoundsException e) {
                System.err.println("Caught IndexOutOfBoundsException. Piece cannot"
                        + " be placed: "+  e.getMessage());
            }         
    }
    
    public void removePiece (PiecesLogic piece){
        try{
            this.board[piece.positionX][piece.positionY] = null;

         } catch (IndexOutOfBoundsException e) {
                System.err.println("Caught IndexOutOfBoundsException. Piece cannot"
                        + " be removed: "+  e.getMessage());

         }
    }
    
    public void replace (String repP, String withP){
        PiecesLogic p1, p2;
        p1= null;
        p2=null;
        for (PiecesLogic p: this.pieces ){
            if (p.NAME.equals(repP) ){
                p1=returnPieceObj (p);
                removePiece(p);
            }    
            if (p.NAME.equals(withP)){
                p2 = returnPieceObj (p);;
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
    
    public Board2D getBoard (){
        return this;
    }
    
    public ArrayList <PiecesLogic> getListPieces (){
        return this.pieces;
    }
    
    public boolean makeMove (Moves m){
        PiecesLogic piece = m.getPiece();
        if (m!= null){
            removePiece(getPieceFromName(piece.NAME));
            piece.positionX = m.getStep().x;
            piece.positionY = m.getStep().y;
            addPiece(piece); 
            return true;
        }
        //test
        else
            System.out.println ("m is null");
        //test
        return false;
    }
    
    private PiecesLogic returnPieceObj ( PiecesLogic piece){
        if (piece instanceof FighterLogic)
            return new FighterLogic (piece);
        else
        if (piece instanceof BomberLogic)
            return new BomberLogic (piece);
        else 
            return new TargetLogic (piece);
    } 
    
    public static void main(String args[]) throws IOException {
        
        ArrayList <PiecesLogic> pieces = new ArrayList<>();
        pieces.add (new FighterLogic ( "B-Fighter", 0, 5, 2));
        pieces.add (new FighterLogic ("W-Fighter", 7, 7, 1));
        pieces.add (new BomberLogic ("B-Bomber", 7, 4, 2, -1));
        pieces.add(new BomberLogic ("W-Bomber", 2, 5, 1, 1));
        pieces.add( new TargetLogic ("B-Target", 2, 7, 2));
        pieces.add( new TargetLogic ("W-Target", 7, 0, 1));        
        
        
        Board2D hi = new Board2D(8,8, pieces);
         Board2D start = new Board2D (hi);
        //GUIFrame gui = new GUIFrame(hi);
        
        WhiteWins test = new WhiteWins (hi);
        test.evaluateWhiteWins();
        System.out.println ("White wins");
        test.getTree().printTreeRelationsMoves();
        
        Strategies.restartSd ();
        BlackWins bTest = new BlackWins (start); 
        bTest.evaluateBlackWins();
        System.out.println ("Black wins");
        bTest.getTree().printTreeRelationsMoves();
        
        
        
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
