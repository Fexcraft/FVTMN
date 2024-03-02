package net.fexcraft.mod.fvtm.data.block;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fvtm.data.Content;
import net.fexcraft.mod.fvtm.data.ContentType;
import net.fexcraft.mod.fvtm.data.root.ItemTextureable;
import net.fexcraft.mod.fvtm.data.root.WithItem;
import net.fexcraft.mod.fvtm.util.ContentConfigUtil;
import net.fexcraft.mod.uni.IDL;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class MultiBlock extends Content<MultiBlock> implements WithItem, ItemTextureable {

	private String ctab;
	private IDL itemtexloc;
	private boolean no3ditem;

	@Override
	public MultiBlock parse(JsonMap map){
		if((pack = ContentConfigUtil.getAddon(map)) == null) return null;
		if((id = ContentConfigUtil.getID(pack, map)) == null) return null;
		//
		name = map.getString("Name", "Unnamed Block");
		description = ContentConfigUtil.getStringList(map, "Description");
		ctab = map.getString("CreativeTab", "default");
		itemtexloc = ContentConfigUtil.getItemTexture(id, getContentType(), map);
		no3ditem = map.getBoolean("Disable3DItemModel", false);
		return this;
	}

	@Override
	public ContentType getContentType(){
		return ContentType.MULTIBLOCK;
	}

	@Override
	public Class<?> getDataClass(){
		return null;
	}

	@Override
	public IDL getItemTexture(){
		return itemtexloc;
	}

	@Override
	public String getItemContainer(){
		return null;
	}

	@Override
	public String getCreativeTab(){
		return ctab;
	}

	@Override
	public boolean noCustomItemModel(){
		return no3ditem;
	}

}
