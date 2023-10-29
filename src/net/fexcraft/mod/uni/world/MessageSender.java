package net.fexcraft.mod.uni.world;

import net.fexcraft.mod.fvtm.FvtmLogger;
import net.fexcraft.mod.uni.EnvInfo;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface MessageSender {

	public void send(String s);

	public void bar(String s);

	public void dismount();

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
