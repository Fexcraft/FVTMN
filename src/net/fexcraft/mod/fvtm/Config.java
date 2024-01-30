package net.fexcraft.mod.fvtm;

import java.io.File;
import java.util.ArrayList;
import java.util.function.BiConsumer;

import net.fexcraft.app.json.FJson;
import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonHandler;
import net.fexcraft.app.json.JsonHandler.PrintOption;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonValue;
import net.fexcraft.mod.uni.UniReg;

/**
 * FVTM Config File
 *
 * @author Ferdinand Calo' (FEX___96)
 */
public class Config {

	private static File FILE;
	private static ArrayList<ConfigEntry> entries = new ArrayList<>();
	private static ArrayList<Runnable> listeners = new ArrayList<>();
	private static boolean changes = false;
	//general
	public static ArrayList<File> PACK_FOLDERS = new ArrayList<>();
	public static boolean VEHICLES_NEED_FUEL;
	public static boolean VEHICLES_DROP_CONTENTS;
	public static boolean UNBREAKABLE_CONTAINERS;
	public static boolean ROADTOOL_FOR_ALL;
	public static boolean LOAD_PACKS_FROM_MODS;
	public static boolean DISMOUNT_ON_LOGOUT;
	public static String[] DEFAULT_TRAFFIC_SIGN_LIBRARIES;
	//client
	public static boolean RENDER_OUT_OF_VIEW;
	public static boolean RENDER_VEHILE_MODELS_AS_ITEMS;
	public static boolean RENDER_BLOCK_MODELS_AS_ITEMS;
	public static boolean DISABLE_LIGHT_BEAMS;
	public static boolean RENDER_VEHICLES_SEPARATELY;
	public static boolean DISABLE_PARTICLES;
	public static int BLINKER_INTERVAL;
	//u12/basic
	public static float U12_MOTION_SCALE;
	//uni/proto
	public static byte VEHICLE_SYNC_RATE;
	//rail
	public static boolean DISABLE_RAILS;
	public static int UNLOAD_INTERVAL;
	public static int RAIL_SEGMENTATOR;
	public static int MAX_RAIL_TRACK_LENGTH;
	//road
	public static boolean DISABLE_ROADS;
	public static int ROAD_PLACING_GRID;
	public static int MAX_ROAD_LENGTH;
	public static int ROAD_UNDO_CACHE_SIZE;
	public static int ROAD_UNDO_CACHE_CLEARTIME;
	//wire
	public static boolean DISABLE_WIRES;
	public static int WIRE_SEGMENTATOR;
	public static int MAX_WIRE_LENGTH;
	//deprecated
	public static int VEHICLE_UPDATE_RANGE;
	public static boolean OVERLAY_ON_BOTTOM;
	public static int RAIL_PLACING_GRID;

