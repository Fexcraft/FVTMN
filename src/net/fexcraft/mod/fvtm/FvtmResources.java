package net.fexcraft.mod.fvtm;

import static net.fexcraft.mod.fvtm.FvtmLogger.LOGGER;
import static net.fexcraft.mod.fvtm.FvtmRegistry.ADDONS;
import static net.fexcraft.mod.fvtm.FvtmRegistry.getAddon;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonHandler;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonValue;
import net.fexcraft.mod.fvtm.data.Content;
import net.fexcraft.mod.fvtm.data.ContentType;
import net.fexcraft.mod.fvtm.data.addon.Addon;
import net.fexcraft.mod.fvtm.data.addon.AddonLocation;
import net.fexcraft.mod.fvtm.data.root.WithItem;
import net.fexcraft.mod.fvtm.util.ContentConfigUtil;
import net.fexcraft.mod.fvtm.util.ZipUtils;
import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.client.CTab;
import net.fexcraft.mod.uni.item.ItemWrapper;
import net.fexcraft.mod.uni.item.StackWrapper;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class FvtmResources {

	public static FvtmResources INSTANCE;
	public static File FVTM_CONFIG_DIR;

	public void init(){
		FVTM_CONFIG_DIR = new File(FvtmRegistry.CONFIG_DIR, "/fvtm/");
		if(!FVTM_CONFIG_DIR.exists()) FVTM_CONFIG_DIR.mkdirs();
		INSTANCE.searchASMPacks();
		boolean failed = searchPacksInResourcePacks();
		if(!EnvInfo.CLIENT || failed){
			searchPacksInFolder(new File(FvtmRegistry.CONFIG_DIR.getParentFile(), "/resourcepacks/"), AddonLocation.RESOURCEPACK, false);
		}
		searchPacksInFolder(new File(FVTM_CONFIG_DIR, "packs/"), AddonLocation.CONFIGPACK, true);
		if(Config.LOAD_PACKS_FROM_MODS){
			searchPacksInFolder(new File(FvtmRegistry.CONFIG_DIR.getParentFile(), "/mods/"), AddonLocation.CONFIGPACK, false);
		}
		for(File file : Config.PACK_FOLDERS){
			searchPacksInFolder(file, AddonLocation.CONFIGPACK, true);
		}
		searchPacksInFolder(new File(FVTM_CONFIG_DIR, "packs/"), AddonLocation.CONFIGPACK, true);
		if(EnvInfo.CLIENT) loadPackTextures();
	}

	public abstract void searchASMPacks();

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

	public void searchContent(){
		FvtmResources.INSTANCE.searchInPacksFor(ContentType.FUEL);
		FvtmResources.INSTANCE.searchInPacksFor(ContentType.MATERIAL);
		FvtmResources.INSTANCE.searchInPacksFor(ContentType.CONSUMABLE);
		FvtmResources.INSTANCE.searchInPacksFor(ContentType.CLOTH);
		FvtmResources.INSTANCE.searchInPacksFor(ContentType.RAILGAUGE);
		FvtmResources.INSTANCE.searchInPacksFor(ContentType.WIRE);
		FvtmResources.INSTANCE.searchInPacksFor(ContentType.CONTAINER);
		FvtmResources.INSTANCE.searchInPacksFor(ContentType.BLOCK);
		FvtmResources.INSTANCE.searchInPacksFor(ContentType.MULTIBLOCK);
		FvtmResources.INSTANCE.searchInPacksFor(ContentType.PART);
		FvtmResources.INSTANCE.searchInPacksFor(ContentType.VEHICLE);
	}

	public void searchInPacksFor(ContentType contype){
		if(contype == ContentType.ADDON) return;
		for(Addon addon : ADDONS){
			if(addon.getFile().isDirectory()){
				File folder = new File(addon.getFile(), "assets/" + addon.getID().id() + "/config/" + contype.folder + "/");
				if(!folder.exists()) continue;
				ArrayList<File> files = findFiles(folder, contype.suffix);
				for(File file : files){
					try{
						JsonMap map = JsonHandler.parse(file);
						Content<?> content = (Content<?>)contype.impl.newInstance().parse(map);
						if(content == null){
							IDL idl = ContentConfigUtil.getID(map);
							LOGGER.log("Errors while loading config file: " + file + " for " + idl.colon());
						}
						contype.register(content);
						if(EnvInfo.CLIENT) checkForCustomModel(addon.getLocation(), contype, content);
					}
					catch(Exception e){
						if(EnvInfo.DEV) e.printStackTrace();
						LOGGER.log("Errors while loading config file: " + file); //Static.stop();
					}
				}
			}
			else{
				String lastentry = null;
				try{
					String path = "assets/" + addon.getID().id() + "/config/" + contype.folder + "/";
					ZipFile zip = new ZipFile(addon.getFile());
					ZipInputStream stream = new ZipInputStream(new FileInputStream(addon.getFile()));
					ZipEntry entry = null;
					while(true){
						entry = stream.getNextEntry();
						if(entry == null) break;
						lastentry = entry.getName();
						if(entry.getName().startsWith(path) && entry.getName().endsWith(contype.suffix)){
							JsonMap map = JsonHandler.parse(zip.getInputStream(entry));
							Content<?> content = (Content<?>)contype.impl.newInstance().parse(map);
							if(content == null){
								IDL idl = ContentConfigUtil.getID(map);
								LOGGER.log("Errors while loading config from zip: " + addon.getFile() + " for " + idl.colon());
							}
							contype.register(content);
							if(EnvInfo.CLIENT) checkForCustomModel(addon.getLocation(), contype, content);
						}
					}
				}
				catch (Exception e){
					if(EnvInfo.DEV) e.printStackTrace();
					LOGGER.log("Errors while loading config from zip: " + addon.getFile() + " - " + lastentry); //Static.stop();
				}
			}
		}
	}

	public static ArrayList<File> findFiles(File file, String suffix){
		ArrayList<File> result = new ArrayList<>();
		if(file.isDirectory()){
			for(File sub : file.listFiles()){
				ArrayList<File> search = findFiles(sub, suffix);
				if(!search.isEmpty()) result.addAll(search);
			}
		}
		else if(file.getName().endsWith(suffix)) result.add(file);
		return result;
	}

	public abstract void checkForCustomModel(AddonLocation loc, ContentType contype, Content<?> content);

	public abstract void createContentItems();

	public static void loadRecipes(){

	}

	public abstract ItemWrapper getItemWrapper(String id);

	public CTab getCreativeTab(WithItem type){
		String tab = type.getCreativeTab();
		Addon addon = null;
		if(tab.contains(":")){
			String[] split = tab.split(":");
			addon = getAddon(split[0]);
			if(addon == null) return null;
			return addon.getCreativeTab(split[1]);
		}
		addon = ((Content<?>)type).getAddon();
		return addon.getCreativeTab(tab);
	}

	public CTab getCreativeTab(String tabid){
		if(tabid.contains(":")){
			String[] split = tabid.split(":");
			Addon addon = getAddon(split[0]);
			if(addon != null) return addon.getCreativeTab(split[1]);
		}
		return ADDONS.get("fvtm:fvtm").getCreativeTab(tabid);
	}

	public abstract StackWrapper newStack(ItemWrapper item);

}
