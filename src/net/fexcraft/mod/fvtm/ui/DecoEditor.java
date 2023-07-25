package net.fexcraft.mod.fvtm.ui;

import static net.fexcraft.mod.fvtm.FvtmRegistry.DECORATIONS;
import static net.fexcraft.mod.fvtm.FvtmRegistry.DECORATION_CATEGORIES;

import java.util.ArrayList;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fvtm.data.DecorationData;
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
	protected int selected = -1, selcol;
	private int category = 0;
	private String searchstr = "";

	public DecoEditor(JsonMap map, ContainerInterface con) throws Exception {
		super(map, con);
	}

	@Override
	public boolean onAction(UIButton button, String id, int l, int t, int x, int y, int b){
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
			default: return false;
		}
		return true;
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

	protected void updateEntries(){
		int j = 0;
		boolean over;
		if(listmode){
			for(int i = 0; i < rows; i++){
				j = scroll0 + i;
				over = j >= (int)container.get("decos.size");
				texts.get("entry" + i).value(over ? "" : format((String)container.get("decos.key", j)));
				buttons.get("l_entry_rem" + i).visible(true);
				buttons.get("l_entry_add" + i).visible(false);
				buttons.get("l_entry" + i).enabled(selected != j);
			}
		}
		else{
			for(int i = 0; i < rows; i++){
				j = scroll1 + i;
				over = j >= results.size();
				texts.get("entry" + i).value(over ? "" : format(results.get(j).key()));
				buttons.get("l_entry_rem" + i).visible(false);
				buttons.get("l_entry_add" + i).visible(true);
				buttons.get("l_entry" + i).enabled(true);
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
