package net.fexcraft.mod.fvtm.util;

import net.fexcraft.mod.fvtm.data.addon.Addon;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;

public class InternalAddon extends Addon {
	
	public static final InternalAddon INSTANCE = new InternalAddon();
	public static final IDL REGNAME = IDLManager.getIDLCached("fvtm:fvtm");

	public InternalAddon(){
		super(PackContainerType.INTERNAL, Location.CODE, null);
	}

}
