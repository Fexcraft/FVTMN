package net.fexcraft.mod.fvtm.data;

import static net.fexcraft.mod.fvtm.FvtmRegistry.getFuel;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fvtm.FvtmResources;
import net.fexcraft.mod.fvtm.data.root.ItemTextureable;
import net.fexcraft.mod.fvtm.data.root.WithItem;
import net.fexcraft.mod.fvtm.util.ContentConfigUtil;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.item.ItemWrapper;
import net.fexcraft.mod.uni.item.StackWrapper;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Material extends Content<Material> implements WithItem, ItemTextureable {
	
	protected byte max_stack;
	protected short max_health;
	protected String ore_dict, container, fuelgroup, ctab;
	protected int burntime, fuel_capacity;
	protected boolean vehicle_key, fuel_container;
	protected ItemWrapper item;
	protected IDL itemtexloc;
	protected Fuel fuel;
	
	public Material(){}

	@Override
	public Material parse(JsonMap map){
		if((pack = ContentConfigUtil.getAddon(map)) == null) return null;
		if((id = ContentConfigUtil.getID(pack, map)) == null) return null;
		//
		this.name = map.getString("Name", "Unnamed Material");
		this.description = ContentConfigUtil.getStringList(map, "Description");
		this.max_stack = (byte)map.getInteger("MaxItemStackSize", 64);
		this.max_health = (short)map.getInteger("MaxItemDamage", 0);
		this.ore_dict = map.getString("OreDictionary", null);
		this.container = map.getString("ContainerItem", null);
		this.burntime = map.getInteger("ItemBurnTime", 0);
		this.vehicle_key = map.getBoolean("VehicleKey", false);
		this.fuel_container = map.getBoolean("FuelContainer", false);
		this.fuel_capacity = map.getInteger("FuelCapacity", fuel_container ? 5000 : 0);
		this.fuel = map.has("FuelType") ? getFuel(map.get("FuelType", (String)null)) : null;
		this.fuelgroup = map.getString("FuelGroup", null);
		//
        this.ctab = map.getString("CreativeTab", "default");
        this.itemtexloc = ContentConfigUtil.getItemTexture(id, getContentType(), map);
		return this;
	}

	@Override
	public ContentType getContentType(){
		return ContentType.MATERIAL;
	}

	@Override
	public Class<?> getDataClass(){
		return null;
	}

	public int getMaxStack(){
		return max_stack;
	}

	public int getMaxHealth(){
		return max_health;
	}
	
	public String getOreDictId(){
		return ore_dict;
	}

	public int getItemBurnTime(){
		return burntime;
	}
	
	public boolean isVehicleKey(){
		return vehicle_key;
	}
	
	public boolean isFuelContainer(){
		return fuel_container;
	}
	
	public int getFuelCapacity(){
		return fuel_capacity;
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
		if(isUniversalFuelContainer()) return true;
		if(fuelgroup.contains(":")){
			String[] split = fuelgroup.split(":");
			return split[0].equals(fuel.getPrimaryGroup()) && split[1].equals(fuel.getSecondaryGroup());
		}
		else return fuelgroup.equals(fuel.getPrimaryGroup());
	}

	@Override
	public String getCreativeTab(){
		return ctab;
	}

	@Override
	public IDL getItemTexture(){
		return itemtexloc;
	}

	@Override
	public void setItemWrapper(ItemWrapper item){
		this.item = item;
	}

	@Override
	public ItemWrapper getItemWrapper(){
		return item;
	}

	@Override
	public StackWrapper getNewStack(){
		return FvtmResources.INSTANCE.newStack(item);
	}

	@Override
	public String getItemContainer(){
		return container;
	}

}
