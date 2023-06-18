package net.fexcraft.mod.fvtm.data.addon;

public enum AddonLocation {
	
	MODJAR, RESOURCEPACK, CONFIGPACK, INTERNAL;
	
	public boolean isInAMod(){
		return this == MODJAR || this == INTERNAL;
	}

	public boolean isNotAMod(){
		return this != MODJAR && this != INTERNAL;
	}

	public boolean isConfigPack(){
		return this == CONFIGPACK;
	}

}
