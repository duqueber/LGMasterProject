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
public class TargetLogic extends PiecesLogic {

    public TargetLogic(String n, int posX, int posY, int team) {
        super(n, posX, posY, team);
    }

    @Override
    public Coordinates[] PossibleMoves(int posX, int posY) {
       return null;
    }

    @Override
    public Coordinates[] AttackMoves(int posX, int posY) {
        return null;
    }
    
    
}
