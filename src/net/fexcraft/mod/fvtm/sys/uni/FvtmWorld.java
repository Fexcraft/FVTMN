package net.fexcraft.mod.fvtm.sys.uni;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.mod.fvtm.data.vehicle.SwivelPoint;
import net.fexcraft.mod.fvtm.packet.Packet_VehMove;
import net.fexcraft.mod.uni.world.WorldW;

import java.util.ArrayList;

public abstract class FvtmWorld extends WorldW {

	public abstract SeatInstance getSeat(int entid, int seatid);

	public abstract SwivelPoint getSwivelPoint(int entid, String pointid);

	public abstract Passenger getPassenger(int source);

	public abstract void onVehicleMove(Packet_VehMove packet);

	public abstract VehicleInstance getVehicle(int entid);

	public abstract boolean noViewEntity();

	public abstract ArrayList<VehicleInstance> getVehicles(V3D pos);

	public abstract Passenger getClientPassenger();

}
