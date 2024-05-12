package net.fexcraft.mod.fvtm.function.part;

import net.fexcraft.app.json.FJson;
import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.mod.fvtm.data.part.Part;
import net.fexcraft.mod.fvtm.data.part.PartFunction;
import net.fexcraft.mod.fvtm.data.part.PartFunction.StaticFunction;
import net.fexcraft.mod.fvtm.util.ContentConfigUtil;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ConnectorFunction extends StaticFunction {

	private V3D offset = V3D.NULL;

	@Override
	public PartFunction init(Part part, FJson json){
		offset = ContentConfigUtil.getVector(json.asArray(), 0);
		return this;
	}

	@Override
	public String getId(){
		return "fvtm:connector";
	}

	public V3D getOffset(){
		return offset;
	}

}
