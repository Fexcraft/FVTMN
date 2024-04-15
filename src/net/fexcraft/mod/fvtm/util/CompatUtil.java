package net.fexcraft.mod.fvtm.util;

import net.fexcraft.mod.fvtm.block.Asphalt;
import net.fexcraft.mod.fvtm.block.generated.G_ROAD;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class CompatUtil {
	
	private static final String[] valid_flenix_blocks = { "road_block", "sidewalk" };

	public static boolean isValidFurenikus(String domain, String path){
		if(!domain.equals("furenikusroads")) return false;
		for(String str : valid_flenix_blocks){
			if(path.startsWith(str)) return true;
		}
		return false;
	}

	public static boolean isValidFurenikus(IDL idl){
		return isValidFurenikus(idl.space(), idl.path());
	}

	public static boolean isValidFurenikus(String id){
		return isValidFurenikus(IDLManager.getIDL(id));
	}

	public static int getRoadHeight(int height, boolean flenix){
		if(!flenix) return height;
		return height == 0 ? 15 : height - 1;
	}

}
