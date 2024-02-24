package net.fexcraft.mod.fvtm.sys.uni;

import net.fexcraft.mod.uni.world.EntityW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class Passenger extends EntityW {

	public abstract SeatInstance getSeatOn();

	public FvtmWorld getFvtmWorld(){
		return (FvtmWorld)getWorld();
	}

}
