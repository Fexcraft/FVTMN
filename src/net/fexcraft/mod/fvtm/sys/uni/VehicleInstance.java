package net.fexcraft.mod.fvtm.sys.uni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.fexcraft.lib.common.math.Time;
import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fvtm.FvtmLogger;
import net.fexcraft.mod.fvtm.data.Seat;
import net.fexcraft.mod.fvtm.data.attribute.Attribute;
import net.fexcraft.mod.fvtm.data.attribute.AttributeUtil;
import net.fexcraft.mod.fvtm.data.vehicle.SimplePhysData;
import net.fexcraft.mod.fvtm.data.vehicle.SwivelPoint;
import net.fexcraft.mod.fvtm.data.vehicle.VehicleData;
import net.fexcraft.mod.fvtm.data.vehicle.VehicleType;
import net.fexcraft.mod.fvtm.function.part.EngineFunction;
import net.fexcraft.mod.fvtm.packet.Packet_TagListener;
import net.fexcraft.mod.fvtm.packet.Packet_VehMove;
import net.fexcraft.mod.fvtm.packet.Packets;
import net.fexcraft.mod.fvtm.ui.UIKey;
import net.fexcraft.mod.fvtm.util.Pivot;
import net.fexcraft.mod.fvtm.packet.Packet_VehKeyPress;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.world.EntityW;

