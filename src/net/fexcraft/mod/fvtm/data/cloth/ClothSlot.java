package net.fexcraft.mod.fvtm.data.cloth;

public enum ClothSlot {
	
	HEAD, UPPER, LOWER, FEET;

	public static ClothSlot parse(String string){
		string = string.toUpperCase();
		for(ClothSlot slot : values()) if(slot.name().equals(string)) return slot;
		return HEAD;
	}

}
