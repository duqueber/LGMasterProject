/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package supportpackage;

import lglogicpackage.BomberLogic;
import lglogicpackage.FighterLogic;
import lglogicpackage.PiecesLogic;
import lglogicpackage.TargetLogic;

/**
 *
 * @author nati
 */
public class Moves {
    
    private Coordinates step;
    private PiecesLogic piece;
    
    public Moves ( PiecesLogic piece,Coordinates step){
        this.step = step; 
        setPiece(piece);
    }
    
    public Moves ( PiecesLogic piece){
        this.step = null; 
        setPiece(piece);
    }
    
    private void setPiece (PiecesLogic piece){
        
        if (piece instanceof BomberLogic)
            this.piece = new BomberLogic (piece);
        else
        if (piece instanceof FighterLogic)
            this.piece = new FighterLogic (piece);
        else
            this.piece = new TargetLogic (piece);
    }
    
    public Coordinates getStep (){
        return this.step;
    }
    
    public PiecesLogic getPiece(){
        return this.piece;
    }
    
    public void Print (){
        System.out.println ("Moves: " + piece.NAME + ", ");
        if (step!= null)
        step.PrintCoor();
    }
    
}
