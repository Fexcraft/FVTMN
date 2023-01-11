package net.fexcraft.mod.fvtm.data;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonObject;
import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.fvtm.data.root.Model;
import net.fexcraft.mod.fvtm.util.Resources;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class DirectPipe {
	
	public String fluidcategory;
	public int transferspeed = 50;
	public Model model;
	public IDL texture;
	public String id, modelloc;

	public DirectPipe(String id, JsonObject<?> elm){
		this.id = id;
		if(elm.isMap()){
			JsonMap map = elm.asMap();
			fluidcategory = map.getString("category", "general");
			transferspeed = map.getInteger("transfer", 100);
			modelloc = map.getString("model", "null");
			texture = map.has("texture") ? IDLManager.getIDLCached(map.get("texture").string_value()) : Resources.NULL_TEXTURE;
		}
		else{
			fluidcategory = elm.string_value();
		}
		Print.debug(id + " " + elm);
		Static.stop();
	}
}
