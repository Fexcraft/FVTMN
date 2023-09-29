package net.fexcraft.mod.fvtm.data.part;

import java.util.ArrayList;
import java.util.HashMap;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.EnvInfo;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class PartSlots extends ArrayList<PartSlot> {

	public static final String VEHPARTSLOTS = "vehicle";
	public HashMap<String, Integer> count;
	public boolean copy_rot;

	public PartSlots(String cat, JsonMap obj){
		this(cat, obj.get("slots").asArray());
		copy_rot = obj.has("copy_rot") ? obj.get("copy_rot").bool() : false;
	}

	public PartSlots(String defcat, JsonArray jslots){
		for(int i = 0; i < jslots.size(); i++){
			JsonArray array = jslots.get(i).asArray();
			add(new PartSlot(defcat, array, i));
		}
		if(EnvInfo.CLIENT){
			count = new HashMap<>();
			for(PartSlot slot : this){
				if(count.containsKey(slot.type)) count.put(slot.type, count.get(slot.type) + 1);
				else count.put(slot.type, 1);
			}
		}
	}

}
