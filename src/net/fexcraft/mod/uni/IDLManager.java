package net.fexcraft.mod.uni;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public interface IDLManager {
	
	public static IDLManager[] INSTANCE = null;
	
	public IDL get(String idl, boolean cache, boolean named);
	
	public static IDL getIDL(String idl, boolean cache, boolean named){
		return INSTANCE[0].get(idl, cache, named);
	}
	
	public static IDL getIDLCached(String idl, boolean named){
		return INSTANCE[0].get(idl, true, named);
	}
	
	public static IDL getIDLNamed(String idl, boolean cache){
		return INSTANCE[0].get(idl, cache, true);
	}
	
	/** Cached and unnamed. */
	public static IDL getIDLCached(String idl){
		return INSTANCE[0].get(idl, true, false);
	}

	/** Cached and named. */
	public static IDL getIDLNamed(String idl){
		return INSTANCE[0].get(idl, true, true);
	}

	/** Not cached and unnamed. */
	public static IDL getIDL(String idl){
		return INSTANCE[0].get(idl, false, false);
	}

}
