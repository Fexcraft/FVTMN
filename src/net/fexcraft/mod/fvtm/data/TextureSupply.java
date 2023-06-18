package net.fexcraft.mod.fvtm.data;

import java.util.ArrayList;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonObject;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class TextureSupply {
	
	private ArrayList<String> targets = new ArrayList<>();
	private ArrayList<IDL> locs = new ArrayList<>();
	public final String id;
	
	public TextureSupply(String key){
		this.id = key;
	}
	
	public TextureSupply(String key, JsonMap map){
		this(key);
		JsonObject tar = map.get("target");
		if(tar.isArray()){
			for(JsonObject jsn : tar.asArray().value) targets.add(jsn.string_value());
		}
		else targets.add(tar.string_value());
		JsonObject tex = map.get("texture");
		if(tex.isArray()){
			for(JsonObject jsn : tex.asArray().value) locs.add(IDLManager.getIDLNamed(jsn.string_value()));
		}
		else locs.add(IDLManager.getIDLNamed(tex.string_value()));
	}
	
	public ArrayList<String> targets(){
		return targets;
	}
	
	public ArrayList<IDL> textures(){
		return locs;
	}

}
