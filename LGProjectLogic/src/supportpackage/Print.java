/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package supportpackage;

import java.util.ArrayList;
import lggrammars.Zones;

/**
 *
 * @author nati
 */
public class Print {
    
    public Print (){};
    
    public static void PrintTable (int [][] obj,int rows, int columns){
        
        for (int i = rows-1; i >=0 ; i-- ){
            for (int j = 0; j < columns; j++){
                System.out.print (obj[j][i] + " ");
            }    
        
        System.out.println(' ');
        
        }    
    }
    
    public static void PrintArray ( ArrayList<Node<Coordinates>> a){
        
        System.out.print ("List: ");
        for (Node<Coordinates> aObj : a){
            System.out.print (""  + aObj.getData().x + ", " + aObj.getData().y + " / ");
        }
    }
}