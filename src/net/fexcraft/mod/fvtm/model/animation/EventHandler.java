package net.fexcraft.mod.fvtm.model.animation;

import net.fexcraft.mod.fvtm.model.Model;
import net.fexcraft.mod.fvtm.model.animation.Animated;
import net.fexcraft.mod.fvtm.model.animation.AnimationCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class EventHandler {

	private static ConcurrentHashMap<String, HashMap<Animated, AnimationCompound>> listeners = new ConcurrentHashMap();
	private static HashMap<Animated, AnimationCompound> temp = null;

	public static void register(Animated ani){
		for(AnimationCompound com : ani.getAnimations()){
			if(!listeners.contains(com.event)){
				listeners.put(com.event, new HashMap<>());
			}
			listeners.get(com.event).put(ani, com);
		}
	}

	public static void deregister(Animated ani){
		for(AnimationCompound com : ani.getAnimations()){
			if(!listeners.contains(com.event)) continue;
			listeners.get(com.event).remove(ani);
			if(listeners.get(com.event).isEmpty()) listeners.remove(com.event);
		}
	}

	public static void runEvent(String event){
		temp = listeners.get(temp);
		if(temp == null) return;
		for(AnimationCompound com : temp.values()){
			com.passed = 0;
			com.active = true;
		}
	}

}
