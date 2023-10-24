package net.fexcraft.mod.uni.world;

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

}
