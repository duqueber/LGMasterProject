
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.image.TextureLoader;
import javax.media.j3d.Appearance;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nati
 */
public class TexturedBox {
    
    Box textbox;
    private int x;
    private int y;
    private int z;
    private Texture2D texture = null;
    private TransformGroup tg;
    
    TexturedBox (int xcoor, int ycoor, int zcoor, float size, String fn){
       
      textbox = new Box(size,size,size,
      Box.GENERATE_TEXTURE_COORDS, createAppearance(fn));
    
        x= xcoor;
        z= ycoor;
        y = zcoor;
        
       translatebox(); 
    }
    
    
    private void loadTexture(String fn){
    TextureLoader texLoader = new TextureLoader(fn, null);
    ImageComponent2D image = texLoader.getImage();
    texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(),
    image.getHeight());
    texture.setMagFilter(Texture.BASE_LEVEL_POINT);
    texture.setImage(0, image);
}
  
  private Appearance createAppearance(String fn)
  {
    loadTexture(fn);
    Appearance app = new Appearance();

    
    TextureAttributes ta = new TextureAttributes( );
    ta.setTextureMode( TextureAttributes.MODULATE );
    app.setTextureAttributes( ta );
    // apply texture to shape
    if (texture != null) {
    // loaded at start, from adjustShapes( )
    app.setTexture(texture);    
     }   
    return app;
    }
  
  public TransformGroup gettbox(){
      return tg;
  }
  
  public void translatebox(){
  Transform3D t3d = new Transform3D();
  Vector3d translateVec = new Vector3d (x+0.5,y+0.5,z+0.5);
  t3d.setTranslation( translateVec);
  tg = new TransformGroup(t3d);
  tg.addChild(textbox);
          
  }     

  }