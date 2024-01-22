package net.fexcraft.mod.uni.world;

import net.fexcraft.lib.common.math.V3I;
import net.minecraft.util.EnumFacing;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class WrapperHolder {

	//public static final ConcurrentHashMap<Object, PlayerW> PLAYERS = new ConcurrentHashMap<>();
	//public static final ConcurrentHashMap<Object, EntityW> ENTITIES = new ConcurrentHashMap<>();
	public static final ConcurrentHashMap<Object, WorldW> WORLDS = new ConcurrentHashMap<>();
	public static WrapperHolder INSTANCE;

	/*public static PlayerW getPlayer(Object obj){
		return INSTANCE.getPlayer0(obj);
	}

	public abstract PlayerW getPlayer0(Object o);*/

	public static EntityW getEntity(Object obj){
		return INSTANCE.getEntity0(obj);
	}

	public abstract EntityW getEntity0(Object o);

	public static WorldW getWorld(Object obj){
		return INSTANCE.getWorld0(obj);
	}

	public abstract WorldW getWorld0(Object o);

	public static V3I getPos(Object obj){
		return INSTANCE.getPos0(obj);
	}

	public abstract V3I getPos0(Object o);

	public static BlockSide getSide(Object o){
		return INSTANCE.getSide0(o);
	}

	public abstract BlockSide getSide0(Object o);

}
