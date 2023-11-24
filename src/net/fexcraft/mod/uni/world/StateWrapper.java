package net.fexcraft.mod.uni.world;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.EnumFacing;

import java.util.function.Function;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class StateWrapper {

    public static Function<Object, StateWrapper> GETTER = null;

    public abstract Object getBlock();

    public abstract <S> S local();

    public abstract Object direct();

    public abstract <V> V getValue(Object prop);

}
