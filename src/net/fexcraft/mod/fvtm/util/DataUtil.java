package net.fexcraft.mod.fvtm.util;

import static net.fexcraft.mod.uni.IDLManager.getIDL;
import static net.fexcraft.mod.uni.IDLManager.getIDLCached;
import static net.fexcraft.mod.uni.IDLManager.getIDLNamed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonObject;
import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.lib.mc.utils.Static;
import net.fexcraft.mod.fvtm.data.addon.Addon;
import net.fexcraft.mod.fvtm.data.root.DataType;
import net.fexcraft.mod.fvtm.data.root.Model;
import net.fexcraft.mod.fvtm.data.root.Model.ModelData;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class DataUtil {

	public static IDL getID(JsonMap map){
		String regname = map.getString("RegistryName", null);
		if(regname == null) return null;
		return getIDLCached(regname.contains(":") ? regname : "fvtm:" + regname);
	}

	public static IDL getID(String key, JsonMap map){
		return map.has(key) ? getIDLCached(map.get("RegistryName").string_value()) : null;
	}

	public static Addon getAddon(JsonMap map){
		if(map.has("Addon")){
			String addin = map.get("Addon").string_value();
			if(addin.contains(":")) addin = addin.split(":")[1];
			Addon addon = Resources.getAddon(addin);
			if(addon != null) return addon;
		}
		return Resources.ADDONS.get(InternalAddon.REGNAME);
	}
	
	public static List<String> getStringArray(JsonMap obj, String key, boolean split){
		return getStringArray(obj, new String[]{ key }, split ? "\n" : null);
	}

	public static List<String> getStringArray(JsonMap map, String[] keys, String split){
		ArrayList<String> list = new ArrayList<>();
		for(String key : keys){
			if(map.has(key)){
				if(map.get(key).isArray()){
					map.get(key).asArray().value.forEach(elm -> list.add(elm.string_value()));
				}
				else{
					if(split != null){
						String[] arr = map.get(key).string_value().split(split);
						for(String string : arr) list.add(string);
					}
					else list.add(map.get(key).string_value());
				}
			}
		}
		return list;
	}

	public static ArrayList<String> getStringArray(JsonObject<?> elm){
		ArrayList<String> list = new ArrayList<>();
		if(elm.isArray()){
			for(JsonObject<?> e : elm.asArray().value) list.add(e.string_value());
		}
		else list.add(elm.string_value());
		return list;
	}

	public static List<IDL> getTextures(JsonMap obj){
		ArrayList<IDL> reslocs = new ArrayList<>();
		if(obj.has("Texture") && obj.get("Texture").isObject()){
			reslocs.add(getIDLNamed(obj.get("Texture").string_value()));
		}
		else if(obj.has("Textures") && obj.get("Textures").isArray()){
			obj.get("Textures").asArray().value.forEach(elm -> {
				reslocs.add(getIDLNamed(elm.string_value()));
			});
		}
		if(reslocs.isEmpty()){
			reslocs.add(Resources.NULL_TEXTURE);
		}
		return reslocs;
	}

	public static RGB getColor(JsonMap obj, String prefix, boolean nell){
		RGB result = null;
		if(obj.has(prefix + "Color")){
			JsonObject<?> elm = obj.get(prefix + "Color");
			if(elm.isObject()){
				result = new RGB(elm.string_value());//HEX expected
			}
			else if(elm.isMap()){
				int red = obj.has("Red") ? obj.get("Red").integer_value() : 0;
				int gre = obj.has("Green") ? obj.get("Green").integer_value() : 0;
				int blu = obj.has("Blue") ? obj.get("Blue").integer_value() : 0;
				result = new RGB(red, gre, blu);
			}
			else if(elm.isArray()){//array of 3 integers expected
				int[] arr = new int[3];
				JsonArray array = elm.asArray();
				for(int x = 0; x < 3; x++){
					arr[x] = array.get(x).integer_value();
				}
				result = new RGB(arr);
			}
			else {};
		}
		return result == null ? nell ? null : new RGB() : result;
	}
	
	public static final IDL RSLC_GENERAL = getIDL("fvtm:textures/items/ph_general.png");
	public static final IDL RSLC_VEHICLE = getIDL("fvtm:textures/items/ph_vehicle.png");
	public static final IDL RSLC_PART = getIDL("fvtm:textures/items/ph_part.png");

	public static IDL getItemTexture(IDL regname, DataType type,  JsonMap obj){
		if(obj.has("ItemTexture")){
			return getIDLCached(obj.get("ItemTexture").string_value());
		}
		else{
			IDL resloc = IDLManager.getIDL(regname.domain() + ":textures/items/" + regname.path() + ".png");
			if(Static.isClient()){
				if(TexUtil.isMissing(resloc)){
					if(type == DataType.VEHICLE) return RSLC_VEHICLE;
					else if(type == DataType.PART) return RSLC_PART;
					else return RSLC_GENERAL;
				}
			}
			return resloc;
		}
	}

	public static ModelData getModelData(JsonMap json){
		return getModelData(json, "ModelData", new ModelData());
	}

	public static ModelData getModelData(JsonMap json, String key, ModelData data){
		if(json.has("ModelData")){
			JsonMap map = json.get(key).asMap();
			for(Entry<String, JsonObject<?>> entry : map.entries()){
				getModelDataEntry(data, entry.getKey(), entry.getValue());
			}
		}
		return data;
	}

	private static void getModelDataEntry(ModelData data, String key, JsonElement value){
		if(value.isJsonArray()){
			ArrayList<Object> list = data.containsKey(key) ? data.get(key) : new ArrayList<>();
			for(JsonElement elm : value.getAsJsonArray()){
				if(elm.isJsonPrimitive()){
					list.add(elm.getAsString());
				}
				else if(key.equals(Model.PROGRAMS)){
					if(elm.isJsonArray()){
						JsonArray array = elm.getAsJsonArray();
						String group = array.get(0).getAsString();
						array.remove(0);
						list.add(new Object[]{ group, array });
					}
					else{
						JsonMap prog = elm.getAsJsonObject();
						String group = prog.get("group").getAsString();
						prog.remove("group");
						list.add(new Object[]{ group, prog });
					}
				}
				else {
					list.add(elm);
				}
			}
			data.set(key, list);
		}
		else if(value.isJsonPrimitive()){
			JsonPrimitive val = value.getAsJsonPrimitive();
			if(val.isBoolean()){
				data.set(key, value.getAsBoolean());
			}
			else if(val.isNumber()){
				data.set(key, value.getAsFloat());
			}
			else data.set(key, value.getAsString());
		}
	
	}

}
