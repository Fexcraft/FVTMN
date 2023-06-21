package net.fexcraft.mod.uni;

/**
 * "ID or Location/path"
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public interface IDL {
	
	public String space();
	
	public String id();
	
	public default String path(){
		return id();
	}
	
	public default String name(){
		return null;
	}

	public default String colon(){
		return space() + ":" + id();
	}
}
