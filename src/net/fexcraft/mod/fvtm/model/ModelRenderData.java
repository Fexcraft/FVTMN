package net.fexcraft.mod.fvtm.model;

import java.util.ArrayList;

import net.fexcraft.mod.fvtm.data.DecorationData;
import net.fexcraft.mod.fvtm.data.block.BlockData;
import net.fexcraft.mod.fvtm.data.container.ContainerData;
import net.fexcraft.mod.fvtm.data.part.PartData;
import net.fexcraft.mod.fvtm.data.root.Colorable;
import net.fexcraft.mod.fvtm.data.root.RenderCache;
import net.fexcraft.mod.fvtm.data.root.Textureable.TextureUser;
import net.fexcraft.mod.fvtm.data.vehicle.VehicleData;
import net.fexcraft.mod.fvtm.item.ClothItem;
import net.fexcraft.mod.fvtm.sys.tsign.TrafficSignData.CompDataRoot;

/**
 * @author Ferdinand Calo' (FEX___96)
 * Reusable object for rendering.
 */
public class ModelRenderData {

	public Object entity;
	public Object tile;
	public Object blockstate;

	public VehicleData vehicle;
	public ContainerData container;
	public BlockData block;
	public Colorable color;
	public TextureUser texture;
	public PartData part;
	public String part_category;
	public DecorationData decoration;
	public CompDataRoot trafficsign_compdata;
	public Object cloth_item;
	public ArrayList<String> cloth_groups;
	public boolean itemrender;
	public boolean separaterender;

	public RenderCache cache;

	public ModelRenderData set(VehicleData data, Object ent, RenderCache renca, boolean item) {
		entity = ent;
		vehicle = data;
		color = data;
		texture = data;
		part = null;
		part_category = null;
		cache = renca;
		itemrender = item;
		separaterender = false;
		return this;
	}


	public ModelRenderData set(VehicleData data, Object ent, RenderCache renca, PartData partdata, String key, boolean item) {
		entity = ent;
		vehicle = data;
		color = data;
		texture = partdata;
		part = partdata;
		part_category = key;
		cache = renca;
		itemrender = item;
		separaterender = false;
		return this;
	}


	public ModelRenderData set(ContainerData data, Object tileent, RenderCache renca, boolean item) {
		container = data;
		tile = tileent;
		cache = renca;
		color = data;
		texture = data;
		itemrender = item;
		return this;
	}


	public ModelRenderData set(BlockData data, Object tileent, RenderCache renca, Object state, boolean item) {
		entity = null;
		block = data;
		tile = tileent;
		cache = renca;
		color = data;
		texture = data;
		itemrender = item;
		blockstate = state;
		separaterender = false;
		return this;
	}


	public ModelRenderData set(DecorationData data, Object ent, RenderCache renca) {
		decoration = data;
		entity = ent;
		cache = renca;
		color = data;
		itemrender = false;
		return this;
	}


	public ModelRenderData set(ClothItem item, ArrayList<String> list, Object ent, RenderCache renca) {
		cloth_item = item;
		cloth_groups = list;
		entity = ent;
		cache = renca;
		itemrender = false;
		return this;
	}


	public ModelRenderData set(CompDataRoot comp) {
		trafficsign_compdata = comp;
		itemrender = false;
		return this;
	}

	public ModelRenderData clear() {
		entity = null;
		vehicle = null;
		color = null;
		texture = null;
		part = null;
		part_category = null;
		cache = null;
		itemrender = false;
		separaterender = false;
		return this;
	}

	public ModelRenderData sep() {
		separaterender = true;
		return this;
	}

}
