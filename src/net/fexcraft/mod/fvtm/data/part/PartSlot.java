package net.fexcraft.mod.fvtm.data.part;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonValue;
import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.mod.fvtm.util.ContentConfigUtil;
import net.fexcraft.mod.fvtm.util.Rot;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class PartSlot {
	
	public final String type;
	public final V3D pos;
	public final float radius;
	public final Rot rotation;

	public PartSlot(String key, JsonValue<?> value){
		if(value.isArray() && value.asArray().not_empty()){
			JsonArray array = value.asArray();
			pos = ContentConfigUtil.getVector(array);
			type = array.size() > 3 ? array.get(3).string_value() : key;
			radius = array.size() > 4 ? array.get(4).float_value() : 0.25f;
			rotation = array.size() > 5 ? new Rot(array.getArray(5)) : Rot.NULL;
		}
		else{
			type = value.string_value();
			pos = V3D.NULL;
			radius = 0.25f;
			rotation = Rot.NULL;
		}
	}

}