import static net.fexcraft.mod.fvtm.Config.VEHICLE_UPDATE_RANGE;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class VehicleInstance {

	public VehicleData data;
	public VehicleType type;
	public EntityW entity;
	private UUID placer;
	public VehicleInstance front, rear;
	public SwivelPoint point;
	//
	public WheelTireData w_front_l;
	public WheelTireData w_front_r;
	public WheelTireData w_rear_l;
	public WheelTireData w_rear_r;
	//
	public double steer_yaw;
	public double throttle;
	public double speed;
	public ArrayList<SeatInstance> seats = new ArrayList<>();
	public HashMap<String, WheelTireData> wheeldata = new HashMap<>();
	public byte toggable_timer;
	public double max_steering_yaw;
	public int fuel_accumulator;
	public int fuel_consumed;
	//
	public static final float GRAVITY = 9.81f;
	public static final float GRAVITY_20th = GRAVITY / 20;

	public VehicleInstance(EntityW wrapper, VehicleData vdata){
		entity = wrapper;
		init(vdata);
	}

	public void init(VehicleData vdata){
		data = vdata;
		if(data == null) return;
		type = data.getType().getVehicleType();
		point = data.getRotationPoint(null);
		max_steering_yaw = data.getAttributeInteger("max_steering_angle", 45);
	}

	public UUID getPlacer(){
		return placer;
	}

	public void setPlacer(UUID uuid){
		if(placer == null) placer = uuid;
	}

	public Pivot pivot(){
		return point.getPivot();
	}

	public Pivot prev_pivot(){
		return point.getPrevPivot();
	}

	public boolean onKeyPress(KeyPress key, Seat seat, Passenger sender, boolean state){
		return onKeyPress(key, seat, sender, false);
	}

	public boolean onKeyPress(KeyPress key, Seat seat, Passenger player){
		//TODO script key press event
		if(!seat.driver && key.driver_only()) return false;
		if(entity.isOnClient() && !key.toggables()){
			Packets.send(Packet_VehKeyPress.class, key);
			return true;
		}
		switch(key){
			case ACCELERATE:{
				throttle += throttle < 0 ? 0.02 : 0.01;
				if(throttle > 1) throttle = 1;
				return true;
			}
			case DECELERATE:{
				throttle -= throttle > 0 ? 0.02 : 0.01;
				if(throttle < -1){
					throttle = -1;
				}
				SimplePhysData spdata = data.getType().getSphData();
				if(spdata != null && throttle < 0 && spdata.min_throttle == 0){
					throttle = 0;
				}
				return true;
			}
			case TURN_LEFT:{
				steer_yaw -= 5;
				return true;
			}
			case TURN_RIGHT:{
				steer_yaw += 5;
				return true;
			}
			case BRAKE:{
				throttle *= 0.8;
				entity.decreaseXZMotion(0.8);
				if(throttle < -0.0001){
					throttle = 0;
				}
				return true;
			}
			case ENGINE:{
				toggleEngine();
				return true;
			}
			case DISMOUNT:{
				player.dismount();
				return true;
			}
			case INVENTORY:{
				player.openUI(UIKey.VEHICLE_MAIN, new V3I(entity.getId(), 0, 0));
				return true;
			}
			case TOGGABLES:{
				if(toggable_timer > 0) return true;
				//TODO toggle action
				toggable_timer = 10;
			}
			case SCRIPTS:{
				//TODO scripts ui
				return true;
			}
			case LIGHTS:{
				if(toggable_timer > 0) return true;
				if(data.getAttribute("lights").asBoolean()){
					if(data.getAttribute("lights_long").asBoolean()){
						data.getAttribute("lights").set(false);
						data.getAttribute("lights_long").set(false);
					}
					else{
						data.getAttribute("lights_long").set(true);
					}
				}
				else{
					data.getAttribute("lights").set(true);
				}
				sendLightsUpdate();
				VehicleInstance trailer = rear;
				while(trailer != null){
					trailer.data.getAttribute("lights").set(data.getAttribute("lights").asBoolean());
					trailer.data.getAttribute("lights_long").set(data.getAttribute("lights_long").asBoolean());
					trailer.sendLightsUpdate();
					trailer = trailer.rear;
				}
				toggable_timer = 10;
				return true;
			}
			case COUPLER_REAR:{
				//TODO coupling
				return true;
			}
			case COUPLER_FRONT:{
				//TODO coupling
				return true;
			}
			default:{
				player.bar("Action '" + key + "' not found.");
				return false;
			}
		}
	}

	public void toggleEngine(){
		if(toggable_timer > 0) return;
		TagCW com = TagCW.create();
		com.set("cargo", "engine_toggle");
		toggable_timer += 10;
		EngineFunction engine = data.getPart("engine").getFunction("fvtm:engine");
		if(entity.isOnClient()) engine.setState(com.getBoolean("engine_toggle_result"));
		else com.set("engine_toggle_result", engine.toggle());
		if(data.getStoredFuel() == 0){
			com.set("engine_toggle_result", engine.setState(false));
			com.set("no_fuel", true);
		}
		Packets.INSTANCE.send(this, com);
		throttle = 0;
	}

	public boolean getKeyPressState(KeyPress key){
		return false;
	}

	public void checkSteerAngle(boolean client){
		if(!client) steer_yaw *= 0.95;
		if(steer_yaw > max_steering_yaw) steer_yaw = max_steering_yaw;
		if(steer_yaw < -max_steering_yaw) steer_yaw = -max_steering_yaw;
	}

	public boolean consumeFuel(EngineFunction engine){
		if(data.outoffuel()) return false;
		if(engine.isOn()){
			if(throttle == 0d || (throttle < .05 && throttle > -.05)){
				fuel_consumed += engine.getIdleFuelConsumption();
			}
			else{
				fuel_consumed += engine.getFuelConsumption(data.getAttribute("fuel_secondary").asString()) * throttle;
			}
		}
		fuel_accumulator++;
		if(fuel_accumulator < 20) return engine.isOn();
		else{
			boolean cons = false;
			if(fuel_consumed > 0){
				int consumed = (int)(fuel_consumed / 20f);
				data.getAttribute("fuel_stored").decrease(consumed < 0 ? 1 : consumed);
				cons = true;
			}
			if(engine.isOn() && data.outoffuel()){
				//TODO send out of fuel packet
				//TODO play out of fuel sound
				throttle = 0;
				engine.setState(false);
			}
			fuel_accumulator = 0;
			fuel_consumed = 0;
			return cons;
		}
	}

	public V3D getV3D(){
		return entity.getPos();
	}

	public void updatePointsSeats(){
		for(SwivelPoint point : data.getRotationPoints().values()) point.update(this);
		for(SeatInstance seat : seats) seat.update();
	}

	public void sendUpdatePacket(){
		data.getAttribute("throttle").set(throttle);
		Packets.sendInRange(Packet_VehMove.class, entity.getWorld(), entity.getPos(), VEHICLE_UPDATE_RANGE, entity, this);
		for(SwivelPoint point : data.getRotationPoints().values()){
			point.sendUpdatePacket(entity);
		}
	}

	public void sendVehicleData(){
		TagCW com = TagCW.create();
		data.write(com);
		com.set("cargo", "vehicledata");
		Packets.INSTANCE.send(this, com);
	}

	public void sendLockUpdate(){
		TagCW com = TagCW.create();
		com.set("cargo", "lock_state");
		com.set("state", data.getLock().isLocked());
		Packets.INSTANCE.send(this, com);
	}

	public void sendLightsUpdate(){
		TagCW com = TagCW.create();
		com.set("cargo", "toggle_lights");
		com.set("lights", data.getAttribute("lights").asBoolean());
		com.set("lights_long", data.getAttribute("lights_long").asBoolean());
		Packets.INSTANCE.send(this, com);
	}

	public void sendAttrToggle(TagCW com){
		com.set("cargo", "toggle_attr");
		Packets.INSTANCE.send(this, com);
	}

	public void sendAttrToggle(Attribute<?> attr){
		if(attr == null) return;
		TagCW packet = TagCW.create();
		packet.set("attr", attr.id);
		packet.set("entity", entity.getId());
		if(attr.valuetype.isTristate()){
			if(attr.asTristate() == null){
				packet.set("value", false);
				packet.set("reset", true);
			}
			else{
				packet.set("value", attr.asBoolean());
			}
		}
		else if(attr.valuetype.isFloat()){
			packet.set("value", attr.asFloat());
		}
		else if(attr.valuetype.isInteger()){
			packet.set("value", attr.asInteger());
		}
		else if(attr.valuetype.isString()){
			packet.set("value", attr.asString());
		}
		else packet.set("value", attr.asString());
		Packets.sendToAll(Packet_TagListener.class, "attr_update", packet);
	}

	public SeatInstance getSeatOf(Object passenger){
		for(SeatInstance seat : seats){
			if(seat.passenger_direct() == passenger) return seat;
		}
		return null;
	}

	public SeatInstance getSeatOf(Passenger passenger){
		return getSeatOf(passenger.direct());
	}

	public void packet(TagCW packet, Passenger passenger){
		String cargo = packet.getString("cargo");
		switch(cargo){
			case "lock_state":{
				data.getLock().setLocked(packet.getBoolean("state"));
				return;
			}
			case "toggle_lights":{
				data.getAttribute("lights").set(packet.getBoolean("lights"));
				data.getAttribute("lights_long").set(packet.getBoolean("lights_long"));
				return;
			}
			case "toggle_attr":{
				if(passenger.isOnClient()) AttributeUtil.processToggleClient(this, packet, passenger);
				return;
			}
			case "vehicledata":{
				data.read(packet);
				return;
			}
			case "engine_toggle":{
				if(passenger.getSeatOn() != null && passenger.getSeatOn().root == this){
					boolean state = packet.getBoolean("engine_toggle_result");
					if(data.getPart("engine").getFunction(EngineFunction.class, "fvtm:engine").setState(state)){
						passenger.send("interact.fvtm.vehicle.engine_toggled_on");
					}
					else{
						passenger.send("interact.fvtm.vehicle.engine_toggled_off");
					}
					if(packet.has("no_fuel") && packet.getBoolean("no_fuel")){
						passenger.send("interact.fvtm.vehicle.engine_no_fuel");
					}
					//TODO sounds
				}
				throttle = 0;
				//TODO engine running sound loop
				return;
			}
			default:{
				FvtmLogger.LOGGER.log("'" + data.getName() + "'/" + entity.getId() + " received invalid packet: " + packet.toString());
				return;
			}
		}
	}

	public boolean isBraking(){
		return false;
	}

	public void assignWheels(){
		w_front_l = w_front_r = w_rear_l = w_rear_r = new WheelTireData();
		for(WheelTireData wheel : wheeldata.values()){
			if(!data.getType().isTrailer()){
				if(wheel.pos.x <= w_front_l.pos.x && wheel.pos.z <= w_front_l.pos.z){
					w_front_l = wheel;
					continue;
				}
				if(wheel.pos.x >= w_front_r.pos.x && wheel.pos.z <= w_front_r.pos.z){
					w_front_r = wheel;
					continue;
				}
			}
			if(wheel.pos.x <= w_rear_l.pos.x && wheel.pos.z >= w_rear_l.pos.z){
				w_rear_l = wheel;
				continue;
			}
			if(wheel.pos.x >= w_rear_r.pos.x && wheel.pos.z >= w_rear_r.pos.z){
				w_rear_r = wheel;
			}
		}
		if(data.getType().isTrailer()){
			w_front_l = new WheelTireData("_" + w_rear_l.id);
			w_front_l.asTrailerFront(w_rear_l);
			wheeldata.put(w_front_l.id, w_front_l);
			w_front_r = new WheelTireData(w_rear_r.id);
			w_front_r.asTrailerFront(w_rear_r);
			wheeldata.put(w_front_r.id, w_front_r);
		}
	}

}
