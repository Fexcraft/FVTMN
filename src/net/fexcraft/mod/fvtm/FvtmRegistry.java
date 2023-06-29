package net.fexcraft.mod.fvtm;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fvtm.data.DecorationData;
import net.fexcraft.mod.fvtm.data.Fuel;
import net.fexcraft.mod.fvtm.data.addon.Addon;
import net.fexcraft.mod.fvtm.sys.particle.Particle;
import net.fexcraft.mod.fvtm.util.Registry;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;

/**
 * FVTM Registry
 *
 * @author Ferdinand Calo' (FEX___96)
 */
public class FvtmRegistry {

	public static final String CORE_VER = "1.0.0";
	public static String LOADER_VER;
	public static File CONFIG_DIR;
	//
	public static IDL INTERNAL_ADDON_ID = IDLManager.getIDLCached("fvtm:fvtm");
	public static IDL NONE_CLOTH_MAT;
	public static final IDL NULL_TEXTURE = IDLManager.getIDLNamed("No Texture;fvtm:textures/entity/null.png");
	public static final IDL WHITE_TEXTURE = IDLManager.getIDLNamed("No Texture;fvtm:textures/entity/white.png");
	//
	public static final Registry<Addon> ADDONS = new Registry<>();
	public static final Registry<Fuel> FUELS = new Registry<>();
	public static TreeMap<String, TreeMap<String, ArrayList<Fuel>>> SORTED_FUELS = new TreeMap<>();
	public static final HashMap<String, Particle> PARTICLES = new HashMap<>();
	public static final HashMap<String, JsonMap> WIRE_DECO_CACHE = new HashMap<>();
	public static final HashMap<String, DecorationData> DECORATIONS = new HashMap<>();
	public static final ArrayList<String> DECORATION_CATEGORIES = new ArrayList<>();

	public static final void init(String loadver, File conf){
		LOADER_VER = loadver;
		CONFIG_DIR = conf;
		if(!CONFIG_DIR.exists()) CONFIG_DIR.mkdirs();
		Config.init(new File(conf.getParentFile(), "fvtm.json"));
	}

	public static Addon getAddon(String id){
		for(Addon addon : ADDONS){
			if(addon.getID().id().equals(id)) return addon;
		}
		return null;
	}

	public static Fuel getFuel(String id){
		return FUELS.get(id);
	}

	public static Fuel getFuel(IDL idl){
		return FUELS.get(idl);
	}

}
