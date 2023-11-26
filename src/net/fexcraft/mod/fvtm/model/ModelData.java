package net.fexcraft.mod.fvtm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import net.fexcraft.lib.mc.utils.Print;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ModelData extends HashMap<String, Object> {

	/**
	 * Gets the specific value based on key, will return null if missing.
	 */
	public <T> T get(String key) {
		return (T) super.get(key);
	}

	/**
	 * Gets the specific value based on key, uses supplier to fill in value if missing.
	 */
	public <T> T get(String key, Supplier<T> ifmissing) {
		if (!containsKey(key)) {
			return (T) set(key, ifmissing.get());
		}
		return (T) super.get(key);
	}

	/**
	 * Gets the specific boolean value based on key, will return false if missing.
	 */
	public boolean bool(String key) {
		Object bool = super.get(key);
		if(bool == null) return false;
		if(bool instanceof Boolean) return (boolean)bool;
		return Boolean.parseBoolean(bool.toString());
	}

	/**
	 * Sets the specific value based on key, returns the set value.
	 */
	public <T> T set(String key, T obj) {
		put(key, obj);
		return obj;
	}

	/**
	 * Sets the specific value based on key, if missing.
	 */
	public <T> void set(String key, Supplier<T> ifmissing) {
		if (!containsKey(key)) set(key, ifmissing.get());
	}

	public boolean contains(String key) {
		return containsKey(key);
	}

	public ArrayList<String> creators() {
		if (!containsKey("creators")) put("creators", new ArrayList<String>());
		return get("creators");
	}

	public void convert() {
		Collection<String> keys = new ArrayList<String>(keySet());
		for (String key : keys) {
			Object obj = get(key);
			if (obj instanceof String == false) continue;
			String val = obj.toString();
			if (val.equals("true") || val.equals("false")) {
				put(key, val.equals("true"));
				continue;
			}
			if (NumberUtils.isCreatable(val)) {
				try {
					put(key, Float.parseFloat(val));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public <T> T get(String key, T def) {
		Object o = super.get(key);
		return o == null ? def : (T) o;
	}

	public List<String> getList(String key) {
		Object obj = super.get(key);
		if (obj == null) return new ArrayList<>();
		if (obj instanceof List == false) {
			ArrayList<String> list = new ArrayList<>();
			list.add(obj.toString());
			put(key, list);
			return list;
		}
		return (List<String>) obj;
	}

}
