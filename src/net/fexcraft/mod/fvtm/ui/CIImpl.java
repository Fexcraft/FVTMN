package net.fexcraft.mod.fvtm.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class CIImpl extends ContainerInterface {

	public CIImpl(JsonMap map){
		super(map);
	}

	@Override
	public Object get(String key, Object... objs) {
		return null;
	}

	@Override
	public void packet(TagCW com, boolean client) {

	}

}
