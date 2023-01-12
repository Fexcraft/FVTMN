package net.fexcraft.mod.fvtm.data;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fvtm.data.root.DataType;
import net.fexcraft.mod.fvtm.data.root.ItemTextureable;
import net.fexcraft.mod.fvtm.data.root.Registrable;
import net.fexcraft.mod.fvtm.data.root.Tabbed;
import net.fexcraft.mod.fvtm.util.DataUtil;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.item.ItemWrapper;
import net.fexcraft.mod.uni.item.StackWrapper;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Consumable extends Registrable<Consumable> implements Tabbed, ItemTextureable {
	
	protected byte maxStackSize;
	protected ItemWrapper item;
	protected String oreDict, container, ctab;
	//
    private int healamount, useduration;
    private float saturation;
    private boolean wolffood, drinkable, alwaysedible;
	protected IDL itemloc;
	
	public Consumable(){}

	@Override
	public Consumable parse(JsonMap map){
		id = DataUtil.getID(map);
		if(id == null) return null;
		pack = DataUtil.getAddon(map);
		if(pack == null) return null;
		//
		name = map.getString("Name", "Unnamed Material");
		description = DataUtil.getStringArray(map, "Description", true);
		maxStackSize = (byte)map.getInteger("MaxItemStackSize", 64);
		oreDict = map.getString("OreDictionary", null);
		container = map.getString("ContainerItem", null);
		//
        healamount = map.getInteger("HealAmount", 1);
        saturation = map.getFloat("Saturation", 0.6f);
        useduration = map.getInteger("UseDuration", 32);
        wolffood = map.getBoolean("WolfFood", false);
        drinkable = map.getBoolean("Drinkable", false);
        alwaysedible = map.getBoolean("AlwaysEdible", false);
		//
        ctab = map.getString("CreativeTab", "default");
        itemloc = DataUtil.getItemTexture(id, getDataType(), map);
		//TODO consumable item
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
	
	public String getOreDictionaryId(){
		return this.oreDict;
	}
	
	public String getContainerItemId(){
		return this.container;
	}

	public void linkContainerItem(){
		if(this.container == null) return;
		//TODO consumable container link
	}

	public void registerIntoOreDictionary(){
        //TODO consumable oredict
	}
	
	//

    public int getHealAmount(){
        return healamount;
    }

    public float getSaturation(){
        return saturation;
    }

    public int getItemUseDuration(){
        return useduration;
    }

    public boolean isWolfFood(){
        return wolffood;
    }

    public boolean isDrinkable(){
        return drinkable;
    }

    public boolean isAlwaysEdible(){
        return alwaysedible;
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
