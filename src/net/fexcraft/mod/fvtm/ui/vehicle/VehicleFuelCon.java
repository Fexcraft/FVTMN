package net.fexcraft.mod.fvtm.ui.vehicle;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fvtm.FvtmLogger;
import net.fexcraft.mod.fvtm.sys.uni.Passenger;
import net.fexcraft.mod.fvtm.sys.uni.VehicleInstance;
import net.fexcraft.mod.fvtm.ui.UIKey;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.world.EntityW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class VehicleFuelCon extends ContainerInterface {

	private VehicleInstance vehicle;

	public VehicleFuelCon(JsonMap map, EntityW player, V3I pos){
		super(map, player, pos);
		vehicle = ((Passenger)player).getFvtmWorld().getVehicle(pos.x);
	}

	@Override
	public Object get(String key, Object... objs){
		if(key.equals("vehicle")) return vehicle;
		return null;
	}

	@Override
	public void packet(TagCW com, boolean client){
		//
	}

	@Override
	public void onClosed() {

	}

}
