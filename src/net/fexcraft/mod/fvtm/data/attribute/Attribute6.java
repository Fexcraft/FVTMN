package net.fexcraft.mod.fvtm.data.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import net.fexcraft.lib.common.math.Vec3f;
import net.fexcraft.mod.fvtm.sys.uni.KeyPress;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;
import net.fexcraft.mod.uni.tag.TagCW;

/**
 * 6th prototype.
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class Attribute6<V> {

	public static final IDL DEF_ICON = IDLManager.getIDLCached("fvtm:textures/gui/icons/attr_other.png");
	public ArrayList<String> access = new ArrayList<>();
	public LinkedHashMap<String, AttrBox> actboxes;
	public HashMap<String, IDL> icons = new HashMap<>();
	public HashMap<KeyPress, Float> keys;
	public final AttrValueType valuetype;
	public boolean editable;
	public boolean autosync;
	public String origin;
	public String group;
	public String perm;
	public float min = Integer.MIN_VALUE;
	public float max = Integer.MAX_VALUE;
	public V initial;
	public V value;
	public final String id;

	public Attribute6(String aid, AttrValueType type, V val){
		initial = value = val;
		valuetype = type;
		id = aid;
	}

	public <T> T initial(){
		return (T)initial;
	}

	public <T> T value(){
		return (T)value;
	}

	public void set(Object val){
		value = validate(val);
	}

	/** Should return a valid value. */
	public abstract V validate(Object val);

	/** @return may be null if invalid */
	public abstract V parse(String val);

	public void reset(){
		value = initial;
	}

	public abstract void increase(float by);

	public abstract void decrease(float by);

	public void limit(float min, float max){
		this.min = min;
		this.max = max;
	}

	public abstract String type();

	// Returns //

	public int asInteger(){
		return (int)value;
	}

	public long asLong(){
		return (long)value;
	}

	public float asFloat(){
		return (float)value;
	}

	public String asString(){
		return value + "";
	}

	public Vec3f asVector(){
		return new Vec3f(asFloat(), 0, 0);
	}

	public boolean asBoolean(){
		if(valuetype.isBoolean()) return value();
		if(valuetype.isNumber()) return asFloat() > 0;
		if(valuetype.isTristate()) return value != null || asBoolean();
		if(valuetype.isString()) return Boolean.parseBoolean(value + "");
		if(valuetype.isVector()) return asVector().x > 0;
		return value != null;
	}

	public Boolean asTristate(){
		if(valuetype.isNumber()){
			return asFloat() == 0f ? null : asFloat() > 0;
		}
		else return asBoolean();
	}

	public <R> R tristate(R n, R t, R f){
		return asTristate() == null ? n : asTristate() ? t : f;
	}

	public <R> R bool(R t, R f){
		return asBoolean() ? t : f;
	}

	// Access //

	public void addAccess(String str){
		if(!access.contains(str)) access.add(str);
	}

	public void remAccess(String str){
		if(access.contains(str)) access.remove(str);
	}

	public void copyAccessFrom(Attribute6<?> other){
		for(String acc : other.access) addAccess(acc);
	}

	// AttrBox //

	public boolean hasBoxes(){
		return actboxes != null && actboxes.size() > 0;
	}

	public AttrBox getBox(String id){
		if(!hasBoxes()) return null;
		if(!actboxes.containsKey(id)){
			if(id.startsWith("external-")) return getBox("external");
			return actboxes.get("default");
		}
		return actboxes.get(id);
	}

	public void addBox(String id, String point, float... data){
		if(actboxes == null) actboxes = new LinkedHashMap<>();
		actboxes.put(id, new AttrBox(id, point, data));
	}

	public void copyBoxesFrom(Attribute6<?> other){
		if(!other.hasBoxes()) return;
		if(actboxes == null) actboxes = new LinkedHashMap<>();
		for(Entry<String, AttrBox> box : other.actboxes.entrySet()){
			if(actboxes.containsKey(box.getKey())) actboxes.get(box.getKey()).copy(box.getValue());
			else actboxes.put(box.getKey(), box.getValue().copy());
		}
	}

	// KeyPress //

	public boolean hasKeyPress(){
		return keys != null && keys.size() > 0;
	}

	public void copyKeysFrom(Attribute6<?> other){
		if(!other.hasKeyPress()) return;
		if(keys == null) keys = new HashMap<>();
		keys.putAll(other.keys);
	}

	public Float getKeyValue(KeyPress key){
		return keys == null ? null : keys.get(key);
	}

	// Saving/Loading //

	public TagCW save(TagCW com){
		com.set("id", id);
		com.set("type", type());
		if(origin != null) com.set("origin", origin);
		saveValue(com);
		return com;
	}

	public abstract void saveValue(TagCW com);

	public Attribute6<V> load(TagCW com){
		if(com.has("origin")) origin = com.getString("origin");
		loadValue(com);
		return this;
	}

	public abstract void loadValue(TagCW com);

	// Parsing //

}
