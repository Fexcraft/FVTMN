package net.fexcraft.mod.fvtm.data.part;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonValue;
import net.fexcraft.mod.fvtm.FvtmResources;
import net.fexcraft.mod.fvtm.data.Content;
import net.fexcraft.mod.fvtm.data.ContentType;
import net.fexcraft.mod.fvtm.data.attribute.Attribute;
import net.fexcraft.mod.fvtm.data.root.Textureable.TextureHolder;
import net.fexcraft.mod.fvtm.util.ContentConfigUtil;
import net.fexcraft.mod.uni.IDL;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Part2 extends Content<Part2> implements TextureHolder {

	protected List<IDL> textures;
	protected ArrayList<String> categories;
	protected ArrayList<PartFunction> functions = new ArrayList<>();
	protected Map<String, Attribute<?>> attributes = new LinkedHashMap<>();
	protected Map<String, String> attr_mods = new LinkedHashMap<>();

	public Part2(){}

	@Override
	public Part2 parse(JsonMap map){
		if((pack = ContentConfigUtil.getAddon(map)) == null) return null;
		if((id = ContentConfigUtil.getID(pack, map)) == null) return null;
		//
		name = map.getString("Name", "Unnamed Material");
		description = ContentConfigUtil.getStringList(map, "Description");
		textures = ContentConfigUtil.getTextures(map);
		if(map.has("Category")){
			if(map.get("Category").isArray()){
				categories = map.getArray("Category").toStringList();
			}
			else{
				categories = new ArrayList<>();
				categories.add(map.getString("Category", "ballast"));
			}
		}
		else categories = new ArrayList<>();
		if(map.has("Attributes")){
			for(Entry<String, JsonValue<?>> entry : map.getMap("Attributes").entries()){
				Attribute<?> attr = Attribute.parse(entry.getKey(), entry.getValue().asMap());
				if(attr != null) attributes.put(attr.id, attr);
			}
		}
		if(map.has("AttributeModifiers")){
			JsonMap mod = map.getMap("AttributeModifiers");
			for(Entry<String, JsonValue<?>> entry : mod.entries()){
				attr_mods.put(entry.getKey(), entry.getValue().string_value());
			}
		}
		if(map.has("Functions")){
			JsonMap funcs = map.getMap("Functions");
			for(Entry<String, JsonValue<?>> entry : funcs.entries()){
				PartFunction fun = FvtmResources.getFunction(entry.getKey());
				if(fun != null) functions.add(fun.init(this, entry.getValue().asMap()));
			}
		}
		return this;
	}

	@Override
	public ContentType getContentType(){
		return ContentType.PART;
	}

	@Override
	public Class<?> getDataClass(){
		return null;
	}

	@Override
	public List<IDL> getDefaultTextures(){
		return textures;
	}

}
