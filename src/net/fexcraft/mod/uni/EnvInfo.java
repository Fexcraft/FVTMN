package net.fexcraft.mod.uni;

import net.fexcraft.mod.fvtm.FvtmRegistry;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class EnvInfo {

	public static boolean CLIENT;
	public static boolean DEV;

	public static boolean is120(){
		return FvtmRegistry.LOADER_VER.startsWith("1.20");
	}

	public static boolean is112(){
		return FvtmRegistry.LOADER_VER.startsWith("1.12");
	}

}
