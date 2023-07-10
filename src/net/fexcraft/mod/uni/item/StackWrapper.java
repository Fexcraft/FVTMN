package net.fexcraft.mod.uni.item;

public abstract class StackWrapper {

	public static StackWrapper EMPTY = null;
	protected ItemWrapper item;

	public StackWrapper(ItemWrapper item){
		this.item = item;
	}

	public abstract <IS> IS local();

	public abstract Object direct();

}
