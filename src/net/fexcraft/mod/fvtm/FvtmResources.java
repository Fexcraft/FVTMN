package net.fexcraft.mod.fvtm;

import static net.fexcraft.mod.fvtm.FvtmLogger.LOGGER;
import static net.fexcraft.mod.fvtm.FvtmRegistry.*;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonHandler;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonValue;
import net.fexcraft.lib.common.Static;
import net.fexcraft.mod.fvtm.data.Content;
import net.fexcraft.mod.fvtm.data.ContentType;
import net.fexcraft.mod.fvtm.data.DecorationData;
import net.fexcraft.mod.fvtm.data.addon.Addon;
import net.fexcraft.mod.fvtm.data.addon.AddonLocation;
import net.fexcraft.mod.fvtm.data.attribute.*;
import net.fexcraft.mod.fvtm.data.part.Part;
import net.fexcraft.mod.fvtm.data.part.PartData;
import net.fexcraft.mod.fvtm.data.part.PartFunction;
import net.fexcraft.mod.fvtm.data.part.PartInstallHandler;
import net.fexcraft.mod.fvtm.data.root.WithItem;
import net.fexcraft.mod.fvtm.data.vehicle.Vehicle;
import net.fexcraft.mod.fvtm.data.vehicle.VehicleData;
import net.fexcraft.mod.fvtm.function.part.*;
import net.fexcraft.mod.fvtm.handler.DefaultPartInstallHandler;
import net.fexcraft.mod.fvtm.model.*;
import net.fexcraft.mod.fvtm.model.loaders.ClassModelLoader;
import net.fexcraft.mod.fvtm.model.loaders.FMFModelLoader;
import net.fexcraft.mod.fvtm.model.loaders.JTMTModelLoader;
import net.fexcraft.mod.fvtm.model.loaders.ObjModelLoader;
import net.fexcraft.mod.fvtm.model.loaders.SMPTBJavaModelLoader;
import net.fexcraft.mod.fvtm.sys.uni.KeyPress;
import net.fexcraft.mod.fvtm.sys.uni.SeatInstance;
import net.fexcraft.mod.fvtm.util.ContentConfigUtil;
import net.fexcraft.mod.fvtm.util.ZipUtils;
import net.fexcraft.mod.fvtm.util.function.InventoryFunction;
import net.fexcraft.mod.fvtm.util.function.TireFunction;
import net.fexcraft.mod.fvtm.util.handler.BogieInstallationHandler;
import net.fexcraft.mod.fvtm.util.handler.ConnectorInstallationHandler;
import net.fexcraft.mod.fvtm.handler.TireInstallationHandler;
import net.fexcraft.mod.fvtm.handler.WheelInstallationHandler;
import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;
import net.fexcraft.mod.uni.client.CTab;
import net.fexcraft.mod.uni.item.ItemWrapper;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.world.EntityW;
import org.apache.commons.io.FilenameUtils;

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

	public void registerAttributes(){
		ATTRIBUTES.put("boolean", AttrBoolean.class);
		ATTRIBUTES.put("bool", AttrBoolean.class);
		ATTRIBUTES.put("bln", AttrBoolean.class);
		ATTRIBUTES.put("float", AttrFloat.class);
		ATTRIBUTES.put("flt", AttrFloat.class);
		ATTRIBUTES.put("integer", AttrInteger.class);
		ATTRIBUTES.put("int", AttrInteger.class);
		ATTRIBUTES.put("tristate", AttrTristate.class);
		ATTRIBUTES.put("tri", AttrTristate.class);
		ATTRIBUTES.put("string", AttrString.class);
		ATTRIBUTES.put("str", AttrString.class);
		ATTRIBUTES.put("vector3", AttrVector.class);
		ATTRIBUTES.put("vector", AttrVector.class);
		ATTRIBUTES.put("vec", AttrVector.class);
	}

	public void registerFunctions(){
		PART_FUNCTIONS.put("fvtm:wheel", WheelFunction.class);
		PART_FUNCTIONS.put("fvtm:wheel_positions", WheelPositionsFunction.class);
		PART_FUNCTIONS.put("fvtm:seats", SeatsFunction.class);
		PART_FUNCTIONS.put("fvtm:engine", EngineFunction.class);
		PART_FUNCTIONS.put("fvtm:inventory", InventoryFunction.class);
		PART_FUNCTIONS.put("fvtm:container", ContainerFunction.class);
		PART_FUNCTIONS.put("fvtm:bogie", BogieFunction.class);
		PART_FUNCTIONS.put("fvtm:part_slots", PartSlotsFunction.class);
		PART_FUNCTIONS.put("fvtm:color", ColorFunction.class);
		PART_FUNCTIONS.put("fvtm:tire", TireFunction.class);
		PART_FUNCTIONS.put("fvtm:transmission", TransmissionFunction.class);
		PART_FUNCTIONS.put("fvtm:particle_emitter", ParticleEmitterFunction.class);
	}

	public void registerHandlers(){
		PartInstallHandler.HANDLERS.put("default", new DefaultPartInstallHandler());
		PartInstallHandler.HANDLERS.put("wheel", new WheelInstallationHandler());
		PartInstallHandler.HANDLERS.put("bogie", new BogieInstallationHandler());
		PartInstallHandler.HANDLERS.put("tire", new TireInstallationHandler());
		PartInstallHandler.HANDLERS.put("connector", new ConnectorInstallationHandler());
	}

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
					LOGGER.log("Errors while loading config from zip: " + addon.getFile() + " - " + lastentry); //Static.stop();
					if(EnvInfo.DEV) e.printStackTrace();
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

	public abstract StackWrapper newStack(Object local);

	public static StackWrapper newStack(IDL id){
		return INSTANCE.newStack(FvtmRegistry.getItem(id.colon()));
	}

	public static JsonMap getJson(String loc){
		try{
			return JsonHandler.parse(FvtmResources.class.getClassLoader().getResourceAsStream(loc));
		}
		catch(IOException e){
			e.printStackTrace();
			if(EnvInfo.DEV) throw new RuntimeException(e);
		}
		return new JsonMap();
	}

	public abstract JsonMap getJsonC(String loc);
	
	//-V-// Model Loading //-V-//

	public void initModelLoaders(){
		MODEL_LOADERS.add(new ClassModelLoader());
		MODEL_LOADERS.add(new JTMTModelLoader());
		MODEL_LOADERS.add(new FMFModelLoader());
		MODEL_LOADERS.add(new ObjModelLoader());
		MODEL_LOADERS.add(new SMPTBJavaModelLoader());
	}

	public abstract void initModelPrograms();
	
	public void initModels(){
		PARTS.forEach(part -> part.loadModel());
		VEHICLES.forEach(vehicle -> vehicle.loadModel());
		//other data types
		for(DecorationData deco : DECORATIONS.values()){
			Model model = getModel(deco.modelid, deco.modeldata, DefaultModel.class);
			if(model != null && model != DefaultModel.EMPTY) MODELS.put(deco.modelid, deco.model = model);
		}
	}

	public void initModelsClear(){
		ObjModelLoader.clearCache();
	}

	public static Model getModel(String location, ModelData data, Class<? extends Model> clazz){
		if(location == null || location.equals("") || location.equals("null")) return getEmptyModelForClass(clazz);
		boolean bake = location.startsWith("baked|");
		if(bake) location = location.substring(6);
		Model model = null;
		if(MODELS.containsKey(location)){
			if(bake && getEmptyModelForClass(clazz) instanceof BlockModel){
				return getEmptyModelForClass(clazz);
			}
			return MODELS.get(location);
		}
		ModelLoader loader = getModelLoader(location, FilenameUtils.getExtension(location));
		if(loader == null) return getEmptyModelForClass(clazz);
		try{
			Object[] ret = loader.load(location, data, () -> {
				try{
					return clazz.getConstructor().newInstance();
				}
				catch(Throwable e){
					e.printStackTrace(); Static.stop();
					return getEmptyModelForClass(clazz);
				}
			});
			if(ret.length == 0 || ret[0] == null) return getEmptyModelForClass(clazz);
			model = (Model)ret[0];
			if(ret.length > 1) data = (ModelData)ret[1];
			data.convert();
			model.parse(data).lock();
		}
		catch(Exception e){
			e.printStackTrace(); //Static.stop();
		}
		MODELS.put(location, model);
		if(bake && model instanceof BlockModel){
			//TODO FCLBlockModelLoader.addBlockModel(new ResourceLocation(location), (FCLBlockModel)model);
			return getEmptyModelForClass(clazz);
		}
		return model;
	}

	public static Model getEmptyModelForClass(Class<? extends Model> clazz){
		if(clazz == ContainerModel.class) return ContainerModel.EMPTY;
		if(clazz == PartModel.class) return PartModel.EMPTY;
		if(clazz == VehicleModel.class) return VehicleModel.EMPTY;
		if(clazz == TrafficSignModel.class) return TrafficSignModel.EMPTY;
		if(clazz == BlockModel.class) return BlockModel.EMPTY;
		if(clazz == RailGaugeModel.class) return RailGaugeModel.EMPTY;
		if(clazz == ClothModel.class) return ClothModel.EMPTY;
		if(clazz == WireModel.class) return WireModel.EMPTY;
		return DefaultModel.EMPTY;
	}

	public static ModelLoader getModelLoader(String name, String extension){
		for(ModelLoader loader : MODEL_LOADERS){
			if(loader.accepts(name, extension)) return loader;
		}
		return null;
	}

	public InputStream getModelInputStream(String loc, boolean log){
		return getModelInputStream(IDLManager.getIDLCached(loc), log);
	}

	public abstract InputStream getModelInputStream(IDL loc, boolean log);

	public static Object[] getModelInputStreamWithFallback(String loc){
		return getModelInputStreamWithFallback(IDLManager.getIDLCached(loc));
	}

	public static Object[] getModelInputStreamWithFallback(IDL loc){
		Closeable[] close = null;
		InputStream stream = INSTANCE.getModelInputStream(loc, false);
		if(stream != null) return new Object[]{ stream };
		try{
			Addon addon = getAddon(loc.space());
			if(addon != null && addon.getLocation().isConfigPack()){
				if(addon.getFile().isDirectory()){
					File file = new File(addon.getFile(), "assets/" + loc.space() + "/" + loc.path());
					if(file.exists()) stream = new FileInputStream(file);
				}
				else{
					String filename = "assets/" + loc.space() + "/" + loc.path();
					ZipFile zip = new ZipFile(addon.getFile());
					ZipInputStream zipstream = new ZipInputStream(new FileInputStream(addon.getFile()));
					close = new Closeable[]{ zip, zipstream };
					while(true){
						ZipEntry entry = zipstream.getNextEntry();
						if(entry == null) break;
						if(entry.getName().equals(filename)){
							stream = zip.getInputStream(entry);
							break;
						}
					}
				}
			}
		}
		catch(Throwable e){
			//e.printStackTrace();
		}
		return close == null ? new Object[]{ stream } : new Object[]{ stream, close };
	}

	public abstract boolean isModPresent(String s);

	public abstract IDL getExternalTexture(String custom);

	public static PartFunction getFunction(String key){
		try{
			return PART_FUNCTIONS.get(key).newInstance();
		}
		catch(Exception e){
			if(EnvInfo.DEV) e.printStackTrace();
			return null;
		}
	}

	public PartData getPartData(TagCW com){
		Part part = PARTS.get(com.getString("Part"));
		if(part == null) return null;
		return new PartData(part).read(com);
	}

	public VehicleData getVehicleData(TagCW com){
		Vehicle vehicle = VEHICLES.get(com.getString("Vehicle"));
		if(vehicle == null) return null;
		return new VehicleData(vehicle).read(com);
	}

	public abstract void registerFvtmBlocks();

	public abstract void registerFvtmItems();

	public abstract boolean handleClick(KeyPress key, EntityW vehicle, SeatInstance seatInstance, EntityW player, StackWrapper stack);

	public abstract boolean sendToggle(Attribute<?> attr, EntityW vehicle, KeyPress key, Float val, EntityW player);

	public abstract double getMouseSensitivity();

	public abstract Object getBlockMaterial(String key, boolean allownull);

}
