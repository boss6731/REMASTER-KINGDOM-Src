package l1j.server.MJWebServer.Dispatcher.my.page.command;

import l1j.server.Config;
import l1j.server.MJWebServer.Dispatcher.my.page.command.MJMyPageGmCommandModel.MyGmCommandDataModel;
import l1j.server.server.IdFactory;
import l1j.server.server.model.Instance.L1PcInstance;

class MJGmCommandDelegate extends L1PcInstance{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int ID_HOLDER = IdFactory.getInstance().nextId();
	private MJMyPageGmCommandModel commandModel;
	MJGmCommandDelegate(MJMyPageGmCommandModel commandModel){
		this.commandModel = commandModel;
		setId(ID_HOLDER);
	}
	
	@Override
	public boolean isGm(){
		return true;
	}
	
	@Override
	public short getAccessLevel(){
		return (short) Config.ServerAdSetting.GMCODE;
	}
	
	@Override
	public String getName(){
		return "웹명령어대리자";
	}
	
	@Override
	public void sendPackets(String s){
		commandModel.dataItems.add(new MyGmCommandDataModel("desc", "메시지", s));
	}
}
