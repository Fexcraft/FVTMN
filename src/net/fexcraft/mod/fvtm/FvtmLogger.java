package net.fexcraft.mod.fvtm;

import net.fexcraft.mod.uni.EnvInfo;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class FvtmLogger {

	public static FvtmLogger LOGGER = null;

	public abstract void log(Object obj);

	public abstract void log(String str);

	public void info(Object obj){
		log(obj);
	}

	public void debug(String s){
		if(EnvInfo.DEV) log(s);
	}

}
