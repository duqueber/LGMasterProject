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
import java.awt.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.Text2D;
import javax.vecmath.*;
import java.util.ArrayList;


public class BottomBoard
{
  private static int FLOOR_LEN;  // should be even

  // colours for floor, etc

  private final static Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

  private BranchGroup floorBG;
  //private BranchGroup sideBoard;

  public BottomBoard(int xsize, int zsize)
  // create tiles, add origin marker, then the axes labels
  {
    FLOOR_LEN= xsize;  
    ArrayList darkCoords = new ArrayList();
    ArrayList lightCoords = new ArrayList();
    floorBG = new BranchGroup();

    boolean isDark;
    
      for(int z=0; z <= FLOOR_LEN-1; z++) {
      isDark = (z%2 == 0)? true : false;    // set colour for new row
      for(int x=0; x <= FLOOR_LEN-1; x++) {

        if (isDark)
          createCoords(x, z, darkCoords);
        else 
          createCoords(x, z, lightCoords);
        isDark = !isDark;
      }
    
    }
    floorBG.addChild( new ColouredTiles(darkCoords, "cementdark.jpeg") );
    floorBG.addChild( new ColouredTiles(lightCoords, "cementlight.jpeg") );

    //addOriginMarker();
    //labelAxes();
  }  // end of CheckerFloor()


  private void createCoords(int x, int z, ArrayList coords)
  // Coords for a single blue or green square, 
  // its left hand corner at (x,0,z)
  {
    // points created in counter-clockwise order
    Point3f p1 = new Point3f(x, 0.0f, z+1.0f);
    Point3f p2 = new Point3f(x+1.0f, 0.0f, z+1.0f);
    Point3f p3 = new Point3f(x+1.0f, 0.0f, z);
    Point3f p4 = new Point3f(x, 0.0f, z);   
    coords.add(p1); coords.add(p2); 
    coords.add(p3); coords.add(p4);
  }  // end of createCoords()


  private void labelAxes()
  // Place numbers along the X- and Z-axes at the integer positions
  {
    Vector3d pt = new Vector3d();
    pt.z=16;
    for (int i=0; i <= FLOOR_LEN; i++) {
      pt.x = i;
      floorBG.addChild( makeText(pt,""+i) );   // along x-axis
    }

    pt.x =16;
    
    for (int i=FLOOR_LEN; i <= FLOOR_LEN; i++) {
      pt.z = i;
      floorBG.addChild( makeText(pt,""+i) );   // along z-axis
    }
  }  // end of labelAxes()


  private TransformGroup makeText(Vector3d vertex, String text)
  // Create a Text2D object at the specified vertex
  {
    Text2D message = new Text2D(text, white, "SansSerif", 50, Font.BOLD );
       // 36 point bold Sans Serif

    TransformGroup tg = new TransformGroup();
    Transform3D t3d = new Transform3D();
    t3d.setTranslation(vertex);
    tg.setTransform(t3d);
    tg.addChild(message);
    return tg;
  } // end of getTG()


  public BranchGroup getBG()
  {  return floorBG;  }
}  

