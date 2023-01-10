package net.fexcraft.mod.fvtm.data.root;

import java.util.List;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fvtm.data.addon.Addon;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.item.uItem;

public abstract class Registrable<SELF> {
	
	public IDL regname(){
		return id;
	}
	
	public abstract SELF parse(JsonMap obj);
	
	public abstract DataType getDataType();
	
	/** @return null if absent */
	public abstract Class<?> getDataClass();
	
	public Addon getAddon(){ return pack; }

	public String getName(){ return name; }

	public uItem getItem(){ return item; }
	
	public List<String> getDescription(){ return description; }
	
	/** Overrride on types using a Model. */
	public void loadModel(){}
	
	protected IDL id;
	protected List<String> description;
	protected String name;
	protected Addon pack;
	protected uItem item;
	
}