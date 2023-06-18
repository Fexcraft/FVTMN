package net.fexcraft.mod.fvtm.data;

import java.util.List;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fvtm.data.addon.AddonNew;
import net.fexcraft.mod.uni.IDL;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class Content<SELF> {

	protected IDL id;
	protected List<String> description;
	protected String name;
	protected AddonNew pack;

	public abstract SELF parse(JsonMap map);

	public abstract ContentType getContentType();

	public abstract Class<?> getDataClass();

	public IDL getID(){
		return id;
	}

	public String getName(){
		return name;
	}

	public AddonNew getAddon(){
		return pack;
	}

	public List<String> getDescription(){
		return description;
	}

	public abstract void loadModel();

}
