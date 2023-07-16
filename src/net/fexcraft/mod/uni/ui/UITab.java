package net.fexcraft.mod.uni.ui;

import java.util.ArrayList;

import net.fexcraft.app.json.JsonMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class UITab extends UIButton{

	public static Class<UITab> IMPLEMENTATION;
	public ArrayList<UIText> texts = new ArrayList<>();
	public ArrayList<UIButton> buttons = new ArrayList<>();
	public ArrayList<UIField> fields = new ArrayList<>();

	public UITab(UserInterface ui, JsonMap map) throws Exception {
		super(ui, map);
		if(map.has("buttons")){
			map.getArray("buttons").value.forEach(json -> {
				buttons.add(ui.buttons.get(json.string_value()));
			});
		}
		if(map.has("texts")){
			map.getArray("texts").value.forEach(json -> {
				texts.add(ui.texts.get(json.string_value()));
			});
		}
		if(map.has("fields")){
			map.getArray("fields").value.forEach(json -> {
				fields.add(ui.fields.get(json.string_value()));
			});
		}
	}

}
