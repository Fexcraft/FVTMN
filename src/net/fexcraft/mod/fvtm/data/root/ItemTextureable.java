package net.fexcraft.mod.fvtm.data.root;

import net.fexcraft.mod.uni.IDL;

public interface ItemTextureable {
	
	public IDL getItemTexture();
	
	public default boolean no3DItemModel(){
		return false;
	}
	
	public static interface ItemTex<TYCO> {
		
		public Registrable<TYCO> getDataType();
		
	}

}
