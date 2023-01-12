package net.fexcraft.mod.fvtm.data.cloth;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fvtm.util.Resources;

public class ClothMaterial {

	public ClothMaterial(String key, int durr, int[] ams, float tgh){
		//
	}

	public static ClothMaterial parse(JsonMap map){
		if(map.has("ClothMaterial")){
			String mat = map.getString("ClothMaterial", "");
			if(mat.contains(":")){
				return Resources.getClothMaterial(mat);
			}
			return Resources.getVanillaClothMaterial(mat);
		}
		return Resources.NONE_MAT;
	}

}
