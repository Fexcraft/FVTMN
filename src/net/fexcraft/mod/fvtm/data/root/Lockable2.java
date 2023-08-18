package net.fexcraft.mod.fvtm.data.root;

import java.util.UUID;

import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface Lockable2 {

	public static final IDL DEFAULT_KEY = IDLManager.getIDLCached("gep:key");

	public boolean isLocked();

	public String getLockCode();

	public void setLocked(Boolean bool);

	public static String newCode(){
		return UUID.randomUUID().toString().substring(0, 8);
	}

}
