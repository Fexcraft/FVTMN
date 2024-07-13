package net.fexcraft.mod.fvtm.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fvtm.data.root.Colorable;
import net.fexcraft.mod.fvtm.data.root.Textureable;
import net.fexcraft.mod.fvtm.packet.Packet_TagListener;
import net.fexcraft.mod.fvtm.packet.Packets;
import net.fexcraft.mod.fvtm.sys.uni.FvtmWorld;
import net.fexcraft.mod.fvtm.sys.uni.VehicleInstance;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ToolboxTextureContainer extends ContainerInterface {

	protected Textureable textureable;
	protected VehicleInstance vehicle;

	public ToolboxTextureContainer(JsonMap map, UniEntity player, V3I vec){
		super(map, player, vec);
		vehicle = ((FvtmWorld)player.entity.getWorld()).getVehicle(vec.x);
		textureable = vehicle.data.getTexture();
	}

	@Override
	public Object get(String key, Object... objs){
		switch(key){
			case "open_wiki":{
				if(player.entity.getWorld().isClient()){
					player.entity.sendLink(ui.root, "https://fexcraft.net/wiki/mod/fvtm/toolbox#texture");
				}
				return null;
			}
		}
		return null;
	}

	@Override
	public void packet(TagCW com, boolean client){
		String task = com.getString("task");
		switch(task){
			case "select":{
				//
				break;
			}
		}
	}

}
