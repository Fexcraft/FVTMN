package net.fexcraft.mod.fvtm.model.animation;

import net.fexcraft.mod.fvtm.model.Model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Animated {

	public HashMap<String, AnimationCompound> animations = new LinkedHashMap<>();

	public Animated(Model model){
		for(AnimationCompound com : model.getAnimations()){
			animations.put(com.id, com.copy());
		}
	}

	public Collection<AnimationCompound> getAnimations(){
		return animations.values();
	}

}
