package net.fexcraft.mod.fvtm.function;

import net.fexcraft.app.json.FJson;
import net.fexcraft.mod.fvtm.data.part.Part2;
import net.fexcraft.mod.fvtm.data.part.PartFunction;
import net.fexcraft.mod.uni.tag.TagCW;

public class BogieFunction extends PartFunction {
	
	private String inst_pos;

	public BogieFunction(){}

	@Override
	public PartFunction init(Part2 part, FJson json){
		return this;
	}

	@Override
	public PartFunction load(TagCW compound){
		inst_pos = compound.has("bogie_pos") ? compound.getString("bogie_pos") : null;
		return this;
	}

	@Override
	public TagCW save(TagCW compound){
		if(inst_pos != null) compound.set("bogie_pos", inst_pos);
		return compound;
	}

	@Override
	public String getId(){
		return "fvtm:bogie";
	}

	public void setBogie(String cat){
		this.inst_pos = cat;
	}

	@Override
	public PartFunction copy(Part2 part){
		return new BogieFunction();
	}

}
