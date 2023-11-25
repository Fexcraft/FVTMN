package net.fexcraft.mod.uni.world;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;

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

	public abstract boolean isLiving();

	public abstract boolean isRiding();

	public abstract String getRegName();

	public abstract <E> E local();

	public abstract Object direct();

	public abstract V3D getPos();

	public abstract void decreaseXZMotion(double x);

	public abstract void setYawPitch(float oyaw, float opitch, float yaw, float pitch);

    public abstract void openUI(int ui, WorldW world, V3I pos);

    public abstract String getName();
}
