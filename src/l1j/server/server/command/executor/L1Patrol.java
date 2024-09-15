package l1j.server.server.server.command.executor;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand.SC_LIST_USER_NOTI;
import l1j.server.MJTemplate.MJString;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;

public class L1Patrol implements L1CommandExecutor {

	private L1Patrol() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Patrol();
	}

	@Override
	public void execute(L1PcInstance gm, String cmdName, String arg) {
		/*boolean begin = true;
		SC_LIST_USER_NOTI noti = SC_LIST_USER_NOTI.newInstance();
		Collection<L1PcInstance> collection = L1World.getInstance().getAllPlayers();
		int count = collection.size();
		for(L1PcInstance pc : collection) {
			SC_LIST_USER_NOTI.Info info = SC_LIST_USER_NOTI.Info.newInstance();
			info.set_account(pc.getAccountName().getBytes());
			info.set_game_class(pc.getType());
			if(pc.getNetConnection() != null) {
				info.set_ip(pc.getNetConnection().getIpBigEndian());
			}else {
				info.set_ip(0);				
			}
			info.set_level(pc.getLevel());
			info.set_name(pc.getName().getBytes());
			
			String clanname = pc.getClanname();
			if(!MJString.isNullOrEmpty(clanname)) {
				info.set_pledge(clanname.getBytes());				
			}else {
				info.set_pledge(new byte[0]);
			}
			--count;
			noti.add_infos(info);
			if(noti.get_infos().size() >= 100) {
				noti.set_begin(begin);
				noti.set_last(count <= 0);	
				System.out.println(count);
				gm.sendPackets(noti, MJEProtoMessages.SC_LIST_USER_NOTI);
				 noti = SC_LIST_USER_NOTI.newInstance();
				 begin = false;
			}
		}
		if(noti.get_infos().size() > 0) {
			System.out.println(count);
			noti.set_begin(begin);
			noti.set_last(true);	
			gm.sendPackets(noti, MJEProtoMessages.SC_LIST_USER_NOTI);
		}*/


		SC_LIST_USER_NOTI noti = SC_LIST_USER_NOTI.newInstance();
		noti.set_begin(true);
		noti.set_last(true);	
		for(L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			SC_LIST_USER_NOTI.Info info = SC_LIST_USER_NOTI.Info.newInstance();
			info.set_account(pc.getAccountName().getBytes());
			info.set_game_class(pc.getType());
			if(pc.getNetConnection() != null) {
				info.set_ip(pc.getNetConnection().getIpBigEndian());
			}else {
				info.set_ip(0);	
			}
			info.set_level(pc.getLevel());
			info.set_name(pc.getName().getBytes());
			
			String clanname = pc.getClanname();
			if(!MJString.isNullOrEmpty(clanname)) {
				info.set_pledge(clanname.getBytes());				
			}else {
				info.set_pledge(new byte[0]);
			}
			noti.add_infos(info);
		}
		gm.sendPackets(noti, MJEProtoMessages.SC_LIST_USER_NOTI);
		//pc.sendPackets(new S_PacketBox(S_PacketBox.CALL_SOMETHING));
	}
}
