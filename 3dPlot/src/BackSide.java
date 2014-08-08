/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.Text2D;
import javax.vecmath.*;
import java.util.ArrayList;


public class BackSide
{
  private final static int BACK_LEN = 16;  // should be even

  // colours for floor, etc
  private final static Color3f blue = new Color3f(0.0f, 0.1f, 0.4f);
  private final static Color3f green = new Color3f(0.0f, 0.5f, 0.1f);
  private final static Color3f medRed = new Color3f(0.8f, 0.4f, 0.3f);
  private final static Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

  private BranchGroup backBG;
  //private BranchGroup sideBoard;

  public BackSide()
  // create tiles, add origin marker, then the axes labels
  {
    ArrayList blueCoords = new ArrayList();
    ArrayList greenCoords = new ArrayList();
    backBG = new BranchGroup();

    boolean isBlue;
    for(int y=0; y <= (BACK_LEN)-1; y++) {
      isBlue = (y%2 == 0)? true : false;    // set colour for new row
      for(int x=0; x <= BACK_LEN-1; x++) {
        if (isBlue)
          createCoords(x, y, blueCoords);
        else 
          createCoords(x, y, greenCoords);
        isBlue = !isBlue;
      }
    }
    backBG.addChild( new ColouredTiles(blueCoords, "bluesky.jpg") );
    backBG.addChild( new ColouredTiles(greenCoords, "whitesky.jpg") );

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
  {  return backBG;  }


}  // end of CheckerFloor class