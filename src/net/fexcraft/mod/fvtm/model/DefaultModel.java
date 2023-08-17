package net.fexcraft.mod.fvtm.model;

import static net.fexcraft.mod.fvtm.model.ModelGroup.COND_PROGRAMS;
import static net.fexcraft.mod.fvtm.model.ModelGroupList.SEPARATE_GROUP_LIST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fexcraft.lib.common.math.Vec3f;
import net.fexcraft.lib.frl.Polygon;
import net.fexcraft.lib.frl.Polyhedron;
import net.fexcraft.lib.frl.Vertex;
import net.fexcraft.mod.fvtm.FvtmRegistry;
import net.fexcraft.mod.fvtm.model.ConditionalPrograms.ConditionBased;
import net.fexcraft.mod.fvtm.model.ModelGroupList.DefaultModelGroupList;
import net.fexcraft.mod.fvtm.model.Program.ConditionalProgram;
import org.lwjgl.opengl.GL11;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class DefaultModel implements Model {

	public static final DefaultModel EMPTY = new DefaultModel();
	public static final ModelRenderData RENDERDATA = new ModelRenderData();
	public TreeMap<RenderOrder, ModelGroupList> sorted = new TreeMap<>();
	public ModelGroupList groups = new DefaultModelGroupList();
	private List<String> authors = new ArrayList<>();
	public Transforms transforms = new Transforms();
	public boolean smooth_shading;
	public int textureX, textureY;
	public int tex_width;
	public int tex_height;
	public boolean locked;
	public String name;

	@Override
	public void render(ModelRenderData data){
		transforms.apply();
		if(FvtmRegistry.is112) GL11.glShadeModel(smooth_shading ? GL11.GL_FLAT : GL11.GL_SMOOTH);
		for(ModelGroupList list : sorted.values()) list.render(data);
		transforms.deapply();
	}

	@Override
	public Collection<String> getCreators(){
		return authors;
	}

	@Override
	public boolean addToCreators(String str){
		if(locked) return false;
		return authors.add(str);
	}

	@Override
	public Model parse(ModelData data){
		smooth_shading = data.get(SMOOTHSHADING, false);
		if(data.contains(PROGRAMS)){
			ArrayList<Object> programs = data.get(PROGRAMS);
			for(Object obj : programs){
				if(obj instanceof String){
					String[] split = obj.toString().trim().split(" ");
					if(!groups.contains(split[0])) continue;
					try{
						groups.get(split[0]).addProgram(parseProgram(split));
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
				else{ // most likely json
					Object[] objs = (Object[])obj;
					if(!groups.contains(objs[0].toString())) continue;
					try{
						groups.get(objs[0].toString()).addProgram(parseProgram(objs[1].toString().split(" ")));
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
		if(data.contains(CONDPROGRAMS)){
			ArrayList<Object> programs = data.get(CONDPROGRAMS);
			for(Object obj : programs){
				if(obj instanceof JsonObject){
					JsonObject json = (JsonObject)obj;
					if(!json.has("id") || !json.has("group")) continue;
					try{
						String group = json.get("group").getAsString();
						String progid = json.get("id").getAsString();
						ConditionalProgram prog = null;
						if(COND_PROGRAMS.containsKey(progid)){
							prog = COND_PROGRAMS.get(progid).getConstructor().newInstance();
						}
						else prog = new ConditionBased(progid);
						if(json.has("ifmet")){
							JsonArray array = json.get("ifmet").getAsJsonArray();
							for(JsonElement elm : array){
								prog.addIf(parseProgram(elm.getAsString().trim().split(" "), 0));
							}
						}
						if(json.has("else")){
							JsonArray array = json.get("else").getAsJsonArray();
							for(JsonElement elm : array){
								prog.addElse(parseProgram(elm.getAsString().trim().split(" "), 0));
							}
						}
						if(json.has("args")){
							prog = (ConditionalProgram)prog.parse(json.get("args").getAsString().trim().split(" "));
						}
						groups.get(group).addProgram(prog);
					}
					catch(Exception e){
						e.printStackTrace();
					}
					continue;
				}
				String string = obj.toString();
				String[] args = string.trim().split("||");
				if(!groups.contains(args[0])) continue;
				try{
					ConditionalProgram prog = null;
					if(COND_PROGRAMS.containsKey(args[1])){
						prog = COND_PROGRAMS.get(args[1]).getConstructor().newInstance();
					}
					else prog = new ConditionBased(args[1]);
					String[] sub = args[2].split("|");
					for(String s : sub){
						prog.addIf(parseProgram(s.trim().split(" ")));
					}
					if(args.length > 3){
						sub = args[3].split("|");
						for(String s : sub){
							prog.addElse(parseProgram(s.trim().split(" ")));
						}
					}
					if(args.length > 4){
						prog = (ConditionBased)prog.parse(args[4].trim().split(" "));
					}
					groups.get(args[0]).addProgram(prog);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		if(data.contains(TRANSFORMS)){
			for(String string : ((List<String>)data.get(TRANSFORMS))){
				transforms.parse(string.trim().split(" "));
			}
		}
		if(data.contains(PIVOTS)){
			for(String string : ((List<String>)data.get(PIVOTS))){
				String[] args = string.trim().split(" ");
				if(!groups.contains(args[0])) continue;
				try{
					ModelGroup group = groups.get(args[0]);
					Vec3f vector = new Vec3f(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]));
					Vec3f rotation = new Vec3f(
							args.length > 4 ? Float.parseFloat(args[4]) : 0,
							args.length > 5 ? Float.parseFloat(args[5]) : 0,
							args.length > 6 ? Float.parseFloat(args[6]) : 0
					);
					for(Polyhedron<?> poly : group){
						for(Polygon poli : poly.polygons){
							for(Vertex vert : poli.vertices){
								vert.vector = vert.vector.sub(vector);
							}
						}
						poly.pos(vector.x, vector.y, vector.z);
						poly.rot(rotation.x, rotation.y, rotation.z);
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		if(data.contains(OFFSET)){
			for(String string : ((List<String>)data.get(OFFSET))){
				String[] args = string.trim().split(" ");
				if(!groups.contains(args[0])) continue;
				try{
					ModelGroup group = groups.get(args[0]);
					Vec3f vector = new Vec3f(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]));
					for(Polyhedron<?> poly : group){
						for(Polygon poli : poly.polygons){
							for(Vertex vert : poli.vertices){
								vert.vector = vert.vector.sub(vector);
							}
						}
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return this;
	}

	@Override
	public void lock(){
		if(locked) return;
		authors = ImmutableList.copyOf(authors);
		for(ModelGroup group : groups) group.initPrograms();
		sort();
		locked = true;
	}

	@Override
	public void sort(){
		sorted.clear();
		ArrayList<ModelGroup> nor = new ArrayList<>();
		ArrayList<ModelGroup> bln = new ArrayList<>();
		ArrayList<ModelGroup> las = new ArrayList<>();
		ArrayList<ModelGroup> sep = new ArrayList<>();
		RenderOrder order;
		for(ModelGroup group : groups){
			order = RenderOrder.NORMAL;
			for(Program prog : group.getAllPrograms()){
				if(prog.order().ordinal() > order.ordinal()) order = prog.order();
			}
			switch(order){
				case BLENDED: bln.add(group); break;
				case LAST: las.add(group); break;
				case SEPARATE: sep.add(group); break;
				case NORMAL:
				default: nor.add(group); break;
			}
		}
		if(nor.size() > 0) sorted.put(RenderOrder.NORMAL, new DefaultModelGroupList(nor));
		if(bln.size() > 0) sorted.put(RenderOrder.BLENDED, new DefaultModelGroupList(bln));
		if(las.size() > 0) sorted.put(RenderOrder.LAST, new DefaultModelGroupList(las));
		if(sep.size() > 0){
			ModelGroupList list = SEPARATE_GROUP_LIST.get();
			list.addAll(sep);
			sorted.put(RenderOrder.SEPARATE, list);
		}
	}

	@Override
	public void clearGLData(){//static pipeline
		for(ModelGroup group : groups){
			for(Polyhedron<GLObject> poly : group){
				if(poly.glId == null) continue;
				GL11.glDeleteLists(poly.glId, 1);
			}
		}
	}

	private static Program parseProgram(String[] args) throws Exception {
		return parseProgram(args, 1);
	}

	private static Program parseProgram(String[] args, int atidx) throws Exception {
		if(args[atidx].startsWith("#")) return null;
		Program prog = ModelGroup.PROGRAMS.get(args[atidx]);
		if(prog == null){
			throw new Exception("PROGRAM WITH ID '" + args[atidx] + "' NOT FOUND!");
		}
		return prog.parse(Arrays.copyOfRange(args, atidx + 1, args.length));
	}

	public ModelGroup get(String key){
		return groups.get(key);
	}

	public void translate(float x, float y, float z){
		for(ModelGroup group : groups) group.translate(x, y, z, false);
	}

	public void translate(float x, float y, float z, boolean set){
		for(ModelGroup group : groups) group.translate(x, y, z, set);
	}

}
