package net.fexcraft.mod.uni.tag;

import java.util.function.Supplier;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface TagCW {

	public String getString(String key);

	public float getFloat(String key);

	public double getDouble(String key);

	public int getInteger(String key);

	public boolean getBoolean(String key);

	public TagCW getCompound(String key);

	public TagLW getList(String key);

	public boolean has(String key);

	public void set(String key, String val);

	public void set(String key, float val);

	public void set(String key, double val);

	public void set(String key, int val);

	public void set(String key, boolean val);

	public void set(String key, TagCW val);

	public void set(String key, TagLW val);

	public int size();

	//

	public static Supplier<TagCW>[] SUPPLIER = new Supplier[1];

	public static TagCW create(){
		return SUPPLIER[0].get();
	}

	public abstract <T> T local();

	public abstract Object direct();

}
