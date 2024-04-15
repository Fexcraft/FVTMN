package net.fexcraft.mod.fvtm.sys.road;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fvtm.FvtmResources;
import net.fexcraft.mod.fvtm.item.RoadToolItem;
import net.fexcraft.mod.fvtm.sys.uni.Passenger;
import net.fexcraft.mod.fvtm.sys.uni.Path;
import net.fexcraft.mod.fvtm.sys.uni.PathType;
import net.fexcraft.mod.fvtm.ui.UIKey;
import net.fexcraft.mod.fvtm.util.CompatUtil;
import net.fexcraft.mod.fvtm.util.QV3D;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.world.StateWrapper;
import net.fexcraft.mod.uni.world.WrapperHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static net.fexcraft.lib.common.utils.Formatter.format;
import static net.fexcraft.mod.fvtm.Config.MAX_ROAD_LENGTH;

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
		if(!pass.isCreative()){
			pass.send("tooltip.fvtm.road_tool.creative");
			return 1;
		}
		if(pass.isShiftDown() && main){
			pass.openUI(UIKey.ROAD_TOOL, V3I.NULL);
			return 2;
		}
		return 3;
	}

	public static boolean placeRoad(Passenger pass, StackWrapper stack, QV3D vector, Road _road){
		if(_road.length > MAX_ROAD_LENGTH){
			pass.bar("interact.fvtm.road_tool.too_long");
			return false;
		}
		StackWrapper stack0 = null;
		int[] layers = stack.getTag().getIntArray("RoadLayers");
		StateWrapper top;
		StateWrapper bot;
		StateWrapper left;
		StateWrapper righ;
		StateWrapper line_b;
		StateWrapper road_b;
		ArrayList<QV3D> roof;
		ArrayList<QV3D> ground;
		ArrayList<QV3D> border_l;
		ArrayList<QV3D> border_r;
		ArrayList<QV3D> line;
		ArrayList<QV3D> road;
		int top_h = 0;
		int border_hl = 0;
		int border_hr = 0;
		ArrayList<ArrayList<QV3D>> rooffill;
		ArrayList<ArrayList<QV3D>> linefill;
		ArrayList<ArrayList<QV3D>> roadfill;
		boolean flnk;
		if(stack.getTag().has("RoadFill")){
			stack0 = FvtmResources.newStack(stack.getTag().getCompound("RoadFill"));
			flnk = CompatUtil.isValidFurenikus(stack.getIDL());
			road_b = StateWrapper.STACK_GETTER.apply(stack0);
		}
		if(layers[1] > 0 && stack.getTag().has("BottomFill")){
			stack0 = FvtmResources.newStack(stack.getTag().getCompound("BottomFill"));
			bot = StateWrapper.STACK_GETTER.apply(stack0);
			ground = new ArrayList<>();
		}
		if(layers[2] > 0 && stack.getTag().has("SideLeftFill")){
			stack0 = FvtmResources.newStack(stack.getTag().getCompound("SideLeftFill"));
			left = StateWrapper.STACK_GETTER.apply(stack0);
			border_hl = layers[2];
			border_l = new ArrayList<>();
		}
		if(layers[3] > 0 && stack.getTag().has("SideRightFill")){
			stack0 = FvtmResources.newStack(stack.getTag().getCompound("SideRightFill"));
			righ = StateWrapper.STACK_GETTER.apply(stack0);
			border_hr = layers[3];
			border_r = new ArrayList<>();
		}
		if(layers[4] > 0 && stack.getTag().has("TopFill") && !stack.getTag().has("CustomTopFill")){
			stack0 = FvtmResources.newStack(stack.getTag().getCompound("TopFill"));
			top = StateWrapper.STACK_GETTER.apply(stack0);
		}
		if(layers[5] > 0 && stack.getTag().has("LinesFill") && !stack.getTag().has("CustomLinesFill")){
			stack0 = FvtmResources.newStack(stack.getTag().getCompound("LinesFill"));
			line_b = StateWrapper.STACK_GETTER.apply(stack0);
		}
		top_h = border_hl > border_hr ? border_hl : border_hr;
		if(top_h == 0){
			if(layers[5] > 0){
				border_hl++;
				border_hr++;
				top_h = 2;
			}
			top_h = 1;
		}
		ArrayList<StateWrapper> roadfill_b;
		ArrayList<StateWrapper> rooffill_b;
		ArrayList<StateWrapper> linefill_b;
		if(stack.getTag().has("CustomRoadFill")){
			roadfill = new ArrayList<>();
			roadfill_b = new ArrayList<>();
			loadFill(roadfill, roadfill_b, layers[0], stack.getTag().getCompound("CustomRoadFill"));
		}
		if(layers[4] > 0 && stack.getTag().has("CustomTopFill")){
			rooffill = new ArrayList<>();
			rooffill_b = new ArrayList<>();
			loadFill(rooffill, rooffill_b, layers[0], stack.getTag().getCompound("CustomTopFill"));
		}
		if(layers[5] > 0 && stack.getTag().has("CustomLinesFill")){
			linefill = new ArrayList<>();
			linefill_b = new ArrayList<>();
			loadFill(linefill, linefill_b, layers[0], stack.getTag().getCompound("CustomLinesFill"));
		}
		return RoadToolItem.placeRoad(pass.local(), pass.getWorld().local(), stack.local(), vector, _road, pass.local());
	}

	private static void loadFill(ArrayList<ArrayList<QV3D>> fill, ArrayList<StateWrapper> bill, int width, TagCW com){
		for(int i = 0; i < width; i++){
			fill.add(new ArrayList<>());
			StateWrapper state = StateWrapper.DEFAULT;
			if(com.has("Block" + i)){
				state = StateWrapper.STACK_GETTER.apply(FvtmResources.newStack(com.getCompound("Block" + i)));
			}
			bill.add(state);
		}
	}

	public static class Road extends Path {

		public Road(QV3D[] gridvecs){
			super(gridvecs);
		}

		public Road(QV3D[] gridvecs, QV3D vector){
			super(gridvecs, vector);
		}

		@Override
		public V3D getVectorPosition(double distance, boolean reverse){
			return super.getVectorPosition0(distance, reverse);
		}

		@Override
		public PathType getType(){
			return PathType.ROAD;
		}

	}

	public static V3D grv(double rad, V3D vec){
		double co = Math.cos(rad), si = Math.sin(rad);
		return new V3D(co * vec.x, vec.y, si * vec.x);
	}

}
