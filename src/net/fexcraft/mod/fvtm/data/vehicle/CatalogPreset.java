package net.fexcraft.mod.fvtm.data.vehicle;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonValue;
import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.mod.fvtm.FvtmRegistry;
import net.fexcraft.mod.fvtm.FvtmResources;
import net.fexcraft.mod.fvtm.util.ContentConfigUtil;
import net.fexcraft.mod.uni.IDLManager;
import net.fexcraft.mod.uni.impl.IDLM;
import net.fexcraft.mod.uni.item.StackWrapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class CatalogPreset {

	public final Map<String, String> parts = new LinkedHashMap<>();
	public final Map<String, RGB> channels = new LinkedHashMap<>();
	public final List<String> recipe = new ArrayList<>();
	public final List<String> desc;
	public final Vehicle vehicle;
	public final String name;
	public final String id;

	public CatalogPreset(Vehicle root, String cid, JsonMap map){
		vehicle = root;
		id = cid;
		name = map.getString("name", root.getName() + "(" + id + ")");
		desc = ContentConfigUtil.getStringList(map, "description");
		if(map.has("parts")){
			map.getMap("parts").entries().forEach(entry -> {
				parts.put(entry.getKey(), entry.getValue().string_value());
			});
		}
		if(map.has("recipe")){
			for(JsonValue<?> elm : map.getArray("recipe").value){
				if(elm.isMap()){

				}
				else{
					recipe.add(elm.string_value());
				}
			}
		}
		if(map.has("colors")){
			map.getMap("colors").entries().forEach(entry -> {
				channels.put(entry.getKey(), new RGB(entry.getValue().string_value()));
			});
		}
	}

	public String getDesc(int i){
		if(desc.isEmpty()) return i == 0 ? "ui.fvtm.vehicle_catalog.no_desc" : "";
		if(i >= desc.size()) return "";
		return desc.get(i);
	}

}
