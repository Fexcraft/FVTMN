package net.fexcraft.mod.fvtm;

import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.world.MessageSender;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class FvtmLogger {

	public static FvtmLogger LOGGER = null;

	public abstract void log(Object obj);

	public abstract void log(String str);

	public void info(Object obj){
		log(obj);
	}

	public void debug(String s){
		if(EnvInfo.DEV) log(s);
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
