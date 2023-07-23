package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class UIButton extends UIElement {

	public static Class<? extends UIButton> IMPLEMENTATION;
	//
	protected UIText text;
	public String action;
	public String target;
	public IDL texture;
	public boolean colorbased;
	public int htx, hty;
	public int dtx, dty;
	public RGB hcolor = new RGB(0xf5da42);
	public RGB ecolor = new RGB(0xffffff);
	public RGB dcolor = new RGB(0xa3a3a3);

	public UIButton(UserInterface ui, JsonMap map) throws Exception {
		super(ui, map);
		action = map.getString("action", null);
		target = map.getString("target", null);
		if(map.has("text")){
			text = UIText.IMPLEMENTATION.getConstructor(UserInterface.class, JsonMap.class).newInstance(ui, map.getMap("text"));
		}
		if(map.getBoolean("colorbased", true)){
			if(map.has("color")){
				ecolor.packed = hcolor.packed = dcolor.packed = map.get("color").integer_value();
			}
			ecolor.packed = map.getInteger("e_color", ecolor.packed);
			dcolor.packed = map.getInteger("d_color", dcolor.packed);
			hcolor.packed = map.getInteger("h_color", hcolor.packed);
			if(map.has("alpha")){
				ecolor.alpha = hcolor.alpha = dcolor.alpha = map.get("alpha").float_value();
			}
			ecolor.alpha = map.getFloat("e_alpha", ecolor.alpha);
			hcolor.alpha = map.getFloat("h_alpha", hcolor.alpha);
			dcolor.alpha = map.getFloat("d_alpha", dcolor.alpha);
		}
		else{
			JsonArray arr = map.getArray("uv");
			if(arr.size() > 2){
				htx = arr.get(2).integer_value();
				hty = arr.get(3).integer_value();
			}
			else{
				htx = tx;
				hty = ty;
			}
			if(arr.size() > 4){
				dtx = arr.get(4).integer_value();
				dty = arr.get(5).integer_value();
			}
			else{
				dtx = tx;
				dty = ty;
			}
		}
		texture = map.has("texture") ? IDLManager.getIDLCached(map.get("texture").string_value()) : null;
	}

	public boolean hovered(int mx, int my){
		return hovered = mx >= x && mx <= x + width && my >= y && my <= y + height;
	}

	public boolean clicked(int mx, int my){
		return enabled && visible && mx >= x && mx <= x + width && my >= y && my <= y + height;
	}

	public boolean onclick(int mx, int my, int mb){
		return false;
	}

	public boolean onscroll(int am, int x, int y){
		return false;
	}

}
