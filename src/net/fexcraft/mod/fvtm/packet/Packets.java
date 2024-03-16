package net.fexcraft.mod.fvtm.packet;

import io.netty.buffer.ByteBuf;
import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fvtm.data.block.BlockData;
import net.fexcraft.mod.fvtm.sys.uni.Passenger;
import net.fexcraft.mod.fvtm.sys.uni.VehicleInstance;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.world.WorldW;

import java.util.HashMap;

import static net.fexcraft.mod.fvtm.Config.VEHICLE_UPDATE_RANGE;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class Packets {

	public static HashMap<String, PacketListener> LIS_CLIENT = new HashMap<>();
	public static HashMap<String, PacketListener> LIS_SERVER = new HashMap<>();
	public static Packets INSTANCE = null;

	public abstract void init();

	public abstract void writeTag(ByteBuf buffer, TagCW tag);

	public abstract TagCW readTag(ByteBuf buffer);

	@Deprecated
	/** Send BlockData Update Packet to all around. */
	public abstract void send(BlockData blockdata, V3I pos, int dim);

	@Deprecated
	/** Send BlockData Update Packet to all around. */
	public abstract void send(WorldW world, V3I pos);

	/** Send generic compound packet to a vehicle, works for both server and client. */
	public abstract void send(VehicleInstance vehicle, TagCW com);

	/** Sends a Packet to the Server. */
	public abstract void send0(Class<? extends PacketBase> packet, Object... data);

	/** Sends a Packet to the Server. */
	public static void send(Class<? extends PacketBase> packet, Object... data){
		INSTANCE.send0(packet, data);
	}

	/** Sends a Packet to all in range. */
	public abstract void sendInRange0(Class<? extends PacketBase> packet, WorldW world, V3D pos, int range, Object... data);

	/** Sends a Packet to all in range. */
	public static void sendInRange(Class<? extends PacketBase> packet, WorldW world, V3D pos, int range, Object... data){
		INSTANCE.sendInRange0(packet, world, pos, range, data);
	}

	/** Sends a Packet to all in range. */
	public static void sendInRange(Class<? extends PacketBase> packet, Passenger pass, Object... data){
		sendInRange(packet, pass.getWorld(), pass.getPos(), VEHICLE_UPDATE_RANGE, data);
	}

	/** Sends a Packet to all. */
	public abstract void sendToAll0(Class<? extends PacketBase> packet, Object... data);

	/** Sends a Packet to all. */
	public static void sendToAll(Class<? extends PacketBase> packet, Object... data){
		INSTANCE.sendToAll0(packet, data);
	}

	/** Sends a Packet to the Server. */
	public abstract void sendTo0(Class<? extends PacketBase> packet, Passenger to, Object... data);

	/** Sends a Packet to the Server. */
	public static void sendTo(Class<? extends PacketBase> packet, Passenger to, Object... data){
		INSTANCE.sendTo0(packet, to, data);
	}

}
