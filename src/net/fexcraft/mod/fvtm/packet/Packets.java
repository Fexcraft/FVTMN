package net.fexcraft.mod.fvtm.packet;

import net.fexcraft.mod.fvtm.sys.uni.SeatInstance;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class Packets {

    public static Packets INSTANCE = null;

    public abstract void init();

    /** Send Seat Update Packet to Server */
    public abstract void send(SeatInstance seat);

}
