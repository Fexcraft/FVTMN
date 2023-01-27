package net.fexcraft.mod.fvtm.data;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.json.JsonUtil;
import net.fexcraft.mod.fvtm.data.root.DataType;
import net.fexcraft.mod.fvtm.data.root.ItemTextureable;
import net.fexcraft.mod.fvtm.data.root.Registrable;
import net.fexcraft.mod.fvtm.data.root.Tabbed;
import net.fexcraft.mod.fvtm.util.DataUtil;
import net.fexcraft.mod.fvtm.util.Resources;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.item.ItemWrapper;
import net.fexcraft.mod.uni.item.StackWrapper;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Material extends Registrable<Material> implements Tabbed, ItemTextureable {
	
	protected byte maxStackSize;
	protected short maxHealth;
	protected ItemWrapper item;
	protected String oreDict, container, fuelgroup, ctab;
	protected int burntime, fuel_capacity;
	protected boolean isVehicleKey, isFuelContainer;
	protected IDL itemloc;
	protected Fuel fuel;
	
	public Material(){}

	@Override
	public Material parse(JsonMap map){
		id = DataUtil.getID(map);
		if(id == null) return null;
		pack = DataUtil.getAddon(map);
		if(pack == null) return null;
		//
		this.name = map.getString("Name", "Unnamed Material");
		this.description = DataUtil.getStringArray(map, "Description", true);
		this.maxStackSize = (byte)map.getInteger("MaxItemStackSize", 64);
		this.maxHealth = JsonUtil.getIfExists(obj, "MaxItemDamage", 0).shortValue();
		this.oreDict = obj.has("OreDictionary") ? obj.get("OreDictionary").getAsString() : null;
		this.container = obj.has("ContainerItem") ? obj.get("ContainerItem").getAsString() : null;
		this.burntime = JsonUtil.getIfExists(obj, "ItemBurnTime", 0).intValue();
		this.isVehicleKey = JsonUtil.getIfExists(obj, "VehicleKey", false);
		this.isFuelContainer = JsonUtil.getIfExists(obj, "FuelContainer", false);
		this.fuel_capacity = JsonUtil.getIfExists(obj, "FuelCapacity", 5000).intValue();
		this.fuel = obj.has("FuelType") ? Resources.getFuel(obj.get("FuelType").getAsString()) : null;
		this.fuelgroup = obj.has("FuelGroup") ? obj.get("FuelGroup").getAsString() : null;
		//
        this.ctab = JsonUtil.getIfExists(obj, "CreativeTab", "default");
        this.itemloc = DataUtil.getItemTexture(registryname, getDataType(), obj);
		this.item = new MaterialItem(this);
		MinecraftForge.EVENT_BUS.post(new TypeEvents.MaterialCreated(this, obj));
		return this;
	}

	@Override
	public DataType getDataType(){
		return DataType.MATERIAL;
	}

	@Override
	public Class<?> getDataClass(){
		return null;
	}

	public int getMaxStackSize(){
		return this.maxStackSize;
	}
	
	@Override
	public ItemWrapper getItem(){
		return item;
	}

	@Override
	public StackWrapper getStack(){
		//TODO stacks
		return null;
	}

	public int getMaxDamage(){
		return this.maxHealth;
	}
	
	public String getOreDictionaryId(){
		return this.oreDict;
	}
	
	public String getContainerItemId(){
		return this.container;
	}

	public void linkContainerItem(){
		if(this.container == null) return;
		//TODO material container link
	}

	public int getItemBurnTime(){
		return burntime;
	}
	
	public boolean isVehicleKey(){
		return this.isVehicleKey;
	}
	
	public boolean isFuelContainer(){
		return this.isFuelContainer;
	}
	
	public int getFuelCapacity(){
		return this.fuel_capacity;
	}
	
	public Fuel getFuelType(){
		return fuel;
	}
	
	/** May be a primary or primary:secondary string. */
	public String getFuelGroup(){
		return fuelgroup;
	}
	
	public boolean isUniversalFuelContainer(){
		return fuel == null && fuelgroup == null;
	}
	
	public boolean isValidFuel(Fuel fuel){
		if(this.isUniversalFuelContainer()) return true;
		if(fuelgroup.contains(":")){
			String[] split = fuelgroup.split(":");
			return split[0].equals(fuel.getPrimaryGroup()) && split[1].equals(fuel.getSecondaryGroup());
		} else return fuelgroup.equals(fuel.getPrimaryGroup());
	}

	public void registerIntoOreDictionary(){
        //TODO material oredict
	}

	@Override
	public String getCreativeTab(){
		return ctab;
	}

	@Override
	public IDL getItemTexture(){
		return itemloc;
	}

}
