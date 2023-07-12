package net.fexcraft.mod.fvtm.data;

import static net.fexcraft.mod.fvtm.FvtmRegistry.CONSUMABLES;
import static net.fexcraft.mod.fvtm.FvtmRegistry.FUELS;
import static net.fexcraft.mod.fvtm.FvtmRegistry.MATERIALS;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public enum ContentType {

	ADDON(".fvtm", null),
	PART(".part", "parts"),
	VEHICLE(".vehicle", "vehicles"),
	MATERIAL(".material", "materials", Material.class),
	CONTAINER(".container", "containers"),
	CONSUMABLE(".consumable", "consumables"),
	FUEL(".fuel", "fuels", Fuel.class),
	BLOCK(".block", "blocks"),
	MULTIBLOCK(".multiblock", "blocks"),
	RAILGAUGE(".gauge", "railgauges"),
	CLOTH(".cloth", "clothes"),
	WIRE(".wire", "wires"),
	;

	public String suffix;
	public String folder;
	public Class<? extends Content<?>> impl;

	ContentType(String suffix, String folder){
		this.suffix = suffix;
		this.folder = folder;
	}

	ContentType(String suffix, String folder, Class<? extends Content<?>> clazz){
		this(suffix, folder);
		impl = clazz;
	}

	public void register(Content<?> content){
		switch(this){
			case PART: break;
			case VEHICLE: break;
			case MATERIAL: MATERIALS.register(content); break;
			case CONTAINER: break;
			case CONSUMABLE: CONSUMABLES.register(content);
			case FUEL: FUELS.register(content); break;
			case BLOCK: break;
			case MULTIBLOCK: break;
			case RAILGAUGE: break;
			case CLOTH: break;
			case WIRE: break;
			default: return;
		}
	}

	@Deprecated
	public boolean usesCustomItemModel(){
		return this == VEHICLE || this == CONTAINER || this == PART || this == BLOCK;
	}

}
