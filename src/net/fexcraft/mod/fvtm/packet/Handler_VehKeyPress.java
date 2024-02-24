package net.fexcraft.mod.fvtm.packet;

import net.fexcraft.mod.fvtm.data.Capabilities;
import net.fexcraft.mod.fvtm.sys.uni.Passenger;
import net.fexcraft.mod.fvtm.sys.uni.RootVehicle;
import net.fexcraft.mod.fvtm.sys.uni.SeatInstance;
import net.fexcraft.mod.uni.world.EntityW;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Handler_VehKeyPress implements PacketHandler<Packet_VehKeyPress> {

	@Override
	public Runnable handleServer(Packet_VehKeyPress packet, Passenger player){
		return () -> {
			SeatInstance seat = player.getSeatOn();
			if(seat != null){
				seat.onKeyPress(packet.keypress, player);
			}
		};
	}

	@Override
	public Runnable handleClient(Packet_VehKeyPress packet, Passenger player){
		return null;
	}

}
