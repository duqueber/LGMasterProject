
package lggui;

import com.microcrowd.loader.java3d.max3ds.Loader3DS;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.vp.*;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javax.media.j3d.*;
import static javax.media.j3d.Background.SCALE_FIT_MAX;
import javax.swing.*;
import javax.vecmath.*;
import lggrammars.Zones;
import lglogicpackage.Board2D;
import lglogicpackage.Gateways;
import lglogicpackage.PiecesLogic;
import supportpackage.Coordinates;
import supportpackage.Tree;


public class BoardScene extends JPanel{

    

    private final int BOUNDSIZE = 100; 
    private final Point3d USERPOSN = new Point3d(4.0,10.5,-1.0);
           // new Point3d(4.0,17.0,1.0);
    private final Color3f WHITE = new Color3f(Color.WHITE);
    private final Color3f BLACK = new Color3f (Color.BLACK);
            //new Point3d(6.5,5,6.5);
    //Point3d(20,16,45)
    
    private Scene loadedScene = null;
    private BranchGroup loadedBG = null;

    private SimpleUniverse su;
    private BranchGroup sceneBG;
    private BranchGroup bg2 = new BranchGroup ();
    private BoundingSphere bounds; 
    private Texture2D texture2;
    private Timer timer;
    private  JButton button; 
    static final double y = 0.8;
    private Board2D board;
    private URL filename = null;
    BoardObjects whiteFighter, blackFighter,  whiteBomber, blackBomber,
            whiteTarget, blackTarget;
    BoardObjects [] gwPiecesInt = new BoardObjects [8];
    BoardObjects [] gwPiecesPro = new BoardObjects [8];
    ArrayList<Coordinates> gwInt = new ArrayList<> ();
    ArrayList <Coordinates> gwProt = new ArrayList<> ();
    private Zones mainBlack = null;
    private Zones mainWhite = null;
    static final Vector3d defaultV = new Vector3d (10, 0, 10);
    static final Color3f defaultColor = new Color3f (Color.GRAY);
    private double gwScale = 0.0;
    private  ArrayList<ArrayList<supportpackage.Node<Coordinates>>> GWZoneInt = new ArrayList<> ();
    private  ArrayList<ArrayList<supportpackage.Node<Coordinates>>> GWZoneProt= new ArrayList<> ();
    
    public BoardScene(Board2D board) throws IOException  {
    
        this.board = board;
       // timer = new Timer(100, this);
        setLayout( new BorderLayout() );
        setOpaque( false );
        //setPreferredSize( new Dimension(PWIDTH, PHEIGHT));

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);
        
        
        canvas3D.setPreferredSize(new Dimension(GUIFrame.BHEIGHT, GUIFrame.PHEIGHT));
        
        canvas3D.setFocusable(true);     // give focus to the canvas 
        canvas3D.requestFocus();
        canvas3D.setDoubleBufferEnable(true);
        this.su = new SimpleUniverse(canvas3D);
        this.bg2.setCapability(BranchGroup.ALLOW_DETACH);
        this.sceneBG = new BranchGroup();
        this.sceneBG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        this.sceneBG.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        this.sceneBG.setCapability(BranchGroup.ALLOW_DETACH);
        
        createSceneGraph();
        initUserPosition();        // set user's viewpoint
        orbitControls(canvas3D);   // controls for moving the viewpoint

