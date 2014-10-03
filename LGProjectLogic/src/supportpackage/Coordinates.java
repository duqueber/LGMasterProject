/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package supportpackage;

import java.util.Vector;
import javax.vecmath.Vector3d;

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
    
    public static Vector3d convertToGraph (Vector3d f){

        return new Vector3d(7.5-f.x, f.y, f.z+0.5);
    }
            
            
    @Override
    public boolean equals (Object c){
        if (c == null)
            return false;
   
        if (this.getClass() != c.getClass())
            return false;
        
        if (this.x == ((Coordinates)c).x && this.y == ((Coordinates)c).y)
            return true;
        else 
            return false;
    }
    
    public void PrintCoor (){
        
        System.out.println ("x:" + x + " y:" +y);
    }
}
