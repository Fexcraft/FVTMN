package net.fexcraft.mod.fvtm.model.animation;

import net.fexcraft.app.json.JsonMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class AnimationCompound {

	public final String id;
	public String event;
	public int duration;
	public int passed;
	public boolean active;

	public AnimationCompound(String key, JsonMap map){
		id = key;
		event = map.getString("event", "");
		duration = map.getInteger("duration", 20);
	}

	private AnimationCompound(String key){
		id = key;
	}

	public AnimationCompound copy(){
		AnimationCompound com = new AnimationCompound(id);
		com.event = event;
		com.duration = duration;
		com.active = false;
		return com;
	}

}
