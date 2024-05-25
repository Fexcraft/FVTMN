package net.fexcraft.mod.fvtm.sys.uni;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.mod.fvtm.data.InteractData;
import net.fexcraft.mod.fvtm.data.InteractType;
import net.fexcraft.mod.fvtm.data.vehicle.SwivelPoint;
import net.fexcraft.mod.fvtm.packet.Packet_VehMove;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.world.StateWrapper;
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

	public abstract ArrayList<InteractData> getInteractables(InteractType type, V3D pos);

	public abstract Passenger getClientPassenger();

	public abstract boolean isFvtmRoad(StateWrapper state);

	public boolean isFvtmRoad(Object state){
		return isFvtmRoad(StateWrapper.of(state));
	}

	public abstract int getRoadHeight(StateWrapper state);

	public abstract StateWrapper getRoadWithHeight(StateWrapper block, int height);

	public abstract void handleBlockEntityPacket(TagCW com, Passenger player);

}
