package net.fexcraft.mod.fvtm.ui.vehicle;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fvtm.FvtmRegistry;
import net.fexcraft.mod.fvtm.data.addon.Addon;
import net.fexcraft.mod.fvtm.data.part.PartSlots;
import net.fexcraft.mod.fvtm.data.vehicle.Vehicle;
import net.fexcraft.mod.fvtm.data.vehicle.VehicleData;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIButton;
import net.fexcraft.mod.uni.ui.UserInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class VehicleCatalog extends UserInterface {

	private ArrayList<Addon> vehpacks = new ArrayList<>();
	private HashMap<Addon, ArrayList<Vehicle>> vehicles = new LinkedHashMap<>();
	private VehicleData data;
	private Vehicle veh;
	private int pack;
	private int vehicle;
	private int recipe;

	public VehicleCatalog(JsonMap map, ContainerInterface container) throws Exception{
		super(map, container);
		for(Vehicle veh : FvtmRegistry.VEHICLES){
			if(!vehpacks.contains(veh.getAddon())){
				vehpacks.add(veh.getAddon());
				vehicles.put(veh.getAddon(), new ArrayList<>());
			}
			vehicles.get(veh.getAddon()).add(veh);
		}
		switchPack(0);
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
			case "veh_prev":{
				switchVehicle(-1);
				return true;
			}
			case "veh_next":{
				switchVehicle(1);
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
		switchVehicle(0);
	}

	private void switchVehicle(int i){
		vehicle += i;
		ArrayList<Vehicle> vehs = vehicles.get(vehpacks.get(pack));
		if(vehicle >= vehs.size()) vehicle = 0;
		if(vehicle < 0) vehicle = vehs.size() - 1;
		veh = vehs.get(vehicle);
		texts.get("vehicle").value(veh.getName());
		switchRecipe(0);
	}

	private void switchRecipe(int i){
		data = new VehicleData(veh);
		int slots = 0;
		int prov = 0;
		for(PartSlots ps : data.getPartSlotProviders().values()){
			slots += ps.size();
			prov++;
		}
		texts.get("desc0").translate("ui.fvtm.vehicle_catalog.desc0", data.getWheelPositions().size(), data.getSeats().size());
		texts.get("desc1").translate("ui.fvtm.vehicle_catalog.desc1", prov, slots);
		texts.get("desc2").translate("ui.fvtm.vehicle_catalog.desc2", recipe + 1, 1);
	}

}
