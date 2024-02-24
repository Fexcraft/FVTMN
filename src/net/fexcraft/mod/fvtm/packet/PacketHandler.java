package net.fexcraft.mod.fvtm.packet;

import io.netty.buffer.ByteBuf;
import net.fexcraft.mod.fvtm.sys.uni.Passenger;
import net.fexcraft.mod.uni.world.EntityW;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface PacketHandler<PACKET extends PacketBase> {

	public Runnable handleServer(PACKET packet, Passenger player);

	public Runnable handleClient(PACKET packet, Passenger player);

}
