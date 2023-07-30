package net.fexcraft.mod.fvtm.ui;

import static net.fexcraft.mod.fvtm.FvtmRegistry.DECORATIONS;
import static net.fexcraft.mod.fvtm.FvtmRegistry.DECORATION_CATEGORIES;

import java.util.ArrayList;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.mod.fvtm.data.DecorationData;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIButton;
import net.fexcraft.mod.uni.ui.UserInterface;
import net.minecraft.client.resources.I18n;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class DecoEditor extends UserInterface {

	private static final int rows = 12;
	private static ArrayList<DecorationData> results = new ArrayList<>();
	private static ArrayList<String> colors = new ArrayList<>();
	private static boolean listmode = true, search;
	private int scroll0, scroll1;
	protected int selected = -1;
	protected int selcol;
	private int category = 0;
	private String searchstr = "";

	public DecoEditor(JsonMap map, ContainerInterface con) throws Exception {
		super(map, con);
		updateCategorySearch();
		select(-1, -1);
	}

	@Override
	public boolean onAction(UIButton button, String id, int l, int t, int x, int y, int b){
		boolean found = true;
		switch(id){
			case "cat_prev":{
				category--;
				if(category < 0) category = DECORATION_CATEGORIES.size() - 1;
				updateCategorySearch();
				break;
			}
			case "cat_next":{
				category++;
				if(category >= DECORATION_CATEGORIES.size()) category = 0;
				updateCategorySearch();
				break;
			}
			case "search":{
				search = !search;
				updateCategorySearch();
				break;
			}
			case "mode_add":{
				listmode = false;
				updateResults();
				break;
			}
			case "mode_list":{
				listmode = true;
				updateEntries();
				break;
			}
			case "list_up":{
				if(listmode) scroll0--;
				else scroll1--;
				if(scroll0 < 0) scroll0 = 0;
				if(scroll1 < 1) scroll1 = 0;
				updateEntries();
				break;
			}
			case "list_down":{
				if(listmode) scroll0++;
				else scroll1++;
				updateEntries();
				break;
			}
			default:{
				found = false;
				break;
			}
		}
		if(!found){
			if(id.startsWith("entry_")){
				int idx = Integer.parseInt(id.substring(6));
				select(selected = scroll0 + idx, selcol);
				updateEntries();
				return true;
			}
			else if(id.startsWith("add_")){
				int idx = Integer.parseInt(id.substring(4));
				TagCW com = TagCW.create();
				com.set("task", "add");
				com.set("key", results.get(scroll1 + idx).key());
				container.SEND_TO_SERVER.accept(com);
				return true;
			}
			else if(id.startsWith("rem_")){
				int idx = Integer.parseInt(id.substring(4));
				TagCW com = TagCW.create();
				com.set("task", "tem");
				com.set("idx", scroll1 + idx);
				container.SEND_TO_SERVER.accept(com);
				return true;
			}
		}
		return found;
	}

	public void select(int idx, int colidx){
		selected = idx;
		colors.clear();
		int decos = (int)container.get("decos.size");
		DecorationData data = idx < 0 || idx >= decos ? null : (DecorationData)container.get("decos.at", idx);
		boolean miss = data == null;
		for(int i = 0; i < 3; i++){
			fields.get("pos" + i).text(miss ? "0" : (i == 0 ? data.offset.x : i == 1 ? data.offset.y : data.offset.z) + "");
			fields.get("rot" + i).text(miss ? "0" : (i == 0 ? data.rotx : i == 1 ? data.roty : data.rotz) + "");
			fields.get("scl" + i).text(miss ? "0" : (i == 0 ? data.sclx : i == 1 ? data.scly : data.sclz) + "");
		}
		texts.get("texc").value(miss ? "" : data.textures.get(data.seltex).name());
		selcol = colidx;
		if(!miss) colors.addAll(data.getColorChannels().keySet());
		if(selcol >= colors.size() || selcol < 0) selcol = 0;
		texts.get("channel").value(miss ? "" : colors.isEmpty() ? I18n.format("gui.fvtm.decoration_editor.no_color_channels") : colors.get(selcol));
		RGB color = miss || colors.isEmpty() ? RGB.WHITE : data.getColorChannel(colors.get(selcol));
		byte[] ar = color.toByteArray();
		fields.get("rgb").text((ar[0] + 128) + ", " + (ar[1] + 128) + ", " + (ar[2] + 128));
		fields.get("hex").text("#" + Integer.toHexString(color.packed));
	}

	protected void updateCategorySearch(){
		texts.get("cat").value(DECORATION_CATEGORIES.get(category));
		texts.get("cat").visible(!search);
		fields.get("search").visible(search);
		updateResults();
	}

	protected void updateResults(){
		results.clear();
		if(search){
			for(DecorationData deco : DECORATIONS.values()){
				if(deco.key().contains(searchstr) || format(deco.key()).contains(searchstr)) results.add(deco);
			}
		}
		else{
			String cat = DECORATION_CATEGORIES.get(category);
			for(DecorationData deco : DECORATIONS.values()){
				if(deco.category().equals(cat)) results.add(deco);
			}
		}
		updateEntries();
	}

	public void updateEntries(){
		int j = 0;
		boolean over;
		if(listmode){
			for(int i = 0; i < rows; i++){
				j = scroll0 + i;
				over = j >= (int)container.get("decos.size");
				buttons.get("entry_" + i).text.value(over ? "" : format((String)container.get("decos.key", j)));
				buttons.get("rem_" + i).visible(true);
				buttons.get("add_" + i).visible(false);
				buttons.get("entry_" + i).enabled(selected != j);
			}
		}
		else{
			for(int i = 0; i < rows; i++){
				j = scroll1 + i;
				over = j >= results.size();
				buttons.get("entry_" + i).text.value(over ? "" : format(results.get(j).key()));
				buttons.get("rem_" + i).visible(false);
				buttons.get("add_" + i).visible(true);
				buttons.get("entry_" + i).enabled(true);
			}
		}
	}

	private String format(String key){
		return I18n.format("fvtm.decoration." + key);
	}

	@Override
	public boolean onScroll(UIButton button, String id, int gl, int gt, int mx, int my, int am) {
		return false;
	}

}
