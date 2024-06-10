package net.fexcraft.mod.fvtm.util;

import net.fexcraft.mod.fvtm.data.ContentData;
import net.fexcraft.mod.fvtm.data.part.PartData;
import net.fexcraft.mod.fvtm.data.vehicle.VehicleData;
import net.fexcraft.mod.uni.item.StackWrapper;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ItemDataCache<CN extends ContentData<?, ?>> {

	protected StackWrapper wrapper;
	protected CN content;

	public CN getContent(){
		return content;
	}

	public StackWrapper getWrapper(){
		return wrapper;
	}

	public static class VehicleDataCache extends ItemDataCache<VehicleData> {

		public VehicleDataCache(StackWrapper stack){
			wrapper = stack;
		}

	}

	public static class PartDataCache extends ItemDataCache<PartData> {

		public PartDataCache(StackWrapper stack){
			wrapper = stack;
		}

	}

}
