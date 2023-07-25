package net.fexcraft.mod.fvtm.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.ui.UIButton;
import net.fexcraft.mod.uni.ui.UserInterface;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class DecoEditor extends UserInterface {

	public DecoEditor(JsonMap map, int entid) throws Exception {
		super(map, new DecoContainer(map, entid));
	}

	@Override
	public boolean onAction(UIButton button, String id, int l, int t, int x, int y, int b) {
		return false;
	}

	@Override
	public boolean onScroll(UIButton button, String id, int gl, int gt, int mx, int my, int am) {
		return false;
	}

}
