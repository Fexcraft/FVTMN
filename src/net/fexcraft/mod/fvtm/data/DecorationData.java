package net.fexcraft.mod.fvtm.data;

import static net.fexcraft.mod.fvtm.FvtmRegistry.DECORATIONS;
import static net.fexcraft.mod.fvtm.FvtmRegistry.WHITE_TEXTURE;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonValue;
import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.mod.uni.Pos;
import net.fexcraft.mod.fvtm.data.root.Colorable;
import net.fexcraft.mod.fvtm.model.Model;
import net.fexcraft.mod.fvtm.model.ModelData;
import net.fexcraft.mod.fvtm.util.ContentConfigUtil;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;
import net.fexcraft.mod.uni.tag.TagCW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class DecorationData implements Colorable {
	
	private final String key, category;
	public String modelid;
	public Model model;
	public ModelData modeldata = new ModelData();
	public ArrayList<IDL> textures = new ArrayList<>();
	private TreeMap<String, RGB> channels = new TreeMap<>();
	public Pos offset = new Pos(0, 0, 0);
	public float rotx, roty, rotz;
	public float sclx = 1, scly = 1, sclz = 1;
	public int size = 8, seltex;
	
	public DecorationData(String key, String category){
		this.key = key;
		this.category = category;
	}
	
	public DecorationData(String key, String category, JsonValue value){
		this(key, category);
		if(value.isValue()){
			modelid = value.string_value();
		}
		else{
			if(value.isArray()){
				JsonArray array = value.asArray();
				modelid = array.get(0).string_value();
				if(array.get(1).isArray()){
					array.getArray(1).value.forEach(val -> textures.add(IDLManager.getIDLNamed(val.string_value())));
				}
				else{
					textures.add(IDLManager.getIDLNamed(array.get(1).string_value()));
				}
				if(array.size() > 2) size = array.get(2).integer_value();
				if(array.size() > 3){
					for(JsonValue val : array.getArray(3).value){
						channels.put(val.string_value(), RGB.WHITE.copy());
					}
				}
			}
			else{
				JsonMap map = value.asMap();
				modelid = map.get("model").string_value();
				if(map.has("size")) size = map.get("size").integer_value();
				if(map.has("channels")){
					for(Entry<String, JsonValue<?>> entry : map.getMap("channels").entries()){
						channels.put(entry.getKey(), new RGB(entry.getValue().string_value()));
					}
				}
				if(map.has("texture")){
					if(map.get("texture").isArray()){
						map.getArray("texture").value.forEach(val -> textures.add(IDLManager.getIDLNamed(val.string_value())));
					}
					else{
						textures.add(IDLManager.getIDLNamed(map.get("texture").string_value()));
					}
				}
				if(map.has("modeldata")){
					modeldata = ContentConfigUtil.getModelData(map, "modeldata", modeldata);
				}
			}
		}
		if(textures.isEmpty()) textures.add(WHITE_TEXTURE);
	}
	
	public DecorationData(TagCW compound, boolean client){
		key = compound.getString("key");
		category = compound.getString("category");
		offset = new Pos(compound.getFloat("offx"), compound.getFloat("offy"), compound.getFloat("offz"));
		if(compound.has("rotx")) rotx = compound.getFloat("rotx");
		if(compound.has("roty")) roty = compound.getFloat("roty");
		if(compound.has("rotz")) rotz = compound.getFloat("rotz");
		if(compound.has("sclx")) sclx = compound.getFloat("sclx");
		if(compound.has("scly")) scly = compound.getFloat("scly");
		if(compound.has("sclz")) sclz = compound.getFloat("sclz");
		if(compound.has("seltex")) seltex = compound.getInteger("seltex");
		DecorationData data = DECORATIONS.get(key);
		if(data != null) copy(data);
		if(seltex >= textures.size()) seltex = textures.size() - 1;
		if(seltex < 0) seltex = 0;
		for(Entry<String, RGB> entry : channels.entrySet()){
			if(compound.has("color_" + entry.getKey())) entry.getValue().packed = compound.getInteger("color_" + entry.getKey());
		}
	}

	@Override
	public RGB getColorChannel(String channel){
		return channels.get(channel);
	}
	
	@Override
	public void setColorChannel(String channel, RGB color){
		channels.put(channel, color);
	}
	
	@Override
	public TreeMap<String, RGB> getColorChannels(){
		return channels;
	}

	public String key(){
		return key;
	}
	
	public String category(){
		return category;
	}
	
	public DecorationData copy(){
		DecorationData data = new DecorationData(key, category);
		data.size = size;
		data.model = model;
		data.modelid = modelid;
		channels.forEach((key, rgb) -> data.channels.put(key, rgb.copy()));
		data.offset = offset.copy();
		data.rotx = rotx;
		data.roty = roty;
		data.rotz = rotz;
		data.sclx = sclx;
		data.scly = scly;
		data.sclz = sclz;
		data.textures.addAll(textures);
		data.seltex = seltex;
		return data;
	}
	
	public DecorationData copy(DecorationData data){
		model = data.model;
		modelid = data.modelid;
		channels.clear();
		textures.clear();
		data.channels.forEach((key, rgb) -> channels.put(key, rgb.copy()));
		textures.addAll(data.textures);
		return data;
	}

	public TagCW write(){
		TagCW compound = TagCW.create();
		compound.set("key", key);
		compound.set("category", category);
		compound.set("offx", offset.x);
		compound.set("offy", offset.y);
		compound.set("offz", offset.z);
		if(rotx != 0f) compound.set("rotx", rotx);
		if(roty != 0f) compound.set("roty", roty);
		if(rotz != 0f) compound.set("rotz", rotz);
		if(sclx != 1f) compound.set("sclx", sclx);
		if(scly != 1f) compound.set("scly", scly);
		if(sclz != 1f) compound.set("sclz", sclz);
		if(seltex != 0) compound.set("seltex", seltex);
		for(Entry<String, RGB> entry : channels.entrySet()){
			compound.set("color_" + entry.getKey(), entry.getValue().packed);
		}
		return compound;
	}

}
