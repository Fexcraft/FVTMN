package net.fexcraft.mod.fvtm.data;

import net.fexcraft.mod.fvtm.data.vehicle.VehicleData;
import net.fexcraft.mod.fvtm.sys.uni.VehicleInstance;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class InteractData {

	public final VehicleInstance vehicle;
	public final VehicleData vehdata;
	public final String part;

	public InteractData(VehicleInstance inst, VehicleData data, String selpart){
		vehicle = inst;
		vehdata = data == null ? inst.data : data;
		part = selpart;
	}

}
