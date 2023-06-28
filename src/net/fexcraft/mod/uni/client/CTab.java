package net.fexcraft.mod.uni.client;

import java.lang.reflect.InvocationTargetException;
import java.util.TreeMap;

import net.fexcraft.mod.fvtm.data.addon.Addon;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface CTab {

	public static TreeMap<IDL, CTab> TABS = new TreeMap<>();
	public static final String DEFAULT = "default";
	public static Class<? extends CTab>[] IMPL = new Class[1];

	public static CTab create(Addon addon, String id){
		IDL aid = IDLManager.getIDLCached(addon.getID().id() + ":" + id);
		if(TABS.containsKey(aid)) return TABS.get(aid);
		try {
			TABS.put(aid, IMPL[0].getConstructor(Addon.class, String.class).newInstance(addon, id));
		}
		catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return TABS.get(aid);
	}

}
