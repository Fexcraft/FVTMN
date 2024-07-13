package net.fexcraft.mod.fvtm.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIButton;
import net.fexcraft.mod.uni.ui.UserInterface;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ToolboxTexture extends UserInterface {

	public ToolboxTexture(JsonMap map, ContainerInterface con) throws Exception{
		super(map, con);
		//
	}

	@Override
	public boolean onAction(UIButton button, String id, int x, int y, int mb){
		switch(id){
			case "prev":{
				//
				break;
			}
			case "next":{
				//
				break;
			}
			case "pack": {
				//
				break;
			}
			case "url":{
				//
				break;
			}
			case "help":{
				container.get("open_wiki");
				break;
			}
			default:{
				break;
			}
		}
		return false;
	}

}
