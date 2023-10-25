package net.fexcraft.mod.fvtm.sys.uni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.fexcraft.mod.fvtm.data.vehicle.SwivelPoint;
import net.fexcraft.mod.fvtm.data.vehicle.VehicleData;
import net.fexcraft.mod.fvtm.data.vehicle.VehicleType;
import net.fexcraft.mod.fvtm.util.Pivot;
import net.fexcraft.mod.uni.world.EntityW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class VehicleInstance {

	public VehicleData data;
	public VehicleType type;
	public EntityW entity;
	private UUID placer;
	public VehicleInstance front, rear;
	public SwivelPoint point;
	//
	public double steer_yaw;
	public double throttle;
	public double speed;
	public Pivot current;
	public Pivot previous;
	public ArrayList<SeatInstance> seats = new ArrayList<>();
	public HashMap<String, WheelTireData> wheeldata = new HashMap<>();
	//
	public static final float GRAVITY = 9.81f;

	public VehicleInstance(EntityW wrapper, VehicleData vdata){
		entity = wrapper;
		init(vdata);
	}

	public void init(VehicleData vdata){
		data = vdata;
		if(data == null) return;
		type = data.getType().getVehicleType();
		point = data.getRotationPoint(null);
	}

	public UUID getPlacer(){
		return placer;
	}

	public void setPlacer(UUID uuid){
		if(placer == null) placer = uuid;
	}

}
