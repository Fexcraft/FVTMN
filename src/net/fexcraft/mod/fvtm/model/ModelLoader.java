package net.fexcraft.mod.fvtm.model;

import java.util.function.Supplier;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface ModelLoader {

	public boolean accepts(String name, String suffix);

	/**
	 * @param name     the model address/resourcelocation
	 * @param confdata existing model data from config
	 * @param model    new model instance if necessary (usually only unused by the class model loader)
	 * @return the model, with optionally a (updated or overridden) ModelData object on 2nd index
	 */
	public Object[] load(String name, ModelData confdata, Supplier<Model> model) throws Exception;

}
