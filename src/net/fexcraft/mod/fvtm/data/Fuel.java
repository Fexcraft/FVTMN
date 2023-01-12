package net.fexcraft.mod.fvtm.data;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fvtm.data.root.DataType;
import net.fexcraft.mod.fvtm.data.root.Registrable;
import net.fexcraft.mod.fvtm.util.DataUtil;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Fuel extends Registrable<Fuel> {
	
	public String primary, secondary;
	public float quality;
	
	public Fuel(){}

	@Override
	public Fuel parse(JsonMap map){
		id = DataUtil.getID(map);
		if(id == null) return null;
		pack = DataUtil.getAddon(map);
		if(pack == null) return null;
		//
		name = map.getString("Name", "Unnamed Material");
		description = DataUtil.getStringArray(map, "Description", true);
		primary = map.getString("PrimaryGroup", "petrol");
		secondary = map.getString("SecondaryGroup", "super95");
		quality = map.getFloat("Quality", 0.95f);
		//
		return this;
	}

	@Override
	public DataType getDataType(){
		return DataType.FUEL;
	}

	@Override
	public Class<?> getDataClass(){
		return null;
	}
	
	public String getPrimaryGroup(){
		return primary;
	}
	
	public String getSecondaryGroup(){
		return secondary;
	}

}
