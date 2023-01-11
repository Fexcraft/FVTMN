package net.fexcraft.mod.fvtm.util;

public abstract class Log {
	
	public static Log INSTANCE;
	
	protected abstract void print0(Object obj);
	
	public static void print(Object obj){
		INSTANCE.print0(obj);
	}

}
