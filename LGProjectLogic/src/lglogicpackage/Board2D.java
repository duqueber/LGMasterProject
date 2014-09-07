package lglogicpackage;

import lggrammars.ShortestTrajectory;
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
    
    public String [][] board;
    public int columns, rows;
    
    public Board2D (int rows, int columns, PiecesLogic [] players ){
        
        this.columns = columns;
        this.rows = rows;
        
        board = new String [columns][rows];
        
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
        
        //test
        /*for (int i = rows-1; i >=0; i-- ){
            for (int j = 0; j < columns; j++){                
                System.out.print ( board[j][i] + "- "+ j +" "+i + "");
            }    
        
        System.out.println(' ');
        } */ 
        //test
    }    
    
    private void addPiece (PiecesLogic piece){
        
        try{
            board[piece.positionX][piece.positionY] = piece.NAME;

         } catch (IndexOutOfBoundsException e) {
                System.err.println("Caught IndexOutOfBoundsException. Piece cannot"
                        + " be placed: "+  e.getMessage());

            }         
    }
    
    private void removePiece (PiecesLogic piece){
        try{
            board[piece.positionX][piece.positionY] = "No piece";

         } catch (IndexOutOfBoundsException e) {
                System.err.println("Caught IndexOutOfBoundsException. Piece cannot"
                        + " be removed: "+  e.getMessage());

         }
    }
    
    public static void main(String args[]) {
        
        PiecesLogic[] pieces = new PiecesLogic[6];
        pieces[0]= new FighterLogic ( "B-Fighter", 0, 5, 2);
        pieces[1]= new FighterLogic ("W-Fighter", 7, 7,1);
        pieces[2]= new BomberLogic ("B-Bomber", 7, 4, 2, -1);
        pieces[3] = new BomberLogic ("W-Bomber", 2, 5, 1, 1);
        pieces[4] = new TargetLogic ("B-Target", 2, 7, 2);
        pieces[5] = new TargetLogic ("W-Target", 7, 0, 1);        
        
        Board2D hi = new Board2D(8,8, pieces);

        
       /* System.out.println ("B=Bomber to W- target" );
        ShortestTrajectory st = new ShortestTrajectory(hi,pieces[2], new Coordinates (7,0));
        st.GenerateShortestTrajectory();
        
        System.out.println ("W-Bomber to B-target" );
        ShortestTrajectory st2 = new ShortestTrajectory(hi,pieces[3], new Coordinates (1,6));
        st2.GenerateShortestTrajectory();
        
        System.out.println ("B-Fighter to W-Bomber" );
        ShortestTrajectory st3 = new ShortestTrajectory(hi,pieces[0], new Coordinates (2,6));
        st3.GenerateShortestTrajectory();*/
        
        System.out.println ("W-Fighter to B-Bomber" );
        ShortestTrajectory st4 = new ShortestTrajectory(hi,pieces[1], new Coordinates (2,7));
        st4.GenerateShortestTrajectory();
    }        
}
