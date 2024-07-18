package l1j.server.server.serverpackets;

import java.util.Collection;
import java.util.StringTokenizer;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_ACTION_UI2 extends ServerBasePacket {
	private static final String S_ACTION_UI2 = "S_ACTION_UI2";

	public static final int Elixir = 0xe9;
	public static final int MAGICEVASION = 488;
	public static final int stateProfile = 0xe7;
	public static final int EMOTICON = 0x40;
	public static final int CLAN_JOIN_SETTING = 0x4D;
	public static final int CLAN_JOIN_WAIT = 0x45;
	public static final int unk1 = 0x41;
	public static final int unknown1 = 0x4E;
	public static final int unknown2 = 0x91;
	public static final int CLAN_RANK = 0x19;
	public static final int ICON_BUFF = 0x6e;

	public S_ACTION_UI2(int type, boolean ck) {
		buildPacket(type, ck);
	}

	public S_ACTION_UI2(int i, int t, int o, int gf, int ms) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(ICON_BUFF);
		writeC(0x00);
		writeC(0x08);
		writeC(0x02);
		writeC(0x10);
		write7B(i);
		writeC(0x18);
		write7B(t);
		writeC(0x20);
		writeC(0x09);
		writeC(0x28);
		write7B(gf);
		writeC(0x30);
		writeC(0x00);
		writeC(0x38);
		writeC(o);
		writeC(0x40);
		write7B(ms);
		writeC(0x48);
		writeC(0x00);
		writeC(0x50);
		writeC(0x00);
		writeC(0x58);
		writeC(0x01);
		writeH(0);
	}

	private void buildPacket(int type, boolean ck) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch (type) {
		case CLAN_JOIN_WAIT:
			writeC(1);
			writeC(8);
			writeC(2);
			writeH(0);
			break;
		}
	}

	public S_ACTION_UI2(L1PcInstance pc, int code) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(code);
		switch (code) {
			case CLAN_RANK:
				writeC(0x02);
				writeC(0x0a);
				int length = 0;
				if (pc.getClanname() != null) {
					length = pc.getClanname().getBytes().length;
				}
				if (length > 0) {
					writeC(length);
					writeByte(pc.getClanname().getBytes());
					writeC(0x10);
					switch (pc.getClanRank()) {
						case L1Clan.一般:
							writeC(L1Clan.CLAN_RANK_LEAGUE_PROBATION);
							break;
                    /* case L1Clan.修練:
                         writeC(L1Clan.CLAN_RANK_LEAGUE_PUBLIC);
                         break; */
						case L1Clan.守護騎士:
							writeC(L1Clan.CLAN_RANK_LEAGUE_GUARDIAN);
							break;
						case L1Clan.盟主:
							writeC(L1Clan.CLAN_RANK_LEAGUE_PRINCE);
							break;
						default:
							writeC(pc.getClanRank());
							break;
					}
				}
				break;
			default:
				// 處理其他 code 的情況（如果需要）
				break;
		}
	}
			} else {
				writeC(0x00);
			}
			writeH(0x00);
			break;
		}
	}


	public S_ACTION_UI2(int subCode) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(subCode);
		switch (subCode) {
		case unknown2:// 母雞抖~~~~
			writeC(01);
			writeC(0x88);
			writeC(0xd4);
			break;
		case unk1:
			writeC(1);
			writeC(8);
			writeC(0x80);
			writeC(0xe1);
			writeC(1);
			writeC(0x10);
			writeC(0xe5);
			writeC(0xe0);
			writeC(1);
			writeC(0x4a);
			writeC(0);
			break;
		case unknown1:
			writeC(1);
			writeC(8);
			writeC(3);
			writeC(0x10);
			writeC(0);
			writeC(0x18);
			writeC(0);
			writeH(0);
			break;
		case CLAN_JOIN_WAIT:
			writeC(0x01);
			writeH(0x0208);
			int size = 1;

			if (size > 0) {
				Collection<L1PcInstance> list = L1World.getInstance()
						.getAllPlayers();
				int i = 0;
				for (L1PcInstance pc : list) {
					writeC(0x12);
					if (i == 0)
						writeC(39);
					else if (i == 1)
						writeC(38);
					else if (i == 2)
						writeC(40);
					else
						writeC(39);
					/*
					 * else if(i == 3) writeC(39); else if(i == 4) writeC(38);
					 * else if(i == 5) writeC(41);
					 */
					writeC(0x08);
					writeD(0x1203A9A2);
					writeD(0xC5C3B208);
					writeD(0xB7ACC5EB);
					writeH(0x18B4);

					// byteWrite(pc.getId());
					// if(i == 0 || i == 3 || i == 4 || i >= 6){
					if (i == 0 || i >= 3) {
						writeD(0x02D8D1BE);
					} else {
						writeC(0xC3);
						writeH(0x5C8A);
					}
					writeC(0x22);
					byte[] name = pc.getName().getBytes();
					writeC(name.length);
					writeByte(name);
					writeC(0x2A);
					writeC("1".getBytes().length);// name.length);
					writeByte("1".getBytes());
					// byte[] memo = pc.getTitle().getBytes();//臨時頭銜
					// writeC(memo.length);
					// writeByte(memo);
					writeC(0x30);
					writeC(L1World.getInstance().getPlayer(pc.getName()) != null ? 1: 0);// 連接中
					writeC(0x38);
					writeC(pc.getType());// 等級
					i++;
					if (i == 2)
						break;
				}
			}			writeH(0x00);
			break;
		}
	}

