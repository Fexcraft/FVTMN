package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class ContainerInterface {

	public ContainerInterface(JsonMap map){

	}

	public abstract Object get(String key, Object... objs);

}
