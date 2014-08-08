
import javax.vecmath.Point3f;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/** This class plots in a 3d space where squares are 1x1x1
 *
 */
public class Plot3d {
    
    public final int x;
    public final int y;
    public final int z;
    
    Plot3d(int xcoor, int ycoor, int zcoor){
    // change the order of the coordinates to adapt to Java3d format
        x= xcoor;
        y= zcoor;
        z= ycoor;
    }
    
    // getp1, p2. p3. p4 get the point of the bottom face ot the cube with
    // coordinates x,y,z
    final Point3f getp1 (){ 
        Point3f p1 = new Point3f(x, y, z+1.0f);
        return p1;
    }
    
    final Point3f getp2 (){
        Point3f p2 = new Point3f(x+1.0f, y, z+1.0f);
        return p2;
    }
    
    final Point3f getp3 (){
         Point3f p3= new Point3f(x+1.0f, y, z);
         return p3;
    }     
    
    final Point3f getp4(){
        Point3f p4 = new Point3f(x, y, z);
        return p4;
    }    

    final Point3f getcenter_bottom (){
        
        Point3f center_bottom = new Point3f(getp4().x+0.5f, y, getp4().z +0.5f);
        return center_bottom;
        
    }
    
    final Point3f getcenter_cube (){
        
        Point3f center_bottom = new Point3f(getp4().x+0.5f, y+0.5f, getp4().z +0.5f);
        return center_bottom;
        
    }
    
}
