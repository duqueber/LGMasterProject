/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package supportpackage;

import lglogicpackage.PiecesLogic;

/**
 *
 * @author nati
 */
public class Table {

    public int columns, rows;
    public int [][] boardInt;
    
    public Table (int rows, int columns, int data){
        
        this.columns = columns;
        this.rows = rows;
        
        boardInt = new int [columns][rows];
        
        for (int i = 0 ; i < columns ; i++){
            for (int j = 0; j < rows; j++)
                boardInt [i][j] = data;
        }
        
    }
    
    public Table ( Table t){
        this.columns = t.columns;
        this.rows = t.rows;
        this.boardInt = t.boardInt;
    }

    public void changeValue (Coordinates c, int value){
        
        try{
            boardInt[c.x][c.y] = value;

         } catch (IndexOutOfBoundsException e) {
                System.err.println("Caught IndexOutOfBoundsException. value cannot"
                        + " be placed: "+  e.getMessage());

            }         
    }
    
    public int getIntValue (int index){
        Coordinates c = Coordinates.getCoordinates(index, this.rows);
        return this.boardInt[c.x][c.y];
    }
    
    public void PrintBoardInt (){
        for (int i = this.rows-1; i >=0 ; i-- ){
            for (int j = 0; j < this.columns; j++){
                System.out.print (boardInt[j][i] + " ");
            }    
        
        System.out.println(' ');
        
        }  
    }
    
}
