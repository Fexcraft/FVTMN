package net.fexcraft.mod.fvtm.sys.event;

import net.fexcraft.mod.fvtm.model.ModelRenderData;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class EventAction {

	public static LinkedHashMap<String, EventAction> ACTIONS = new LinkedHashMap<>();
	public static EventAction NONE = new EventAction("none");
	public static EventAction SET_ATTR = new EventAction("set_attr");
	public static EventAction RESET_ATTR = new EventAction("reset_attr");

	public final String key;
	private BiConsumer<ModelRenderData, EventListener> consumer;

	private EventAction(String ky){
		key = ky;
		ACTIONS.put(key, this);
	}

	public EventAction set(BiConsumer<ModelRenderData, EventListener> cons){
		consumer = cons;
		return this;
	}

	public static EventAction parse(String type){
		for(EventAction et : ACTIONS.values()){
			if(et.key.equals(type)) return et;
		}
		return NONE;
	}

	public void run(ModelRenderData data, EventListener lis){
		consumer.accept(data, lis);
	}

}
