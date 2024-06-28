package net.fexcraft.mod.fvtm.ui.vehicle;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fvtm.sys.uni.Passenger;
import net.fexcraft.mod.fvtm.ui.UIKeys;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIKey;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class VehicleMainCon extends ContainerInterface {

	public VehicleMainCon(JsonMap map, UniEntity player, V3I pos){
		super(map, player, pos);
	}

	@Override
	public Object get(String key, Object... objs) {
		return null;
	}

	@Override
	public void packet(TagCW com, boolean client){
		if(client) return;
		UIKey ui = null;
		switch(com.getString("open")){
			case "info": ui  = UIKeys.VEHICLE_INFO; break;
			case "fuel": ui  = UIKeys.VEHICLE_FUEL; break;
			case "attributes": ui  = UIKeys.VEHICLE_ATTRIBUTES; break;
			case "inventories": ui  = UIKeys.VEHICLE_INVENTORIES; break;
			case "containers": ui  = UIKeys.VEHICLE_CONTAINERS; break;
			case "connectors": ui  = UIKeys.VEHICLE_CONNECTORS; break;
			default: return;
		}
		if(ui != null) ((Passenger)player).openUI(ui, pos);
	}

}
