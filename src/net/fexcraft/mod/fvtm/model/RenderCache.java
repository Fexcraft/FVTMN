package net.fexcraft.mod.fvtm.model;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Ferdinand Calo' (FEX___96)
 * 
 * Capability to hold temporary animation data.
 */
public interface RenderCache {

	public Map<String, Float> fltmap();

	public Map<String, Object> map();
	
	/** Gets a float value if present, else returns null. */
	public Float getFlt(String id);
	
	/** Returns the specified default value if entry is missing. */
	public Float getFlt(String id, Float def);
	
	/** Set value to `null` to remove the entry. Otherwise, it updates the cache. */
	public Float setFlt(String id, Float value);

	/** Gets a object if present, else returns null. */
	public <V> V get(String id);

	/** Returns a new default object if entry is missing. */
	public <V> V get(String id, Supplier<V> def);

	/** Set object to `null` to remove the entry. Otherwise, it updates the cache. */
	public <V> V set(String id, V value);

}