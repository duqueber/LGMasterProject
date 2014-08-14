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
import javax.media.j3d.*;
import javax.vecmath.*;
import java.util.ArrayList;


public class BackBoard
{
  private static int BACK_LEN;  // should be even



  private BranchGroup backBG;
  //private BranchGroup sideBoard;

  public BackBoard(int xsize, int ysize)
  // create tiles, add origin marker, then the axes labels
  {
    BACK_LEN = xsize;  
    ArrayList darkCoords = new ArrayList();
    ArrayList lightCoords = new ArrayList();
    backBG = new BranchGroup();

    boolean isDark;
    for(int y=0; y <= (BACK_LEN)-1; y++) {
      isDark = (y%2 == 0)? true : false;    // set colour for new row
      for(int x=0; x <= BACK_LEN-1; x++) {
        if (isDark)
          createCoords(x, y, darkCoords);
        else 
          createCoords(x, y, lightCoords);
        isDark = !isDark;
      }
    }
    backBG.addChild( new ColouredTiles(darkCoords, "bluesky.jpg") );
    backBG.addChild( new ColouredTiles(lightCoords, "whitesky.jpg") );

    //addOriginMarker();
    //labelAxes();
  }  // end of CheckerFloor()


  private void createCoords(int x, int y, ArrayList coords)
  // Coords for a single blue or green square, 
  // its left hand corner at (x,0,z)
  {
    // points created in counter-clockwise order
    Point3f p1 = new Point3f(x, y+1.0f, 0);
    Point3f p2 = new Point3f(x+1.0f, y+1.0f,0);
    Point3f p3 = new Point3f(x+1.0f, y, 0);
    Point3f p4 = new Point3f(x, y,0);   
    coords.add(p1); coords.add(p2); 
    coords.add(p3); coords.add(p4);
  }  // end of createCoords()


 /* 

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
  {  return backBG;  }


} 