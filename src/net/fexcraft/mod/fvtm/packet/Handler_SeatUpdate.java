package net.fexcraft.mod.fvtm.packet;

import net.fexcraft.mod.fvtm.sys.uni.Passenger;
import net.fexcraft.mod.fvtm.sys.uni.RootVehicle;
import net.fexcraft.mod.fvtm.sys.uni.SeatInstance;
import net.fexcraft.mod.uni.world.EntityW;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import static net.fexcraft.mod.fvtm.Config.VEHICLE_UPDATE_RANGE;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Handler_SeatUpdate implements PacketHandler<Packet_SeatUpdate> {

	@Override
	public Runnable handleServer(Packet_SeatUpdate packet, Passenger player){
		return () -> findAndApply(packet, player, true);
	}

	@Override
	public Runnable handleClient(Packet_SeatUpdate packet, Passenger player){
		return () -> findAndApply(packet, player, false);
	}

	private void findAndApply(Packet_SeatUpdate packet, Passenger player, boolean send){
		SeatInstance seat = player.getFvtmWorld().getSeat(packet.entid, packet.seatid);
		if(seat != null){
			seat.pelook = seat.elook.copy();
			seat.elook.set_rotation(packet.yaw, packet.pitch, 0F, true);
			if(!send) return;
			Packets.sendInRange(Packet_SeatUpdate.class, player, seat);
		}
	}

}
