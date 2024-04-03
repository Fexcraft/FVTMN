package net.fexcraft.mod.fvtm.ui.vehicle;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fvtm.FvtmRegistry;
import net.fexcraft.mod.fvtm.data.addon.Addon;
import net.fexcraft.mod.fvtm.data.attribute.Attribute;
import net.fexcraft.mod.fvtm.data.vehicle.Vehicle;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIButton;
import net.fexcraft.mod.uni.ui.UserInterface;

import java.util.ArrayList;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class VehicleCatalog extends UserInterface {

	private ArrayList<Addon> vehpacks = new ArrayList<Addon>();
	private int pack;

	public VehicleCatalog(JsonMap map, ContainerInterface container) throws Exception{
		super(map, container);
		for(Vehicle veh : FvtmRegistry.VEHICLES){
			if(!vehpacks.contains(veh.getAddon())) vehpacks.add(veh.getAddon());
		}
	}

	@Override
	public boolean onAction(UIButton button, String id, int x, int y, int b){
		switch(id){
			case "pack_prev":{
				switchPack(-1);
				return true;
			}
			case "pack_next":{
				switchPack(1);
				return true;
			}
		}
		return false;
	}

	private void switchPack(int i){
		pack += i;
		if(pack >= vehpacks.size()) pack = 0;
		if(pack < 0) pack = vehpacks.size() - 1;
		texts.get("pack").value(vehpacks.get(pack).getName());
	}

}
