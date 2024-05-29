package net.fexcraft.mod.fvtm.data;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.mod.fvtm.data.vehicle.SwivelPoint;
import net.fexcraft.mod.fvtm.data.vehicle.VehicleData;
import net.fexcraft.mod.fvtm.sys.uni.VehicleInstance;
import net.fexcraft.mod.fvtm.util.ContentConfigUtil;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class InteractZone {

	public final String id;
	public final String point;
	public final V3D pos;
	public final Boolean set;
	public float range;
	public boolean def;

	public InteractZone(String izid, V3D vec, float size, String spoint){
		id = izid;
		pos = vec;
		range = size;
		point = spoint;
		set = null;
		validate();
	}

	public InteractZone(String izid, JsonArray arr){
		id = izid;
		if(arr.get(0).string_value().equals("expand") || arr.get(0).string_value().equals("set")){
			pos = V3D.NULL;
			range = arr.get(1).float_value();
			point = arr.size() > 2 ? arr.get(2).string_value() : SwivelPoint.DEFAULT;
			set = arr.get(0).string_value().equals("set");
		}
		else{
			pos = ContentConfigUtil.getVector(arr);
			range = arr.size() > 3 ? arr.get(3).float_value() : 4;
			point = arr.size() > 4 ? arr.get(4).string_value() : SwivelPoint.DEFAULT;
			set = null;
		}
		validate();
	}

	public void validate(){
		if(range < 1) range = 1;
		if(range > 16) range = 16;
		def = point.equals(SwivelPoint.DEFAULT) && pos.isNull();
	}

	public InteractZone copy(){
		return new InteractZone(id, pos, range, point);
	}

	public boolean inRange(VehicleInstance inst, V3D pos){
		V3D loc = def ?  V3D.NULL : inst.data.getRotationPoint(point).getRelativeVector(pos);
		return loc.add(inst.entity.getPos()).dis(pos) < range + 1;
	}

	public boolean inRange(VehicleData data, V3D lift, V3D pos){
		V3D loc = def ? V3D.NULL : data.getRotationPoint(point).getRelativeVector(pos);
		return loc.add(lift).dis(pos) < range + 1;
	}

}
