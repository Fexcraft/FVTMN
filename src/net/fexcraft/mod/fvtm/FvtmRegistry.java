package net.fexcraft.mod.fvtm;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fvtm.data.DecorationData;
import net.fexcraft.mod.fvtm.sys.particle.Particle;
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
	public static IDL NONE_CLOTH_MAT;
	public static final IDL NULL_TEXTURE = IDLManager.getIDLNamed("No Texture;fvtm:textures/entity/null.png");
	public static final IDL WHITE_TEXTURE = IDLManager.getIDLNamed("No Texture;fvtm:textures/entity/white.png");
	//
	public static final HashMap<String, Particle> PARTICLES = new HashMap<>();
	public static final HashMap<String, JsonMap> WIRE_DECO_CACHE = new HashMap<>();
	public static final HashMap<String, DecorationData> DECORATIONS = new HashMap<>();
	public static final ArrayList<String> DECORATION_CATEGORIES = new ArrayList<>();

	public static final void init(String loadver, File conf){
		LOADER_VER = loadver;
		CONFIG_DIR = conf;
		Config.init(new File(conf, "fvtm.json"));
	}

}
