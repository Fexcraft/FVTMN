package net.fexcraft.mod.uni.item;

public abstract class ItemWrapper {

	public abstract void linkContainer();

	public abstract void regToDict();

	public abstract <LI> LI local();

	public abstract Object direct();

}
