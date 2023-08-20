package net.fexcraft.mod.fvtm.data.vehicle;

public interface SwivelPointMover {
	
	public void update(VehicleEntity entity, SwivelPoint point);
	
	public SwivelPointMover clone();
	
	public boolean shouldUpdate();

}
