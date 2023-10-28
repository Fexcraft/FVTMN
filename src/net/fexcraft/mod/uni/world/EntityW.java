package net.fexcraft.mod.uni.world;

import net.fexcraft.lib.common.math.V3D;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class EntityW implements MessageSender {

	public abstract boolean isOnClient();

	public abstract int getId();

	public abstract WorldW getWorld();

	public abstract boolean isPlayer();

	public abstract boolean isAnimal();

	public abstract boolean isHostile();

	public abstract String getRegName();

	public abstract void decreaseXZMotion(double x);

	public abstract <E> E local();

	public abstract Object direct();

	public abstract V3D getPos();

}
