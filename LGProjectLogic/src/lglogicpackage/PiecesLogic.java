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
    
    public PiecesLogic (PiecesLogic c){
        this.NAME = c.NAME;
        this.DIRECTION = c.DIRECTION;
        this.TEAM = c.TEAM;
        this.positionX = c.positionX;
        this.positionY = c.positionY;
    }
    
    public int getDirection () {
        return DIRECTION;
    }
    
    public Coordinates getCoordinates (){
        return new Coordinates (this.positionX, this.positionY); 
    }
    
    public int getTeam (){
        return TEAM;    
    }
    
    public void setDirection (int dir){
        DIRECTION = dir;
    }
    @Override
    public boolean equals (Object c){
        if (c == null)
            return false;
   
        if (this.getClass() != c.getClass())
            return false;
        
        PiecesLogic cp = (PiecesLogic)c;
        if (this.NAME.equals(cp.NAME) && this.DIRECTION == cp.DIRECTION &&
            this.TEAM == cp.TEAM && this.positionX == cp.positionX &&
            this.positionY == cp.positionY)
            return true;  
        return false;
    }
    
    
    public abstract Coordinates[] PossibleMoves (int posX, int posY);
    
    public abstract Coordinates[] AttackMoves (int posX, int posY);
    
}
