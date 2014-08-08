
// WrapCheckers3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Illustrates how to create a simple world. The checkboard
   floor is created by CheckerFloor. The background, lighting,
   key controls, and initial user positioning is done here.

   Most of this will stay the same from one example to another.

   floatingSphere() shows how to create a coloured, shiny object
   which is affected by the lighting in the world.

   The scene graph display code is in WrapCheckers3D() and 
   createSceneGraph().
*/


import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.vp.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import java.awt.*;
import static java.awt.Color.black;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.media.j3d.*;
import static javax.media.j3d.GeometryArray.COORDINATES;
import javax.swing.*;
import javax.vecmath.*;





public class WrapCheckers3D extends JPanel
// Holds the 3D canvas where the loaded image is displayed
{
  private static final int PWIDTH = 1000;   // size of panel
  private static final int PHEIGHT = 712; 

  private static final int BOUNDSIZE = 100;  // larger than world

  private static final Point3d USERPOSN = new Point3d(20,16,45);
              private Scene loadedScene = null;
  private BranchGroup loadedBG = null;

  private SimpleUniverse su;
  private BranchGroup sceneBG;
  private BoundingSphere bounds;   // for environment nodes

   public WrapCheckers3D() throws IOException
  // A panel holding a 3D canvas: the usual way of linking Java 3D to Swing
  {
    setLayout( new BorderLayout() );
    setOpaque( false );
    setPreferredSize( new Dimension(PWIDTH, PHEIGHT));

    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
    Canvas3D canvas3D = new Canvas3D(config);
    add("Center", canvas3D);
    canvas3D.setFocusable(true);     // give focus to the canvas 
    canvas3D.requestFocus();
    canvas3D.setDoubleBufferEnable(true);

    su = new SimpleUniverse(canvas3D);

    createSceneGraph();
    initUserPosition();        // set user's viewpoint
    orbitControls(canvas3D);   // controls for moving the viewpoint

    su.addBranchGraph( sceneBG );

  } // end of WrapCheckers3D()



  private void createSceneGraph() throws IOException
  // initilise the scene
  { 
    sceneBG = new BranchGroup();
    bounds = new BoundingSphere(new Point3d(0,0,0), BOUNDSIZE);
    lightScene();         // add the lights
    addBackground();      // add the sky
    addLine(new Point3f(0,0,0), new Point3f(5,5,5));
    //floatingSphere();     // add the floating sphere
 
    sceneBG.addChild( new BackSide().getBG() );  // add the floor
    sceneBG.addChild( new CheckerFloor().getBG() );  
    sceneBG.addChild( new SideBoard().getBG() );
    sceneBG.addChild(new TexturedBox(15,15,15,0.5f,"cementdark.jpeg").gettbox());
     loadModel("tornado.obj");
    sceneBG.compile();   // fix the scene
  } // end of createSceneGraph()

private void addModelToUniverse() throws IOException {
        Scene scene = getSceneFromFile("tornado.obj"); 
        sceneBG = scene.getSceneGroup();
    }

    public static Scene getSceneFromFile(String location) throws IOException {
        ObjectFile file = new ObjectFile(ObjectFile.RESIZE);
        return file.load(new FileReader(location));
    }
    
    

    
  private void lightScene()
  /* One ambient light, 2 directional lights */
  {
    Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

    // Set up the ambient light
    AmbientLight ambientLightNode = new AmbientLight(white);
    ambientLightNode.setInfluencingBounds(bounds);
    sceneBG.addChild(ambientLightNode);

    // Set up the directional lights
    Vector3f light1Direction  = new Vector3f(-1.0f, -1.0f, -1.0f);
       // left, down, backwards 
    Vector3f light2Direction  = new Vector3f(1.0f, -1.0f, 1.0f);
       // right, down, forwards

    DirectionalLight light1 = 
            new DirectionalLight(white, light1Direction);
    light1.setInfluencingBounds(bounds);
    sceneBG.addChild(light1);

    DirectionalLight light2 = 
        new DirectionalLight(white, light2Direction);
    light2.setInfluencingBounds(bounds);
    sceneBG.addChild(light2);
  }  // end of lightScene()



  private void addBackground()
  // A blue sky
  { Background back = new Background();
    back.setApplicationBounds( bounds );
    back.setColor(0.0f, 0.0f, 0.0f);    // sky colour
    sceneBG.addChild( back );
  }  // end of addBackground()



 private void orbitControls(Canvas3D c)
  //OrbitBehaviour allows the user to rotate around the scene, and to
    // zoom in and out.  
  {
    OrbitBehavior orbit = 
		new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL);
    orbit.setSchedulingBounds(bounds);

    ViewingPlatform vp = su.getViewingPlatform();
    vp.setViewPlatformBehavior(orbit);	    
  }  // end of orbitControls()



  private void initUserPosition()
  // Set the user's initial viewpoint using lookAt()
  {
    ViewingPlatform vp = su.getViewingPlatform();
    TransformGroup steerTG = vp.getViewPlatformTransform();

    Transform3D t3d = new Transform3D();
    steerTG.getTransform(t3d);

    // args are: viewer posn, where looking, up direction
    
    t3d.lookAt( USERPOSN, new Point3d(8,6,8), new Vector3d(0,1,0));
    t3d.invert();

    
    steerTG.setTransform(t3d);
  }  // end of initUserPosition()

 //---------------------------------------------------------------------------
 //load model
  


  
  private void loadModel(String fn)
    /* Load the model from fn into the scene graph using a NCSA 
       Portfolio loader. Rotate and scale it to make it easier to see.
       Store the loaded model's scene in the global loadedScene, 
       and its branch group in loadedBG.
    */
    {
      FileWriter ofw = null;
      System.out.println( "Loading: " + fn );

      try {
        ObjectFile loader = new ObjectFile(ObjectFile.RESIZE) ;    // the NCSA portfolio loader
        // System.out.println("Loader flags: " + loader.getFlags());
        loadedScene = loader.load(fn);             // the loaded scene

        // Rotate and scale the model
        if(loadedScene != null ) {
          loadedBG = loadedScene.getSceneGroup();    // the model's BG
 
          Transform3D t3d = new Transform3D();
          t3d.rotX( -Math.PI/2.0 );    // models are often on their face; fix that
          Vector3d scaleVec = calcScaleFactor(loadedBG, fn);   // scale the model
          t3d.setScale( scaleVec );
          Vector3d translateVec = new Vector3d (10,10,13);
          t3d.setTranslation(translateVec);
          
          TransformGroup tg = new TransformGroup(t3d);
          tg.addChild(loadedBG);

          sceneBG.addChild(tg);   // add (tg->loadedBG) to scene
        }
        else
          System.out.println("Load error with: " + fn);
      }
      catch( IOException ioe )
      { System.err.println("Could not find object file: " + fn); }
    } // end of loadModel()


  private Vector3d calcScaleFactor(BranchGroup loadedBG, String fn)
  // Scale the model based on its original bounding box size
  {
     BoundingBox boundbox = new BoundingBox( loadedBG.getBounds() );
     // System.out.println(boundbox);

     // obtain the upper and lower coordinates of the box
     Point3d lower = new Point3d();
     boundbox.getLower( lower );
     Point3d upper = new Point3d();
     boundbox.getUpper( upper );

     // store the largest X, Y, or Z dimension and calculate a scale factor
     double max = 0.0;
     if( (upper.x - lower.x ) > max )
       max = (upper.x - lower.x );

     if( (upper.y - lower.y ) > max )
       max = (upper.y - lower.y );

     if( (upper.z - lower.z ) > max )
       max = (upper.z - lower.z );

     double scaleFactor = 1.5/max;    // 10 is half the width of the floor
   //  System.out.println("max dimension: " + df.format(max) + 
     //                   "; scaleFactor: " + df.format(scaleFactor) );

     // limit the scaling so that a big model isn't scaled too much
     if( scaleFactor < 0.0005 )
         scaleFactor = 0.0005;

     return new Vector3d(scaleFactor, scaleFactor, scaleFactor);
  }  // end of calcScaleFactor()


