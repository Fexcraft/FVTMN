package net.fexcraft.mod.uni.ui;

import java.util.function.Consumer;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.tag.TagCW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class ContainerInterface {

	public Consumer<TagCW> SEND_TO_CLIENT;
	public Consumer<TagCW> SEND_TO_SERVER;
	protected UserInterface ui;//client side only

	public ContainerInterface(JsonMap map){

	}

	public abstract Object get(String key, Object... objs);

	public abstract void packet(TagCW com, boolean client);

	public ContainerInterface set(UserInterface ui){
		this.ui = ui;
		return this;
	}

}
