/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LGPackage;
import java.awt.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.Text2D;
import javax.vecmath.*;
import java.util.ArrayList;


public class SideBoard
{
  private static int SIDE_LEN;  // should be even


  private BranchGroup sideBG;
  //private BranchGroup sideBoard;

  public SideBoard(int ysize, int zsize)
  // create tiles, add origin marker, then the axes labels
  {
    SIDE_LEN = ysize;  
    ArrayList darkCoords = new ArrayList();
    ArrayList lightCoords = new ArrayList();
    sideBG = new BranchGroup();

    boolean isDark;
    for(int y=0; y <= SIDE_LEN-1; y++) {
      isDark = (y%2 == 0)? true : false;    // set colour for new row
      for(int z=0; z <= SIDE_LEN-1; z++) {
        if (isDark)
          createCoords(y, z, darkCoords);
        else 
          createCoords(y, z, lightCoords);
        isDark = !isDark;
      }
    }
    sideBG.addChild( new ColouredTiles(darkCoords, "bluesky.jpg") );
    sideBG.addChild( new ColouredTiles(lightCoords, "whitesky.jpg") );

    //addOriginMarker();
    //labelAxes();
  }  // end of CheckerFloor()


  private void createCoords(int y, int z, ArrayList coords)
  // Coords for a single blue or green square, 
  // its left hand corner at (x,0,z)
  {
    // points created in counter-clockwise order
    Point3f p1 = new Point3f(0, y,z+1.0f);
    Point3f p2 = new Point3f(0, y+1.0f, z+1.0f);
    Point3f p3 = new Point3f(0, y+1.0f, z);
    Point3f p4 = new Point3f(0,y, z);   
    coords.add(p1); coords.add(p2); 
    coords.add(p3); coords.add(p4);
  }  // end of createCoords()


 /* private void addOriginMarker()
  // A red square centered at (0,0,0), of length 0.5
  {  // points created counter-clockwise, a bit above the floor
    Point3f p1 = new Point3f(-0.25f, 0.01f, 0.25f);
    Point3f p2 = new Point3f(0.25f, 0.01f, 0.25f);
    Point3f p3 = new Point3f(0.25f, 0.01f, -0.25f);    
    Point3f p4 = new Point3f(-0.25f, 0.01f, -0.25f);

    ArrayList oCoords = new ArrayList();
    oCoords.add(p1); oCoords.add(p2);
    oCoords.add(p3); oCoords.add(p4);

    floorBG.addChild( new ColouredTiles(oCoords, medRed) );
  } // end of addOriginMarker();


  private void labelAxes()
  // Place numbers along the X- and Z-axes at the integer positions
  {
    Vector3d pt = new Vector3d();
    for (int i=-FLOOR_LEN/2; i <= FLOOR_LEN/2; i++) {
      pt.x = i;
      floorBG.addChild( makeText(pt,""+i) );   // along x-axis
    }

    pt.x = 0;
    for (int i=-FLOOR_LEN/2; i <= FLOOR_LEN/2; i++) {
      pt.z = i;
      floorBG.addChild( makeText(pt,""+i) );   // along z-axis
    }
  }  // end of labelAxes()


  private TransformGroup makeText(Vector3d vertex, String text)
  // Create a Text2D object at the specified vertex
  {
    Text2D message = new Text2D(text, white, "SansSerif", 36, Font.BOLD );
       // 36 point bold Sans Serif

    TransformGroup tg = new TransformGroup();
    Transform3D t3d = new Transform3D();
    t3d.setTranslation(vertex);
    tg.setTransform(t3d);
    tg.addChild(message);
    return tg;
  } // end of getTG()

*/
  public BranchGroup getBG()
  {  return sideBG;  }


}  // end of CheckerFloor class
