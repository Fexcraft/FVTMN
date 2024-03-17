package net.fexcraft.mod.fvtm.handler;

import net.fexcraft.lib.common.math.Time;
import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.mod.fvtm.data.ContentType;
import net.fexcraft.mod.fvtm.data.attribute.AttrBox;
import net.fexcraft.mod.fvtm.data.attribute.Attribute;
import net.fexcraft.mod.fvtm.data.block.AABB;
import net.fexcraft.mod.fvtm.data.part.Part;
import net.fexcraft.mod.fvtm.data.part.PartData;
import net.fexcraft.mod.fvtm.data.part.PartSlot;
import net.fexcraft.mod.fvtm.data.part.PartSlots;
import net.fexcraft.mod.fvtm.data.vehicle.SwivelPoint;
import net.fexcraft.mod.fvtm.handler.DefaultPartInstallHandler.DPIHData;
import net.fexcraft.mod.fvtm.packet.Packet_TagListener;
import net.fexcraft.mod.fvtm.packet.Packets;
import net.fexcraft.mod.fvtm.sys.uni.*;
import net.fexcraft.mod.uni.item.ItemType;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.world.WrapperHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class InteractionHandler {

	private static FvtmWorld world;
	private static String last = "";
	private static long cooldown = 0;
	private static float seatbbs = .375f;
	private static float seatbbsh = .1875f;
	private static float th32 = .0625f * .5f;
	private static AABB aabb;

	/** Vehicle Interaction */
	public static boolean handle(KeyPress key, VehicleInstance vehicle, SeatInstance seat, Passenger pass, StackWrapper stack){
		if(key.equals(KeyPress.MOUSE_RIGHT) && mountSeat(vehicle, seat, pass, stack)) return true;
		if(stack.empty() || !stack.isItemOf(ItemType.PART)) return false;
		if(Time.getDate() < cooldown) return false;
		PartData data = stack.getContent(ContentType.PART);
		if(!(data.getType().getInstallHandlerData() instanceof DPIHData)) return false;
		ArrayList<Interactive> list = new ArrayList<>();
		SwivelPoint point = null;
		for(Map.Entry<String, PartSlots> entry : vehicle.data.getPartSlotProviders().entrySet()){
			point = vehicle.data.getRotationPointOfPart(entry.getKey());
			for(Map.Entry<String, PartSlot> sentry : entry.getValue().entrySet()){
				String type = sentry.getValue().type;
				if(vehicle.data.hasPart(type)){
					Part part = vehicle.data.getPart(type).getType();
					if(!(part.getInstallHandlerData() instanceof DPIHData) || !((DPIHData)part.getInstallHandlerData()).swappable) continue;
				}
				for(String sub : data.getType().getCategories()){
					if(!sub.equals(type)) continue;
					list.add(new Interactive(point, entry.getKey(), entry.getValue(), sentry.getKey()));
				}
			}
		}
		Interactive res = getInteracted(seat == null, vehicle, pass, list);
		if(res == null) return false;
		if(res.id().equals(last) && Time.getDate() < cooldown) return true;
		TagCW com = TagCW.create();
		com.set("source", res.source);
		com.set("category", res.category);
		com.set("entity", vehicle.entity.getId());
		Packets.send(Packet_TagListener.class, "install_part", com);
		last = res.id();
		cooldown = Time.getDate() + 20;
		return true;
	}

	public static boolean handle(KeyPress key, StackWrapper stack){
		if(!stack.empty() && !(stack.isItemOf(ItemType.PART) || stack.isItemOf(ItemType.LEAD))) return false;
		world = WrapperHolder.getClientWorld();
		Passenger pass = world.getClientPassenger();
		ArrayList<VehicleInstance> vehs = world.getVehicles(pass.getPos());
		for(VehicleInstance veh : vehs){
			if(handle(key, veh, null, pass, stack)) return true;
		}
		return false;
	}

	private static boolean mountSeat(VehicleInstance vehicle, SeatInstance seat, Passenger pass, StackWrapper stack){
		if(last.equals("seat") && Time.getDate() < cooldown) return false;
		if(!stack.empty() && stack.isItemOf(ItemType.LEAD)) return false;
		if(seat == null) seat = pass.getSeatOn();
		V3D evec = pass.getEyeVec();
		V3D lvec = evec.add(pass.getLookVec().multiply(3));
		V3D vec0;
		for(int i = 0; i < vehicle.data.getSeats().size(); i++){
			if(seat != null && seat.index == i) continue;
			SeatInstance nseat = vehicle.seats.get(i);
			if(nseat.passenger_direct() != null) continue;
			SwivelPoint point = nseat.point;
			vec0 = point.getRelativeVector(nseat.seat.pos).add(vehicle.entity.getPos());
			aabb = AABB.create(-seatbbsh, -.0125, -seatbbsh, seatbbsh, seatbbs - .0125, seatbbsh).offset(vec0);
			for(float f = 0; f < 3.125f; f += th32){
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

	private static Interactive getInteracted(boolean external, VehicleInstance vehicle, Passenger pass, ArrayList<Interactive> list){
		if(pass.getFvtmWorld().noViewEntity()) return null;
		V3D evec = pass.getEyeVec();
		V3D lvec = evec.add(pass.getLookVec().multiply(external ? 3 : 2));
		V3D vec0;
		LinkedHashMap<String, AABB> map = new LinkedHashMap<>();
		for(Interactive inter : list){
			inter.collect(external, vehicle, pass, map);
		}
		for(float f = 0; f < (external ? 3.125f : 2.125f); f += th32){
			vec0 = evec.distance(lvec, f);
			for(Interactive inter : list){
				if(map.containsKey(inter.id()) && map.get(inter.id()).contains(vec0)) return inter;
			}
		}
		return null;
	}

	private static class Interactive {

		protected Attribute<?> attribute;
		private SwivelPoint point;
		private PartSlots slots;
		private String source;
		private String category;

		public Interactive(Attribute attr){
			attribute = attr;
		}

		public Interactive(SwivelPoint spoint, String src, PartSlots pslots, String cat){
			point = spoint;
			source = src;
			slots = pslots;
			category = cat;
		}

		public String id(){
			return attribute == null ? source + ":" + category : attribute.id;
		}

		public void collect(boolean external, VehicleInstance vehicle, Passenger player, Map<String, AABB> aabbs){
			if(attribute == null){
				V3D pos = slots.get(category).pos.copy();
				if(!source.equals("vehicle")){
					pos = pos.add(vehicle.data.getPart(source).getInstalledPos());
				}
				pos = point.getRelativeVector(pos).add(vehicle.entity.getPos());
				double hs = slots.get(category).radius * .5;
				aabbs.put(id(), AABB.create(pos.x - hs, pos.y - hs, pos.z - hs, pos.x + hs, pos.y + hs, pos.z + hs));
			}
			else{
				String val = attribute.asString();
				if(external) val = "external-" + val;
				AttrBox ab = attribute.getBox(val);
				if(ab == null) return;
				point = vehicle.data.getRotationPoint(ab.swivel_point);
				V3D pos = point.getRelativeVector(ab.pos).add(vehicle.entity.getPos());
				double hs = ab.size * .5;
				aabbs.put(attribute.id, AABB.create(pos.x - hs, pos.y - hs, pos.z - hs, pos.x + hs, pos.y + hs, pos.z + hs));
			}
		}

	}

}
