package lggui;


// ColouredTiles.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

// ColouredTiles creates a coloured quad array of tiles.
// No lighting since no normals or Material used

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.image.TextureLoader;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.media.j3d.*;
import javax.vecmath.*;


public class ColouredTiles extends Shape3D {
    private QuadArray plane;
    private Texture2D texture = null;

    public ColouredTiles (){

    }

    public ColouredTiles(ArrayList coords, String fn) {

        plane = new QuadArray(coords.size(), GeometryArray.COORDINATES|    
                GeometryArray.TEXTURE_COORDINATE_2);
                                            //TEXTURE_COORDINATE_2 );
        createGeometry(coords);
        loadTexture(fn);
        createAppearance();
      }    

    private void createGeometry(ArrayList coords){ 
    
        int numPoints = coords.size();

        Point3f[] points = new Point3f[numPoints];
        coords.toArray( points );
        plane.setCoordinates(0, points);

    /*Color3f cols[] = new Color3f[numPoints];
    for(int i=0; i < numPoints; i++)
      cols[i] = col;
    plane.setColors(0, cols);*/
    
        TexCoord2f[] textCoord = {new TexCoord2f(0.0f,1.0f), new TexCoord2f(0.0f,0.0f),
                new TexCoord2f(1.0f,0.0f), new TexCoord2f(1.0f,1.0f)};
   
        for (int i = 0; i < numPoints; i+=4){
            plane.setTextureCoordinate(0,i,textCoord[0]);
            plane.setTextureCoordinate(0,i+1,textCoord[1]);
            plane.setTextureCoordinate(0,i+2,textCoord[2]);
            plane.setTextureCoordinate(0,i+3,textCoord[3]);
        }
        
        setGeometry(plane);
      }  // end of createGeometry()

    private void loadTexture(String fn){
        
        TextureLoader texLoader = new TextureLoader(fn, null);
        ImageComponent2D image = texLoader.getImage();
        texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(),
        image.getHeight());
        texture.setMagFilter(Texture.BASE_LEVEL_POINT);
        texture.setImage(0, image);
    }

    private void createAppearance(){
        
        Appearance app = new Appearance();
     //   PolygonAttributes pa = new PolygonAttributes();
      //  pa.setCullFace(PolygonAttributes.CULL_NONE);   
          // so can see the ColouredTiles from both sides
       // app.setPolygonAttributes(pa);

        //TransparencyAttributes trans = new TransparencyAttributes();
        //trans.setTransparencyMode (trans.BLENDED);
        //trans.setTransparency (0.9f);
        //app.setTransparencyAttributes(trans);
        
        TextureAttributes ta = new TextureAttributes( );
        ta.setTextureMode( TextureAttributes.MODULATE );
        app.setTextureAttributes( ta );
        // apply texture to shape
        if (texture != null) {
        // loaded at start, from adjustShapes( )
            app.setTexture(texture);
            setAppearance(app);
        }    
    }  // end of createAppearance()

    /*  private void createGeometry(ArrayList coords){
          
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
    }*/
} // end of ColouredTiles class
