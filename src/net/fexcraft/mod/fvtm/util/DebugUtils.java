package net.fexcraft.mod.fvtm.util;

import net.fexcraft.lib.common.math.Vec3f;
import net.fexcraft.lib.frl.Polyhedron;
import net.fexcraft.lib.tmt.ModelRendererTurbo;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class DebugUtils {

	public static boolean ACTIVE = false;
	public static Vec3f CYNCOLOR = new Vec3f(0, 1, 1);
	public static Vec3f REDCOLOR = new Vec3f(1, 0, 0);
	public static Vec3f GRNCOLOR = new Vec3f(0, 1, 0);
	public static Vec3f YLWCOLOR = new Vec3f(1, 1, 0);
	public static Vec3f GRYCOLOR = new Vec3f(.8, .8, .8);
	public static Vec3f SEATCOLOR = new Vec3f(1, 1, 0);
	//
	public static Polyhedron SPHERE = new Polyhedron()
		.importMRT(new ModelRendererTurbo(null, 0, 0, 1, 1)
			.addSphere(0, 0, 0, 1, 16, 16, 1, 1), false, 1f);

}
