package net.fexcraft.mod.fvtm.handler;

import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.common.math.Time;
import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.Vec3f;
import net.fexcraft.mod.fvtm.data.block.AABB;
import net.fexcraft.mod.fvtm.data.vehicle.SwivelPoint;
import net.fexcraft.mod.fvtm.packet.Packet_TagListener;
import net.fexcraft.mod.fvtm.packet.Packets;
import net.fexcraft.mod.fvtm.sys.uni.*;
import net.fexcraft.mod.uni.item.ItemType;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class InteractionHandler {

	private static String last = "";
	private static long cooldown = 0;
	private static float seatbbs = .375f;
	private static float seatbbsh = .1875f;
	private static float th32 = .0625f * .5f;
	private static AABB aabb;

	/** Vehicle Interaction */
	public static boolean handle(KeyPress key, VehicleInstance vehicle, SeatInstance seat, Passenger pass, StackWrapper stack){
		if(key.equals(KeyPress.MOUSE_RIGHT) && mountSeat(vehicle, seat, pass, stack)) return true;


		return false;
	}

	private static boolean mountSeat(VehicleInstance vehicle, SeatInstance seat, Passenger pass, StackWrapper stack){
		if(last.equals("seat") && Time.getDate() < cooldown) return false;
		if(!stack.empty() && stack.isItemOf(ItemType.LEAD)) return false;
		if(seat == null) seat = pass.getSeatOn();
		V3D evec = pass.getEyeVec();
		V3D lvec = pass.getLookVec().multiply(3);
		V3D vec0;
		for(int i = 0; i < vehicle.data.getSeats().size(); i++){
			if(seat != null && seat.index == i) continue;
			SeatInstance nseat = vehicle.seats.get(i);
			if(nseat.passenger_direct() != null) continue;
			SwivelPoint point = nseat.point;
			vec0 = point.getRelativeVector(nseat.seat.pos).add(vehicle.entity.getPos());
			aabb = AABB.create(-seatbbsh, -.0125, -seatbbsh, seatbbsh, seatbbs - .0125, seatbbsh).offset(vec0);
			for(float f = 0; f < 4; f += th32){
				vec0 = evec.distance(lvec, f);
				if(aabb.contains(vec0)){
					TagCW com = TagCW.create();
					com.set("entity", vehicle.entity.getId());
					com.set("seat", i);
					Packets.send(Packet_TagListener.class, "mount_seat", com);
					cooldown = Time.getDate() + 20;
					last = "seat";
					return true;
				}
			}
		}
		return false;
	}

}