public S_ACTION_UI2(int sub, int skillId, boolean on, int msgNum, int time) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(sub);
		switch (sub) {
		case ICON_BUFF:
		writeC(0x08);
		writeC(on ? 2 : 3); // true/false
		writeC(0x10);
		writeBit(sub);
		writeC(0x18);
		writeBit(time); // 秒數
		writeC(0x20);
		writeC(0x08);
		writeC(0x28);
		writeBit(skillId); // 想要的圖標形狀或編號
		writeH(0x30);
		writeC(0x38);
		writeC(0x03);
		writeC(0x40);
		writeBit(msgNum); // 字符串編號（圖標內的內容）
		writeC(0x48);
		writeC(0x00);
		writeH(0x0050);
		writeC(0x58);
		writeC(0x01);
		writeC(0x60);
		writeC(0x00);
		writeC(0x68);
		writeC(0x00);
		writeC(0x70);
		writeC(0x00);
		writeH(0x00);
		break;
		}
}

	public static final int[] hextable = { 0x80, 0x81, 0x82, 0x83, 0x84, 0x85,
		0x86, 0x87, 0x88, 0x89, 0x8a, 0x8b, 0x8c, 0x8d, 0x8e, 0x8f, 0x90,
		0x91, 0x92, 0x93, 0x94, 0x95, 0x96, 0x97, 0x98, 0x99, 0x9a, 0x9b,
		0x9c, 0x9d, 0x9e, 0x9f, 0xa0, 0xa1, 0xa2, 0xa3, 0xa4, 0xa5, 0xa6,
		0xa7, 0xa8, 0xa9, 0xaa, 0xab, 0xac, 0xad, 0xae, 0xaf, 0xb0, 0xb1,
		0xb2, 0xb3, 0xb4, 0xb5, 0xb6, 0xb7, 0xb8, 0xb9, 0xba, 0xbb, 0xbc,
		0xbd, 0xbe, 0xbf, 0xc0, 0xc1, 0xc2, 0xc3, 0xc4, 0xc5, 0xc6, 0xc7,
		0xc8, 0xc9, 0xca, 0xcb, 0xcc, 0xcd, 0xce, 0xcf, 0xd0, 0xd1, 0xd2,
		0xd3, 0xd4, 0xd5, 0xd6, 0xd7, 0xd8, 0xd9, 0xda, 0xdb, 0xdc, 0xdd,
		0xde, 0xdf, 0xe0, 0xe1, 0xe2, 0xe3, 0xe4, 0xe5, 0xe6, 0xe7, 0xe8,
		0xe9, 0xea, 0xeb, 0xec, 0xed, 0xee, 0xef, 0xf0, 0xf1, 0xf2, 0xf3,
		0xf4, 0xf5, 0xf6, 0xf7, 0xf8, 0xf9, 0xfa, 0xfb, 0xfc, 0xfd, 0xfe,
		0xff };
	
	private void byteWrite(long value) {
		long temp = value / 128;
		if (temp > 0) {
			writeC(hextable[(int) value % 128]);
			while (temp >= 128) {
				writeC(hextable[(int) temp % 128]);
				temp = temp / 128;
			}
			if (temp > 0)
				writeC((int) temp);
		} else {
			if (value == 0) {
				writeC(0);
			} else {
				writeC(hextable[(int) value]);
				writeC(0);
			}
		}
	}
