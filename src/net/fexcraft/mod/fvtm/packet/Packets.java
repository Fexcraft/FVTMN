package net.fexcraft.mod.fvtm.packet;

import io.netty.buffer.ByteBuf;
import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fvtm.data.block.BlockData;
import net.fexcraft.mod.fvtm.handler.AttrReqHandler;
import net.fexcraft.mod.fvtm.sys.road.RoadPlacingUtil;
import net.fexcraft.mod.fvtm.sys.uni.Passenger;
import net.fexcraft.mod.fvtm.sys.uni.VehicleInstance;
import net.fexcraft.mod.fvtm.util.QV3D;
import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.world.WorldW;

import java.util.HashMap;
import java.util.UUID;

import static net.fexcraft.mod.fvtm.Config.VEHICLE_UPDATE_RANGE;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class Packets {

	public static HashMap<String, PacketListener> LIS_CLIENT = new HashMap<>();
	public static HashMap<String, PacketListener> LIS_SERVER = new HashMap<>();
	public static Packets INSTANCE = null;

	public void init(){
		LIS_SERVER.put("attr_toggle", (com, player) -> {
			AttrReqHandler.processToggleRequest(player, com);
		});
		LIS_SERVER.put("attr_update", (com, player) -> {
			AttrReqHandler.processUpdateRequest(player, com);
		});
		if(EnvInfo.CLIENT){
			LIS_CLIENT.put("attr_toggle", (tag, player) -> {
				AttrReqHandler.processToggleResponse(player, tag);
			});
			LIS_CLIENT.put("attr_update", (tag, player) -> {
				AttrReqHandler.processUpdateResponse(player, tag);
			});
			LIS_CLIENT.put("road_tool_new", (tag, player) -> {
				UUID uuid = new UUID(tag.getLong("uuid_m"), tag.getLong("uuid_l"));
				RoadPlacingUtil.CL_CURRENT = new RoadPlacingUtil.NewRoad(uuid, new QV3D(tag, "vector"), tag.getInteger("width"));
				RoadPlacingUtil.QUEUE.put(uuid, RoadPlacingUtil.CL_CURRENT);
			});
			LIS_CLIENT.put("road_tool_add", (tag, player) -> {
				UUID uuid = new UUID(tag.getLong("uuid_m"), tag.getLong("uuid_l"));
				RoadPlacingUtil.NewRoad road = RoadPlacingUtil.QUEUE.get(uuid);
				if(road == null) return;
				road.add(new QV3D(tag, "vector"), tag.getInteger("width"));
			});
			LIS_CLIENT.put("road_tool_selected", (tag, player) -> {
				UUID uuid = new UUID(tag.getLong("uuid_m"), tag.getLong("uuid_l"));
				RoadPlacingUtil.NewRoad road = RoadPlacingUtil.QUEUE.get(uuid);
				if(road == null) return;
				road.selected = tag.getInteger("selected");
			});
			LIS_CLIENT.put("road_tool_remove", (tag, player) -> {
				UUID uuid = new UUID(tag.getLong("uuid_m"), tag.getLong("uuid_l"));
				RoadPlacingUtil.NewRoad road = RoadPlacingUtil.QUEUE.get(uuid);
				if(road == null) return;
				road.remove(player, new QV3D(tag, "vector"));
			});
			LIS_CLIENT.put("road_tool_reset", (tag, player) -> {
				UUID uuid = new UUID(tag.getLong("uuid_m"), tag.getLong("uuid_l"));
				if(RoadPlacingUtil.CL_CURRENT.id.equals(uuid)) RoadPlacingUtil.CL_CURRENT = null;
				RoadPlacingUtil.QUEUE.remove(uuid);
			});
		}
	}

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
