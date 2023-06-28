package net.fexcraft.mod.fvtm;

import static net.fexcraft.mod.fvtm.FvtmRegistry.ADDONS;

import java.io.File;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonHandler;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonValue;
import net.fexcraft.mod.fvtm.data.addon.Addon;
import net.fexcraft.mod.fvtm.data.addon.AddonLocation;
import net.fexcraft.mod.fvtm.util.ContentConfigUtil;
import net.fexcraft.mod.fvtm.util.ZipUtils;
import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.IDL;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class FvtmResources {

	public static FvtmResources INSTANCE;
	public static File FVTM_CONFIG_DIR;

	public void init(){
		FVTM_CONFIG_DIR = new File(FvtmRegistry.CONFIG_DIR, "/fvtm/");
		if(!FVTM_CONFIG_DIR.exists()) FVTM_CONFIG_DIR.mkdirs();
		boolean failed = searchPacksInResourcePacks();
		if(!EnvInfo.CLIENT || failed){
			searchPacksInFolder(new File(FvtmRegistry.CONFIG_DIR.getParentFile(), "/resourcepacks/"), AddonLocation.RESOURCEPACK, false);
		}
		searchPacksInFolder(new File(FVTM_CONFIG_DIR, "packs/"), AddonLocation.CONFIGPACK, true);
		if(Config.LOAD_PACKS_FROM_MODS){
			searchPacksInFolder(new File(FvtmRegistry.CONFIG_DIR.getParentFile(), "/mods/"), AddonLocation.CONFIGPACK, false);
		}
		if(EnvInfo.CLIENT) loadPackTextures();
	}

	public abstract boolean searchPacksInResourcePacks();

	public void searchPacksInFolder(File folder, AddonLocation loc, boolean create){
		if(!folder.exists()){
			if(!create) return;
			folder.mkdir();
		}
		for(File file : folder.listFiles()){
			if(file.isHidden()) continue;
			if(file.isDirectory()){
				File assets = new File(file, "assets/");
				if(assets.exists()){
					for(File fl : assets.listFiles()){
						if(!fl.isDirectory()) continue;
						File dec = new File(fl, "addonpack.fvtm");
						if(dec.exists()){
							JsonMap map = JsonHandler.parse(dec);
							if(isDuplicateOrInvalidPack(map)) continue;
							ADDONS.register(new Addon(file, loc).parse(map));
						}
					}
				}
			}
			else if(file.getName().endsWith(".zip") || file.getName().endsWith(".jar")){
				JsonArray array = ZipUtils.getValuesAt(file, "assets", "addonpack.fvtm");
				for(JsonValue<?> value : array.value){
					if(!value.isMap()) continue;
					JsonMap map = value.asMap();
					if(isDuplicateOrInvalidPack(map)) continue;
					ADDONS.register(new Addon(file, loc).parse(map));
				}
			}
		}
	}

	public static boolean isDuplicateOrInvalidPack(JsonMap map){
		if(!map.has("RegistryName") && !map.has("ID")) return true;
		IDL id = ContentConfigUtil.getID(map);
		for(Addon addon : ADDONS){
			if(addon.getID().equals(id)) return true;
		}
		return false;
	}

	public abstract void loadPackTextures();

}
