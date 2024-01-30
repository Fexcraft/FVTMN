package net.fexcraft.mod.fvtm.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.world.EntityW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class CIImpl extends ContainerInterface {

	public CIImpl(JsonMap map, EntityW player, V3I pos){
		super(map, player, pos);
	}

	@Override
	public Object get(String key, Object... objs) {
		return null;
	}

	@Override
	public void packet(TagCW com, boolean client) {

	}

	@Override
	public void onClosed() {

	}

}
