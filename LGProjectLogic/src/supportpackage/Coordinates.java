/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package supportpackage;

import java.util.Vector;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

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
    
    public static Coordinates parseString (String string){
        if (string == null)
            return null;
        
        if (string.length()!= 2)
            return null;
        int number =Integer.parseInt(string);
        return new Coordinates (number/10, number%10);
        
    }
    public static Vector3d convertToGraph (Vector3d f){

        return new Vector3d(7.5-f.x, f.y, f.z+0.5);
    }
    
    public static Vector3f convertToGraph (Vector3f f){

        return new Vector3f((float)7.5-f.x, (float)f.y, (float)(f.z+0.5));
    }
            
    public static String convertToChess (String s){
        Coordinates c = parseString (s);
        String newS = new String ();
        switch (c.x){
            case 0:
                newS = "a"+ (c.y+1);
                break;
            case 1:
                newS = "b" + (c.y+1);
                break;
            case 2:
                newS = "c"+ (c.y+1);
                break;
            case 3:
                newS = "d"+ (c.y+1);
                break;
            case 4:
                newS = "e" + (c.y+1);
                break;
            case 5:
                newS = "f" + (c.y+1);
                break;
            case 6:
                newS = "g"+ (c.y+1);    
                break;
            case 7:
                newS = "h"+ (c.y+1);    
                break;    
        }
        return newS;
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
