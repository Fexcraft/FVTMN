package net.fexcraft.mod.fvtm.ui.road;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIButton;
import net.fexcraft.mod.uni.ui.UserInterface;

import java.util.List;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class RoadToolCustomUI extends UserInterface {

	protected RoadToolCustomCon rtc;
	protected int[] size = new int[]{ 1, 0, 0, 0, 0, 0 };
	protected boolean sscr;

	public RoadToolCustomUI(JsonMap map, ContainerInterface con) throws Exception{
		super(map, con);
		rtc = (RoadToolCustomCon)con;
		sscr = rtc.size[0] > 8;
		tabs.get("left").visible(sscr);
		tabs.get("right").visible(sscr);
		tabs.get("full0").visible(sscr);
		tabs.get("full1").visible(sscr);
		tabs.get("full2").visible(sscr);
		tabs.get("scroll").visible(sscr);
		buttons.get("scroll_left").visible(sscr);
		buttons.get("scroll_right").visible(sscr);
	}

	@Override
	public void predraw(float ticks, int mx, int my){

	}

	@Override
	public void drawbackground(float ticks, int mx, int my){
		if(!sscr){
			int left = gLeft + 88 - rtc.offset;
			drawer.draw(left - 7, gTop, 0, 0, 25, 32);
			if(rtc.size[0] == 1){
				drawer.draw(left + 18, gTop, 61, 0, 7, 32);
			}
			else{
				for(int i = 1; i < rtc.size[0] - 1; i++){
					drawer.draw(left + i * 18, gTop, 25, 0, 18, 32);
				}
				drawer.draw(left + (rtc.size[0] - 1) * 18, gTop, 43, 0, 25, 32);
			}
		}
	}

	@Override
	public boolean onAction(UIButton button, String id, int x, int y, int b){
		switch(id){
			case "scroll_left":{
				TagCW com = TagCW.create();
				com.set("cargo", "scroll");
				com.set("by", -1);
				container.SEND_TO_SERVER.accept(com);
				return true;
			}
			case "scroll_right":{
				TagCW com = TagCW.create();
				com.set("cargo", "scroll");
				com.set("by", 1);
				container.SEND_TO_SERVER.accept(com);
				return true;
			}
		}
		return false;
	}

	@Override
	public void getTooltip(int mx, int my, List<String> list){
		//
	}

}
