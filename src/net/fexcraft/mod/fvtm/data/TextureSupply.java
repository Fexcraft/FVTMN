package net.fexcraft.mod.fvtm.data;

import java.util.ArrayList;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonObject;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;

public class TextureSupply {
	
	private ArrayList<String> targets = new ArrayList<>();
	private ArrayList<IDL> reslocs = new ArrayList<>();
	public final String id;
	
	public TextureSupply(String key){
		this.id = key;
	}
	
	public TextureSupply(String key, JsonMap map){
		this(key);
		JsonObject<?> obj = map.get("target");
		if(obj.isArray()) obj.asArray().elements().forEach(elm -> targets.add(elm.string_value()));
		else targets.add(obj.string_value());
		obj = map.get("texture");
		if(obj.isArray()) obj.asArray().elements().forEach(elm -> reslocs.add(IDLManager.getIDLNamed(elm.string_value())));
		else reslocs.add(IDLManager.getIDLNamed(obj.string_value()));
	}
	
	public ArrayList<String> targets(){
		return targets;
	}
	
	public ArrayList<IDL> textures(){
		return reslocs;
	}

}
