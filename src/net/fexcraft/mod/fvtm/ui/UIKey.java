package net.fexcraft.mod.fvtm.ui;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UIKey {

	public static final ArrayList<UIKey> ALLKEYS = new ArrayList<>();
	public static final int ID12_TOOLBOX_COLORS = 600;
	public static final int ID12_DECORATION_EDITOR = 713;
	public static final int ID12_VEHICLE_MAIN = 930;
	public static final int ID12_VEHICLE_INFO = 931;
	public static final int ID12_VEHICLE_ATTR_EDITOR = 932;
	public static final int ID12_VEHICLE_FUEL = 933;
	public static final int ID12_VEHICLE_ATTRIBUTES = 934;
	public static final int ID12_VEHICLE_INVENTORIES = 935;
	public static final int ID12_VEHICLE_INVENTORY_ITEM = 9361;
	public static final int ID12_VEHICLE_INVENTORY_FLUID = 9362;
	public static final int ID12_VEHICLE_INVENTORY_VAR = 9363;
	public static final int ID12_VEHICLE_CONTAINERS = 937;
	public static final int ID12_VEHICLE_CONNECTORS = 939;
	public static final int ID12_BLOCK_INVENTORY_ITEM = 961;
	public static final int ID12_BLOCK_INVENTORY_FLUID = 962;
	public static final int ID12_BLOCK_INVENTORY_VAR = 963;
	public static final int ID12_MULTIBLOCK_INVENTORY_ITEM = 9511;
	public static final int ID12_MULTIBLOCK_INVENTORY_FLUID = 9512;
	public static final int ID12_MULTIBLOCK_INVENTORY_VAR = 9513;
	public static final UIKey TOOLBOX_COLORS = new UIKey(ID12_TOOLBOX_COLORS, "fvtm:toolbox_colors");
	public static final UIKey VEHICLE_MAIN = new UIKey(ID12_VEHICLE_MAIN, "fvtm:vehicle_main");
	public static final UIKey VEHICLE_INFO = new UIKey(ID12_VEHICLE_INFO, "fvtm:vehicle_info");
	public static final UIKey VEHICLE_ATTR_EDITOR = new UIKey(ID12_VEHICLE_ATTR_EDITOR, "fvtm:vehicle_attr_editor");
	public static final UIKey VEHICLE_FUEL = new UIKey(ID12_VEHICLE_FUEL, "fvtm:vehicle_fuel");
	public static final UIKey VEHICLE_ATTRIBUTES = new UIKey(ID12_VEHICLE_ATTRIBUTES, "fvtm:vehicle_attributes");
	public static final UIKey VEHICLE_INVENTORIES = new UIKey(ID12_VEHICLE_INVENTORIES, "fvtm:vehicle_inventories");
	public static final UIKey VEHICLE_INVENTORY_ITEM = new UIKey(ID12_VEHICLE_INVENTORY_ITEM, "fvtm:vehicle_inventory_item");
	public static final UIKey VEHICLE_INVENTORY_FLUID = new UIKey(ID12_VEHICLE_INVENTORY_FLUID, "fvtm:vehicle_inventory_fluid");
	public static final UIKey VEHICLE_INVENTORY_VAR = new UIKey(ID12_VEHICLE_INVENTORY_VAR, "fvtm:vehicle_inventory_var");
	public static final UIKey VEHICLE_CONTAINERS = new UIKey(ID12_VEHICLE_CONTAINERS, "fvtm:vehicle_containers");
	public static final UIKey VEHICLE_CONNECTORS = new UIKey(ID12_VEHICLE_CONNECTORS, "fvtm:vehicle_connectors");
	public static final UIKey DECORATION_EDITOR = new UIKey(ID12_DECORATION_EDITOR, "fvtm:decoration_editor");
	public static final UIKey BLOCK_INVENTORY_ITEM = new UIKey(ID12_BLOCK_INVENTORY_ITEM, "fvtm:block_inventory_item");
	public static final UIKey BLOCK_INVENTORY_FLUID = new UIKey(ID12_BLOCK_INVENTORY_FLUID, "fvtm:block_inventory_fluid");
	public static final UIKey BLOCK_INVENTORY_VAR = new UIKey(ID12_BLOCK_INVENTORY_VAR, "fvtm:block_inventory_var");
	public static final UIKey MULTIBLOCK_INVENTORY_ITEM = new UIKey(ID12_MULTIBLOCK_INVENTORY_ITEM, "fvtm:mb_inventory_item");
	public static final UIKey MULTIBLOCK_INVENTORY_FLUID = new UIKey(ID12_MULTIBLOCK_INVENTORY_FLUID, "fvtm:mb_inventory_fluid");
	public static final UIKey MULTIBLOCK_INVENTORY_VAR = new UIKey(ID12_MULTIBLOCK_INVENTORY_VAR, "fvtm:mb_inventory_var");

	public final int id;
	public final String key;

	public UIKey(int id12, String id20){
		id = id12;
		key = id20;
		ALLKEYS.add(this);
	}

	public static int get(String id){
		for(UIKey key : ALLKEYS){
			if(key.key.equals(id)) return key.id;
		}
		return 0;
	}

}
