package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class UIElement {

	protected UserInterface ui;
	public int ox, oy;
	public int x, y;
	public int otx, oty;
	public int tx, ty;
	public int owidth, width;
	public int oheight, height;
	public boolean absolute;
	public boolean visible;
	public boolean enabled;
	public boolean hovered;

	public UIElement(UserInterface ui, JsonMap map) throws Exception {
		this.ui = ui;
		absolute = map.getBoolean("absolute", false);
		JsonArray arr = map.getArray("pos");
		ox = x = arr.get(0).integer_value();
		oy = y = arr.get(1).integer_value();
		arr = map.getArray("uv");
		otx = tx = arr.get(0).integer_value();
		oty = ty = arr.get(1).integer_value();
		arr = map.getArray("size");
		owidth = width = arr.get(0).integer_value();
		oheight = height = arr.get(1).integer_value();
		visible = map.getBoolean("visible", true);
		enabled = map.getBoolean("enabled", true);
	}

	public boolean hovered(int mx, int my){
		return hovered = mx >= x && mx <= x + width && my >= y && my <= y + height;
	}

}
