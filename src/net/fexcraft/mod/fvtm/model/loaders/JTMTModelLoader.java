package net.fexcraft.mod.fvtm.model.loaders;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.function.Supplier;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fexcraft.lib.common.json.JsonUtil;
import net.fexcraft.lib.frl.Polyhedron;
import net.fexcraft.lib.mc.utils.Static;
import net.fexcraft.lib.tmt.JsonToTMT;
import net.fexcraft.lib.tmt.ModelRendererTurbo;
import net.fexcraft.mod.fvtm.FvtmResources;
import net.fexcraft.mod.fvtm.model.DefaultModel;
import net.fexcraft.mod.fvtm.model.Model;
import net.fexcraft.mod.fvtm.model.ModelData;
import net.fexcraft.mod.fvtm.model.ModelGroup;
import net.fexcraft.mod.fvtm.model.ModelLoader;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class JTMTModelLoader implements ModelLoader {

	@Override
	public boolean accepts(String name, String suffix){
		return suffix.equals("jtmt") || suffix.equals("frlj");
	}

	@Override
	public Object[] load(String name, ModelData confdata, Supplier<Model> supplier) throws Exception {
		DefaultModel model = (DefaultModel)supplier.get();
		JsonObject obj = JsonUtil.getObjectFromInputStream(FvtmResources.INSTANCE.getModelInputStream(name, true));
		if(obj.has("creators")){
			obj.get("creators").getAsJsonArray().forEach(elm -> {
				confdata.creators().add(elm.getAsString());
			});
		}
		model.tex_width = confdata.set(Model.TEXTURE_WIDTH, obj.get("texture_size_x").getAsInt());
		model.tex_height = confdata.set(Model.TEXTURE_HEIGHT, obj.get("texture_size_y").getAsInt());
		confdata.set(Model.SMOOTHSHADING, () -> obj.has("smooth_shading") && obj.get("smooth_shading").getAsBoolean());
        try{
            if(JsonUtil.getIfExists(obj, "format", 2).intValue() == 1){
                JsonObject modelobj = obj.get("model").getAsJsonObject();
                for(Entry<String, JsonElement> entry : modelobj.entrySet()){
					ModelRendererTurbo[] mrts = JsonToTMT.parse(null, entry.getValue().getAsJsonArray(), model.tex_width, model.tex_height);
					ModelGroup group = new ModelGroup(entry.getKey());
					for(ModelRendererTurbo mrt : mrts) group.add(new Polyhedron().importMRT(mrt, false, 0.0625f));
					model.groups.add(group);
                }
            }
            else{
            	JsonObject modelobj = obj.get("groups").getAsJsonObject();
                for(Entry<String, JsonElement> entry : modelobj.entrySet()){
                	JsonObject group = entry.getValue().getAsJsonObject();
					//
					ModelRendererTurbo[] mrts = JsonToTMT.parse(null, group.get("polygons").getAsJsonArray(), model.tex_width, model.tex_height);
					ModelGroup mgroup = new ModelGroup(entry.getKey());
					for(ModelRendererTurbo mrt : mrts) mgroup.add(new Polyhedron().importMRT(mrt, false, 0.0625f));
					model.groups.add(mgroup);
					//
                	if(group.has("fvtm:programs")){
                		ArrayList<Object> arr = confdata.get(Model.PROGRAMS, () -> new ArrayList<>());
                		JsonArray array = group.get("fvtm:programs").getAsJsonArray();
                		for(JsonElement elm : array){
                			if(elm.isJsonPrimitive()) arr.add(entry.getKey() + " " + elm.getAsString());
                			else arr.add(new Object[]{ entry.getKey(), elm });
                		}
                	}
                }
            }
        }
        catch(Throwable thr){
        	thr.printStackTrace();
        	Static.stop();
        }
		return new Object[]{ model, confdata };
	}

}
