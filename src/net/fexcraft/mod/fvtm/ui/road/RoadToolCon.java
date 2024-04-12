package net.fexcraft.mod.fvtm.ui.road;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fvtm.sys.uni.Passenger;
import net.fexcraft.mod.fvtm.ui.UIKey;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.InventoryInterface;
import net.fexcraft.mod.uni.world.EntityW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class RoadToolCon extends InventoryInterface {

	public static final String[] fills = new String[]{ "RoadFill", "BottomFill", "SideLeftFill", "SideRightFill", "TopFill", "LinesFill" };
	protected StackWrapper stack;
	protected boolean custom_road;
	protected boolean custom_top;
	protected boolean custom_lines;

	public RoadToolCon(JsonMap map, EntityW player, V3I pos){
		super(map, player, pos);
		stack = player.getHeldItem(true);
		stack.createTagIfMissing();
		custom_road = stack.getTag().has("CustomRoadFill");
		custom_top = stack.getTag().has("CustomTopFill");
		custom_lines = stack.getTag().has("CustomLinesFill");
	}

	protected void initInv(){
		for(int i = 0; i < fills.length; i++){
        	if(stack.getTag().has(fills[i])){
				setInventoryContent(i, stack.getTag().getCompound(fills[i]));
        	}
        }
	}

	@Override
	public Object get(String key, Object... objs){
		//
		return null;
	}

	@Override
	public void packet(TagCW com, boolean client){
		switch(com.getString("cargo")){
			case "save":{
				stack.getTag().set("RoadLayers", com.getIntArray("sizes"));
				break;
			}
			case "custom":{
				((Passenger)player).openUI(UIKey.ROAD_TOOL_CUSTOM, new V3I(com.getInteger("layer"), 0, 0));
				break;
			}
		}
	}

	@Override
	public void onClosed(){
		for(int i = 0; i < fills.length; i++){
        	if(!isInventoryEmpty(i)){
				TagCW tag = TagCW.create();
				getInventoryContent(i).save(tag);
        		stack.getTag().set(fills[i], tag);
        	}
        	else stack.getTag().rem(fills[i]);
        }
	}

}
