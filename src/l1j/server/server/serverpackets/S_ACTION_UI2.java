         package l1j.server.server.serverpackets;

         import java.util.Collection;
         import java.util.StringTokenizer;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1World;



         public class S_ACTION_UI2
           extends ServerBasePacket
         {
           private static final String S_ACTION_UI2 = "S_ACTION_UI2";
           public static final int Elixir = 233;
           public static final int MAGICEVASION = 488;
           public static final int stateProfile = 231;
           public static final int EMOTICON = 64;
           public static final int CLAN_JOIN_SETTING = 77;
           public static final int CLAN_JOIN_WAIT = 69;
           public static final int unk1 = 65;
           public static final int unknown1 = 78;
           public static final int unknown2 = 145;
           public static final int CLAN_RANK = 25;
           public static final int ICON_BUFF = 110;

           public S_ACTION_UI2(int type, boolean ck) {
             buildPacket(type, ck);
           }

           public S_ACTION_UI2(int i, int t, int o, int gf, int ms) {
             writeC(19);
             writeC(110);
             writeC(0);
             writeC(8);
             writeC(2);
             writeC(16);
             write7B(i);
             writeC(24);
             write7B(t);
             writeC(32);
             writeC(9);
             writeC(40);
             write7B(gf);
             writeC(48);
             writeC(0);
             writeC(56);
             writeC(o);
             writeC(64);
             write7B(ms);
             writeC(72);
             writeC(0);
             writeC(80);
             writeC(0);
             writeC(88);
             writeC(1);
             writeH(0);
           }

           private void buildPacket(int type, boolean ck) {
             writeC(19);
             writeC(type);
             switch (type) {
               case 69:
                 writeC(1);
                 writeC(8);
                 writeC(2);
                 writeH(0);
                 break;
             }
           }
           public S_ACTION_UI2(L1PcInstance pc, int code) {
             int length;
             writeC(19);
             writeC(code);
             switch (code) {
               case 25:
                 writeC(2);
                 writeC(10);
                 length = 0;
                 if (pc.getClanname() != null)
                   length = (pc.getClanname().getBytes()).length;
                 if (length > 0) {
                   writeC(length);
                   writeByte(pc.getClanname().getBytes());
                   writeC(16);
                   switch (pc.getClanRank()) {
                     case 8:
                       writeC(5);
                       break;



                     case 9:
                       writeC(6);
                       break;
                     case 10:
                       writeC(4);
                       break;
                     default:
                       writeC(pc.getClanRank());
                       break;
                   }
                 } else {
                   writeC(0);
                 }
                 writeH(0);
                 break;
             }
           }

           public S_ACTION_UI2(int subCode) {
             int size;
             writeC(19);
             writeC(subCode);
             switch (subCode) {
               case 145:
                 writeC(1);
                 writeC(136);
                 writeC(212);
                 break;
               case 65:
                 writeC(1);
                 writeC(8);
                 writeC(128);
                 writeC(225);
                 writeC(1);
                 writeC(16);
                 writeC(229);
                 writeC(224);
                 writeC(1);
                 writeC(74);
                 writeC(0);
                 break;
               case 78:
                 writeC(1);
                 writeC(8);
                 writeC(3);
                 writeC(16);
                 writeC(0);
                 writeC(24);
                 writeC(0);
                 writeH(0);
                 break;
               case 69:
                 writeC(1);
                 writeH(520);
                 size = 1;

                 if (size > 0) {

                   Collection<L1PcInstance> list = L1World.getInstance().getAllPlayers();
                   int i = 0;
                   for (L1PcInstance pc : list) {
                     writeC(18);
                     if (i == 0) {
                       writeC(39);
                     } else if (i == 1) {
                       writeC(38);
                     } else if (i == 2) {
                       writeC(40);
                     } else {
                       writeC(39);
                     }



                     writeC(8);
                     writeD(302229922);
                     writeD(-977030648);
                     writeD(-1213413909);
                     writeH(6324);



                     if (i == 0 || i >= 3) {
                       writeD(47763902);
                     } else {
                       writeC(195);
                       writeH(23690);
                     }
                     writeC(34);
                     byte[] name = pc.getName().getBytes();
                     writeC(name.length);
                     writeByte(name);
                     writeC(42);
                     writeC(("1".getBytes()).length);
                     writeByte("1".getBytes());



                     writeC(48);
                     writeC((L1World.getInstance().getPlayer(pc.getName()) != null) ? 1 : 0);
                     writeC(56);
                     writeC(pc.getType());
                     i++;
                     if (i == 2)
                       break;
                   }
                 }  writeH(0);
                 break;
             }
           }

           public S_ACTION_UI2(int sub, int skillId, boolean on, int msgNum, int time) {
             writeC(19);
             writeH(sub);
             switch (sub) {
               case 110:
                 writeC(8);
                 writeC(on ? 2 : 3);
                 writeC(16);
                 writeBit(sub);
                 writeC(24);
                 writeBit(time);
                 writeC(32);
                 writeC(8);
                 writeC(40);
                 writeBit(skillId);
                 writeH(48);
                 writeC(56);
                 writeC(3);
                 writeC(64);
                 writeBit(msgNum);
                 writeC(72);
                 writeC(0);
                 writeH(80);
                 writeC(88);
                 writeC(1);
                 writeC(96);
                 writeC(0);
                 writeC(104);
                 writeC(0);
                 writeC(112);
                 writeC(0);
                 writeH(0);
                 break;
             }
           }

           public static final int[] hextable = new int[] { 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255 };




           private static final String 艾恩哈薩德庇護1 = "00 08 01 10 82 22 18";



           private static final String 艾恩哈薩德庇護2 = "20 0e 28 aa 4b 30 80 44 38 01 40 c9 36 48 00 50 00 58 01 60 00 68 00 70 00 78 2a 80 01 00 00 00";




           private void byteWrite(long value) {
             long temp = value / 128L;
             if (temp > 0L) {
               writeC(hextable[(int)value % 128]);
               while (temp >= 128L) {
                 writeC(hextable[(int)temp % 128]);
                 temp /= 128L;
               }
               if (temp > 0L) {
                 writeC((int)temp);
               }
             } else if (value == 0L) {
               writeC(0);
             } else {
               writeC(hextable[(int)value]);
               writeC(0);
             }
           }





             public S_ACTION_UI2(String 代碼, long 時間) {
                // 寫入初始字節值
                 writeC(19);
                 writeC(110);

                 String 增益封包 = "";
                // 如果代碼等於 "艾恩庇護"，設置增益封包的初始值
                 if (代碼.equals("艾恩庇護")) {
                     增益封包 = "00 08 01 10 82 22 18";
                 }

                // 使用 StringTokenizer 分割增益封包字串
                 StringTokenizer st = new StringTokenizer(增益封包.toString());
                 while (st.hasMoreTokens()) {
                // 解析每個 token 並以 16 進制格式寫入
                     writeC(Integer.parseInt(st.nextToken(), 16));
                 }

                // 將時間以秒為單位寫入
                 byteWrite(時間 / 1000L);

                // 如果代碼等於 "艾恩庇護"，設置增益封包的第二個值
                 if (代碼.equals("艾恩庇護")) {
                     增益封包 = "20 0e 28 aa 4b 30 80 44 38 01 40 c9 36 48 00 50 00 58 01 60 00 68 00 70 00 78 2a 80 01 00 00 00";
                 }
                // 使用 StringTokenizer 分割新的增益封包字串
                 st = new StringTokenizer(增益封包.toString());
                 while (st.hasMoreTokens()) {
                // 解析每個 token 並以 16 進制格式寫入
                     writeC(Integer.parseInt(st.nextToken(), 16));
                 }
             }

           public S_ACTION_UI2(int type, int stat) {
             writeC(19);
             writeC(type);
             writeC(1);

             switch (type) {
               case 233:
               case 488:
                 writeC(8);
                 writeC(stat);
                 break;
               case 231:
                 writeC(10);
                 if (stat == 45) {
                   writeH(2056);
                   writeC(stat);
                   writeD(51905296);
                   writeH(288);
                 } else {
                   writeH(2054);
                   writeC(stat);
                   writeD(18350352);
                 }

                 writeC(18);
                 if (stat == 45) {
                   writeH(2056);
                   writeC(stat);
                   writeD(51905296);
                   writeH(288);
                 } else {
                   writeH(2054);
                   writeC(stat);
                   writeD(18350352);
                 }

                 writeC(26);
                 if (stat == 25) {
                   writeH(2054);
                   writeC(stat);
                   writeD(18350352);
                   writeH(12856);
                 } else if (stat == 35) {
                   writeH(2056);
                   writeC(stat);
                   writeD(18350352);
                   writeH(25656);
                 } else if (stat == 45) {
                   writeH(2057);
                   writeC(stat);
                   writeD(51905296);
                   writeC(56);
                   writeH(406);
                 }

                 writeC(34);
                 if (stat == 45) {
                   writeH(2056);
                   writeC(stat);
                   writeD(51905296);
                   writeH(288);
                 } else {
                   writeH(2054);
                   writeC(stat);
                   writeD(18350352);
                 }

                 writeC(42);
                 if (stat == 25) {
                   writeH(2054);
                   writeC(stat);
                   writeD(842006800);
                 } else if (stat == 35) {
                   writeH(2056);
                   writeC(stat);
                   writeD(18350352);
                   writeH(25648);
                 } else if (stat == 45) {
                   writeH(2057);
                   writeC(stat);
                   writeD(35128080);
                   writeC(48);
                   writeH(406);
                 }

                 writeD(-16250062);
                 writeD(-1);
                 writeD(-1);
                 writeC(1);
                 break;
             }

             writeH(0);
           }

           public S_ACTION_UI2(int type, int subtype, int objid) {
             int temp;
             writeC(19);
             writeC(type);
             switch (type) {
               case 64:
                 writeC(1);
                 writeC(8);
                 temp = objid / 128;
                 if (temp > 0) {
                   writeC(hextable[objid % 128]);
                   while (temp > 128) {
                     writeC(hextable[temp % 128]);
                     temp /= 128;
                   }
                   writeC(temp);
                 }
                 else if (objid == 0) {
                   writeC(0);
                 } else {
                   writeC(hextable[objid]);
                   writeC(0);
                 }


                 writeC(16);
                 writeC(2);
                 writeC(24);
                 writeC(subtype);
                 writeH(0);
                 break;
               case 77:
                 writeD(268503041);
                 writeC(subtype);
                 writeC(24);
                 writeC(objid);
                 writeD(5154);
                 writeD(0);
                 writeD(0);
                 writeD(0);
                 writeD(0);
                 writeD(0);
                 break;
             }
           }






           public byte[] getContent() {
             return getBytes();
           }

           public String getType() {
             return "S_ACTION_UI2";
           }
         }


