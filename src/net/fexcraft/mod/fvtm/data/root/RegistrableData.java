package net.fexcraft.mod.fvtm.data.root;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.item.uItemStack;
import net.fexcraft.mod.uni.tag.uTagMap;

public abstract class RegistrableData<TYPE extends Registrable<TYPE>, SELF> {
	
	protected TYPE type;
	
	public RegistrableData(TYPE type){
		this.type = type;
	}
	
	public abstract uTagMap write(uTagMap compound);
	
	public abstract SELF read(uTagMap compound);
	
	public abstract SELF parse(JsonMap obj);
	
	public abstract JsonMap toJson();
	
	public TYPE getType(){ return type; }
	
	public DataType getDataType(){
		return type.getDataType();
	}
	
	public static interface DataCoreItem<SELF> {
		
		public SELF getData(uItemStack stack);
		
		public SELF getData(uTagMap compound);
		
	}
	
}