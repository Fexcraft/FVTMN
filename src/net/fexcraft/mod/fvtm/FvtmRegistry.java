package net.fexcraft.mod.fvtm;

import java.io.File;
import java.util.HashMap;

import net.fexcraft.mod.fvtm.sys.particle.Particle;
import net.fexcraft.mod.uni.IDL;

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
	//
	public static HashMap<String, Particle> PARTICLES = new HashMap<>();

	public static final void init(String loadver, File conf){
		LOADER_VER = loadver;
		CONFIG_DIR = conf;
		Config.init(new File(conf, "fvtm.json"));
	}

}
