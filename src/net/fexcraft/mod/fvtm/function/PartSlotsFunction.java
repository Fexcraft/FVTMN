package net.fexcraft.mod.fvtm.function;

import java.util.List;

import net.fexcraft.app.json.FJson;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.mod.fvtm.data.part.Part2;
import net.fexcraft.mod.fvtm.data.part.PartData;
import net.fexcraft.mod.fvtm.data.part.PartFunction;
import net.fexcraft.mod.fvtm.data.part.PartFunction.StaticFunction;
import net.fexcraft.mod.fvtm.data.part.PartSlot.PartSlots;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.world.WorldW;

public class PartSlotsFunction extends StaticFunction {
	
	private PartSlots partslots;

	public PartSlotsFunction(){}

	@Override
	public PartFunction init(Part2 part, FJson json){
		partslots = new PartSlots(part.getCategory(), json.asMap());
		return this;
	}

	@Override
	public String getId(){
		return "fvtm:part_slots";
	}
	
	public PartSlots getPartSlotss(){
		return partslots;
	}

    @Override
    public void addInformation(StackWrapper stack, WorldW world, PartData data, List<String> tooltip, boolean ext){
        tooltip.add(Formatter.format("&9Provides part slots:"));
    	for(String type : partslots.count.keySet()){
    		int c  = partslots.count.get(type);
            tooltip.add(Formatter.format("&7- " + type + (c > 0 ? " &ex" + c : "")));
    	}
    }

	public boolean copyRot(){
		return partslots.copy_rot;
	}
	
}