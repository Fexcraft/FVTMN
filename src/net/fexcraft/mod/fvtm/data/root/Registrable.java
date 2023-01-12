package net.fexcraft.mod.fvtm.data.root;

import java.util.List;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fvtm.data.addon.Addon;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.item.ItemWrapper;
import net.fexcraft.mod.uni.item.StackWrapper;

public abstract class Registrable<SELF> {
	
	public IDL regname(){
		return id;
	}
	
	public abstract SELF parse(JsonMap map);
	
	public abstract DataType getDataType();
	
	/** @return null if absent */
	public abstract Class<?> getDataClass();
	
	public Addon getAddon(){ return pack; }

	public String getName(){ return name; }

	public ItemWrapper getItem(){ return item; }

	public StackWrapper getStack(){ return StackWrapper.EMPTY; }
	
	public List<String> getDescription(){ return description; }
	
	/** Overrride on types using a Model. */
	public void loadModel(){}
	
	protected IDL id;
	protected List<String> description;
	protected String name;
	protected Addon pack;
	protected ItemWrapper item;
	
}