package net.fexcraft.mod.fvtm.packet;

import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fvtm.data.block.BlockData;
import net.fexcraft.mod.fvtm.sys.uni.SeatInstance;
import net.fexcraft.mod.fvtm.sys.uni.VehicleInstance;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.world.WorldW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class Packets {

    public static Packets INSTANCE = null;

    public abstract void init();

    /** Send Seat Update Packet to Server */
    public abstract void send(SeatInstance seat);

    /** Send BlockData Update Packet to all around. */
    public abstract void send(BlockData blockdata, V3I pos, int dim);

    /** Send BlockData Update Packet to all around. */
    public abstract void send(WorldW world, V3I pos);

    /** Send generic nbt packet to a vehicle. */
    public abstract void send(VehicleInstance vehicleInstance, TagCW com);

}
