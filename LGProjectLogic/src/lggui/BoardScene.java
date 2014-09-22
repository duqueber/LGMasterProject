
package lggui;

import com.microcrowd.loader.java3d.max3ds.Loader3DS;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.vp.*;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Map;
import javax.media.j3d.*;
import static javax.media.j3d.Background.SCALE_FIT_MAX;
import static javax.media.j3d.Background.SCALE_FIT_MIN;
import javax.swing.*;
import javax.vecmath.*;
import supportpackage.Coordinates;


public class BoardScene extends JPanel //implements ActionListener{
{
    Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize(); 
    private final int PWIDTH = screenDim.width-60;   
    private final int PHEIGHT = screenDim.height-60; 
    private final int BOUNDSIZE = 100; 
    private final Point3d USERPOSN = new Point3d(4.0,17.0,1.0);
            //new Point3d(6.5,5,6.5);
    //Point3d(20,16,45)
    
    private Scene loadedScene = null;
    private BranchGroup loadedBG = null;

    private SimpleUniverse su;
    private BranchGroup sceneBG;
    private BoundingSphere bounds; 
    private Texture2D texture2;
    private Timer timer;
    private  JButton button; 
    //private Transform3D t3d;
    //private TransformGroup tg;
    private BoardObjects whiteJet, blackJet,  whiteSaturn;
   
    public BoardScene() throws IOException  {
    
        //timer = new Timer(100, this);
        setLayout( new BorderLayout() );
        setOpaque( false );
        setPreferredSize( new Dimension(PWIDTH, PHEIGHT));

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);
        JPanel b = new JPanel ();
        button = new JButton ("Start");
        b.add(button);
       // button.addActionListener(this);
        add ("North", b);
        canvas3D.setFocusable(true);     // give focus to the canvas 
        canvas3D.requestFocus();
        canvas3D.setDoubleBufferEnable(true);
        su = new SimpleUniverse(canvas3D);
  
        createSceneGraph();
        initUserPosition();        // set user's viewpoint
        orbitControls(canvas3D);   // controls for moving the viewpoint

