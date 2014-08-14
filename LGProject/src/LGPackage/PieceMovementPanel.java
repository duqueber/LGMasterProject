/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LGPackage;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 *
 * @author nati
 */
public class PieceMovementPanel  extends JPanel{
    
  Dimension dim2 = Toolkit.getDefaultToolkit().getScreenSize();
  
  private static int PWIDTH ;   
  private static int PHEIGHT ; 
  
  private static final int BOUNDSIZE = 100;  // larger than world

  private static Point3d USERPOSN;
          //= new Point3d(20,16,45);
  private Scene loadedScene = null;
  private BranchGroup loadedBG = null;

  private SimpleUniverse su;
  private BranchGroup sceneBG;
  private BoundingSphere bounds;   // for environment nodes
  private Texture2D texture2;
  private static int x, y, z;
  
   public PieceMovementPanel(SelectionScreen s, int x, int y, int z) throws IOException
  // A panel holding a 3D canvas: the usual way of linking Java 3D to Swing
  {
       
    this.x=x;
    this.y=z;
    this.z=y;
    setLayout( new BorderLayout() );
    setOpaque( false );
    
    PWIDTH=  dim2.width/2;
            //- s.board.getWidth();
    PHEIGHT = dim2.height/2+60;
    
    System.out.println("PWIDTH and PHEIGHT " + PWIDTH +  "and " + PHEIGHT);
    setPreferredSize( new Dimension(PWIDTH, PHEIGHT));
    setBorder(new EmptyBorder (10,10,10,10));

    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
    Canvas3D canvas3D = new Canvas3D(config);
    add("Center", canvas3D);
    canvas3D.setFocusable(true);     // give focus to the canvas 
    canvas3D.requestFocus();
    canvas3D.setDoubleBufferEnable(true);

    su = new SimpleUniverse(canvas3D);

    createSceneGraph();

    orbitControls(canvas3D); 
  } 

public void addBranchGroup (){
  su.addBranchGraph( sceneBG );    

}

  private void createSceneGraph() throws IOException
  // initilise the scene
  { 
    sceneBG = new BranchGroup();
    bounds = new BoundingSphere(new Point3d(0,0,0), BOUNDSIZE);
    lightScene();         // add the lights
    addBackground();      // add the sky
 
    sceneBG.addChild( new BackBoard(x, y).getBG() );  // add the floor
    sceneBG.addChild( new BottomBoard(x,z).getBG() );  
    sceneBG.addChild( new SideBoard(y,z).getBG() );
    //sceneBG.addChild(new TexturedBox(15,15,15,0.5f,"cementdark.jpeg").gettbox());
    // loadModel("tornado.obj");
   // sceneBG.compile();   // fix the scene
  } // end of createSceneGraph()

public void addModelToUniverse() throws IOException {
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



  public void initUserPosition(Point3d userPos, Point3d lookAt) 
  // Set the user's initial viewpoint using lookAt()
  {
    USERPOSN = userPos;  
    ViewingPlatform vp = su.getViewingPlatform();
    TransformGroup steerTG = vp.getViewPlatformTransform();

    Transform3D t3d = new Transform3D();
    steerTG.getTransform(t3d);

    // args are: viewer posn, where looking, up direction
    
    t3d.lookAt( USERPOSN, lookAt, new Vector3d(0,1,0));
    t3d.invert();

    
    steerTG.setTransform(t3d);
  }  // end of initUserPosition()

 //---------------------------------------------------------------------------
 //load model
  


  
  public void loadModel(String fn, Vector3d translateVec)

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
 
         listSceneNamedObjects(loadedScene);
               
          Transform3D t3d = new Transform3D();
          t3d.rotX( -Math.PI/2.0 );    // models are often on their face; fix that
          // t3d.rotY (Math.PI);
         //t3d.rotZ( Math.PI/4.0 );
          //t3d.rotZ( Math.PI/2.0 );
          Vector3d scaleVec = calcScaleFactor(loadedBG, fn);   // scale the model
          t3d.setScale( scaleVec );
          //Vector3d translateVec = new Vector3d (10,10,13);
          t3d.setTranslation(translateVec);
          
          
          Transform3D t3d2 = new Transform3D();
          t3d2.rotZ((5*Math.PI)/4);//helicopter x-pi/2
          
          t3d.mul(t3d2);
          TransformGroup tg = new TransformGroup(t3d);
          //tg.
          

          //loadedScene.
          tg.addChild(loadedBG);
 
          sceneBG.addChild(tg);   // add (tg->loadedBG) to scene
         
        }
        else
          System.out.println("Load error with: " + fn);
      }
      catch( IOException ioe )
      { System.err.println("Could not find object file: " + fn); }
    } // end of loadModel()

void listSceneNamedObjects(Scene scene) {
    Map<String, Shape3D> nameMap = scene.getNamedObjects();
    

              Appearance ap = new Appearance ();
          Color3f col = new Color3f(0.0f, 0.0f, 0.0f);
          ColoringAttributes ca = new ColoringAttributes (col, ColoringAttributes.NICEST); 
          ap.setColoringAttributes(ca);
              
             // loadTexture("cementdark.jpeg");
             // ap.setTexture(texture2);
  /*               Color3f black = new Color3f(0.0f, 0.0f, 0.0f);

   Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

   Color3f red = new Color3f(0.7f, .15f, .15f);
              Material mat = new Material(red, black, red, red, 70f);
              ap.setMaterial(mat);*/
    for(Map.Entry<String, Shape3D> entry : nameMap.entrySet())
{
    System.out.println(entry.getKey() + "/" + entry.getValue());
    Shape3D newShape = entry.getValue();
    newShape.setAppearance(ap);
}
 }

private void loadTexture(String fn)
{
    TextureLoader texLoader = new TextureLoader(fn, null);
    ImageComponent2D image = texLoader.getImage();
    texture2 = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(),
    image.getHeight());
    texture2.setMagFilter(Texture.BASE_LEVEL_POINT);
    texture2.setImage(0, image);
}
  
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

     double scaleFactor = 1.0/max;   
     if( scaleFactor < 0.0005 )
         scaleFactor = 0.0005;

     return new Vector3d(scaleFactor, scaleFactor, scaleFactor);
  }  // end of calcScaleFactor()


public void addLine(Point3f pointa, Point3f pointb){
      
    Point3f[] dotPts = new Point3f[2];
    dotPts[0] = pointa;
    dotPts[1]=pointb;
   // dotPts[2]=pointb;
   // dotPts[3]=new Point3f (9,5,5);
    
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
}
