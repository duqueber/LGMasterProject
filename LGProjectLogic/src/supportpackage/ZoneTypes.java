/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package supportpackage;

/**
 *
 * @author nati
 */
public class ZoneTypes {
    private String blackZone, whiteZone;
    
    public ZoneTypes (String black, String white){
        this.blackZone = black;
        this.whiteZone = white;
    }
    
    //WhiteIntercept, BlackIntercept, WhiteProtect, Black Protect
    public boolean isWhiteWin () {
        if (this.blackZone== null && this.whiteZone == null)
            throw new IllegalArgumentException ("both zones cannot be null zones types");
        if (("0_1_".equals(this.whiteZone)|| this.whiteZone == null)&& 
                ("_1_0".equals(this.blackZone) || this.blackZone == null))
            return true;
        return false;
    }
    
    public boolean isBlackWin () {
        if (this.blackZone== null && this.whiteZone == null)
            throw new IllegalArgumentException ("both zones cannot be null zones types");
        if (("1_0_".equals(this.whiteZone)|| this.whiteZone == null)
                && ("_0_1".equals(this.blackZone) || this.blackZone == null))
            return true;
        return false;
    }
    
    public boolean isBothIntercept () {
        if (this.blackZone== null && this.whiteZone == null)
            throw new IllegalArgumentException ("both zones cannot be null zones types");
        if ("1_0_".equals(this.whiteZone)&& "_1_0".equals(this.blackZone))
            return true;
        return false;
    }
    
    public boolean isBothProtect () {
        if (this.blackZone== null && this.whiteZone == null)
            throw new IllegalArgumentException ("both zones cannot be null zones types");
        if ("0_1_".equals(this.whiteZone)&& "_0_1".equals(this.blackZone))
            return true;
        return false;
    }
}
