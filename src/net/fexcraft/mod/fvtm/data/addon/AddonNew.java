package net.fexcraft.mod.fvtm.data.addon;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fvtm.data.Content;
import net.fexcraft.mod.fvtm.data.ContentType;
import net.fexcraft.mod.fvtm.data.TextureSupply;
import net.fexcraft.mod.fvtm.util.ContentConfigUtil;
import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.IDLManager;
import net.fexcraft.mod.uni.client.CTab;
import net.fexcraft.mod.uni.item.ClothMaterial;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class AddonNew extends Content<AddonNew> {

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

	public AddonNew(File file, AddonLocation loc){
		isJar = !file.isDirectory();
		this.file = file;
		this.loc = loc;
	}

	@Override
	public AddonNew parse(JsonMap map){
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
				clothmats.put(entry.getKey(), ClothMaterial.create(IDLManager.getIDLCached(id.id() + ":" + entry.getKey()), entry.getValue().asMap()));
			});
		}
		if(map.has("SupplyTextures")){
			map.getMap("SupplyTextures").entries().forEach(entry -> {
				supp_tex.put(entry.getKey(), new TextureSupply(entry.getKey(), entry.getValue().asMap()));
			});
		}
		//
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

}
