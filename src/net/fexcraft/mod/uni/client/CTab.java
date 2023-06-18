package net.fexcraft.mod.uni.client;

import java.util.TreeMap;

import net.fexcraft.mod.fvtm.data.addon.AddonNew;
import net.fexcraft.mod.uni.IDL;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface CTab {

	public static TreeMap<IDL, CTab> TABS = new TreeMap<>();
	public static final String DEFAULT = "default";
	public static Manager MANAGER = null;

	public static interface Manager {

		public CTab create(AddonNew addon, String id);

	}

}
