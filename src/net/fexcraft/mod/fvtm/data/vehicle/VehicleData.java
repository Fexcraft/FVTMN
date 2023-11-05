package net.fexcraft.mod.fvtm.data.vehicle;

import static net.fexcraft.mod.fvtm.FvtmRegistry.PARTS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.mod.fvtm.FvtmResources;
import net.fexcraft.mod.fvtm.data.ContentData;
import net.fexcraft.mod.fvtm.data.Seat;
import net.fexcraft.mod.fvtm.data.attribute.Attribute;
import net.fexcraft.mod.fvtm.data.part.Part;
import net.fexcraft.mod.fvtm.data.part.PartData;
import net.fexcraft.mod.fvtm.data.part.PartFunction;
import net.fexcraft.mod.fvtm.data.part.PartSlot;
import net.fexcraft.mod.fvtm.data.part.PartSlots;
import net.fexcraft.mod.fvtm.data.root.Colorable;
import net.fexcraft.mod.fvtm.data.root.Lockable;
import net.fexcraft.mod.fvtm.data.root.Sound;
import net.fexcraft.mod.fvtm.data.root.Soundable;
import net.fexcraft.mod.fvtm.data.root.Textureable;
import net.fexcraft.mod.fvtm.data.root.Textureable.TextureHolder;
import net.fexcraft.mod.fvtm.data.root.Textureable.TextureUser;
import net.fexcraft.mod.fvtm.function.ColorFunction;
import net.fexcraft.mod.fvtm.function.EngineFunction;
import net.fexcraft.mod.fvtm.function.PartSlotsFunction;
import net.fexcraft.mod.fvtm.function.SeatsFunction;
import net.fexcraft.mod.fvtm.function.WheelPositionsFunction;
import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;
import net.fexcraft.mod.uni.world.EntityW;
import net.fexcraft.mod.uni.world.MessageSender;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class VehicleData extends ContentData<Vehicle, VehicleData> implements Colorable, Soundable, TextureUser {

	public HashMap<String, ArrayList<Entry<String, PartData>>> sorted_parts = new HashMap<>();
	protected TreeMap<String, Attribute<?>> attributes = new TreeMap<>();
	protected TreeMap<String, PartData> parts = new TreeMap<>();
	protected TreeMap<String, RGB> channels = new TreeMap<>();
	protected TreeMap<String, WheelSlot> wheels = new TreeMap<>();
	protected TreeMap<String, V3D> wheelpos = new TreeMap<>();
	protected TreeMap<String, Sound> sounds = new TreeMap<>();
	protected TreeMap<String, SwivelPoint> rotpoints = new TreeMap<>();
	protected TreeMap<String, PartSlots> partproviders = new TreeMap<>();
	protected ArrayList<VehicleScript> scripts = new ArrayList<>();
	protected ArrayList<String> inventories = new ArrayList<>();
	protected ArrayList<Seat> seats = new ArrayList<>();
	protected V3D front_conn, rear_conn;
	protected SwivelPoint rootpoint;
	protected Textureable texture;
	protected Lockable lock;
	protected String displayname;

	public VehicleData(Vehicle type){
		super(type);
		texture = new Textureable(type);
		rotpoints.put("vehicle", rootpoint = new SwivelPoint("vehicle", (String)null));
		for(SwivelPoint point : type.getDefaultSwivelPoints().values()){
			rotpoints.put(point.id, point.clone(null));
		}
		for(Attribute<?> attr : type.getDefaultAttributes().values()){
			Attribute<?> copy = attr.createCopy(null);
			attributes.put(copy.id, copy);
		}
		for(Entry<String, WheelSlot> entry: type.getWheelPositions().entrySet()){
			wheels.put(entry.getKey(), entry.getValue().copy(null));
		}
		for(Entry<String, RGB> entry : type.getDefaultColorChannels().entrySet()){
			channels.put(entry.getKey(), entry.getValue().copy());
		}
		front_conn = type.getDefaultConnectorFront();
		rear_conn = type.getDefaultConnectorRear();
		if(type.getInstalled() != null){
			for(java.util.Map.Entry<String, IDL> entry : type.getInstalled().entrySet()){
				try{
					Part part = PARTS.get(entry.getValue());
					if(part == null) continue;
					if(installPart(MessageSender.DEVLOG, new PartData(part), entry.getKey(), false) != null){
						Static.stop();
					}
				}
				catch(Exception e){
					e.printStackTrace();
					Static.stop();
				}
			}
		}
		rotpoints.values().forEach(point -> point.linkToParent(this));
		sounds.putAll(type.getSounds());
		partproviders.put("vehicle", type.getPartSlots());
		lock = new Lockable();
	}

	@Override
	public TagCW write(TagCW compound){
		if(compound == null) compound = TagCW.create();
		compound.set("format", 4f);
		compound.set("Vehicle", type.getID().toString());
		//
		TagCW cparts = TagCW.create();
		for(Entry<String, PartData> part : parts.entrySet()){
			cparts.set(part.getKey(), part.getValue().write(null));
		}
		compound.set("Parts", cparts);
		//
		TagCW cattrs = TagCW.create();
		for(Attribute<?> attr : attributes.values()){
			cattrs.set(attr.id, attr.save(TagCW.create()));
		}
		compound.set("Attributes", cattrs);
		//
		texture.save(compound);
		for(String str : channels.keySet()){
			compound.set("RGB_" + str, channels.get(str).packed);
		}
		TagCW cwpos = TagCW.create();
		for(Entry<String, V3D> vec : wheelpos.entrySet()){
			TagLW list = TagLW.create();
			list.add(vec.getValue().x);
			list.add(vec.getValue().y);
			list.add(vec.getValue().z);
			cwpos.set(vec.getKey(), list);
		}
		compound.set("WheelPos", cwpos);
		if(!scripts.isEmpty()){
			TagCW cscripts = TagCW.create();
			for(VehicleScript script : scripts){
				TagCW com = script.save(this, TagCW.create());
				if(com == null || com.empty()) continue;
				cscripts.set(script.getId(), com);
			}
			if(!cscripts.empty()) compound.set("Scripts", cscripts);
		}
		if(!rotpoints.isEmpty()){
			TagCW csp = TagCW.create();
			for(Entry<String, SwivelPoint> point : rotpoints.entrySet()){
				if(point.getKey().equals("vehicle")) continue;
				csp.set(point.getKey(), point.getValue().write(TagCW.create()));
			}
			if(!csp.empty()) compound.set("SwivelPoints", csp);
		}
		lock.save(compound);
		if(front_conn != null) compound.set("FrontConnector", front_conn);
		if(rear_conn != null) compound.set("RearConnector", rear_conn);
		if(displayname != null) compound.set("DisplayName", displayname);
		return compound;
	}

	@Override
	public VehicleData read(TagCW compound){
		//if(!compound.has("format") || compound.getFloat("format") < 4f) return this;
		if(compound.has("Parts")){
			TagCW cparts = compound.getCompound("Parts");
			parts.clear();
			for(String key : cparts.keys()){
				TagCW com = cparts.getCompound(key);
				PartData part = FvtmResources.INSTANCE.getPartData(com);
				if(part != null) parts.put(key, part);
			}
		}
		if(compound.has("Attributes")){
			TagCW cattrs = compound.getCompound("Attributes");
			for(String key : cattrs.keys()){
				TagCW com = cattrs.getCompound(key);
				Attribute<?> attr = getAttribute(key);
				if(attr == null){
					attr = Attribute.parse(key, com);
					if(attr != null) attributes.put(key, attr);
				}
				else{
					attr.load(com);
				}
			}
		}
		for(Attribute<?> attr : type.getDefaultAttributes().values()){
			if(!attributes.containsKey(attr.id)){
				Attribute<?> copy = attr.createCopy(null);
				attributes.put(copy.id, copy);
			}
			Attribute<?> attri = attributes.get(attr.id);
			attri.copyBoxesFrom(attr);
			attri.perm = attr.perm;
		}
		if(EnvInfo.CLIENT){
			for(Attribute<?> attr : attributes.values()) attr.genDefaultIcons();
		}
		//
		texture.load(compound);
		for(String str : channels.keySet()){
			if(compound.has("RGB_" + str)){
				channels.get(str).packed = compound.getInteger("RGB_" + str);
			}
		}
		//
		refreshModificableDataByParts();
		//
		if(compound.has("WheelPos")){
			TagCW cwp = compound.getCompound("WheelPos");
			wheelpos.clear();
			for(String key : cwp.keys()){
				TagCW com = cwp.getCompound(key);
				wheelpos.put(key, com.getV3D(key));
			}
		}
		if(compound.has("Scripts")){
			TagCW cscripts = compound.getCompound("Scripts");
			for(String key : cscripts.keys()){
				if(getVehicleScript(key) != null){
					getVehicleScript(key).load(this, cscripts.getCompound(key));
				}
			}
		}
		if(compound.has("SwivelPoints")){
			TagCW csp = compound.getCompound("SwivelPoints");
			for(String key : csp.keys()){
				TagCW com = csp.getCompound(key);
				if(rotpoints.containsKey(key)){
					rotpoints.get(key).read(null, this, com);
				}
				else{
					rotpoints.put(key, new SwivelPoint(this, key, com));
				}
			}
		}
		rotpoints.values().forEach(point -> point.linkToParent(this));
		lock.load(compound);
		front_conn = compound.getV3D("FrontConnector");
		if(front_conn == null) front_conn = type.getDefaultConnectorFront();
		rear_conn = compound.getV3D("RearConnector");
		if(rear_conn == null) rear_conn = type.getDefaultConnectorRear();
		if(compound.has("DisplayName")) displayname = compound.getString("DisplayName");
		//
		return this;
	}

	private void refreshModificableDataByParts(){
		wheels.clear();
		type.getWheelPositions().entrySet().forEach(entry -> wheels.put(entry.getKey(), entry.getValue().copy(null)));
		for(PartData part : parts.values()){
			if(part.hasFunction("fvtm:wheel_positions")){
				WheelPositionsFunction func = part.getFunction("fvtm:wheel_positions");
				func.getPositions().entrySet().forEach(entry -> wheels.put(entry.getKey(), entry.getValue().copy(part.getInstalledPos())));
			}
		}
		//
		seats.clear();
		for(PartData part : parts.values()){
			if(!part.hasFunction("fvtm:seats")) continue;
			for(Seat seat : part.getFunction(SeatsFunction.class, "fvtm:seats").getSeats()){
				seats.add(seat.relative ? seat.copy(part.getInstalledPos()) : seat);
			}
		}
		//colors
		TreeMap<String, RGB> chan = new TreeMap<>(channels);
		channels.clear();
		for(Entry<String, RGB> entry : type.getDefaultColorChannels().entrySet()){
			channels.put(entry.getKey(), entry.getValue().copy());
		}
		for(PartData part : parts.values()){
			if(!part.hasFunction("fvtm:color")) continue;
			TreeMap<String, RGB> colors = part.getFunction(ColorFunction.class, "fvtm:color").getColors();
			for(Entry<String, RGB> entry : colors.entrySet()){
				if(!channels.containsKey(entry.getKey())){
					channels.put(entry.getKey(), entry.getValue().copy());
				}
			}
		}
		for(Entry<String, RGB> entry : chan.entrySet()){
			if(channels.containsKey(entry.getKey())){
				channels.get(entry.getKey()).packed = entry.getValue().packed;
			}
		}
		//
		inventories.clear();
		parts.forEach((key, value) -> {
			if(value.hasFunction("fvtm:inventory")) inventories.add(key);
		});
		//
		sounds.clear(); sounds.putAll(type.getSounds());
		for(PartData data : parts.values()){
			for(Map.Entry<String, Sound> entry : data.getType().getSounds().entrySet()){
				if(!sounds.containsKey(entry.getKey()) || entry.getValue().override) sounds.put(entry.getKey(), entry.getValue());
			}
		}
		//
		for(Attribute<?> attr : attributes.values()){
			if(attr.origin == null) continue;
			String origin = attr.origin.split("\\|")[0];
			PartData part = parts.get(origin);
			if(part == null) continue;
			for(Attribute<?> ettr : part.getType().getDefaultAttributes().values()){
				if(ettr.id.equals(attr.id)){
					attr.copyBoxesFrom(ettr);
					break;
				}
			}
		}
		//
		partproviders.clear();
		if(!type.getPartSlots().isEmpty()) partproviders.put("vehicle", type.getPartSlots());
		for(Entry<String, PartData> data : this.getParts().entrySet()){
			if(data.getValue().hasFunction("fvtm:part_slots")){
				PartSlots ps = data.getValue().getFunction(PartSlotsFunction.class, "fvtm:part_slots").getPartSlotss();
				if(!ps.isEmpty()) partproviders.put(data.getKey(), ps);
			}
		}
		sortparts();
	}

	public void sortparts(){
		sorted_parts.clear();
		for(String str : getRotationPoints().keySet()) sorted_parts.put(str, new ArrayList<>());
		for(Entry<String, PartData> part : parts.entrySet()){
			if(part.getValue().isInstalledOnSwivelPoint()){
				sorted_parts.get(part.getValue().getSwivelPointInstalledOn()).add(part);
			}
			else sorted_parts.get("vehicle").add(part);
		}
	}

	@Override
	public VehicleData parse(JsonMap obj){
		//
		return this;
	}

	@Override
	public JsonMap toJson(){
		return new JsonMap();
	}
	
	public Attribute<?> getAttribute(String id){
		return attributes.get(id);
	}
	
	public <VAL> Attribute<VAL> getAttributeCasted(String id){
		return (Attribute<VAL>)attributes.get(id);
	}
	
	public TreeMap<String, Attribute<?>> getAttributes(){
		return attributes;
	}

	/** Null-Safe attribute value check. Works with tri-state attributes too. */
	public Boolean getAttributeBoolean(String id, boolean def){
		Attribute<?> attr = getAttribute(id);
		return attr == null ? def : attr.asTristate();
	}

	/** Null-Safe attribute value check. Works with integer attributes too. */
	public float getAttributeFloat(String id, float def){
		Attribute<?> attr = getAttribute(id);
		return attr == null ? def : attr.asFloat();
	}

	/** Null-Safe attribute value check. */
	public int getAttributeInteger(String id, int def){
		Attribute<?> attr = getAttribute(id);
		return attr == null ? def : attr.asInteger();
	}

	/** Null-Safe attribute value check. Works with other attribute types too. */
	public String getAttributeString(String id, String def){
		Attribute<?> attr = getAttribute(id);
		return attr == null ? def : attr.asString();
	}

	public Boolean getAttributeTristate(String id, Boolean def){
		Attribute<?> attr = getAttribute(id);
		return attr == null ? def : attr.asTristate();
	}

	public void setAttribute(String id, Object val){
		Attribute<?> attr = getAttribute(id);
		if(attr != null) attr.set(val);
	}
	
	/** @return null if installed successfully. */
	public PartData installPart(MessageSender engineer, PartData data, String category, boolean swap){
		if(!data.getType().getInstallHandler().validInstall(engineer, data, category, this, swap)) return data;
		if(data.getType().getInstallHandler().processInstall(engineer, data, category, this)){
			this.insertSwivelPointsFromPart(data, category);
			this.insertAttributesFromPart(data, category);
			//
			if(!swap){
				this.resetAttributes();
				//TODO replace with static modifiers
				//this.updateAttributes(null, AttrUpdate.INITIAL);
			}
			//
			this.refreshModificableDataByParts();
			return null;
		}
		return data;
	}

	public boolean deinstallPart(MessageSender sender, String category, boolean swap){
		PartData part = this.getPart(category);
		//TODO if(part == null){ Print.chatnn(sender, "No part in that category."); return false; }
		if(!part.getType().getInstallHandler().validUninstall(sender, part, category, this, swap)) return false;
		if(part.getType().getInstallHandler().processUninstall(sender, part, category, this)){
			this.removeSwivelPointsFromPart(part, category);
			this.removeAttributesFromPart(part, category);
			//
			if(!swap){
				this.resetAttributes();
				//TODO replace with static modifiers
				//this.updateAttributes(null, AttrUpdate.INITIAL);
			}
			//
			this.refreshModificableDataByParts();
			return true;
		} else return false;
	}

	private void insertSwivelPointsFromPart(PartData data, String category){
		if(data.getType().getDefaultSwivelPoints().isEmpty()) return;
		for(SwivelPoint point : data.getType().getDefaultSwivelPoints().values()){
			if(!rotpoints.containsKey(point.id)){
				rotpoints.put(point.id, point.clone(category + "|" + data.getType().getIDS()));
			}
		}
		rotpoints.values().forEach(point -> point.linkToParent(this));
	}

	private void removeSwivelPointsFromPart(PartData data, String category){
		String dataid = category + "|" + data.getType().getIDS();
		rotpoints.values().removeIf(filter -> filter.origin != null && filter.origin.equals(dataid));
		rotpoints.values().forEach(point -> point.linkToParent(this));
	}

	private void insertAttributesFromPart(PartData data, String catin){
		String dataid = catin + "|" + data.getType().getIDS();
		for(Attribute<?> attr : data.getType().getDefaultAttributes().values()){
			String[] valid = attr.target.split(",");
			boolean pass = false;
			boolean not = false;
			for(String val : valid){
				val = val.trim();
				if(val.startsWith("!")){
					not = true;
					val = val.substring(1);
				}
				if(val.equals("vehicle")){
					pass = !not;
					break;
				}
				if(val.contentEquals(this.getType().getID().colon())){
					pass = !not;
					break;
				}
				if(this.getType().getCategories().contains(val)){
					pass = !not;
					break;
				}
				if(val.startsWith("pack-")){
					if(val.substring(5).equals(getType().getAddon().getID().colon())){
						pass = !not;
						break;
					}
				}
			}
			if(pass && !this.getAttributes().containsKey(attr.id)){
				this.getAttributes().put(attr.id, attr.createCopy(dataid));
			}
		}
	}

	private void removeAttributesFromPart(PartData data, String category){
		String datain = category + "|" + data.getType().getIDS();
		this.attributes.entrySet().removeIf(pre -> pre.getValue().origin != null && pre.getValue().origin.equals(datain));
	}

	public void resetAttributes(){
		for(Attribute<?> attr : attributes.values()){ attr.reset(); }
	}

	public void clearAttributes(){
		if(!attributes.isEmpty()) attributes.clear();
		for(Attribute<?> attr : type.getDefaultAttributes().values()){
			if(!attr.target.startsWith("self")) continue;
			Attribute<?> copy = attr.createCopy(null);
			attributes.put(copy.id, copy);
		}
	}
	
	public java.util.Map<String, PartData> getParts(){
		return parts;
	}
	
	public PartData getPart(String string){
		return parts.get(string);
	}

	public boolean hasPart(String string){
		return getPart(string) != null;
	}

	@Override
	public RGB getColorChannel(String channel){
		return channels.get(channel);
	}

	@Override
	public void setColorChannel(String channel, RGB color){
		channels.put(channel, color);
	}

	@Override
	public TreeMap<String, RGB> getColorChannels(){
		return channels;
	}

	public StackWrapper newItemStack(){
		StackWrapper stack = this.type.getNewStack();
		stack.setTag(write(TagCW.create()));
		return stack;
	}
	
	public TreeMap<String, WheelSlot> getWheelSlots(){
		return wheels;
	}
	
	public TreeMap<String, V3D> getWheelPositions(){
		return wheelpos;
	}

	public List<Seat> getSeats(){
		return seats;
	}

	public Seat getSeat(String id){
		for(Seat seat : seats) if(seat.name.equals(id)) return seat; return null;
	}

	public Seat getSeat(int index){
		return index < 0 ? null : index >= seats.size() ? null : seats.get(index);
	}

	public Lockable getLock(){
		return lock;
	}

	public double getThrottle(){
		return getAttribute("throttle").asFloat();
	}

	public ArrayList<String> getInventories(){
		return inventories;
	}

	public String[] getFuelGroup(){
		if(!parts.containsKey("engine")) return new String[]{ "no engine" };
		if(!parts.get("engine").hasFunction("fvtm:engine")) return new String[]{ "disfunctional engine" };
		return parts.get("engine").getFunction(EngineFunction.class, "fvtm:engine").getFuelGroup();
	}

	public boolean getLightsState(){
		return getAttribute("lights").asBoolean();
	}

	public boolean getFogLightsState(){
		return getAttribute("lights_fog").asBoolean();
	}

	public boolean getLongLightsState(){
		return getAttribute("lights_long").asBoolean();
	}

	public boolean getSpecialLightsState(){
		return getAttribute("lights_other").asBoolean();
	}

	public boolean getTurnLightLeft(){
		return Boolean.FALSE.equals(getAttribute("turn_lights").asTristate());
	}

	public boolean getTurnLightRight(){
		return Boolean.TRUE.equals(getAttribute("turn_lights").asTristate());
	}

	public boolean getWarningLights(){
		return getAttribute("warning_lights").asBoolean();
	}

	public int getStoredFuel(){
		return getAttribute("fuel_stored").asInteger();
	}

	public int getFuelCapacity(){
		return getAttribute("fuel_capacity").asInteger();
	}

	public VehicleScript getVehicleScript(String string){
		for(VehicleScript script : scripts) if(script.getId().equals(string)) return script; return null;
	}

	public <V extends VehicleScript> V getVehicleScriptCasted(String string){
		for(VehicleScript script : scripts) if(script.getId().equals(string)) return (V)script; return null;
	}

	public ArrayList<VehicleScript> getScripts(){
		return scripts;
	}

	public V3D getFrontConnector(){
		return front_conn;
	}

	public V3D getRearConnector(){
		return rear_conn;
	}
	
	public void setConnector(V3D newcon, boolean front){
		if(newcon == null)
			if(front) front_conn = type.getDefaultConnectorFront();
			else rear_conn = type.getDefaultConnectorRear();
		else if(front) front_conn = newcon; else rear_conn = newcon;
	}
	
	public List<Attribute<?>> getAttributes(String group){
		return attributes.values().stream().filter(pre -> pre.group != null && pre.group.equals(group)).collect(Collectors.toList());
	}

	@Override
	public Object getSoundEvent(String event){
		Sound sound = getSound(event);
		return sound == null ? null : sound.event;
	}

	@Override
	public float getSoundVolume(String event){
		Sound sound = getSound(event);
		return sound == null ? 1f : sound.volume;
	}

	@Override
	public float getSoundPitch(String event){
		Sound sound = getSound(event);
		return sound == null ? 1f : sound.pitch;
	}

	@Override
	public Sound getSound(String event){
		return sounds.get(event);
	}

	@Override
	public void playSound(EntityW at, String event){
		Sound sound = getSound(event);
		if(sound == null) return;
		//TODO at.playSound((SoundEvent)sound.event, sound.volume, sound.pitch);
	}
	
	public TreeMap<String, SwivelPoint> getRotationPoints(){
		return rotpoints;
	}
	
	/** Falls back to "vehicle" rotpoint if the specified one isn't found. */
	public SwivelPoint getRotationPoint(String id){
		if(id == null) return rootpoint;
		SwivelPoint point = rotpoints.get(id);
		if(point == null) return rootpoint;
		return point;
	}

	public SwivelPoint getRotationPointOfPart(String cat){
		if(cat == null || !parts.containsKey(cat)) return rootpoint;
		cat = parts.get(cat).getSwivelPointInstalledOn();
		return getRotationPoint(cat);
	}
	
	public String getName(){
		return displayname == null ? type.getName() : displayname;
	}

	public void setDisplayName(String string){
		displayname = string;
	}

	public String getDisplayName(){
		return displayname;
	}

	public <F extends PartFunction> F getFunctionInPart(String part, String function){
		return parts.containsKey(part) ? parts.get(part).getFunction(function) : null;
	}

	public VehicleData copy(){
		return new VehicleData(type).read(write(null));
	}
	
	public List<Attribute<?>> getAttributeGroup(String group){
		return attributes.values().stream().filter(attr -> attr.group != null && attr.group.equals(group)).collect(Collectors.toList());
	}

	public TreeMap<String, PartSlots> getPartSlotProviders(){
		return partproviders;
	}

	public PartSlots getPartSlotsProvider(String psp_id){
		return partproviders.get(psp_id);
	}

	public boolean hasPartSlot(String str){
		for(PartSlots slots : partproviders.values()){
			for(Entry<String, PartSlot> entry : slots.entrySet()){
				if(entry.getKey().equals(str)) return true;
			}
		}
		return false;
	}

	public int getAttributeIndex(Attribute<?> attribute){
		return new ArrayList<>(attributes.keySet()).indexOf(attribute.id);
	}

	public Attribute<?> getAttributeByIndex(int idx){
		return new ArrayList<>(attributes.values()).get(idx);
	}

	@Override
	public Textureable getTexture(){
		return texture;
	}

	@Override
	public TextureHolder getTexHolder(){
		return type;
	}

	public boolean outoffuel(){
		return getAttribute("fuel_stored").asInteger() <= 0;
	}

	public boolean hasfuel(){
		return getAttribute("fuel_stored").asInteger() > 0;
	}

}
