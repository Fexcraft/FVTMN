package net.fexcraft.mod.fvtm.data;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.tag.TagCW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class ContentData<TYPE extends Content<TYPE>, SELF> {

	protected TYPE type;

	public ContentData(TYPE type){
		this.type = type;
	}

	public TYPE getType(){
		return type;
	}

	public ContentType getContentType(){
		return type.getContentType();
	}

	public abstract TagCW write(TagCW compound);

	public abstract SELF read(TagCW compound);

	public abstract SELF parse(JsonMap obj);

	public abstract JsonMap toJson();

}
