package net.fexcraft.mod.uni.ui;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonValue;
import net.fexcraft.mod.uni.IDL;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class UserInterface {

	public static Class<? extends UserInterface> IMPLEMENTATION;
	//
	public ContainerInterface container;
	public LinkedHashMap<String, UIText> texts = new LinkedHashMap<>();
	public LinkedHashMap<String, UIButton> buttons = new LinkedHashMap<>();
	public LinkedHashMap<String, UIField> fields = new LinkedHashMap<>();
	public LinkedHashMap<String, UITab> tabs = new LinkedHashMap<>();
	public boolean background;
	public int width, height;

	public UserInterface(JsonMap map, ContainerInterface container) throws Exception {
		this.container = container;
		if(map.has("texts")){
			for(Entry<String, JsonValue<?>> entry : map.getMap("texts").entries()){
				texts.put(entry.getKey(), UIText.IMPLEMENTATION.getConstructor(UserInterface.class, JsonMap.class).newInstance(this, entry.getValue()));
			}
		}
		if(map.has("buttons")){
			for(Entry<String, JsonValue<?>> entry : map.getMap("buttons").entries()){
				buttons.put(entry.getKey(), UIButton.IMPLEMENTATION.getConstructor(UserInterface.class, JsonMap.class).newInstance(this, entry.getValue()));
			}
		}
		if(map.has("fields")){
			for(Entry<String, JsonValue<?>> entry : map.getMap("fields").entries()){
				fields.put(entry.getKey(), UIField.IMPLEMENTATION.getConstructor(UserInterface.class, JsonMap.class).newInstance(this, entry.getValue()));
			}
		}
		if(map.has("tabs")){
			for(Entry<String, JsonValue<?>> entry : map.getMap("tabs").entries()){
				tabs.put(entry.getKey(), UITab.IMPLEMENTATION.getConstructor(UserInterface.class, JsonMap.class).newInstance(this, entry.getValue()));
			}
		}
		else{
			UITab main = UITab.IMPLEMENTATION.getConstructor(UserInterface.class, JsonMap.class).newInstance(this, map);
			main.texts.addAll(texts.values());
			main.buttons.addAll(buttons.values());
			main.fields.addAll(fields.values());
			tabs.put("main", main);
		}
		background = map.getBoolean("background", true);
		JsonArray arr = map.getArray("size");
		width = arr.get(0).integer_value();
		height = arr.get(1).integer_value();
	}

	public boolean onClick(int gl, int gt, int mx, int my, int mb){
		UIButton button = null;
		for(Entry<String, UIButton> entry : buttons.entrySet()){
			button = entry.getValue();
			if(!button.visible || !button.enabled) continue;
			if(!button.hovered(gl, gt, mx, my)) continue;
			return button.onclick(gl, gt, mx, my, mb) || onAction(button, entry.getKey(), gl, gt, mx, my, mb);
		}
		UIField field = null;
		for(Entry<String, UIField> entry : fields.entrySet()){
			field = entry.getValue();
			if(!field.visible /*|| !field.enabled*/) continue;
			if(field.hovered(gl, gt, mx, my) && field.onclick(mx, my, mb)) return true;
		}
		return false;
	}

	public abstract boolean onAction(UIButton button, String id, int l, int t, int x, int y, int b);

	public abstract boolean onScroll(UIButton button, String id, int gl, int gt, int mx, int my, int am);

	public abstract void bindTexture(IDL texture);

}