        su.addBranchGraph( sceneBG );

    }

    private void createSceneGraph() throws IOException
    { 
        sceneBG = new BranchGroup();
       // tg = new TransformGroup();
       // tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
       // sceneBG.addChild(tg);
        bounds = new BoundingSphere(new Point3d(0,0,0), BOUNDSIZE);
        lightScene();         // add the lights
        addBackground();      // add the sky
        sceneBG.addChild( new CheckerFloor().getBG() );     
      
        whiteJet = new BoardObjects ("Su-37_Terminator/Su-37_Terminator.obj" , 
                Coordinates.convertToGraph(new Vector3d(7, 0.1, 7)),
                -Math.PI/2.0, 0.0, -Math.PI/4, 1.5, new Transform3D (), new TransformGroup(), "obj");
        loadModel (whiteJet);
        
        blackJet = new BoardObjects ("Su-35_SuperFlanker/Su-35_SuperFlanker.obj" , 
                Coordinates.convertToGraph(new Vector3d(0, 0.1,6 )),
                -Math.PI/2.0, 0.0, Math.PI/2, 1.5, new Transform3D (), new TransformGroup(), "obj");
        loadModel (blackJet);
        
        whiteSaturn = new BoardObjects ("BForge/BForge.3DS" , 
                Coordinates.convertToGraph(new Vector3d(2, 0.1,5 )),
                0, 0.0, 0, 7.0, new Transform3D (), new TransformGroup(), "3ds");
        loadModel (whiteSaturn);
        //     BoardObjects (String fn, Vector3d translateVec, double roty, double scale, 
          //  Transform3D t3d,TransformGroup tg, String type)

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
 
    private void lightScene(){/* One ambient light, 2 directional lights */
    
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

        DirectionalLight light1 = new DirectionalLight(white, light1Direction);
        light1.setInfluencingBounds(bounds);
        sceneBG.addChild(light1);

        DirectionalLight light2 = new DirectionalLight(white, light2Direction);
        light2.setInfluencingBounds(bounds);
        sceneBG.addChild(light2);
    }  // end of lightScene()

    
    private void addBackground(){ 
        
        TextureLoader txl = new TextureLoader ("755569.jpg", this);
       
        Background back = new Background(txl.getImage());
        back.setApplicationBounds( bounds );
        back.setImageScaleMode(SCALE_FIT_MAX);
        //imageComponent2d
        //back.setImage(null);
        //back.setColor(0.0f, 0.0f, 0.0f);   
        sceneBG.addChild( back );
    }  // end of addBackground()

    private void orbitControls(Canvas3D c){//OrbitBehaviour allows the user to rotate around the scene, and to zoom in and out. 
    
        OrbitBehavior orbit = new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(bounds);

        ViewingPlatform vp = su.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);	    
    }  // end of orbitControls()

    private void initUserPosition(){  // Set the user's initial viewpoint using lookAt()

        ViewingPlatform vp = su.getViewingPlatform();
        TransformGroup steerTG = vp.getViewPlatformTransform();

        Transform3D td = new Transform3D();
        steerTG.getTransform(td);

        // args are: viewer posn, where looking, up direction
        td.lookAt( USERPOSN, new Point3d(4.0,0.0,4.0), new Vector3d(0,1,0));
        td.invert();

        //Point3d(8,6,8)
        steerTG.setTransform(td);
    }  // end of initUserPosition()

       public void loadModel(BoardObjects bo){
           
        FileWriter ofw = null;
        System.out.println( "Loading: " + bo.fileName );
        
        Loader3DS loader = null;
        ObjectFile ld= null;
        try {
            if (bo.type.equals("3ds")){
                 loader = new Loader3DS();
                 loadedScene = loader.load(bo.fileName); 
            }
            else{
                ld = new ObjectFile(ObjectFile.RESIZE) ;  
                loadedScene = ld.load(bo.fileName); 
            }         
    
            if(loadedScene != null ) {
                loadedBG = loadedScene.getSceneGroup();    

                //listSceneNamedObjects(loadedScene);

                bo.t3d.rotX( bo.rotx );  
                Vector3d scaleVec = calcScaleFactor(loadedBG, bo.scale); 
         
                bo.t3d.setScale( scaleVec );
                bo.t3d.setTranslation(bo.translateVec);

                Transform3D t3dY = new Transform3D();
                t3dY.rotY(bo.roty);//helicopter x-pi/2
                bo.t3d.mul(t3dY);
                
                Transform3D t3dZ = new Transform3D();
                t3dZ.rotZ(bo.rotz);//helicopter x-pi/2
                bo.t3d.mul(t3dZ);
                
                bo.tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);             
                bo.tg.setTransform(bo.t3d);
                bo.tg.addChild(loadedBG);
                sceneBG.addChild(bo.tg); 
            }   
            else
                System.out.println("Load error with: " + bo.fileName);
            }
        catch( FileNotFoundException ioe )
        {   System.err.println("Could not find object file: " + bo.fileName); }
    } // end of loadModel()

    void listSceneNamedObjects(Scene scene) {
        
        Map<String, Shape3D> nameMap = scene.getNamedObjects();

        Appearance ap = new Appearance ();
        //Color3f col = new Color3f(0.0f, 0.0f, 0.0f);
        //ColoringAttributes ca = new ColoringAttributes (col, ColoringAttributes.NICEST); 
        //ap.setColoringAttributes(ca);
              
       //loadTexture("cementlight.jpeg");
        ap.setTexture(texture2);
        /*Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
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
  
    private Vector3d calcScaleFactor(BranchGroup loadedBG, double scale){  
        // Scale the model based on its original bounding box size
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

        double scaleFactor = scale/max; 
        // limit the scaling so that a big model isn't scaled too much
        if( scaleFactor < 0.0005 )
            scaleFactor = 0.0005;

        return new Vector3d(scaleFactor, scaleFactor, scaleFactor);
  }  // end of calcScaleFactor()

    private void addLine(Point3f pointa, Point3f pointb){
    
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

   /* @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button)
        {
            if(!timer.isRunning())
            {
                timer.start();
                System.out.println ("hello");
            }
        }
        
        //if the action event's source is not from the button press,
        //it is a timer tick
        else
        {
           
            Vector3d loc = new Vector3d();
            t3d.get(loc);
            
           
            
            int tempX,tempZ;
            tempX = (int) (loc.x*10);
            tempZ = (int)(loc.z*10);
            Vector3d tempVec = new Vector3d (tempX, 0, tempZ);
            System.out.println ("Vector: "+ tempVec.x + ", " + tempVec.y + ", " + tempVec.z);
            Vector3d newLoc = new Vector3d (loc.x+.1f, loc.y , loc.z+.1f );
            if (!tempVec.equals(new Vector3d (37, 0, 37))){
               
            t3d.setTranslation(newLoc);
            tg.setTransform(t3d);} else
             timer.stop();
           
        }
    }*/
} 