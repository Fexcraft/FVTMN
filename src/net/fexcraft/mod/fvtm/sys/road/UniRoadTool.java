package net.fexcraft.mod.fvtm.sys.road;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fvtm.FvtmResources;
import net.fexcraft.mod.fvtm.item.RoadToolItem;
import net.fexcraft.mod.fvtm.sys.uni.FvtmWorld;
import net.fexcraft.mod.fvtm.sys.uni.Passenger;
import net.fexcraft.mod.fvtm.sys.uni.Path;
import net.fexcraft.mod.fvtm.sys.uni.PathType;
import net.fexcraft.mod.fvtm.ui.UIKey;
import net.fexcraft.mod.fvtm.util.CompatUtil;
import net.fexcraft.mod.fvtm.util.QV3D;
import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.world.StateWrapper;

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
		StateWrapper line_b = null;
		StateWrapper road_b = null;
		ArrayList<QV3D> roof;
		ArrayList<QV3D> ground = null;
		ArrayList<QV3D> border_l = null;
		ArrayList<QV3D> border_r = null;
		ArrayList<QV3D> line;
		ArrayList<QV3D> road;
		int top_h = 0;
		int border_hl = 0;
		int border_hr = 0;
		ArrayList<ArrayList<QV3D>> rooffill = null;
		ArrayList<ArrayList<QV3D>> linefill = null;
		ArrayList<ArrayList<QV3D>> roadfill = null;
		boolean flnk = false;
		if(stack.getTag().has("RoadFill")){
			stack0 = FvtmResources.newStack(stack.getTag().getCompound("RoadFill"));
			flnk = CompatUtil.isValidFurenikus(stack.getIDL());
			road_b = StateWrapper.from(stack0);
		}
		if(layers[1] > 0 && stack.getTag().has("BottomFill")){
			stack0 = FvtmResources.newStack(stack.getTag().getCompound("BottomFill"));
			bot = StateWrapper.from(stack0);
			ground = new ArrayList<>();
		}
		if(layers[2] > 0 && stack.getTag().has("SideLeftFill")){
			stack0 = FvtmResources.newStack(stack.getTag().getCompound("SideLeftFill"));
			left = StateWrapper.from(stack0);
			border_hl = layers[2];
			border_l = new ArrayList<>();
		}
		if(layers[3] > 0 && stack.getTag().has("SideRightFill")){
			stack0 = FvtmResources.newStack(stack.getTag().getCompound("SideRightFill"));
			righ = StateWrapper.from(stack0);
			border_hr = layers[3];
			border_r = new ArrayList<>();
		}
		if(layers[4] > 0 && stack.getTag().has("TopFill") && !stack.getTag().has("CustomTopFill")){
			stack0 = FvtmResources.newStack(stack.getTag().getCompound("TopFill"));
			top = StateWrapper.from(stack0);
		}
		if(layers[5] > 0 && stack.getTag().has("LinesFill") && !stack.getTag().has("CustomLinesFill")){
			stack0 = FvtmResources.newStack(stack.getTag().getCompound("LinesFill"));
			line_b = StateWrapper.from(stack0);
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
		V3I pos = new V3I();
		V3D last;
		V3D vec;
		StateWrapper state;
		int width = layers[0];
		int height = 0;
		double angle;
		double passed = 0;
		double half = width * 0.5 - 0.5;
		road = roadfill == null && road_b != null ? new ArrayList<>() : null;
		roof = rooffill == null && layers[4] > 0 ? new ArrayList<>() : null;
		line = linefill == null && layers[5] > 0 ? new ArrayList<>() : null;
		vec = _road.getVectorPosition(0.001, false);
		angle = Math.atan2(_road.vecpath[0].z - vec.z, _road.vecpath[0].x - vec.x) + Static.rad90;
		for(double db = -half; db <= half; db += 0.25){
			if(road != null) road.add(gen(_road.vecpath[0], angle, db, 0));
			if(ground != null) ground.add(gen(_road.vecpath[0], angle, db, -1));
			if(line != null) line.add(gen(_road.vecpath[0], angle, db, 1));
			if(roof != null) roof.add(gen(_road.vecpath[0], angle, db, top_h));
		}
		if(roadfill != null){
			for(int i = 0; i < roadfill.size(); i++){
				roadfill.get(i).add(gen(_road.vecpath[0], angle, -half + 0.25 + i, 0));
			}
		}
		if(linefill != null){
			for(int i = 0; i < linefill.size(); i++){
				linefill.get(i).add(gen(_road.vecpath[0], angle, -half + 0.25 + i, 1));
			}
		}
		if(rooffill != null){
			for(int i = 0; i < rooffill.size(); i++){
				rooffill.get(i).add(gen(_road.vecpath[0], angle, -half + 0.25 + i, top_h));
			}
		}
		if(border_l != null) border_l.add(gen(_road.vecpath[0], angle, -half - 1, 0));
		if(border_r != null) border_r.add(gen(_road.vecpath[0], angle, half + 1, 0));
		double off;
		while(passed < _road.length){
			passed += 0.125;
			last = vec;
			vec = _road.getVectorPosition(passed, false);
			angle = Math.atan2(last.z - vec.z, last.x - vec.x) + Static.rad90;
			off = roadfill == null ? 0 : 0.25;
			for(double db = -half; db <= half; db += 0.25){
				if(road != null) road.add(gen(vec, angle, db, 0));
				if(ground != null) ground.add(gen(vec, angle, db + off, -1));
				if(line != null) line.add(gen(vec, angle, db, 1));
				if(roof != null) roof.add(gen(vec, angle, db, top_h));
			}
			if(roadfill != null){
				for(int i = 0; i < roadfill.size(); i++){
					roadfill.get(i).add(gen(vec, angle, -half + 0.25 + i, 0));
				}
			}
			if(linefill != null){
				for(int i = 0; i < linefill.size(); i++){
					linefill.get(i).add(gen(vec, angle, -half + off + 0.25 + i, 1));
				}
			}
			if(rooffill != null){
				for(int i = 0; i < rooffill.size(); i++){
					rooffill.get(i).add(gen(vec, angle, -half + off+ 0.25 + i, top_h));
				}
			}
			if(border_l != null) border_l.add(gen(vec, angle, -half + off + - 1, 0));
			if(border_r != null) border_r.add(gen(vec, angle, half + off + 1, 0));
		}
		JsonMap map = new JsonMap();
		if(road != null){
			roadFill(pass.getFvtmWorld(), road, road_b, top_h, flnk, map);
		}
		return RoadToolItem.placeRoad(pass.local(), pass.getWorld().local(), stack.local(), vector, _road, pass.local());
	}

	private static void insert(JsonMap map, V3I pos, StateWrapper state){
		if(map.has(pos.toString())) return;
		JsonArray array = new JsonArray();
		array.add(state.getIDL().colon());
		if(EnvInfo.is112()) array.add(state.get12Meta());
		map.add(pos.toString(), array);
	}

	private static void loadFill(ArrayList<ArrayList<QV3D>> fill, ArrayList<StateWrapper> bill, int width, TagCW com){
		for(int i = 0; i < width; i++){
			fill.add(new ArrayList<>());
			StateWrapper state = StateWrapper.DEFAULT;
			if(com.has("Block" + i)){
				state = StateWrapper.from(FvtmResources.newStack(com.getCompound("Block" + i)));
			}
			bill.add(state);
		}
	}

	private static void roadFill(FvtmWorld world, ArrayList<QV3D> road, StateWrapper block, int th, boolean flnk, JsonMap map){
		int height;
		V3I pos = new V3I();
		StateWrapper state;
		boolean bool;
		for(QV3D vec : road){
			height = vec.y;
			pos.set(vec.pos.x, vec.pos.y + (vec.y > 0 ? 1 : 0), vec.pos.z);
			state = world.getStateAt(pos);
			if(!isRoad(world, state, block) || isLower(world, state, height)){
				if(bool = isRoad(world, world.getStateAt(pos.add(0, 1, 0)))) height = 0;
				insert(map, pos, state);
				world.setBlockState(pos, world.getRoadWithHeight(block, CompatUtil.getRoadHeight(height, flnk)));
			}
			if((height < 9 || height == 0) || isRoad(world, world.getStateAt(pos.add(0, -1, 0)))){
				V3I down = pos.add(0, -1, 0);
				insert(map, down, world.getStateAt(down));
				world.setBlockState(down, world.getRoadWithHeight(block, CompatUtil.getRoadHeight(0, flnk)));
			}
			int c = th < 4 ? 4 : th;
			for(int i = 1; i < c; i++){
				pos.y++;
				insert(map, pos, world.getStateAt(pos));
				world.setBlockState(pos, StateWrapper.DEFAULT);
			}
		}
	}

	private static boolean isRoad(FvtmWorld world, StateWrapper state, StateWrapper block){
		return isRoad(world, state) && state.getBlock() == block.getBlock();
	}

	private static boolean isRoad(FvtmWorld world, StateWrapper state){
		return world.isFvtmRoad(state) || CompatUtil.isValidFurenikus(state.getIDL());
	}

	private static boolean isLower(FvtmWorld world, StateWrapper state, int height){
		return world.getRoadHeight(state) < height;
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

	public static V3D grv(double rad, double x, double y){
		return new V3D(Math.cos(rad) * x, y, Math.sin(rad) * x);
	}

	public static QV3D gen(V3D vec, double rad, double x, double y){
		return new QV3D(vec.add(grv(rad, x, y)), 16);
	}

}
