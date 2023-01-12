package net.fexcraft.mod.fvtm.data.cloth;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.mc.utils.Static;
import net.fexcraft.mod.fvtm.data.root.DataType;
import net.fexcraft.mod.fvtm.data.root.ItemTextureable;
import net.fexcraft.mod.fvtm.data.root.Model;
import net.fexcraft.mod.fvtm.data.root.Model.ModelData;
import net.fexcraft.mod.fvtm.data.root.Registrable;
import net.fexcraft.mod.fvtm.data.root.Tabbed;
import net.fexcraft.mod.fvtm.util.DataUtil;
import net.fexcraft.mod.fvtm.util.Resources;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;
import net.fexcraft.mod.uni.item.ItemWrapper;
import net.fexcraft.mod.uni.item.StackWrapper;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Cloth extends Registrable<Cloth> implements Tabbed, ItemTextureable {
	
	protected short maxHealth;
	protected ItemWrapper item;
	protected String ctab, modelid;
	protected ClothSlot eq_slot;
	protected ClothMaterial material;
	protected Model model;
	protected ModelData modeldata;
	protected IDL texture, itemloc;
	
	public Cloth(){}

	@Override
	public Cloth parse(JsonMap map){
		id = DataUtil.getID(map);
		if(id == null) return null;
		pack = DataUtil.getAddon(map);
		if(pack == null) return null;
		//
		name = map.getString("Name", "Unnamed Clothing");
		description = DataUtil.getStringArray(map, "Description", true);
		maxHealth = (short)map.getInteger("MaxItemDamage", 0);
		eq_slot = ClothSlot.parse(map.getString("EquipmentSlot", "head"));
		material = ClothMaterial.parse(map);
		//
		texture = IDLManager.getIDLCached(map.getString("Texture", Resources.NULL_TEXTURE.toString()));
		if(!Static.isServer()){
			modelid = map.getString("Model", null);
			modeldata = DataUtil.getModelData(map);
		}
        ctab = map.getString("CreativeTab", "default");
        itemloc = DataUtil.getItemTexture(id, getDataType(), map);
        //TODO cloth item
		//TODO type event
		return this;
	}

	@Override
	public DataType getDataType(){
		return DataType.MATERIAL;
	}

	@Override
	public Class<?> getDataClass(){
		return null;
	}
	
	@Override
	public ItemWrapper getItem(){
		return item;
	}

	@Override
	public StackWrapper getStack(){
		//TODO stacks
		return null;
	}

	public int getMaxDamage(){
		return this.maxHealth;
	}

	@Override
	public String getCreativeTab(){
		return ctab;
	}

	public ClothSlot getClothSlot(){
		return eq_slot;
	}

	public ClothMaterial getArMaterial(){
		return material;
	}

	public Model getModel(){
		return model;
	}
	
	@Override
	public void loadModel(){
		//TODO models this.model = (ClothModel)Resources.getModel(modelid, modeldata, ClothModel.class);
	}
	
	public IDL getTexture(){
		return texture;
	}

	@Override
	public IDL getItemTexture(){
		return itemloc;
	}

}
