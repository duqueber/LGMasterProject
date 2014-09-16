/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lglogicpackage;

import supportpackage.Coordinates;

/**
 *
 * @author nati
 */
public class BomberLogic extends PiecesLogic {

    public BomberLogic(String n, int posX, int posY, int team, int northSouth) {
        super(n, posX, posY, team, northSouth);
    }
    
    public BomberLogic (PiecesLogic b){
        super (b);
    }
    
    @Override
    public Coordinates[] PossibleMoves(int posX, int posY) {
        Coordinates[] moves = new Coordinates [1];
        
        if (this.getDirection() == 1){
            moves[0] = new Coordinates (posX,posY+1);
        } 
        else {
            moves[0] = new Coordinates (posX,posY-1);
        }
        
        return moves;        
    }

    @Override
    public Coordinates[] AttackMoves(int posX, int posY) {
        Coordinates [] moves = new Coordinates[2];
        
        if (this.getDirection() == 1){
            moves[0] = new Coordinates (posX-1, posY+1);
            moves[1] = new Coordinates (posX+1, posY+1);
        }    
        else {
            moves[0] = new Coordinates (posX-1, posY-1);
            moves[1] = new Coordinates (posX+1, posY-1);                
        }
        
        return moves;
    }
    
}
