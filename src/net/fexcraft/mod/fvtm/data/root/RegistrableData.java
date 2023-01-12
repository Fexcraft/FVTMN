package net.fexcraft.mod.fvtm.data.root;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.tag.TagMap;

public abstract class RegistrableData<TYPE extends Registrable<TYPE>, SELF> {
	
	protected TYPE type;
	
	public RegistrableData(TYPE type){
		this.type = type;
	}
	
	public abstract TagMap write(TagMap compound);
	
	public abstract SELF read(TagMap compound);
	
	public abstract SELF parse(JsonMap obj);
	
	public abstract JsonMap toJson();
	
	public TYPE getType(){ return type; }
	
	public DataType getDataType(){
		return type.getDataType();
	}
	
	public static interface DataCoreItem<SELF> {
		
		public SELF getData(StackWrapper stack);
		
		public SELF getData(TagMap compound);
		
	}
	
}