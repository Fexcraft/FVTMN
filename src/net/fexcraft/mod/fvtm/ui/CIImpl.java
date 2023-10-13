package net.fexcraft.mod.fvtm.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.world.PlayerW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class CIImpl extends ContainerInterface {

	public CIImpl(JsonMap map, PlayerW player){
		super(map, player);
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
