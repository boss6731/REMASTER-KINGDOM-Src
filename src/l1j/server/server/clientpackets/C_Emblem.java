package l1j.server.server.server.clientpackets;

import java.io.File;
import java.io.FileOutputStream;


import l1j.server.server.IdFactory;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ReturnedStat;

public class C_Emblem extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_EMBLEM = "[C] C_Emblem";

	public C_Emblem(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);

		L1PcInstance player = clientthread.getActiveChar();
		if (player == null) {
			return;
		} else if (player.getClanRank() != 4 && player.getClanRank() != 10) {
			return;
		}
		if (player.getClanid() != 0) {
			FileOutputStream fos = null;
			try {
//				byte[] buff = new byte[1258];
				
				byte[] buff = readByte();
				
//				for (short cnt = 0; cnt < 1258; cnt++) {
//					buff[cnt] = (byte)(readC() & 0xff);
//				}
				int newEmblemdId = IdFactory.getInstance().nextId();
				String emblem_file = String.valueOf(newEmblemdId);
				fos = new FileOutputStream("emblem/" + emblem_file);
//				fos.write(buff,  0, 1258);
				fos.write(buff,  0, buff.length);
				
				L1Clan clan = ClanTable.getInstance().getTemplate(player.getClanid());
				clan.setEmblemId(newEmblemdId);
				ClanTable.getInstance().updateClan(clan);

				for(L1PcInstance pc : clan.getOnlineClanMember()){
					pc.sendPackets(String.valueOf(new S_ReturnedStat(pc.getId(), newEmblemdId)));
					pc.broadcastPacket(new S_ReturnedStat(pc.getId(), newEmblemdId));
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(String.format("%s 徽章註冊錯誤", player.getName()));
			} finally {
				if (null != fos) {
					fos.close();
				}
				fos = null;
			}
		}
	}
/*	public C_Emblem(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);
		try{
			L1PcInstance player = clientthread.getActiveChar();
			if(player == null || player.getClanid() == 0)return;
			if (player.getClanRank() != L1Clan.CLAN_RANK_LEAGUE_PRINCE && player.getClanRank() != L1Clan.CLAN_RANK_PRINCE) return;
			L1Clan clan = ClanTable.getInstance().getTemplate(player.getClanid());
			if(clan==null)return;
			
			File deleteFile = null;
			try{
				deleteFile = new File("emblem/" + String.valueOf(clan.getEmblemId()));
				if (deleteFile.exists()) deleteFile.delete(); // 刪除現有文件
			}catch(Exception e){
				e.printStackTrace();
				System.out.println(String.format("%s 徽章刪除錯誤", player.getName()));
			}finally{
				deleteFile = null;
			}
			
			FileOutputStream fos = null;
			try{
				byte[] buff = readByte();
				int newEmblemdId = IdFactory.getInstance().nextId();
				String emblem_file = String.valueOf(newEmblemdId);
				fos = new FileOutputStream("emblem/" + emblem_file);
				fos.write(buff, 0, buff.length); // 文件創建
				
				clan.setEmblemId(newEmblemdId);
				ClanTable.getInstance().updateClan(clan);
				for(L1PcInstance pc : clan.getOnlineClanMember()) {
					pc.sendPackets(new S_ReturnedStat(pc.getId(), newEmblemdId));
					pc.broadcastPacket(new S_ReturnedStat(pc.getId(), newEmblemdId));
				}
			}catch(Exception e){
				e.printStackTrace();
//				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				System.out.println(String.format("%s 徽章註冊錯誤", player.getName()));
			}finally{
				if(fos != null)fos.close();
				fos = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			clear();
		}
	}*/
	@Override
	public String getType() {
		return C_EMBLEM;
	}
}
