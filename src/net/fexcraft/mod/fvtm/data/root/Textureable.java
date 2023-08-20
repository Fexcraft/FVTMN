package net.fexcraft.mod.fvtm.data.root;

import java.util.List;

import net.fexcraft.mod.fvtm.FvtmResources;
import net.fexcraft.mod.fvtm.util.ExternalTextureLoader;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;
import net.fexcraft.mod.uni.tag.TagCW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Textureable {
	
	private IDL current;
	private String custom = "";
	private boolean external;
	private int selected;
	
	public Textureable(TextureHolder holder){
		current = holder.getDefaultTextures().get(0);
	}
	
	public IDL getTexture(){
		return current;
	}
	
	public int getSelected(){
		return selected;
	}
	
	public boolean isExternal(){
		return external;
	}

	public String getCustom(){
		return custom;
	}
	
	public void setSelectedTexture(TextureHolder holder, int idx, String tex, boolean ext){
		if(idx < 0){
			external = ext;
			selected = -1;
			custom = tex;
			current = ext ? FvtmResources.INSTANCE.getExternalTexture(custom) : IDLManager.getIDLNamed(custom);
		}
		else{
			external = false;
			if(idx > holder.getDefaultTextures().size()){
				current = holder.getDefaultTextures().get(selected = holder.getDefaultTextures().size() - 1);
			}
			else{
				current = holder.getDefaultTextures().get(selected = idx);
			}
		}
	}
	
	public static interface TextureHolder {
		
		public List<IDL> getDefaultTextures();
		
	}
	
	public static interface TextureUser {
		
		public Textureable getTexture();
		
		public TextureHolder getTexHolder();
		
		public default IDL getCurrentTexture(){
			return getTexture().current;
		}
		
		public default int getSelectedTexture(){
			return getTexture().selected;
		}
		
		public default boolean isTextureExternal(){
			return getTexture().external;
		}
		
		public default String getCustomTexture(){
			return getTexture().custom;
		}
		
	}

	public void save(TagCW compound){
		compound.set("SelectedTexture", selected);
		compound.set("ExternalTexture", external);
		compound.set("CurrentTexture", external ? current.path() : current.toString());
	}

	public void load(TagCW compound, TextureHolder holder){
		selected = compound.getInteger("SelectedTexture");
		external = compound.getBoolean("ExternalTexture");
		if(selected < 0) selected = -1;
		if(selected >= holder.getDefaultTextures().size()) selected = holder.getDefaultTextures().size() - 1;
		if(!compound.has("CurrentTexture")){
			custom = compound.getString("CustomTexture");
			if(selected < 0) current = external ? ExternalTextureLoader.get(custom) : IDLManager.getIDLNamed(custom);
			else current = holder.getDefaultTextures().get(selected > holder.getDefaultTextures().size() ? 0 : selected);
		}
		else{
			String str = compound.getString("CurrentTexture");
			if(selected < 0){
				current = external ? ExternalTextureLoader.get(str) : IDLManager.getIDLNamed(custom);
				custom = external ? str : current.toString();
			}
			else{
				current = holder.getDefaultTextures().get(selected);
				custom = "";
			}
		}
	}

}
