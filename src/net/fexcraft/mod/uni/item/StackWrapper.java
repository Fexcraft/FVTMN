package net.fexcraft.mod.uni.item;

import net.fexcraft.mod.uni.tag.TagCW;

public abstract class StackWrapper {

	public static StackWrapper EMPTY = null;
	protected ItemWrapper item;

	public StackWrapper(ItemWrapper item){
		this.item = item;
	}

	public abstract <IS> IS local();

	public abstract Object direct();

	public abstract StackWrapper setTag(TagCW tag);

	public abstract TagCW getTag();

	public abstract boolean hasTag();

}
