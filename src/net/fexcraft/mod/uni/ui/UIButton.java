package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class UIButton extends UIElement {

	public static Class<? extends UIButton> IMPLEMENTATION;
	//
	protected UIText text;
	public String action;
	public String target;
	public String texture;
	public boolean colorbased;
	public int htx, hty;
	public int dtx, dty;
	public int hcolor = 0xf5da42;
	public int ecolor = 0xffffff;
	public int dcolor = 0xa3a3a3;

	public UIButton(UserInterface ui, JsonMap map) throws Exception {
		super(ui, map);
		action = map.getString("action", null);
		target = map.getString("target", null);
		if(map.has("text")){
			text = UIText.IMPLEMENTATION.getConstructor(UserInterface.class, JsonMap.class).newInstance(ui, map.getMap("text"));
		}
		if(map.getBoolean("colorbased", true)){
			ecolor = map.getInteger("e_color", ecolor);
			dcolor = map.getInteger("d_color", dcolor);
			hcolor = map.getInteger("h_color", hcolor);
		}
		else{
			JsonArray arr = map.getArray("uv");
			if(arr.size() > 2){
				htx = arr.get(2).integer_value();
				hty = arr.get(3).integer_value();
			}
			if(arr.size() > 4){
				dtx = arr.get(4).integer_value();
				dty = arr.get(5).integer_value();
			}
		}
	}

	public boolean hovered(int mx, int my){
		return hovered = mx >= x && mx <= x + width && my >= y && my <= y + height;
	}

}
