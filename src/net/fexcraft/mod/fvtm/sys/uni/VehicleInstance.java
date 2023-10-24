package net.fexcraft.mod.fvtm.sys.uni;

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

	private VehicleData data;
	private VehicleType type;
	private EntityW entity;
	private UUID placer;
	private VehicleInstance front, rear;
	private SwivelPoint point;
	//
	public double steer_yaw;
	public double throttle;
	public double speed;
	public Pivot current;
	public Pivot previous;

	public VehicleInstance(EntityW wrapper, VehicleData vdata){
		entity = wrapper;
		data = vdata;
		type = data.getType().getVehicleType();
		point = data.getRotationPoint(null);
	}

}
