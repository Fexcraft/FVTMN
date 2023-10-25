package net.fexcraft.mod.fvtm.sys.uni;

import net.fexcraft.mod.fvtm.data.Seat;
import net.fexcraft.mod.fvtm.data.vehicle.SwivelPoint;
import net.fexcraft.mod.fvtm.util.Pivot;
import net.fexcraft.mod.uni.world.EntityW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class SeatInstance {

	public int index;
	public VehicleInstance root;
	private EntityW passenger;
	protected SwivelPoint point;
	public final Seat seat;
	//
	public Pivot slook;
	public Pivot pslook;
	public Pivot elook;
	public Pivot pelook;
	public float eyaw;
	public float epitch;
	public float peyaw;
	public float pepitch;
	public byte clicktimer;
	public byte interacttimer;

	public SeatInstance(VehicleInstance veh, int idx){
		root = veh;
		index = idx;
		seat = veh.data.getSeat(index);
		point = veh.data.getRotationPoint(seat.swivel_point);
		resetPivots();
	}

	private void resetPivots(){
		pslook = new Pivot();
		slook = new Pivot();
		pelook = new Pivot();
		elook = new Pivot();
		slook.set_rotation((seat.minyaw + seat.maxyaw) / 2, 0, 0, true);
		pslook.set_rotation((seat.minyaw + seat.maxyaw) / 2, 0, 0, true);
	}

}