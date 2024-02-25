package net.fexcraft.mod.fvtm.ui;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UIKey {

	public static final int ID12_VEHICLE_MAIN = 930;
	public static final int ID12_BLOCK_INVENTORY_ITEM = 961;
	public static final int ID12_BLOCK_INVENTORY_FLUID = 962;
	public static final int ID12_BLOCK_INVENTORY_VAR = 963;
	public static final UIKey VEHICLE_MAIN = new UIKey(ID12_VEHICLE_MAIN, "fvtm:vehicle_main");
	public static final UIKey BLOCK_INVENTORY_ITEM = new UIKey(ID12_BLOCK_INVENTORY_ITEM, "fvtm:block_inventory_item");
	public static final UIKey BLOCK_INVENTORY_FLUID = new UIKey(ID12_BLOCK_INVENTORY_FLUID, "fvtm:block_inventory_fluid");
	public static final UIKey BLOCK_INVENTORY_VAR = new UIKey(ID12_BLOCK_INVENTORY_VAR, "fvtm:block_inventory_var");

	public final int id;
	public final String key;

	public UIKey(int id12, String id20){
		id = id12;
		key = id20;
	}

}
