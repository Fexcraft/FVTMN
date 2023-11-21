package net.fexcraft.mod.fvtm.data.block;

import java.util.ArrayList;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class AABB {

    public static final AABB[] NULL = new AABB[]{ null };
    public static final AABB[] FULL = new AABB[]{ new AABB(0, 0, 0, 1, 1, 1) };
    public float x0, y0, z0;
    public float x1, y1, z1;

    public AABB(float sx, float sy, float sz, float ex, float ey, float ez){
        x0 = sx; y0 = sy; z0 = sz;
        x1 = ex; y1 = ey; z1 = ez;
    }

}
