package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UIText extends UIElement {

	public static Class<? extends UIText> IMPLEMENTATION;
	//
	public String initial_value;
	public String value;
	public boolean shadow;
	public float scale;
	public int color;

	public UIText(UserInterface ui, JsonMap map) throws Exception {
		super(ui, map);
		initial_value = value = map.getString("value", "");
		scale = map.getFloat("scale", 1);
		if(map.getBoolean("autoscale", false)) scale = -1;
		shadow = map.getBoolean("shadow", false);
		color = map.getInteger("color", 0xf0f0f0);
	}

}
