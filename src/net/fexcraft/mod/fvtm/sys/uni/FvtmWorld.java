package net.fexcraft.mod.fvtm.sys.uni;

import net.fexcraft.mod.fvtm.data.vehicle.SwivelPoint;
import net.fexcraft.mod.uni.world.WorldW;

public abstract class FvtmWorld extends WorldW {

	public abstract SeatInstance getSeat(int entid, int seatid);

	public abstract SwivelPoint getSwivelPoint(int entid, String pointid);

	public abstract Passenger getPassenger(int source);

}
