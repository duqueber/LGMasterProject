/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LGPackage;

/**
 *
 * @author nati
 */
public class Board {
    
    private static int Yaxis;
    private static int Xaxis;
    private static int Zaxis;
    
    public Board(){};
    public Board (int x, int y, int z){
        this.Yaxis = y;
        this.Xaxis = x;
        this.Zaxis =z;
        //this.squaresize =
        
        System.out.println("The Y chosen by user is " + Yaxis + " and the "
                + "width is " + Xaxis + " and the Z is " + Zaxis);
    }
    
    public final int getUserY(){
        return Yaxis;
    }
    
    public final int getUserX(){
        return Xaxis;
    }

    public final int getUserZ(){
        return Zaxis;
    }
    
}
