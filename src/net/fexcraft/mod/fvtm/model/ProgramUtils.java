package net.fexcraft.mod.fvtm.model;

import java.util.function.Function;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ProgramUtils {

	public static final Function<ModelRenderData, Float> FLOAT_SUPP = mrd -> 0f;
	public static final Function<ModelRenderData, Float[]> FLOAT2_SUPP = mrd -> new Float[]{ 0f, 0f };

}
