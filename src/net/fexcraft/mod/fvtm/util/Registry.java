package net.fexcraft.mod.fvtm.util;

import java.util.ArrayList;

import net.fexcraft.mod.fvtm.data.root.Registrable;
import net.fexcraft.mod.uni.IDL;

public class Registry<Type extends Registrable<Type>> extends ArrayList<Type> {

	public void register(Type type){
		add(type);
	}

	public Type get(IDL id){
		for(Type type : this) if(type.regname().equals(id)) return type;
		return null;
	}

	public Type get(String regname){
		for(Type type : this) if(type.regname().equals(regname)) return type;
		return null;
	}

}