	public static void init(File file){
		FILE = file;
		if(!file.exists()){
			file.getParentFile().mkdirs();
			JsonMap map = new JsonMap();
			map.add("format", 1);
			map.add("info", "FVTM Main Configuration File");
			map.add("wiki", "https://fexcraft.net/wiki/mod/fvtm");
			map.addArray("pack_folders");
			JsonHandler.print(file, map, PrintOption.SPACED);
		}

		//categories
		String catg = "general";
		String catc = "client";
		String catu = "u12/basic";
		String catv = "vehicle";
		String catr = "rail";
		String cato = "road";
		String catw = "wire";

		//general
		entries.add(new ConfigEntry(catg, "vehicles_need_fuel", new JsonValue(true))
				.info("If vehicles need Fuel (in survival mode) to function.")
				.cons((con, map) -> VEHICLES_NEED_FUEL = con.getBoolean(map)));
		entries.add(new ConfigEntry(catg, "vehicle_drop_contents", new JsonValue(false))
				.info("If vehicles should drop their inventory contents upon being removed.")
				.cons((con, map) -> VEHICLES_DROP_CONTENTS = con.getBoolean(map)));
		entries.add(new ConfigEntry(catg, "vehicle_drop_contents", new JsonValue(false))
				.info("If containers should be unbreakable (via tools/hand).")
				.cons((con, map) -> UNBREAKABLE_CONTAINERS = con.getBoolean(map)));
		entries.add(new ConfigEntry(catg, "road_tool_for_all", new JsonValue(false))
				.info("When not using a Forge PermissionsAPI compatible permission manager, to allow any player to use the Road Placing Tool.")
				.cons((con, map) -> ROADTOOL_FOR_ALL = con.getBoolean(map)));
		entries.add(new ConfigEntry(catg, "load_packs_from_mods", new JsonValue(true))
				.info("If true, FVTM will search for packs in the /mods/ folder.")
				.cons((con, map) -> LOAD_PACKS_FROM_MODS = con.getBoolean(map)));
		entries.add(new ConfigEntry(catg, "dismount_on_logout", new JsonValue(true))
				.info("If players should automatically dismount vehicles on log out (leaving server).")
				.cons((con, map) -> DISMOUNT_ON_LOGOUT = con.getBoolean(map)));
		entries.add(new ConfigEntry(catg, "traffic_sign_libraries", new JsonArray("default_fexcraft;http://fexcraft.net/files/mod_data/fvtm/default_traffic_sign_library.json"))
				.info("List of External Traffic Sign Libraries. Separate the ID from the URL using a semicolon.")
				.cons((con, map) -> DEFAULT_TRAFFIC_SIGN_LIBRARIES = con.getJson(map).asArray().toStringArray()));

		//client
		entries.add(new ConfigEntry(catc, "render_out_of_view", new JsonValue(false))
				.info("If vehicles should be rendered out of default view.")
				.cons((con, map) -> RENDER_OUT_OF_VIEW = con.getBoolean(map)));
		entries.add(new ConfigEntry(catc, "render_vehicle_models_as_items", new JsonValue(true))
				.info("If the Vehicle's model should be rendered as Item. Could cause lags.")
				.cons((con, map) -> RENDER_VEHILE_MODELS_AS_ITEMS = con.getBoolean(map)));
		entries.add(new ConfigEntry(catc, "render_block_models_as_items", new JsonValue(true))
				.info("If the (non-vanilla) Block models should be rendered as Item.")
				.cons((con, map) -> RENDER_BLOCK_MODELS_AS_ITEMS = con.getBoolean(map)));
		entries.add(new ConfigEntry(catc, "disable_light_beams", new JsonValue(false))
				.info("If light beam rendering should be disabled.")
				.cons((con, map) -> DISABLE_LIGHT_BEAMS = con.getBoolean(map)));
		entries.add(new ConfigEntry(catc, "render_vehicles_separately", new JsonValue(true))
				.info("If vehicles should be rendered separately new a new render pass. Allows for higher view distance.")
				.cons((con, map) -> RENDER_VEHICLES_SEPARATELY = con.getBoolean(map)));
		entries.add(new ConfigEntry(catc, "disable_particles", new JsonValue(false))
				.info("If FVTM particles (particle system) should be disabled.")
				.cons((con, map) -> DISABLE_PARTICLES = con.getBoolean(map)));
		entries.add(new ConfigEntry(catc, "blinker_interval", new JsonValue(750))
				.info("Blinker/Turn Signal toggle interval, in milliseconds.").rang(100, 2000)
				.cons((con, map) -> BLINKER_INTERVAL = con.getInteger(map)));

		//u12/basic
		entries.add(new ConfigEntry(catu, "motion_scale", new JsonValue(0.2f))
				.info("Physics Motion Scale Multiplier.").rang(0.001f, 2f)
				.cons((con, map) -> U12_MOTION_SCALE = con.getFloat(map)));

		//u12/basic
		entries.add(new ConfigEntry(catv, "sync_rate", new JsonValue(5))
				.info("Entity sync rate in ticks. Lesser value means higher sync AND higher bandwidth. Higher value means slower sync and less bandwidth.").rang(1, 10)
				.cons((con, map) -> VEHICLE_SYNC_RATE = (byte)con.getInteger(map)));

		//rail
		entries.add(new ConfigEntry(catr, "disable", new JsonValue(false))
				.info("If FVTM rail system should be disabled.")
				.cons((con, map) -> DISABLE_RAILS = con.getBoolean(map)));
		entries.add(new ConfigEntry(catr, "unload_interval", new JsonValue(300000))
				.info("Interval (milliseconds) in which it is checked for trains/rails to be unloaded.")
				.cons((con, map) -> UNLOAD_INTERVAL = con.getInteger(map))
				.rang(60000, 86400000));
		entries.add(new ConfigEntry(catr, "placing_grid", new JsonValue(4))
				.info("Grid size for when using the rail/junction creation tool, valid are 16 ('per-pixel accuracy'), 8, 4, 2 or 1 (full block)")
				.cons((con, map) -> {
					RAIL_PLACING_GRID = con.getInteger(map);
					if(RAIL_PLACING_GRID > 16) RAIL_PLACING_GRID = 16;
					if(RAIL_PLACING_GRID > 8 && RAIL_PLACING_GRID < 16) RAIL_PLACING_GRID = 8;
					if(RAIL_PLACING_GRID > 4 && RAIL_PLACING_GRID < 8) RAIL_PLACING_GRID = 4;
					if(RAIL_PLACING_GRID > 2 && RAIL_PLACING_GRID < 4) RAIL_PLACING_GRID = 2;
					if(RAIL_PLACING_GRID < 1) RAIL_PLACING_GRID = 1;
				})
				.rang(1, 16));
		entries.add(new ConfigEntry(catr, "generation_segmentator", new JsonValue(4))
				.info("Segmentator divider for rail generator, valid are 16, 8, 4, 2 or 1.")
				.cons((con, map) -> {
					RAIL_SEGMENTATOR = con.getInteger(map);
					if(RAIL_SEGMENTATOR > 16) RAIL_SEGMENTATOR = 16;
					if(RAIL_SEGMENTATOR > 8 && RAIL_SEGMENTATOR < 16) RAIL_SEGMENTATOR = 8;
					if(RAIL_SEGMENTATOR > 4 && RAIL_SEGMENTATOR < 8) RAIL_SEGMENTATOR = 4;
					if(RAIL_SEGMENTATOR > 2 && RAIL_SEGMENTATOR < 4) RAIL_SEGMENTATOR = 2;
					if(RAIL_SEGMENTATOR < 1) RAIL_SEGMENTATOR = 1;
				})
				.rang(1, 16));
		entries.add(new ConfigEntry(catr, "max_length", new JsonValue(32))
				.info("Max total vector length of new placed rail tracks.")
				.cons((con, map) -> MAX_RAIL_TRACK_LENGTH = con.getInteger(map))
				.rang(1, 256));

		//road
		entries.add(new ConfigEntry(cato, "disable", new JsonValue(false))
				.info("If FVTM road system should be disabled.")
				.cons((con, map) -> DISABLE_ROADS = con.getBoolean(map)));
		entries.add(new ConfigEntry(cato, "placing_grid", new JsonValue(4))
				.info("Grid size for when using the road placing tool, valid are 16 ('per-pixel accuracy'), 8, 4, 2 or 1 (full block)")
				.cons((con, map) -> {
					ROAD_PLACING_GRID = con.getInteger(map);
					if(ROAD_PLACING_GRID > 16) ROAD_PLACING_GRID = 16;
					if(ROAD_PLACING_GRID > 8 && ROAD_PLACING_GRID < 16) ROAD_PLACING_GRID = 8;
					if(ROAD_PLACING_GRID > 4 && ROAD_PLACING_GRID < 8) ROAD_PLACING_GRID = 4;
					if(ROAD_PLACING_GRID > 2 && ROAD_PLACING_GRID < 4) ROAD_PLACING_GRID = 2;
					if(ROAD_PLACING_GRID < 1) ROAD_PLACING_GRID = 1;
				})
				.rang(1, 16));
		entries.add(new ConfigEntry(cato, "max_length", new JsonValue(256))
				.info("Max total vector length of new placed roads.")
				.cons((con, map) -> MAX_ROAD_LENGTH = con.getInteger(map))
				.rang(4, 4096));
		entries.add(new ConfigEntry(cato, "undo_cache_size", new JsonValue(8))
				.info("How many roads should be remembered in the undo cache. Set '0' to disable the undo cache.")
				.cons((con, map) -> ROAD_UNDO_CACHE_SIZE = con.getInteger(map))
				.rang(0, 32));
		entries.add(new ConfigEntry(cato, "undo_cache_cleartime", new JsonValue(5))
				.info("After how many minutes the undo cache of a player should reset. Useful if your players have unstable connection. Set to '0' for instant deletion.")
				.cons((con, map) -> ROAD_UNDO_CACHE_CLEARTIME = con.getInteger(map))
				.rang(0, 60));

		//wire
		entries.add(new ConfigEntry(catw, "disable", new JsonValue(false))
				.info("If FVTM wire system should be disabled.")
				.cons((con, map) -> DISABLE_WIRES = con.getBoolean(map)));
		entries.add(new ConfigEntry(catw, "generation_segmentator", new JsonValue(4))
				.info("Segmentator divider for wire generator, valid are 16, 8, 4, 2 or 1.")
				.cons((con, map) -> {
					WIRE_SEGMENTATOR = con.getInteger(map);
					if(WIRE_SEGMENTATOR > 16) WIRE_SEGMENTATOR = 16;
					if(WIRE_SEGMENTATOR > 8 && WIRE_SEGMENTATOR < 16) WIRE_SEGMENTATOR = 8;
					if(WIRE_SEGMENTATOR > 4 && WIRE_SEGMENTATOR < 8) WIRE_SEGMENTATOR = 4;
					if(WIRE_SEGMENTATOR > 2 && WIRE_SEGMENTATOR < 4) WIRE_SEGMENTATOR = 2;
					if(WIRE_SEGMENTATOR < 1) WIRE_SEGMENTATOR = 1;
				})
				.rang(1, 16));
		entries.add(new ConfigEntry(catw, "max_length", new JsonValue(64))
				.info("Max total vector length of new placed wires.")
				.cons((con, map) -> MAX_WIRE_LENGTH = con.getInteger(map))
				.rang(1, 1024));

		//1.12 specific settings
		if(UniReg.LOADER_VERSION.equals("1.12")){
			entries.add(new ConfigEntry(catg, "vehicle_update_range", new JsonValue(256))
					.info("Range in which Vehicle Update Packets will be sent.").rang(64, 4096)
					.cons((con, map) -> VEHICLE_UPDATE_RANGE = con.getInteger(map)));
			entries.add(new ConfigEntry(catg, "default_overlay_on_bottom", new JsonValue(true))
					.info("If the default steering overlay should be on bottom rather than on top of screen.")
					.cons((con, map) -> OVERLAY_ON_BOTTOM = con.getBoolean(map)));
		}

		reload();
	}

