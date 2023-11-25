package net.fexcraft.mod.fvtm.data.block;

import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class AABB {

    public static Supplier<AABB> SUPPLIER = null;
    //public float x0, y0, z0;
    //public float x1, y1, z1;

    public static AABB create(){
        return SUPPLIER.get();
    }

    public static AABB create(float sx, float sy, float sz, float ex, float ey, float ez){
        return create().set(sx, sy, sz, ex, sy, sz);
    }

    public abstract AABB set(float sx, float sy, float sz, float ex, float ey, float ez);

    public abstract <AB> AB local();

    public abstract Object direct();

    public abstract <AB> AB offset(int x, int y, int z);

}
