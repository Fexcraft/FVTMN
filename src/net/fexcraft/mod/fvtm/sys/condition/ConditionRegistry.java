package net.fexcraft.mod.fvtm.sys.condition;

import java.util.HashMap;
import java.util.function.Function;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ConditionRegistry {
	
	public static HashMap<String, Condition> CONDITIONS = new HashMap<>();
	private static HashMap<String, Conditional> CONDITIONALS = new HashMap<>();
	public static Function<Condition, Conditional> BUILDER;
	
	public static void register(Condition con){
		CONDITIONS.put(con.id, con);
	}
	
	public static Conditional get(String id){
		if(!CONDITIONALS.containsKey(id)){
			Condition con = CONDITIONS.get(id);
			if(con == null) return null;
			CONDITIONALS.put(con.id, BUILDER.apply(con));
		}
		return CONDITIONALS.get(id);
	}

}
