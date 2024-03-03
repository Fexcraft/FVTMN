package net.fexcraft.mod.fvtm.sys.uni;

import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fvtm.ui.UIKey;
import net.fexcraft.mod.uni.world.EntityW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class Passenger extends EntityW {

	public abstract SeatInstance getSeatOn();

	public FvtmWorld getFvtmWorld(){
		return (FvtmWorld)getWorld();
	}

	public abstract void openUI(UIKey key, V3I pos);

	public abstract void set(int veh, int seatid);

	public abstract int vehicle();

	public abstract int seat();

}
