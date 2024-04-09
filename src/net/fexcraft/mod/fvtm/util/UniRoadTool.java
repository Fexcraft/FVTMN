package net.fexcraft.mod.fvtm.util;

import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.mod.fvtm.FvtmRegistry;
import net.fexcraft.mod.fvtm.FvtmResources;
import net.fexcraft.mod.fvtm.sys.uni.Passenger;
import net.fexcraft.mod.fvtm.ui.UIKey;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static net.fexcraft.lib.common.utils.Formatter.format;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniRoadTool {

	public static final Object[] NA = new Object[0];

	public static void addTooltip(TagCW com, List<String> list, BiFunction<String, Object[], String> translator){
		list.add(format(translator.apply("tooltip.fvtm.road_tool.toolbox", NA)));
		if(com.empty()){
			list.add(format(translator.apply("tooltip.fvtm.road_tool.empty", NA)));
		}
		else{
			int[] layers = com.getIntArray("RoadLayers");
			if(layers.length < 6){
				int[] n = new int[6];
				for(int i = 0; i < 6; i++){
					n[i] = i >= layers.length ? 0 : layers[i];
				}
				com.set("RoadLayers", layers = n);
			}
			StackWrapper stack = null;
			if(com.has("CustomRoadFill")){
				list.add(format(translator.apply("tooltip.fvtm.road_tool.road_fill_custom", new Object[]{ layers[0] })));
			}
			else{
				stack = FvtmResources.newStack(com.getCompound("RoadFill"));
				list.add(format(translator.apply("tooltip.fvtm.road_tool.road_fill", new Object[]{ stack.getName(), stack.count() })));
			}
			if(com.has("BottomFill") && layers[1] > 0){
				stack = FvtmResources.newStack(com.getCompound("BottomFill"));
				list.add(format(translator.apply("tooltip.fvtm.road_tool.ground_fill", new Object[]{ stack.getName() })));
			}
			if(com.has("SideLeftFill") && layers[2] > 0){
				stack = FvtmResources.newStack(com.getCompound("SideLeftFill"));
				list.add(format(translator.apply("tooltip.fvtm.road_tool.left_fill", new Object[]{ stack.getName(), layers[2] })));
			}
			if(com.has("SideRightFill") && layers[2] > 0){
				stack = FvtmResources.newStack(com.getCompound("SideRightFill"));
				list.add(format(translator.apply("tooltip.fvtm.road_tool.right_fill", new Object[]{ stack.getName(), layers[3] })));
			}
			//
			if(com.has("CustomTopFill") && layers[4] > 0){
				list.add(format(translator.apply("tooltip.fvtm.road_tool.top_fill_custom", new Object[]{ layers[0] })));
			}
			else if(com.has("TopFill") && layers[4] > 0){
				stack = FvtmResources.newStack(com.getCompound("TopFill"));
				list.add(format(translator.apply("tooltip.fvtm.road_tool.top_fill", new Object[]{ stack.getName(), stack.count() })));
			}
			//
			if(com.has("CustomLinesFill") && layers[5] > 0){
				list.add(format(translator.apply("tooltip.fvtm.road_tool.lines_fill_custom", new Object[]{ layers[0] })));
			}
			else if(com.has("LinesFill") && layers[5] > 0){
				stack = FvtmResources.newStack(com.getCompound("LinesFill"));
				list.add(format(translator.apply("tooltip.fvtm.road_tool.lines_fill", new Object[]{ stack.getName(), stack.count() })));
			}
			list.add(format(translator.apply("tooltip.fvtm.road_tool.undo", NA)));
		}
	}

	public static int onUse(Passenger pass, boolean main){
		if(pass.getWorld().isClient()) return 0;
		if(pass.isShiftDown() && main){
			pass.openUI(UIKey.ROAD_TOOL, V3I.NULL);
			return 2;
		}
		if(!pass.isCreative()){
			pass.send("tooltip.fvtm.road_tool.creative");
			return 1;
		}
		return 3;
	}

}
