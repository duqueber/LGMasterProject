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
    
    private static int height;
    private static int width;
 
    
    public Board(){};
    public Board (int h, int w){
        this.height = h;
        this.width = w;
        //this.squaresize =
        
        System.out.println("The height chosen by user is " + height + " and the "
                + "width is " + width);
    }
    
    public final int getUserHeight(){
        return height;
    }
    
    public final int getUserWidth(){
        return width;
    }

}
