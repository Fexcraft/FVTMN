package net.fexcraft.mod.fvtm.sys.uni;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.mod.fvtm.util.function.TireFunction.TireAttr;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class WheelTireData {

	public final String id;
	public V3D pos;
	public Axle axle;
	public TireAttr function;

	public WheelTireData(String key){
		this.id = key;
	}

	public WheelTireData(){
		id = "null";
		pos = V3D.NULL;
	}

}
