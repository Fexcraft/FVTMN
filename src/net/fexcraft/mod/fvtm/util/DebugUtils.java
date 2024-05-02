package net.fexcraft.mod.fvtm.util;

import net.fexcraft.lib.frl.Polyhedron;
import net.fexcraft.lib.tmt.ModelRendererTurbo;

public class DebugUtils {

	public static boolean ACTIVE = false;
	//
	public static Polyhedron SPHERE = new Polyhedron()
		.importMRT(new ModelRendererTurbo(null, 0, 0, 1, 1)
			.addSphere(0, 0, 0, 1, 16, 16, 1, 1), false, 1f);

}
