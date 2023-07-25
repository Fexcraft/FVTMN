package net.fexcraft.mod.fvtm.sys.condition;

import net.fexcraft.mod.fvtm.model.ModelRenderData;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@FunctionalInterface
public interface Conditional {

	boolean isMet(ModelRenderData data);

}
