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
public class Moves {
    
    private Coordinates step;
    private PiecesLogic piece;
    
    public Moves ( PiecesLogic piece,Coordinates step){
        this.step = step; 
        this.piece = piece;
    }
    
    public Moves ( PiecesLogic piece){
        this.step = null; 
        this.piece = piece;
    }
    
    public Coordinates getStep (){
        return this.step;
    }
    
    public PiecesLogic getPiece(){
        return this.piece;
    }
    
}
