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
public class FighterLogic extends PiecesLogic {

    public FighterLogic(String n, int posX, int posY, int team) {
        super(n, posX, posY, team);
    }

    public FighterLogic (PiecesLogic f){
        super(f);
    }
            
    @Override
    public Coordinates[] PossibleMoves(int posX, int posY) {
        Coordinates [] moves = new Coordinates [8];
        moves[0] = new Coordinates (posX-1, posY+1);
        moves[1] = new Coordinates (posX, posY+1);
        moves[2] = new Coordinates (posX+1, posY+1);
        moves[3] = new Coordinates (posX-1, posY);        
        moves[4] = new Coordinates (posX+1, posY);
        moves[5] = new Coordinates (posX-1, posY-1);        
        moves[6] = new Coordinates (posX, posY-1);
        moves[7] = new Coordinates (posX+1, posY-1);
   
        return moves;
    }

    @Override
    public Coordinates[] AttackMoves(int posX, int posY) {
      return PossibleMoves (posX, posY);
    }
}
