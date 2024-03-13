package net.fexcraft.mod.fvtm.ui.vehicle;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fvtm.sys.uni.Passenger;
import net.fexcraft.mod.fvtm.ui.UIKey;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.world.EntityW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class VehicleMainCon extends ContainerInterface {

	public VehicleMainCon(JsonMap map, EntityW player, V3I pos){
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
			case "info": ui  = UIKey.VEHICLE_INFO; break;
			case "fuel": ui  = UIKey.VEHICLE_FUEL; break;
			case "attributes": ui  = UIKey.VEHICLE_ATTRIBUTES; break;
			case "inventories": ui  = UIKey.VEHICLE_INVENTORIES; break;
			case "containers": ui  = UIKey.VEHICLE_CONTAINERS; break;
			case "connectors": ui  = UIKey.VEHICLE_CONNECTORS; break;
			default: return;
		}
		if(ui != null) ((Passenger)player).openUI(ui, pos);
	}

}
