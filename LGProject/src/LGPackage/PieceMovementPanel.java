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
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import javax.media.j3d.*;
import javax.vecmath.*;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class PieceMovementPanel  extends JPanel{

  Dimension dim2 = Toolkit.getDefaultToolkit().getScreenSize();

  private static int PWIDTH ;   
  private static int PHEIGHT ; 

  private static final int BOUNDSIZE = 100;  // larger than world

  private static Point3d USERPOSN;
  //= new Point3d(20,16,45); for 16x16
  private Scene loadedScene = null;
  private BranchGroup loadedBG = null;

  private final SimpleUniverse su;
  private BranchGroup sceneBG;
  private BoundingSphere bounds;  
  private Texture2D texture2;
  private static int x, y, z;

    public PieceMovementPanel(SelectionScreen s, int x, int y, int z) throws IOException
    {   
        this.x=x;
        this.y=z;
        this.z=y;
        setLayout( new BorderLayout() );
        setOpaque( false );

        PWIDTH=  dim2.width/2;
        PHEIGHT = dim2.height/2+60;

        System.out.println("PWIDTH and PHEIGHT " + PWIDTH +  "and " + PHEIGHT);
        setPreferredSize( new Dimension(PWIDTH, PHEIGHT));
        setBorder(new EmptyBorder (10,10,10,10));

        //Create Simple Univers in Canvas
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);
        canvas3D.setFocusable(true);   
        canvas3D.requestFocus();
        canvas3D.setDoubleBufferEnable(true);

        su = new SimpleUniverse(canvas3D);

        // Create a scene, 3 planes, lights, and background
        createSceneGraph();
        orbitControls(canvas3D); 
  } 

    public void addBranchGroup (){
        su.addBranchGraph( sceneBG );    

    }

    private void createSceneGraph() throws IOException
    { 
        sceneBG = new BranchGroup();
        bounds = new BoundingSphere(new Point3d(0,0,0), BOUNDSIZE);
        lightScene();        
        addBackground();     

        sceneBG.addChild( new BackBoard(x, y).getBG() ); 
        sceneBG.addChild( new BottomBoard(x,z).getBG() );  
        sceneBG.addChild( new SideBoard(y,z).getBG() );

        //sceneBG.addChild(new TexturedBox(15,15,15,0.5f,"cementdark.jpeg").gettbox());
        // sceneBG.compile();   // fix the scene
        
    } // end of createSceneGraph()


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

    private void addBackground() { 
        Background back = new Background();
        back.setApplicationBounds( bounds );
        back.setColor(0.0f, 0.0f, 0.0f);   
        sceneBG.addChild( back );
    }  // end of addBackground()

    private void orbitControls(Canvas3D c)
    {
        OrbitBehavior orbit = new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(bounds);

        ViewingPlatform vp = su.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);	    
    }  // end of orbitControls()

    public void initUserPosition(Point3d userPos, Point3d lookAt) 
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

 
    public void loadModel(String fn, Vector3d translateVec){
        FileWriter ofw = null;
        System.out.println( "Loading: " + fn );

        try {
            ObjectFile loader = new ObjectFile(ObjectFile.RESIZE) ;    
            loadedScene = loader.load(fn);            
    
            if(loadedScene != null ) {
                loadedBG = loadedScene.getSceneGroup();    

                listSceneNamedObjects(loadedScene);

                Transform3D t3d = new Transform3D();
                t3d.rotX( -Math.PI/2.0 );  
                Vector3d scaleVec = calcScaleFactor(loadedBG, fn);  
                t3d.setScale( scaleVec );
                t3d.setTranslation(translateVec);

                Transform3D t3d2 = new Transform3D();
                t3d2.rotZ((5*Math.PI)/4);//helicopter x-pi/2
                t3d.mul(t3d2);
                TransformGroup tg = new TransformGroup(t3d);

                tg.addChild(loadedBG);
                sceneBG.addChild(tg); 
            }   
            else
                System.out.println("Load error with: " + fn);
            }
        catch( FileNotFoundException ioe )
        {   System.err.println("Could not find object file: " + fn); }
    } // end of loadModel()

    void listSceneNamedObjects(Scene scene) {
        
        Map<String, Shape3D> nameMap = scene.getNamedObjects();

        Appearance ap = new Appearance ();
        Color3f col = new Color3f(0.0f, 0.0f, 0.0f);
        ColoringAttributes ca = new ColoringAttributes (col, ColoringAttributes.NICEST); 
        ap.setColoringAttributes(ca);
              
        /*/OTHER Appearances     // loadTexture("cementdark.jpeg");
        // ap.setTexture(texture2);
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f red = new Color3f(0.7f, .15f, .15f);
        Material mat = new Material(red, black, red, red, 70f);
        ap.setMaterial(mat);*/
        
        for(Map.Entry<String, Shape3D> entry : nameMap.entrySet()){
            System.out.println(entry.getKey() + "/" + entry.getValue());
            Shape3D newShape = entry.getValue();
            newShape.setAppearance(ap);
        }
    } //End listSceneNamedObjects

    private void loadTexture(String fn)
    {
        TextureLoader texLoader = new TextureLoader(fn, null);
        ImageComponent2D image = texLoader.getImage();
        texture2 = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(),
        image.getHeight());
        texture2.setMagFilter(Texture.BASE_LEVEL_POINT);
        texture2.setImage(0, image);
    }
  
    private Vector3d calcScaleFactor(BranchGroup loadedBG, String fn)  {
     
        BoundingBox boundbox = new BoundingBox( loadedBG.getBounds() );
    
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
      
        LineArray lineArr=new LineArray(4,LineArray.COORDINATES);
        lineArr.setCoordinates(0, dotPts);
        
        ColoringAttributes ca = new ColoringAttributes(new Color3f (1,0,0),
        ColoringAttributes.SHADE_FLAT);

        LineAttributes lineAt =new LineAttributes();
        lineAt.setLineWidth(3.0f);
        lineAt.setLinePattern(LineAttributes.PATTERN_SOLID);
        Appearance dotApp = new Appearance();
        dotApp.setLineAttributes(lineAt);
        dotApp.setColoringAttributes(ca);
        Shape3D dotShape = new Shape3D(lineArr, dotApp);

        sceneBG.addChild(dotShape);
    
    } //End addLine
}//End PieceMovementPanel class
