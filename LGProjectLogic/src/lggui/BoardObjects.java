/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lggui;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

/**
 *
 * @author nati
 */
public class BoardObjects {
    
    String fileName, type;
    Vector3d translateVec;
    double roty, rotx, rotz, scale;
    Transform3D t3d ;
    TransformGroup tg;
    
    
    BoardObjects (String fn, Vector3d translateVec, double rotx, double roty,double rotz,
            double scale, Transform3D t3d,TransformGroup tg, String type){
        
        this.fileName = fn;
        this.translateVec = new Vector3d (translateVec);
        this.roty = roty;
        this.rotx = rotx;
        this.rotz = rotz;
        this.scale = scale;
        this.type = type;
        this.t3d = t3d;
        this.tg = tg;
        
    }
    
}
