package net.fexcraft.mod.fvtm.util;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ContentConfigUtil {

	public static IDL getID(JsonMap map){
		if(map.has("ID")) return IDLManager.getIDLCached(map.get("ID").string_value());
		else return IDLManager.getIDLCached(map.get("RegistryName").string_value());
	}

}
