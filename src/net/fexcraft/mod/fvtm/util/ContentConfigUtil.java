package net.fexcraft.mod.fvtm.util;

import static net.fexcraft.mod.fvtm.FvtmRegistry.ADDONS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonValue;
import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.mod.fvtm.FvtmRegistry;
import net.fexcraft.mod.fvtm.data.ContentType;
import net.fexcraft.mod.fvtm.data.addon.Addon;
import net.fexcraft.mod.fvtm.model.Model;
import net.fexcraft.mod.fvtm.model.ModelData;
import net.fexcraft.mod.uni.EnvInfo;
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

	public static IDL getID(Addon pack, JsonMap map){
		String id = map.getString("ID", null);
		if(id == null) id = map.get("RegistryName", null);
		if(id == null) return null;
		if(id.contains(":")) return IDLManager.getIDLCached(id);
		else return IDLManager.getIDLCached((pack == null ? "fvtm" : pack.getID().id()) + ":" + id);
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

	public static Addon getAddon(JsonMap map){
		if(map.has("Addon")){
			String id = map.get("Addon").string_value();
			if(id.contains(":")) id = id.split(":")[1];
			for(Addon addon : ADDONS){
				if(addon.getID().id().equals(id)) return addon;
			}
		}
		return ADDONS.get(FvtmRegistry.INTERNAL_ADDON_ID);
	}

	public static List<String> getStringList(JsonMap map, String key){
		if(!map.has(key)) return new ArrayList<>();
		if(map.get(key).isArray()){
			return map.getArray(key).toStringList();
		}
		else{
			ArrayList list = new ArrayList();
			list.add(map.get(key).string_value());
			return list;
		}
	}

	public static final IDL ITL_GENERAL = IDLManager.getIDLCached("fvtm:textures/item/ph_general.png");
	public static final IDL ITL_VEHICLE = IDLManager.getIDLCached("fvtm:textures/item/ph_vehicle.png");
	public static final IDL ITL_MBLOCK = IDLManager.getIDLCached("fvtm:textures/item/ph_multiblock.png");
	public static final IDL ITL_PART = IDLManager.getIDLCached("fvtm:textures/item/ph_part.png");

	public static IDL getItemTexture(IDL id, ContentType contype, JsonMap map){
		if(map.has("ItemTexture")){
			return IDLManager.getIDLCached(map.get("ItemTexture").string_value());
		}
		else{
			IDL idl = IDLManager.getIDLCached(id.space() + ":textures/item/" + id.path() + ".png");
			if(EnvInfo.CLIENT){
				//TODO check if missing and return placeholder instead
			}
			return idl;
		}
	}

	public static List<IDL> getTextures(JsonMap map){
		ArrayList<IDL> list = new ArrayList<>();
		if(map.has("Texture")){
			list.add(IDLManager.getIDLNamed(map.get("Texture", "No Texture;fvtm:textures/entity/null.png")));
		}
		if(map.has("Textures")){
			for(JsonValue<?> tex : map.get("Textures").asArray().value){
				list.add(IDLManager.getIDLNamed(tex.string_value()));
			}
		}
		if(list.isEmpty()){
			list.add(IDLManager.getIDLNamed("No Texture;fvtm:textures/entity/null.png"));
		}
		return list;
	}

	public static V3D getVector(JsonArray array){
		V3D vec = new V3D();
		if(array.size() > 0) vec.x = array.get(0).float_value();
		if(array.size() > 1) vec.y = array.get(1).float_value();
		if(array.size() > 2) vec.z = array.get(2).float_value();
		return vec;
	}

	public static V3D getVector(JsonArray array, int offset){
		V3D vec = new V3D();
		if(array.size() > offset + 0) vec.x = array.get(offset + 0).float_value();
		if(array.size() > offset + 1) vec.y = array.get(offset + 1).float_value();
		if(array.size() > offset + 2) vec.z = array.get(offset + 2).float_value();
		return vec;
	}

	public static V3D getVector(JsonMap map){
		V3D vec = new V3D();
		vec.x = map.getFloat("x", 0f);
		vec.y = map.getFloat("y", 0f);
		vec.z = map.getFloat("z", 0f);
		return vec;
	}

	public static V3D getVector(JsonMap map, float x, float y, float z){
		V3D vec = new V3D();
		vec.x = map.getFloat("x", x);
		vec.y = map.getFloat("y", y);
		vec.z = map.getFloat("z", z);
		return vec;
	}

	public static V3D getVector(JsonMap map, String prefix){
		V3D vec = new V3D();
		vec.x = map.getFloat(prefix + "x", 0f);
		vec.y = map.getFloat(prefix + "y", 0f);
		vec.z = map.getFloat(prefix + "z", 0f);
		return vec;
	}

}
