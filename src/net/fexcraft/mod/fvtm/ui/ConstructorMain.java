package net.fexcraft.mod.fvtm.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIButton;
import net.fexcraft.mod.uni.ui.UserInterface;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ConstructorMain extends UserInterface {

	//

	public ConstructorMain(JsonMap map, ContainerInterface con) throws Exception {
		super(map, con);
	}

	@Override
	public boolean onAction(UIButton button, String id, int l, int t, int x, int y, int mb){
		//
		return true;
	}

	@Override
	public boolean onScroll(UIButton button, String id, int gl, int gt, int mx, int my, int am) {
		//
		return false;
	}

	@Override
	public void predraw(float ticks, int mx, int my){
		//
	}

	@Override
	public void postdraw(float ticks, int mx, int my){
		//
	}

	@Override
	public void scrollwheel(int a, int x, int y){
		//
	}

}
