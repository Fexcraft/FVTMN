package net.fexcraft.mod.fvtm.data.part;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.mc.utils.Static;
import net.fexcraft.mod.fvtm.util.ContentConfigUtil;
import net.fexcraft.mod.fvtm.util.Rot;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class PartSlot {
	
	public final String type;
	public final V3D pos;
	public final String category;
	public final float radius;
	public final Rot rotation;
	
	public PartSlot(String defcat, JsonArray array, int idx){
		type = array.get(3).string_value();
		pos = ContentConfigUtil.getVector(array);
		category = array.size() > 4 ? array.get(4).string_value() : defcat + "_" + idx;
		radius = array.size() > 5 ? array.get(5).integer_value() * Static.sixteenth : 0.25f;
		rotation = array.size() > 6 && array.get(6).isArray() ? new Rot(array.get(6).asArray()) : Rot.NULL;
	}

	public String category(String provider){
		if(category.contains("*")) return category.replace("*", provider);
		return category;
	}

}
