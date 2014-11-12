/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package supportpackage;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import java.awt.Color;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3f;

/**
 *
 * @author nati
 */
public class Triangle3D {
    
        public Triangle3D (BranchGroup group){

        Point3f e = new Point3f(1.0f, 0.0f, 0.0f); // east
        Point3f s = new Point3f(0.0f, 0.0f, 1.0f); // south
        Point3f w = new Point3f(-1.0f, 0.0f, 0.0f); // west
        Point3f n = new Point3f(0.0f, 0.0f, -1.0f); // north
        Point3f t = new Point3f(0.0f, 0.721f, 0.0f); // top

        TriangleArray pyramidGeometry = new TriangleArray(18,
                        TriangleArray.COORDINATES);
        pyramidGeometry.setCoordinate(0, e);
        pyramidGeometry.setCoordinate(1, t);
        pyramidGeometry.setCoordinate(2, s);

        pyramidGeometry.setCoordinate(3, s);
        pyramidGeometry.setCoordinate(4, t);
        pyramidGeometry.setCoordinate(5, w);

        pyramidGeometry.setCoordinate(6, w);
        pyramidGeometry.setCoordinate(7, t);
        pyramidGeometry.setCoordinate(8, n);

        pyramidGeometry.setCoordinate(9, n);
        pyramidGeometry.setCoordinate(10, t);
        pyramidGeometry.setCoordinate(11, e);

        pyramidGeometry.setCoordinate(12, e);
        pyramidGeometry.setCoordinate(13, s);
        pyramidGeometry.setCoordinate(14, w);

        pyramidGeometry.setCoordinate(15, w);
        pyramidGeometry.setCoordinate(16, n);
        pyramidGeometry.setCoordinate(17, e);
        GeometryInfo geometryInfo = new GeometryInfo(pyramidGeometry);
        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals(geometryInfo);

        GeometryArray result = geometryInfo.getGeometryArray();

        // yellow appearance
        Appearance appearance = new Appearance();
        Color3f color = new Color3f(Color.yellow);
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        Texture texture = new Texture2D();
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
        Material mat = new Material(color, black, color, white, 70f);
        appearance.setTextureAttributes(texAttr);
        appearance.setMaterial(mat);
        appearance.setTexture(texture);
        Shape3D shape = new Shape3D(result, appearance);
        group.addChild(shape);
    }    
}
