package l1j.server.MJWebServer.Dispatcher.my.service.chat;

import java.util.concurrent.CopyOnWriteArrayList;

import l1j.server.Config;
import l1j.server.server.IdFactory;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJMyChatCommandInstance extends L1PcInstance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final MJMyChatCommandInstance instance = new MJMyChatCommandInstance();
	public static MJMyChatCommandInstance instance(){
		return instance;
	}
	
	private final CopyOnWriteArrayList<MJMyChatCommandListener> listeners = new CopyOnWriteArrayList<>(); 
	private MJMyChatCommandInstance(){
		setId(IdFactory.getInstance().nextId());
	}
	
	public void addCommandListener(MJMyChatCommandListener listener){
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}
	}
	
	public void removeCommandListener(MJMyChatCommandListener listener){
		listeners.remove(listener);
	}
	
	@Override
	public boolean isGm() {
		return true;
	}
	
	@Override
	public short getAccessLevel(){
		return (short) Config.ServerAdSetting.GMCODE;
	}
	
	@Override
	public String getName(){
		return "웹관리자";
	}
	
	@Override
	public void sendPackets(String s){
		for(MJMyChatCommandListener listener : listeners){
			listener.appendCommandResult(s);
		}
	}
	
	public void onCommandFlush(){
		for(MJMyChatCommandListener listener : listeners){
			listener.onCommandFlush();
		}
	}
	
	public interface MJMyChatCommandListener{
		public void appendCommandResult(String s);
		public void onCommandFlush();
	}
}
