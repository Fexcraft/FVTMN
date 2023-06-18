package net.fexcraft.mod.fvtm;

import java.io.File;

/**
 * FVTM Registry
 *
 * @author Ferdinand Calo' (FEX___96)
 */
public class FvtmRegistry {

	public static String LOADER_VER;
	public static File CONFIG_DIR;

	public static final void init(String loadver, File conf){
		LOADER_VER = loadver;
		CONFIG_DIR = conf;
		Config.init(new File(conf, "fvtm.json"));
	}

}
