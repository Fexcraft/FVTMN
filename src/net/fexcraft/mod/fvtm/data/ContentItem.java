package net.fexcraft.mod.fvtm.data;

import javax.annotation.Nullable;

import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface ContentItem<TYPE extends Content<TYPE>, DATA> {

	public TYPE getContent();

	public ContentType getType();

	@Nullable
	public ContentData<TYPE, DATA> getData(StackWrapper stack);

	@Nullable
	public ContentData<TYPE, DATA> getData(TagCW compound);

}
