package net.fexcraft.mod.fvtm.ui.road;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.Time;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fvtm.data.Fuel;
import net.fexcraft.mod.fvtm.data.attribute.Attribute;
import net.fexcraft.mod.fvtm.sys.uni.Passenger;
import net.fexcraft.mod.fvtm.sys.uni.VehicleInstance;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.InventoryInterface;
import net.fexcraft.mod.uni.world.EntityW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class RoadToolCon extends InventoryInterface {

	public RoadToolCon(JsonMap map, EntityW player, V3I pos){
		super(map, player, pos);
	}

	@Override
	public Object get(String key, Object... objs){
		//
		return null;
	}

	@Override
	public void packet(TagCW com, boolean client){
		//
	}

	@Override
	public void update(Object lc){
		//
	}

}
