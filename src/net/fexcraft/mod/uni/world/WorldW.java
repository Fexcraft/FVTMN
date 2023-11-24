package net.fexcraft.mod.uni.world;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fvtm.data.block.BlockEntity;
import net.minecraft.world.World;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class WorldW {

	public abstract boolean isClient();

    public abstract boolean isTilePresent(V3I pos);

    public abstract BlockEntity getBlockEntity(V3I pos);

    public abstract <W> W local();

    public abstract Object direct();

    public abstract void setBlockState(V3I pos, StateWrapper state, int flag);

    public abstract void spawnBlockSeat(V3D add, EntityW player);

}
