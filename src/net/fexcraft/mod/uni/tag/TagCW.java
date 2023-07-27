package net.fexcraft.mod.uni.tag;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface TagCW {

	public String getString(String key);

	public float getFloat(String key);

	public int getInteger(String key);

	public boolean getBoolean(String key);

	public boolean has(String key);

	public void set(String key, String val);

	public void set(String key, float val);

	public void set(String key, int val);

	public void set(String key, boolean val);

	//

	public static Class<? extends TagCW>[] IMPL = new Class[1];

	public static TagCW create(){
		try {
			return IMPL[0].newInstance();
		}
		catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public abstract <T> T local();

	public abstract Object direct();

}
