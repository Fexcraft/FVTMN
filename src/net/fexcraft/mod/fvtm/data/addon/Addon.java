package net.fexcraft.mod.fvtm.data.addon;

import static net.fexcraft.mod.fvtm.FvtmRegistry.DECORATIONS;
import static net.fexcraft.mod.fvtm.FvtmRegistry.DECORATION_CATEGORIES;
import static net.fexcraft.mod.uni.IDL.conid;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonValue;
import net.fexcraft.mod.fvtm.FvtmRegistry;
import net.fexcraft.mod.fvtm.data.Content;
import net.fexcraft.mod.fvtm.data.ContentType;
import net.fexcraft.mod.fvtm.data.DecorationData;
import net.fexcraft.mod.fvtm.data.TextureSupply;
import net.fexcraft.mod.fvtm.sys.condition.Condition;
import net.fexcraft.mod.fvtm.sys.condition.ConditionRegistry;
import net.fexcraft.mod.fvtm.sys.particle.Particle;
import net.fexcraft.mod.fvtm.sys.tsign.TrafficSignLibrary;
import net.fexcraft.mod.fvtm.util.ContentConfigUtil;
import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.IDLManager;
import net.fexcraft.mod.uni.client.CTab;
import net.fexcraft.mod.uni.item.ClothMaterial;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Addon extends Content<Addon> {

	private HashMap<String, CTab> creativetabs = new HashMap<>();
	protected HashMap<String, ClothMaterial> clothmats = new HashMap<>();
	protected LinkedHashMap<String, TextureSupply> supp_tex = new LinkedHashMap<>();
	private List<String> authors = new ArrayList<>();
	private AddonLocation loc;
	private boolean isJar;
	private File file;
	private String version;
	private String website;
	private String license;

	public Addon(File file, AddonLocation loc){
		isJar = !file.isDirectory();
		this.file = file;
		this.loc = loc;
	}

	@Override
	public Addon parse(JsonMap map){
		id = ContentConfigUtil.getID(map);
		pack = this;
		name = map.getString("Name", "Unnamed Addon");
		version = map.getString("Version", "0.0");
		if(map.has("Authors")){
			map.getArray("Authors").value.forEach(val -> authors.add(val.string_value()));
		}
		if(map.has("Author")) authors.add(map.get("Author").string_value());
		website = map.getString("Website", "http://fexcraft.net/minecraft/content");
		license = map.getString("License", "All Rights Reserved");
		if(EnvInfo.CLIENT){
			if(!map.has("CreativeTabs")){
				creativetabs.put(CTab.DEFAULT, CTab.create(this, CTab.DEFAULT));
			}
			else{
				map.getArray("CreativeTabs").value.forEach(jsn -> {
					creativetabs.put(jsn.string_value(), CTab.create(this, jsn.string_value()));
				});
			}
		}
		if(map.has("ClothMaterials")){
			map.getMap("ClothMaterials").entries().forEach(entry -> {
				clothmats.put(entry.getKey(), ClothMaterial.create(IDLManager.getIDLCached(conid(id, entry.getKey())), entry.getValue().asMap()));
			});
		}
		if(map.has("SupplyTextures")){
			map.getMap("SupplyTextures").entries().forEach(entry -> {
				supp_tex.put(entry.getKey(), new TextureSupply(entry.getKey(), entry.getValue().asMap()));
			});
		}
		if(map.has("WireDecos")){
			FvtmRegistry.WIRE_DECO_CACHE.put(getID().id(), map.getMap("WireDecos"));
		}
		if(map.has("Particles") && EnvInfo.CLIENT){
			for(Entry<String, JsonValue<?>> entry : map.getMap("Particles").entries()){
				new Particle(conid(id, entry.getKey()), entry.getValue().asMap());
			}
		}
		if(map.has("Conditions")){
			for(Entry<String, JsonValue<?>> entry : map.getMap("Conditions").entries()){
				Condition cond = null;
				if(entry.getValue().isArray()){
					cond = new Condition(conid(id, entry.getKey()), entry.getValue().asArray());
				}
				else{
					cond = new Condition(conid(id, entry.getKey()), entry.getValue().asMap());
				}
				if(cond != null) ConditionRegistry.register(cond);
			}
		}
		if(map.has("TrafficSigns")){
			JsonMap tsn = map.getMap("TrafficSigns");
			TrafficSignLibrary.AddonLib lib = new TrafficSignLibrary.AddonLib(id.id());
			if(tsn.has("backgrounds")){
				for(Entry<String, JsonValue<?>> entry : tsn.getMap("backgrounds").entries()){
					lib.backgrounds.put(entry.getKey(), entry.getValue().string_value());
				}
			}
			if(tsn.has("components")){
				for(Entry<String, JsonValue<?>> entry : tsn.getMap("components").entries()){
					lib.components.put(entry.getKey(), entry.getValue().string_value());
				}
			}
			if(tsn.has("fonts")){
				for(Entry<String, JsonValue<?>> entry : tsn.getMap("fonts").entries()){
					lib.fonts.put(entry.getKey(), entry.getValue().string_value());
				}
			}
			if(tsn.has("presets")){
				for(Entry<String, JsonValue<?>> entry : tsn.getMap("presets").entries()){
					lib.presets.put(entry.getKey(), entry.getValue().asMap());
				}
			}
			TrafficSignLibrary.LIBRARIES.put(lib.id, lib);
			lib.load();
		}
		if(map.has("Decorations")){
			for(Entry<String, JsonValue<?>> entry : map.getMap("Decorations").entries()){
				String cat = entry.getKey();
				JsonMap decos = entry.getValue().asMap();
				decos.entries().forEach(deco -> {
					String key = conid(id, deco.getKey());
					DECORATIONS.put(key, new DecorationData(key, cat, deco.getValue()));
				});
				if(decos.size() > 0 && !DECORATION_CATEGORIES.contains(cat)){
					DECORATION_CATEGORIES.add(cat);
				}
			}
		}
		return this;
	}

	@Override
	public ContentType getContentType(){
		return ContentType.ADDON;
	}

	@Override
	public Class<?> getDataClass(){
		return null;
	}

	@Override
	public void loadModel(){}

	public File getFile(){
		return file;
	}

	public AddonLocation getLocation(){
		return loc;
	}

	public boolean isJar(){
		return isJar;
	}

	public String getVersion(){
		return version;
	}

	public Map<String, TextureSupply> getTextureSuppliers(){
		return supp_tex;
	}

	public CTab getDefaultCreativeTab(){
		if(creativetabs.size() == 0) return null;
		if(creativetabs.containsKey("default")) return creativetabs.get("default");
		else return creativetabs.values().toArray(new CTab[0])[0];
	}

	public CTab getCreativeTab(String id){
		if(creativetabs.containsKey(id)) return creativetabs.get(id);
		else return getDefaultCreativeTab();
	}

	public List<String> getAuthors(){
		return ImmutableList.copyOf(authors);
	}

	public String getWebsite(){
		return website;
	}

	public String getLicense(){
		return license;
	}

}