        su.addBranchGraph( this.sceneBG );
        
    }

    private void createSceneGraph() throws IOException{ 

        bounds = new BoundingSphere(new Point3d(0,0,0), BOUNDSIZE);
        lightScene();         // add the lights
        addBackground();      // add the sky
        sceneBG.addChild( new CheckerFloor().getBG() );   
        prepareModels (this.board);
    } // end of createSceneGraph()

    public void prepareModels (Board2D board){
        
        Coordinates wf = board.getPieceFromName("W-Fighter").getCoordinates();
        Coordinates bf = board.getPieceFromName("B-Fighter").getCoordinates();
        Coordinates bb = board.getPieceFromName("B-Bomber").getCoordinates();
        Coordinates wb = board.getPieceFromName("W-Bomber").getCoordinates();
        Coordinates bt = board.getPieceFromName("B-Target").getCoordinates();
        Coordinates wt = board.getPieceFromName("W-Target").getCoordinates();
        
        whiteFighter = new BoardObjects ("chess/WhitePieces/King.obj" , 
                Coordinates.convertToGraph(new Vector3d(wf.x, this.y,wf.y )),
                0.0, 0.0, 0.0, 2.0, new Transform3D (), new TransformGroup(), 
                WHITE, "obj");
        loadModel (whiteFighter);
        
        blackFighter = new BoardObjects ("chess/BlackPieces/King.obj" , 
                Coordinates.convertToGraph(new Vector3d(bf.x, this.y, bf.y)),
                0.0, 0.0, 0.0, 2.0, new Transform3D (), new TransformGroup(), 
                BLACK, "obj");
        loadModel (blackFighter);

        whiteBomber = new BoardObjects ("chess/WhitePieces/pawn.obj" , 
                Coordinates.convertToGraph(new Vector3d(wb.x, this.y,wb.y )),
                0.0, 0.0, 0.0, 1.5, new Transform3D (), new TransformGroup(), 
                WHITE, "obj");
        loadModel (whiteBomber);
        
        blackBomber = new BoardObjects ("chess/BlackPieces/pawn.obj" , 
                Coordinates.convertToGraph(new Vector3d(bb.x, this.y,bb.y )),
                0.0, 0.0, 0.0, 1.5, new Transform3D (), new TransformGroup(), 
                BLACK, "obj");
        loadModel (blackBomber);
        
        calculateGateways(board);
        loadGateways ();

    }
    private void loadGateways (){
        
        for (Coordinates i: this.gwInt){
            BoardObjects blackGW = new BoardObjects ("chess/BlackPieces/Circle.obj" , 
            Coordinates.convertToGraph(new Vector3d(i.x, 0, i.y)),-Math.PI/2, 
            0.0, 0.0, 1.0, new Transform3D (), new TransformGroup(), BLACK, "obj");
            this.gwPiecesInt[i.x] = blackGW;
            loadModel (blackGW);
        }
        
         for (Coordinates i: this.gwProt){
            BoardObjects blackGW = new BoardObjects ("chess/WhitePieces/Circle.obj" , 
            Coordinates.convertToGraph(new Vector3d(i.x, 0, i.y)),-Math.PI/2, 
            0.0, 0.0, 1.0, new Transform3D (), new TransformGroup(), BLACK, "obj");
            this.gwPiecesPro[i.y] = blackGW;
            loadModel (blackGW);
        }
        BoardObjects defaultGw = null; 
        for (int i = 4; i < this.gwPiecesPro.length; i++){
            if (this.gwPiecesPro[i] == null){
                defaultGw = new BoardObjects ("chess/WhitePieces/Circle.obj", 
                Coordinates.convertToGraph(defaultV),-Math.PI/2, 
                0.0, 0.0, 1.0, new Transform3D (), new TransformGroup(), defaultColor, "obj");
                this.gwPiecesPro[i] = defaultGw;
                loadModel (defaultGw);
            }   
        }
        
        for (int i = 4; i < this.gwPiecesInt.length; i++){
            if (this.gwPiecesInt[i] == null){
                defaultGw = new BoardObjects ("chess/BlackPieces/Circle.obj", 
                Coordinates.convertToGraph(defaultV),-Math.PI/2, 
                0.0, 0.0, 1.0, new Transform3D (), new TransformGroup(), defaultColor, "obj");
                this.gwPiecesInt[i] = defaultGw;
                loadModel (defaultGw);
            }   
        }
        this.gwScale = this.gwPiecesInt[4].t3d.getScale();
    }
    
    public void showGwZones (){
        
    }
    
    public void showZones(Board2D board) {

       this.bg2.removeAllChildren();

        calculateZones (board);
        if (this.mainBlack != null)
            addZones (this.mainBlack);
        if (this.mainWhite != null)
            addZones (this.mainWhite);    
        
        for (ArrayList<supportpackage.Node<Coordinates>> trajToGW: this.GWZoneProt)
                    drawShortestPath(trajToGW, WHITE);
        
        for (ArrayList<supportpackage.Node<Coordinates>> trajToGW: this.GWZoneInt)
                    drawShortestPath(trajToGW,BLACK);
        this.sceneBG.addChild(this.bg2);
        
        /*ArrayList <Tree <Zones.Trajectory>> w= mainWhite.getZonesTree();
        ArrayList <Tree <Zones.Trajectory>> b= mainBlack.getZonesTree();
        
        
        Gateways g = new Gateways (this.board, b.get(0), w.get(0), null);
      
        ArrayList <Coordinates> bGatewaysP =g.getBlackGatewaysProtect();
        ArrayList <Coordinates> wGatewaysP =g.getWhiteGatewaysProtect();
        ArrayList <Coordinates> bGatewaysI =g.getBlackGatewaysIntercept();
        ArrayList <Coordinates> wGatewaysI =g.getWhiteGatewaysIntercept();
        
        System.out.println("black coordinates P");
        for (Coordinates bG : bGatewaysP){
            bG.PrintCoor();
        }
        
        System.out.println("white coordinates P");
           for (Coordinates wG : wGatewaysP){
            wG.PrintCoor();
        }
           
        System.out.println("black coordinates I");
            for (Coordinates bG : bGatewaysI){
            bG.PrintCoor();
        }
        
        System.out.println("white coordinates I");
           for (Coordinates wG : wGatewaysI){
            wG.PrintCoor();
        }
        

        ArrayList<ArrayList<supportpackage.Node<Coordinates>>> pathsToGW;
        pathsToGW = g.generateGatewaysZones(Gateways.Teams.WHITE, Gateways.Types.PROTECT);

        for (ArrayList<supportpackage.Node<Coordinates>> trajToGW: pathsToGW){
                    drawShortestPath(trajToGW, WHITE);
        }
        
        
        ArrayList<ArrayList<supportpackage.Node<Coordinates>>> pathsToGWII;
        pathsToGW = g.generateGatewaysZones(Gateways.Teams.BLACK, Gateways.Types.INTERCEPT);

        for (ArrayList<supportpackage.Node<Coordinates>> trajToGWII: pathsToGW){
                    drawShortestPath(trajToGWII, WHITE);
        }
        */
     }//end of testfunction
    
    public void calculateZones (Board2D board){
        
        PiecesLogic startPiece = board.getPieceFromName("B-Bomber");
           if (startPiece != null){
            this.mainBlack = new Zones (board,startPiece, board.getPieceFromName("W-Target"));
            this.mainBlack.GenerateZones();
        }
        else 
            this.mainBlack = null;       
           
        startPiece = board.getPieceFromName("W-Bomber");
        if (startPiece != null){
            this.mainWhite = new Zones (board,startPiece, board.getPieceFromName("B-Target"));
            this.mainWhite.GenerateZones();
        }
        else 
            this.mainWhite = null;
    }
    
    public void calculateGateways (Board2D board){
        calculateZones (board);
        Tree<Zones.Trajectory> black, white;
        if (this.mainBlack!= null)
            black = this.mainBlack.getZonesTree().get(0);
        else 
            black = null;
        if (this.mainWhite != null)
            white = this.mainWhite.getZonesTree().get(0);
        else
            white=null;
        
        Gateways g = new Gateways (board, black, white, null);
      
        this.gwProt =g.getWhiteGatewaysProtect();
        this.gwInt = g.getBlackGatewaysIntercept();
        boolean isOverlap = false;
        
        for (Coordinates gP: gwProt){
            for  (Coordinates gI: gwInt){
                if (gP.equals(gI)){
                        isOverlap = true;
                        this.gwPiecesPro[gP.y].t3d.setScale(this.gwScale/1.4);
                        this.gwPiecesPro[gP.y].t3d.setTranslation(Coordinates.convertToGraph(new Vector3d(gP.x, 0.1, gP.y)));
                        this.gwPiecesPro[gP.y].tg.setTransform(this.gwPiecesPro[gP.y].t3d);
                }  
                else
                   if (this.gwPiecesPro[gP.y]!= null){
                        this.gwPiecesPro[gP.y].t3d.setScale(this.gwScale);
                        this.gwPiecesPro[gP.y].t3d.setTranslation(Coordinates.convertToGraph(new Vector3d(gP.x, 0, gP.y)));
                        this.gwPiecesPro[gP.y].tg.setTransform(this.gwPiecesPro[gP.y].t3d);
                   }    
            }
        }
        
        this.GWZoneProt = g.generateGatewaysZones(Gateways.Teams.WHITE, Gateways.Types.PROTECT);
        this.GWZoneInt = g.generateGatewaysZones(Gateways.Teams.BLACK, Gateways.Types.INTERCEPT);
        
    }    
    public void removeZones (){
        this.sceneBG.removeChild (this.bg2);
    }
    
    private void addModelToUniverse() throws IOException {
        Scene scene = getSceneFromFile("chess/King.3DS"); 
        sceneBG = scene.getSceneGroup();
    }

    public static Scene getSceneFromFile(String location) throws IOException {
        ObjectFile file = new ObjectFile(ObjectFile.RESIZE);
        return file.load(new FileReader(location));
    }
 
    private void lightScene(){/* One ambient light, 2 directional lights */
    
        Color3f white = new Color3f(Color.WHITE);

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
        
        /*TextureLoader txl = new TextureLoader ("755569.jpg", this);
       
        Background back = new Background(txl.getImage());
        back.setApplicationBounds( bounds );
        back.setImageScaleMode(SCALE_FIT_MAX);
        //imageComponent2d
        //back.setImage(null);
        //back.setColor(0.0f, 0.0f, 0.0f);   
        sceneBG.addChild( back );*/
        
          // A blue sky
        Background back = new Background();
        back.setApplicationBounds( bounds );
        back.setColor(0.119f, 0.136f, 0.153f); //(119,136,153)   // sky colour
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
//new Point3d(4.0,0.0,4.0)
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

               // listSceneNamedObjects(loadedScene, bo.color);

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
                //this.sceneBG.addChild(this.loadedBG);
            }   
            else
                System.out.println("Load error with: " + bo.fileName);
            }
        catch( FileNotFoundException ioe )
        {   System.err.println("Could not find object file: " + bo.fileName); }
    } // end of loadModel()

    void listSceneNamedObjects(Scene scene, Color3f cc) {
        
        Map<String, Shape3D> nameMap = scene.getNamedObjects();

        Appearance ap = new Appearance ();
      /*  Color3f col = new Color3f(0.0f, 1.0f, 0.0f);
        ColoringAttributes ca = new ColoringAttributes (col, ColoringAttributes.NICEST); 
        ap.setColoringAttributes(ca);*/
              
       //loadTexture("cementlight.jpeg");
        //ap.setTexture(texture2);
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f color = cc;
        Material mat = new Material(color, black, color, white, 70f);
        ap.setMaterial(mat);
        
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

    private void addZones (Zones z){
        
        ArrayList <Tree <Zones.Trajectory>> m= z.getZonesTree();
        
        for (Tree<Zones.Trajectory> zone : m){
            ArrayList<supportpackage.Node<Zones.Trajectory>> t;
            t = zone.getNodes();
            
            for (supportpackage.Node<Zones.Trajectory> node : t)
                drawTrajectory (node.getData());
        }        
    }
    
    private void drawTrajectory (Zones.Trajectory t){
        Color3f color;
        if (t.getPieceName() == "W-Fighter" ||t.getPieceName() =="W-Bomber")
            color= WHITE; 
        else
            color= BLACK; 
            
        ArrayList <supportpackage.Node<Coordinates>> sp= t.getShortestPath();
        drawShortestPath (sp, color);
        
    }
    
    private void drawShortestPath (ArrayList <supportpackage.Node<Coordinates>>  sp, Color3f color){
        
        Iterator<supportpackage.Node<Coordinates>> itFirst = sp.iterator();
        Iterator<supportpackage.Node<Coordinates>> itSecond = sp.iterator();
        if (itSecond.hasNext())
            itSecond.next();
        while (itSecond.hasNext()){
            addLine (itFirst.next().getData(), itSecond.next().getData(), color);
        }       
    }
    
    
    private void addLine(Coordinates pointa, Coordinates pointb, Color3f c){
    
        Vector3d vecA =Coordinates.convertToGraph(new Vector3d (pointa.x, 1, pointa.y));
        Vector3d vecB =Coordinates.convertToGraph(new Vector3d (pointb.x, 1, pointb.y));
        Point3f[] pts = new Point3f [2];
        pts[0] = new Point3f ((float)vecA.x, (float)vecA.y/5, (float)vecA.z);
        pts[1]=new Point3f ((float)vecB.x, (float)vecB.y/5, (float)vecB.z);
     
        
        PointArray pointArray = new PointArray(pts.length, GeometryArray.COORDINATES );
        pointArray.setCoordinates (0, pts);
        Appearance pointAp = new Appearance ();
        PointAttributes pointAt = new PointAttributes (8, true);
        pointAp.setPointAttributes(pointAt);
        pointAp.setColoringAttributes (new ColoringAttributes (new Color3f(Color.BLACK), ColoringAttributes.NICEST));
        
        Shape3D dot = new Shape3D (pointArray, pointAp);
        this.bg2.addChild (dot);
        
        Appearance app = new Appearance();
        ColoringAttributes ca = new ColoringAttributes(c,ColoringAttributes.NICEST);
        app.setColoringAttributes(ca);

        LineArray lineArr=new LineArray(2,LineArray.COORDINATES);

        lineArr.setCoordinates(0, pts);

        LineAttributes lineAt =new LineAttributes();
        lineAt.setLineWidth(3.0f);
        //lineAt.setLinePattern(LineAttributes.ALLOW_PATTERN_WRITE | LineAttributes.PATTERN_SOLID);
        Appearance dotApp = new Appearance();
        dotApp.setLineAttributes(lineAt);
        dotApp.setColoringAttributes(ca);
        Shape3D dotShape = new Shape3D(lineArr, dotApp);
        this.bg2.addChild(dotShape);

        
    }
/*
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button)
        {
            if(!timer.isRunning())
            {
                timer.start();
                System.out.println ("hello");
        }      
        //if the action event's source is not from the button press,
        //it is a timer tick
        else
        {
           /* Transform3D t3dstep = new Transform3D();
            Matrix4d matrix = new Matrix4d();
            t3dstep.rotZ(-Math.PI / 256);
            whiteTarget.tg.getTransform(whiteTarget.t3d);
            whiteTarget.t3d.get(matrix);
            whiteTarget.t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
            whiteTarget.t3d.mul(t3dstep);
            whiteTarget.t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
            whiteTarget.tg.setTransform(whiteTarget.t3d);
            
            Vector3d loc = new Vector3d();
            whiteFighter.t3d.get(loc);

            Vector3d newLoc = new Vector3d (loc.x+.1f, loc.y , loc.z-.1f );
            Vector3d stop = Coordinates.convertToGraph(new Vector3d (6, 0, 6));
            
            if (loc.x< stop.x && loc.z>stop.z){  
                whiteFighter.t3d.setTranslation(newLoc);
                whiteFighter.tg.setTransform(whiteFighter.t3d);
            } 

           
        }
    }
*/

} 