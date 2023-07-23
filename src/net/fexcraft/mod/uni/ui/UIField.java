package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UIField extends UIElement {

	public static Class<? extends UIField> IMPLEMENTATION;
	//
	public String initial_value;
	public String value;
	public String regex;
	public boolean background;
	public float scale;
	public int color;

	public UIField(UserInterface ui, JsonMap map) throws Exception {
		super(ui, map);
		initial_value = value = map.getString("value", "");
		scale = map.getFloat("scale", 1);
		if(map.getBoolean("autoscale", false)) scale = -1;
		background = map.getBoolean("background", false);
		color = map.getInteger("color", 0xf0f0f0);
		if(map.has("numberfield")) regex = "[^\\d\\-\\.\\,]";
	}

	public boolean onclick(int mx, int my, int mb){
		return false;
	}

	public boolean keytyped(char c, int code){
		return false;
	}
}
