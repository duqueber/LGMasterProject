/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package supportpackage;

/**
 *
 * @author nati
 */
public class Coordinates {
    
    public int x, y;
    
    public Coordinates () {};
    
    public Coordinates (int x, int y){       
        this.x = x;
        this.y = y;
    }
    
    public void setX (int x) {
        this.x = x;
    }
    
    public void setY (int y) {
        this.y=y;
    }
    
    public int getInteger (int columns){
        return this.x + (this.y * columns);
    }
    
    public static Coordinates getCoordinates(int value, int rows){
        return new Coordinates (value%rows,value/rows ) ;
    }
            
            
            
    public boolean equals (Coordinates c){
        if (this.x == c.x && this.y == c.y)
            return true;
        else 
            return false;
    }
    
    public void PrintCoor (){
        
        System.out.println ("x:" + x + " y:" +y);
    }
}
