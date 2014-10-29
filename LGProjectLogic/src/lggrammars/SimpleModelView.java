/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lggrammars;

/**
 *
 * @author nati
 */
import com.microcrowd.loader.java3d.max3ds.Loader3DS;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import javax.media.j3d.*;
import javax.swing.JFrame;
import javax.vecmath.*;


/**
 * This class creates a very simple window in which a 3D canvas occupies the 
 * entire window and displays a 3D model loaded from disk. You will have to 
 * change the line where the path of the model is set.
 *
 * @author Dalton Filho
 * @since 12/28/2005
 */
public class SimpleModelView extends JFrame {

    
    // INSTANCE ****************************************************************

    /** The canvas where the object is rendered. */
    private Canvas3D canvas;

    /** Simplifies the configuration of the scene. */
    private SimpleUniverse universe;

    /** The root node of the scene. */
    private BranchGroup root= new BranchGroup ();
    
    // INITIALIZATION **********************************************************

    /**
     * Creates a window with a 3D canvas on its center.
     *
     * @throws IOException if some error occur while loading the model
     */
    public SimpleModelView() throws IOException {
        configureWindow();
        configureCanvas();
        conigureUniverse();
        addModelToUniverse();
        addLightsToUniverse();
        root.compile();
        universe.addBranchGraph(root);
    }

    /**
     * Defines basic properties of this window.
     */
    private void configureWindow() {
        setTitle("Basic Java3D Program");
        setSize(640, 480);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int locationX = (screenSize.width - getWidth()) / 2;
        int locationY = (screenSize.height - getHeight()) / 2;
        setLocation(locationX,locationY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Defines basic properties of the canvas. 
     */
    private void configureCanvas() {
        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        canvas.setDoubleBufferEnable(true);
        getContentPane().add(canvas, BorderLayout.CENTER);
    }

    /** 
     * Defines basic properties of the universe.
     */
    private void conigureUniverse() {
        universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
    }

    /**
     * Loads a model from disk and assign the root node of the scene
     *
     * @throws IOException if it's impossible to find the 3D model
     */
    private void addModelToUniverse() throws IOException {
        Scene scene = getSceneFromFile("/home/nati/NetBeansProjects/LGProjectLogic/chess/chess_obj/chess_3ds.obj"); 
       // listSceneNamedObjects(scene);
       // root = scene.getSceneGroup();
        root.addChild(scene.getSceneGroup());
    }

    /** 
     * Adds a dramatic blue light... 
     */
    private void addLightsToUniverse() {
        Bounds bounds = new BoundingSphere();
        /*Color3f lightColor = new Color3f(Color.BLUE);
        Vector3f lightDirection = new Vector3f(-1F, -1F, -1F);
        DirectionalLight light = new DirectionalLight(lightColor, lightDirection);
        light.setInfluencingBounds(influenceRegion);
        root.addChild(light);
                Color3f white = new Color3f(1.0f, 1.0f, 1.0f);*/
        Color3f white = new Color3f (Color.WHITE);        // Set up the ambient light
        AmbientLight ambientLightNode = new AmbientLight(white);
        ambientLightNode.setInfluencingBounds(bounds);
        root.addChild(ambientLightNode);

        // Set up the directional lights
        Vector3f light1Direction  = new Vector3f(-1.0f, -1.0f, -1.0f);
        // left, down, backwards 
        Vector3f light2Direction  = new Vector3f(1.0f, -1.0f, 1.0f);
        // right, down, forwards

        DirectionalLight light1 = new DirectionalLight(white, light1Direction);
        light1.setInfluencingBounds(bounds);
        root.addChild(light1);

        DirectionalLight light2 = new DirectionalLight(white, light2Direction);
        light2.setInfluencingBounds(bounds);
        root.addChild(light2);
    }  // end of lightScene()
        
    

    // ACCESS ******************************************************************

    /**
     * Loads a scene from a Wavefront .obj model.
     *
     * @param location the path of the model
     * @return Scene the scene contained on the model
     * @throws IOException if some error occur while loading the model
     */
    public static Scene getSceneFromFile(String location) throws IOException {
        Loader3DS file = new Loader3DS ();
        file.setFlags(Loader3DS.LOAD_ALL);
        //ObjectFile file = new ObjectFile(ObjectFile.RESIZE);
        return file.load (location);
//return file.load(new FileReader(location));
    }
void listSceneNamedObjects(Scene scene) {
    Map<String, Shape3D> nameMap = scene.getNamedObjects();

    for (String name : nameMap.keySet()) {
    System.out.printf("Name: %s\n", name);
 }
}
    // MAIN ********************************************************************

    public static void main(String[] args) {
        try {
            new SimpleModelView().setVisible(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
}