	private static void reload(){
		changes = false;
		JsonMap map = JsonHandler.parse(FILE);
		if(map.has("pack_folders")){
			map.getArray("pack_folders").value.forEach(val -> PACK_FOLDERS.add(new File(val.string_value())));
		}
		for(ConfigEntry entry : entries) entry.consumer.accept(entry, map);
		if(changes) JsonHandler.print(FILE, map, PrintOption.SPACED);
		for(Runnable run : listeners) run.run();
	}

	public static void addListener(Runnable run){
		listeners.add(run);
	}

	public static class ConfigEntry {

		private String key, cat, info, limits;
		private JsonValue defval;
		private BiConsumer<ConfigEntry, JsonMap> consumer;
		private float min, max;

		public ConfigEntry(String cat, String key, JsonValue def){
			this.cat = cat;
			this.key = key;
			this.defval = def;
		}

		public ConfigEntry info(String str){
			info = str;
			return this;
		}

		public ConfigEntry limi(String str){
			limits = str;
			return this;
		}

		public ConfigEntry rang(float mn, float mx){
			min = mn;
			max = mx;
			return this;
		}

		public ConfigEntry cons(BiConsumer<ConfigEntry, JsonMap> cons){
			consumer = cons;
			return this;
		}

		public String getString(JsonMap map){
			map = checkCat(map);
			if(!map.has(key)) fill(map);
			return map.getMap(key).get("value").string_value();
		}

		public boolean getBoolean(JsonMap map){
			map = checkCat(map);
			if(!map.has(key)) fill(map);
			return map.getMap(key).get("value").bool();
		}

		public int getInteger(JsonMap map){
			map = checkCat(map);
			if(!map.has(key)) fill(map);
			int i = map.getMap(key).get("value").integer_value();
			return i > max ? (int)max : i < min ? (int)min : i;
		}

		public float getFloat(JsonMap map){
			map = checkCat(map);
			if(!map.has(key)) fill(map);
			float f = map.getMap(key).get("value").float_value();
			return f > max ? max : f < min ? min : f;
		}

		public FJson getJson(JsonMap map){
			map = checkCat(map);
			if(!map.has(key)) fill(map);
			return map.getMap(key).get("value");
		}

		private JsonMap checkCat(JsonMap map){
			if(!map.has(cat)) map.addMap(cat);
			return map.getMap(cat);
		}

		private void fill(JsonMap map){
			JsonMap con = new JsonMap();
			if(info != null) con.add("info", info);
			if(min != 0 || max != 0) con.add("range", min + " ~ " + max);
			con.add("value", defval);
			map.add(key, con);
			changes = true;
		}

	}


}
