package net.fexcraft.mod.fvtm.model.program;

import net.fexcraft.lib.common.math.Time;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.fvtm.FvtmLogger;
import net.fexcraft.mod.fvtm.model.Program;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static net.fexcraft.mod.fvtm.Config.BLINKER_INTERVAL;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class DefaultPrograms {

	public static boolean BLINKER_TOGGLE;
	public static Timer BLINKER_TIMER;

	public static void init(){
		//
	}

	public static void setupBlinkerTimer(){
		if(BLINKER_TIMER != null) BLINKER_TIMER.cancel();
		FvtmLogger.debug("Setting up blinker-toggle timer.");
		LocalDateTime midnight = LocalDateTime.of(LocalDate.now(ZoneOffset.systemDefault()), LocalTime.MIDNIGHT);
		long mid = midnight.toInstant(ZoneOffset.UTC).toEpochMilli(); long date = Time.getDate();
		while((mid += BLINKER_INTERVAL) < date);
		(BLINKER_TIMER = new Timer()).schedule(new TimerTask(){
			@Override
			public void run(){
				BLINKER_TOGGLE = !BLINKER_TOGGLE;
			}
		}, new Date(mid), BLINKER_INTERVAL);
	}

}