private void addLine(Point3f pointa, Point3f pointb){
    
    //The lines are defined in pairs.
    
    Point3f[] dotPts = new Point3f[4];
    dotPts[0] = pointa;
    dotPts[1]=pointb;
    dotPts[2]=pointb;
    dotPts[3]=new Point3f (9,5,5);
    
    Appearance app = new Appearance();
    ColoringAttributes ca = new ColoringAttributes(new Color3f (1,0,0),
    ColoringAttributes.SHADE_FLAT);
    app.setColoringAttributes(ca);

    LineArray lineArr=new LineArray(4,LineArray.COORDINATES);

    lineArr.setCoordinates(0, dotPts);

    
    LineAttributes lineAt =new LineAttributes();
    lineAt.setLineWidth(3.0f);
    lineAt.setLinePattern(LineAttributes.PATTERN_SOLID);
    Appearance dotApp = new Appearance();
    dotApp.setLineAttributes(lineAt);
    dotApp.setColoringAttributes(ca);
    Shape3D dotShape = new Shape3D(lineArr, dotApp);


    sceneBG.addChild(dotShape);
    
}

  // ---------------------- floating sphere -----------------

/*
  private void floatingSphere()
  // A shiny blue sphere located at (0,4,0)
  {
    // Create the blue appearance node
    Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
    Color3f blue = new Color3f(0.3f, 0.3f, 0.8f);
    Color3f specular = new Color3f(0.9f, 0.9f, 0.9f);

    Material blueMat= new Material(blue, black, blue, specular, 25.0f);
       // sets ambient, emissive, diffuse, specular, shininess
    blueMat.setLightingEnable(true);

    Appearance blueApp = new Appearance();
    blueApp.setMaterial(blueMat);

    // position the sphere
    Transform3D t3d = new Transform3D();
    t3d.set( new Vector3f(0,4,0)); 
    TransformGroup tg = new TransformGroup(t3d);
    tg.addChild(new Sphere(2.0f, blueApp));   // set its radius and appearance
 
    sceneBG.addChild(tg);
  }  // end of floatingSphere()
*/
} // end of WrapCheckers3D class