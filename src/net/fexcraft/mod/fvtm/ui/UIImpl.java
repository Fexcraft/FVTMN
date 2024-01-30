package net.fexcraft.mod.fvtm.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.ui.UIButton;
import net.fexcraft.mod.uni.ui.UserInterface;
import net.fexcraft.mod.uni.world.EntityW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UIImpl extends UserInterface {

	public UIImpl(JsonMap map, EntityW player, V3I pos) throws Exception {
		super(map, new CIImpl(map, player, pos));
	}

	@Override
	public boolean onAction(UIButton button, String id, int l, int t, int x, int y, int b){
		return false;
	}

	@Override
	public boolean onScroll(UIButton button, String id, int gl, int gt, int mx, int my, int am){
		return false;
	}

	@Override
	public void predraw(float ticks, int mx, int my) {

	}

	@Override
	public void postdraw(float ticks, int mx, int my) {

	}

	@Override
	public void scrollwheel(int am, int mx, int my) {

	}

}
