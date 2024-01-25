package net.fexcraft.mod.fvtm.model;

import java.util.Map;
import java.util.Set;

/**
 * @author Ferdinand Calo' (FEX___96)
 * 
 * Capability to hold temporary animation data.
 */
public interface RenderCache {

	public Map<String, Float> map();

	public Set<String> keys();
	
	/** Gets a float value if present, else returns null. */
	public Float get(String id);
	
	/** Returns the specified default value if entry is missing. */
	public Float get(String id, Float def);
	
	/** Set value to `null` to remove the entry. Otherwise, it updates the cache. */
	public Float set(String id, Float value);

}