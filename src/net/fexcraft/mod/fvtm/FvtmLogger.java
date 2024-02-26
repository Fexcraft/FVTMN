package net.fexcraft.mod.fvtm;

import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.world.MessageSender;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class FvtmLogger {

	public static FvtmLogger LOGGER = null;

	protected abstract void log0(Object obj);

	public void info(String s){
		log0(s);
	}

	public static void log(Object o){
		LOGGER.log0(o);
	}

	public static void debug(Object o){
		if(EnvInfo.DEV) LOGGER.log0(o);
	}

	public static final MessageSender LOG = new MessageSender(){

		@Override
		public void send(String s){
			FvtmLogger.LOGGER.log(s);
		}

		@Override
		public void bar(String s){
			FvtmLogger.LOGGER.log(s);
		}

		@Override
		public void dismount(){
			//
		}

	};
	public static final MessageSender DEVLOG = new MessageSender(){

		@Override
		public void send(String s){
			if(EnvInfo.DEV) FvtmLogger.LOGGER.log(s);
		}

		@Override
		public void bar(String s){
			if(EnvInfo.DEV) FvtmLogger.LOGGER.log(s);
		}

		@Override
		public void dismount(){
			//
		}

	};
	public static final MessageSender NONE = new MessageSender(){

		@Override
		public void send(String s){
			//
		}

		@Override
		public void bar(String s){
			//
		}

		@Override
		public void dismount(){
			//
		}

	};
}
