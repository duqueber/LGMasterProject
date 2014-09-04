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
public abstract class PiecesLogic {
    
    // A piece has a name, team (1 is white, 2 is black), movement, position
    public final String NAME;
    public int positionX, positionY; 
    private final int TEAM;
    private int DIRECTION /* North is 1, South is -1*/; 
               
    public PiecesLogic ( String n, int posX, int posY, int team){
        NAME = n;
        positionX = posX;
        positionY = posY;
        TEAM = team;   
        DIRECTION = 1;
    }
    
    public PiecesLogic ( String n, int posX, int posY, int team, int northSouth){
        NAME = n;
        positionX = posX;
        positionY = posY;
        TEAM = team;   
        DIRECTION = northSouth;
    }
    
    public int getDirection () {
        return DIRECTION;
    }
    
    public void setDirection (int dir){
        DIRECTION = dir;
    }
    public abstract Coordinates[] PossibleMoves (int posX, int posY);
    
    public abstract Coordinates[] AttackMoves (int posX, int posY);
    
}
