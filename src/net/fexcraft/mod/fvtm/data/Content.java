package net.fexcraft.mod.fvtm.data;

import java.util.List;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fvtm.data.addon.Addon;
import net.fexcraft.mod.uni.IDL;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class Content<SELF> {

	protected IDL id;
	protected List<String> description;
	protected String name;
	protected Addon pack;

	public abstract SELF parse(JsonMap map);

	public abstract ContentType getContentType();

	public abstract Class<?> getDataClass();

	public IDL getID(){
		return id;
	}

	public String getName(){
		return name;
	}

	public Addon getAddon(){
		return pack;
	}

	public List<String> getDescription(){
		return description;
	}

	public void loadModel(){}

}