private static final String 阿因哈薩德祝福1 = "00 08 01 10 82 22 18";
private static final String 阿因哈薩德祝福2 = "20 0e 28 aa 4b 30 80 44 38 01 40 c9 36 48 "+ "00 50 00 58 01 60 00 68 00 70 00 78 2a 80 01 00 00 00";

public S_ACTION_UI2(String 代碼, long 時間) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(ICON_BUFF);

		String 增益包 = "";
		if (代碼.equals("阿因祝福")) {
		增益包 = 阿因哈薩德祝福1;
		}

		StringTokenizer st = new StringTokenizer(增益包.toString());
		while (st.hasMoreTokens()) {
		writeC(Integer.parseInt(st.nextToken(), 16));
		}

		byteWrite(時間 / 1000);

		if (代碼.equals("阿因祝福")) {
		增益包 = 阿因哈薩德祝福2;
		}
		st = new StringTokenizer(增益包.toString());
		while (st.hasMoreTokens()) {
		writeC(Integer.parseInt(st.nextToken(), 16));
		}
		}
    
	public S_ACTION_UI2(int type, int stat) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		writeC(0x01);

		switch(type){
		case MAGICEVASION:
		case Elixir:
			writeC(0x08);
			writeC(stat);
			break;
		case stateProfile:
			writeC(0x0a); //力量
			if(stat == 45){
				writeH(0x0808);
				writeC(stat);
				writeD(0x03180310);
				writeH(0x0120);
			}else{
				writeH(0x0806);
				writeC(stat);
				writeD(0x01180110);
			}

			writeC(0x12); //整數
			if(stat == 45){
				writeH(0x0808);
				writeC(stat);
				writeD(0x03180310);
				writeH(0x0120);
			}else{
				writeH(0x0806);
				writeC(stat);
				writeD(0x01180110);
			}

			writeC(0x1a);
			if(stat == 25){
				writeH(0x0806);
				writeC(stat);
				writeD(0x01180110);
				writeH(0x3238);
			}else if(stat == 35){
				writeH(0x0808);
				writeC(stat);
				writeD(0x01180110);
				writeH(0x6438);
			}else if(stat == 45){
				writeH(0x0809);
				writeC(stat);
				writeD(0x03180310);
				writeC(0x38);
				writeH(0x0196);
			}

			writeC(0x22);
			if(stat == 45){
				writeH(0x0808);
				writeC(stat);
				writeD(0x03180310);
				writeH(0x0120);
			}else{
				writeH(0x0806);
				writeC(stat);
				writeD(0x01180110);
			}

			writeC(0x2a);
			if(stat == 25){
				writeH(0x0806);
				writeC(stat);
				writeD(0x32300110);
			}else if(stat == 35){
				writeH(0x0808);
				writeC(stat);
				writeD(0x01180110);
				writeH(0x6430);
			}else if(stat == 45){
				writeH(0x0809);
				writeC(stat);
				writeD(0x02180310);
				writeC(0x30);
				writeH(0x0196);
			}

			writeD(0xff080b32);
			writeD(0xffffffff);
			writeD(0xffffffff);
			writeC(0x01);
			break;
		}

		writeH(0);

	}

	public S_ACTION_UI2(int type, int subtype, int objid) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch (type) {
		case EMOTICON:
			writeC(0x01);
			writeC(0x08);
			int temp = objid / 128;
			if (temp > 0) {
				writeC(hextable[objid % 128]);
				while (temp > 128) {
					writeC(hextable[temp % 128]);
					temp = temp / 128;
				}
				writeC(temp);
			} else {
				if (objid == 0) {
					writeC(0);
				} else {
					writeC(hextable[objid]);
					writeC(0);
				}
			}
			// byteWrite(value);
			writeC(0x10);
			writeC(0x02);
			writeC(0x18);
			writeC(subtype);
			writeH(0);
			break;
		case CLAN_JOIN_SETTING:
			writeD(0x10010801);
			writeC(subtype);// 訂閱設定
			writeC(0x18);
			writeC(objid);// 訂閱類型
			writeD(0x00001422);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			break;	

		default:
			break;
		}
	}


	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_ACTION_UI2;
	}
}


