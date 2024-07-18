package l1j.server.server.serverpackets;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.MJString;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.storage.mysql.MySqlCharacterStorage;


public class S_Pledge extends ServerBasePacket {
    private static final String _S_Pledge = "[S] _S_Pledge";
    private static final byte[] emptyMemo = new byte[470];
    
    public S_Pledge(int ClanId) {
        L1Clan clan = ClanTable.getInstance().getTemplate(ClanId);
        writeC(Opcodes.S_EVENT);
        writeC(S_PacketBox.HTML_PLEDGE_ANNOUNCE);
        writeS(clan.getClanName());
        writeS(clan.getLeaderName());
        writeD(clan.getEmblemId());
        writeC(clan.getHouseId() != 0 ? 1 : 0);
        writeC(clan.getCastleId() != 0 ? 1 : 0);    
        writeC(0);
        writeD((clan.getClanBirthDay().getTime() / 1000)); 
        /*try {
        	
            byte[] text = new byte[478];
            Arrays.fill(text, (byte) 0);
            int i = 0;
            for (byte b : clan.getAnnouncement().getBytes("UTF-8")) {
                text[i++] = b;
            }
            writeByte(text);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        /*
        if(!MJString.isNullOrEmpty(clan.getAnnouncement())) {
           // System.out.println("s_ " + clan.getAnnouncement());
            writeByte(clan.getAnnouncement().getBytes(MJEncoding.EUCKR));        	
        }*/
        writeD(0);
        writeD(0);
        if(!MJString.isNullOrEmpty(clan.getAnnouncement())) {
        	//writeS(clan.getAnnouncement());
            writeByte(clan.getAnnouncement().getBytes(MJEncoding.EUCKR));        	
        }else {
        	writeByte(emptyMemo);
        }
        writeH(0x00);
    }

    public S_Pledge(int page, int current_page, ArrayList<String> list) {
        writeC(Opcodes.S_EVENT);
        writeC(S_PacketBox.HTML_PLEDGE_MEMBERS);      
        writeC(page);
        writeC(current_page);
        writeC(list.size());
        for (String name : list) {
            if (name == null) continue;
            try {
                L1PcInstance clanMember = CharacterTable.getInstance().restoreCharacter(name);
				if (clanMember != null) {
					writeS(clanMember.getName());
					if (clanMember.getAccountName().equals("MJBOT") && clanMember.getClanRank() == 0) {
						writeC(0x07);
					} else {
						writeC(clanMember.getClanRank());
					}
					
					writeC(clanMember.getLevel());
					
					byte[] text = new byte[62];
					Arrays.fill(text, (byte) 0);

					if (clanMember.getClanMemberNotes().length() != 0) {
						int i = 0;
						for (byte b : clanMember.getClanMemberNotes().getBytes("UTF-8")) {
							text[i++] = b;
						}
					}
					writeByte(text);
					writeD(clanMember.getClanMemberId());
					writeC(clanMember.getType());
					Long jointime = Timestamp.valueOf(MySqlCharacterStorage.getClanJoinTime(name).toString()).getTime();
					writeD(jointime / 1000L);	//加入部落日期

					write8x(clanMember.getClanContribution(), 2);
//					if (!clanMember.isWorld) {
					
//					if (L1World.getInstance().findPlayer(name) == null) {
//					System.out.println(clanMember.getLastLogoutTime());
						Long logouttime = Timestamp.valueOf(clanMember.getLastLogoutTime().toString()).getTime();
//						Long logintime = Timestamp.valueOf(clanMember.getLastLoginTime().toString()).getTime();
//						Long logouttime = Timestamp.valueOf(clanMember.getLastLogoutTime().toString()).getTime();
						writeD(logouttime / (30 * 1000L * 1000L));	//最後訪問時間??
//					} else {
//						writeD(0x00);	//最後訪問時間??
//					}
	/*				} else {
						writeD(System.currentTimeMillis() / 1000L / 60L);
					}*/
				} else {
					if (name.equalsIgnoreCase("管理員")) { // "매니저" 是韓文，意思是 "經理"
						writeS("馬蒂斯"); // "매티스" 是人名或角色名稱
						writeC(4); // 君主
						writeC(0); // 等級

						byte[] text = new byte[62];
						Arrays.fill(text, (byte) 0);

						int i = 0;
						for (byte b : "新血盟".getBytes("UTF-8")) { // "신규혈맹" 是韓文，意思是 "新血盟"
							text[i++] = b;
						}
						writeByte(text);
						writeD(0);
						writeC(0);
						writeD((System.currentTimeMillis() / 1000L)); // 記錄加入日期
						write8x(0, 2);
						writeD(0x00);
					}
				}
			} catch (Exception e) {
			}
		}
		writeH(0);

		/**
		 * 記錄
		 * @param name 血盟成員名稱
		 * @param notes 記錄內容
		 */
public S_Pledge(String name, String notes) {
			writeC(Opcodes.S_EVENT);
			writeC(S_PacketBox.HTML_PLEDGE_WRITE_NOTES);
			writeS(name);

			byte[] text = new byte[62];
			Arrays.fill(text, (byte) 0);

			if (notes.length() != 0) {
				int i = 0;
				try {
					for (byte b : notes.getBytes("UTF-8")) {
						text[i++] = b;
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			writeByte(text);
			writeH(0);
		}

public S_Pledge(L1Clan clan, int bless) {
			writeC(Opcodes.S_EXTENDED_PROTOBUF);
			writeH(0x8a);
			writeC(0x08);// 當前祝福氣息
			write7B((int) clan.getBlessCount() / 10000);
			writeC(0x10);// 最大祝福氣息
			write7B(40000);
			writeC(0x18);// 每次Buff值
			write7B(30000);
			writeC(0x20);// 每次交換值[再次使用Buff時]
			write7B(1000);
			for (int i = 0; i < 4; i++) {
				int time = clan.getBuffTime()[i];
				if (time == 0)
					time = 172800;
				writeC(0x2a);// 總長度
				write7B(27 + bitlengh(time));
				writeC(0x0a);
				writeC(bitlengh(time) + 6);
				writeC(0x08);// Buff ID
				write7B(2724 + i);
				writeC(0x10);// 秒
				write7B(time);
				writeC(0x18);// 1:可用 2:使用中 3:等待
				writeC(clan.getBuffTime()[i] == 0 ? 1 : bless == i + 1 ? 2 : 3);
				writeC(0x12);// 名稱
				writeS2("$" + Integer.toString(22503 + i));
				writeC(0x1a);// 描述
				writeS2("$" + Integer.toString(22508 + i));
				writeC(0x20);// 背包圖像
				write7B(7233 + (i * 2));
			}
			writeH(0);
		}
    
    @Override
    public byte[] getContent() {
    	return getBytes();
    }

    @Override
    public String getType() {
        return _S_Pledge;
    }
}


