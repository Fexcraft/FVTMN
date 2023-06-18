package net.fexcraft.mod.uni.item;

import java.util.TreeMap;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fvtm.data.addon.AddonNew;
import net.fexcraft.mod.uni.IDL;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface ClothMaterial {

	public static TreeMap<IDL, ClothMaterial> TABS = new TreeMap<>();
	public static ClothMaterial.Manager MANAGER = null;

	public static interface Manager {

		public ClothMaterial create(AddonNew addon, JsonMap map);

	}

}
