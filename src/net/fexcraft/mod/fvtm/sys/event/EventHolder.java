package net.fexcraft.mod.fvtm.sys.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class EventHolder {

	public HashMap<EventType, ArrayList<EventListener>> listeners = new HashMap<>();

	public void integrate(EventHolder holder){
		for(Map.Entry<EventType, ArrayList<EventListener>> entry : holder.listeners.entrySet()){
			if(!listeners.containsKey(entry.getKey())) listeners.put(entry.getKey(), new ArrayList<>());
			listeners.get(entry.getKey()).addAll(entry.getValue());
		}
	}

	public void deintegrate(EventHolder holder){
		ArrayList<EventListener> lis;
		for(Map.Entry<EventType, ArrayList<EventListener>> entry : holder.listeners.entrySet()){
			lis = listeners.get(entry.getKey());
			if(lis == null) continue;
			lis.removeAll(entry.getValue());
		}
		listeners.entrySet().removeIf(entry -> entry.getValue().isEmpty());
	}

	public void insert(EventListener lis){
		if(!listeners.containsKey(lis.type)) listeners.put(lis.type, new ArrayList<>());
	}

}
