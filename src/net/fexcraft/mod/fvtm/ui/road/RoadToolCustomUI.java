package net.fexcraft.mod.fvtm.ui.road;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.mod.fvtm.FvtmLogger;
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
	protected boolean sscr;
	protected float seg;

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
		seg = 162f / rtc.size[0];
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
		else{
			float b = seg * rtc.scroll;
			float w = seg * 9;
			drawer.draw(gLeft + 7 + b, gTop - 18, 7, 151, (int)w, 6);
			drawer.draw(gLeft + 3.5f + b + (w / 2), gTop - 24, 177, 132, 7, 7);
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
				if(--rtc.scroll < 0) rtc.scroll = 0;
				return true;
			}
			case "scroll_right":{
				TagCW com = TagCW.create();
				com.set("cargo", "scroll");
				com.set("by", 1);
				container.SEND_TO_SERVER.accept(com);
				if(++rtc.scroll + 9 >= rtc.size[0]) rtc.scroll = rtc.size[0] - 9;
				return true;
			}
		}
		return false;
	}

	@Override
	public void getTooltip(int mx, int my, List<String> list){
		if(rtc.size[0] < 9) return;
		if(tabs.get("scroll").hovered(gLeft, gTop, mx, my)){
			Object[] objs = new Object[]{ rtc.scroll + 1, rtc.scroll + 9, rtc.size[0] };
			list.add(Formatter.format(container.TRANSFORMAT.apply("ui.fvtm.road_tool_custom.scroll_status", objs)));
		}
	}


}
