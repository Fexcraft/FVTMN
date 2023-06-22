package net.fexcraft.mod.fvtm.util;

import java.util.ArrayList;
import java.util.Map.Entry;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonValue;
import net.fexcraft.mod.fvtm.data.root.Model;
import net.fexcraft.mod.fvtm.data.root.Model.ModelData;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ContentConfigUtil {

	public static IDL getID(JsonMap map){
		if(map.has("ID")) return IDLManager.getIDLCached(map.get("ID").string_value());
		else return IDLManager.getIDLCached(map.get("RegistryName").string_value());
	}

	public static ModelData getModelData(JsonMap map, String key, ModelData data){
		if(key == null) key = "ModelData";
		if(map.has(key)){
			for(Entry<String, JsonValue<?>> entry : map.getMap(key).entries()){
				getModelDataEntry(data, entry.getKey(), entry.getValue());
			}
		}
		return data;
	}

	private static void getModelDataEntry(ModelData data, String key, JsonValue value){
		if(value.isArray()){
			ArrayList<Object> list = data.containsKey(key) ? data.get(key) : new ArrayList<>();
			for(JsonValue<?> val : value.asArray().value){
				if(val.isValue()){
					list.add(val.string_value());
				}
				else if(key.equals(Model.PROGRAMS)){
					if(val.isArray()){
						JsonArray array = val.asArray();
						String group = array.get(0).string_value();
						array.value.remove(0);
						list.add(new Object[]{ group, array });
					}
					else{
						JsonMap prog = val.asMap();
						String group = prog.get("group").string_value();
						prog.rem("group");
						list.add(new Object[]{ group, prog });
					}
				}
				else {
					list.add(val);
				}
			}
			data.set(key, list);
		}
		else if(value.isValue()){
			if(value.isBoolean()){
				data.set(key, value.bool());
			}
			else if(value.isNumber()){
				data.set(key, value.float_value());
			}
			else data.set(key, value.string_value());
		}
	}

}
