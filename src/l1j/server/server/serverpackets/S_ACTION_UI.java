         package l1j.server.server.serverpackets;

         import java.util.StringTokenizer;
         import l1j.server.server.model.Instance.L1PcInstance;








         public class S_ACTION_UI
           extends ServerBasePacket
         {
           public static final int TAM = 194;
           public static final int CLAN_JOIN_MESSAGE = 67;
           public static final int TEST = 204;
           public static final int TEST2 = 58;
           public static final int SAFETYZONE = 207;
           public static final int PCBANG_SET = 126;
           public static final int MONSTER_BOOK_WEEK_QUEST = 810;
           public static final int EINHASAD = 1020;
           private static final String S_ACTION_UI = "S_ACTION_UI";
           private static final String 테스트진행 = "0a e3 01 0a 55 12 10 08 01 10 9a 83 2c 18 00 22 06 08 d8 87 01 10 01 12 11 08 02 10 81 88 6e 18 c6 20 22 06 08 d8 87 01 10 02 12 12 08 03 10 84 a0 b8 03 18 9f 78 22 06 08 d8 87 01 10 05 1a 0b 08 01 10 b5 bf f0 06 18 c1 87 01 1a 0b 08 01 10 84 a0 b8 03 18 c0 87 01 20 01 ";

           public S_ACTION_UI(boolean flag) {
             writeC(19);
             String aa = "3b 01 08 02 10 01 cf 3a";
             StringTokenizer st = new StringTokenizer(aa.toString());
             while (st.hasMoreTokens()) {
               writeC(Integer.parseInt(st.nextToken(), 16));
             }
           }




           public S_ACTION_UI(byte[] flag) {
             writeC(19);
             writeH(339);
             writeByte(flag);
             writeH(0);
           }







           public S_ACTION_UI(int code, boolean isOpen) {
             writeC(19);
             writeC(code);
             switch (code) {
               case 126:
                 writeC(0);
                 writeC(8);
                 writeC(isOpen ? 1 : 0);
                 writeC(16);
                 writeC(1);
                 break;
               case 207:
                 writeC(1);
                 writeC(8);
                 write7B(isOpen ? 128L : 0L);
                 writeC(16);
                 writeC(0);
                 writeC(24);
                 writeC(0);
                 break;
             }
             writeH(0);
           }




           public S_ACTION_UI(String clanname, int rank) {
             writeC(19);
             writeC(25);
             writeC(2);
             writeC(10);
             writeS2(clanname);
             writeC(16);
             writeC(rank);
             writeH(0);
           }

           public S_ACTION_UI(int reward) {
             writeC(19);
             writeH(146);
             writeC(8);
             writeBit(reward);
             writeH(0);
           }




           public S_ACTION_UI(int type, int skillnum) {
             writeC(19);
             writeC(type);
             if (type == 145) {

               writeC(1);
               writeC(10);
               writeC((skillnum != 5) ? 2 : 4);
               writeC(8);
               writeC(skillnum);
               if (skillnum == 5) {
                 writeC(16);
                 writeC(10);
               }
               writeH(61837);
             } else if (type == 146) {

               writeC(1);
               writeC(8);
               writeC(skillnum);
               if (skillnum == 5) {
                 writeC(16);
                 writeC(10);
               }
               writeH(0);
             } else if (type == 194) {
               writeC(1);
               writeC(8);
               write4bit(skillnum);
               writeH(0);

             }
             else if (type == 204) {
               writeC(13);
               write4bit(skillnum);
             } else if (type == 67) {
               writeH(2049);
               writeC(skillnum);
               writeH(0);
             }
           }
           public S_ACTION_UI(int type, L1PcInstance pc) {
             int blessOfAin, bless_efficiency, bless_exp, extra;
             writeC(19);
             writeH(type);
             switch (type) {
               case 1020:
                 blessOfAin = pc.getAccount().getBlessOfAin() / 10000;
                 bless_efficiency = pc.getBlessOfAinEfficiency() + pc.getBlessAinEfficiency();
                 bless_exp = pc.getBlessOfAinExp();
                 extra = 0;

                 if (blessOfAin <= 200) {
                   bless_exp += 10000;
                 }
                 else if (blessOfAin >= 201 && blessOfAin <= 1800) {
                   bless_exp += 6000;
                   extra += 40;

                 }
                 else if (blessOfAin >= 1801 && blessOfAin <= 3400) {
                   bless_exp += 8000;
                   extra += 50;

                 }
                 else if (blessOfAin >= 3401 && blessOfAin <= 8000) {
                   bless_exp += 10000;
                   extra += 60;
                 }



                 if (pc.getAinExpBonus() > 0) {
                   extra += pc.getAinExpBonus() * 100;
                 }

                 writeC(8);
                 writeBit(blessOfAin);
                 writeC(16);
                 writeBit(bless_exp);
                 writeC(24);
                 writeBit(extra);
                 writeC(32);
                 writeBit(bless_efficiency);


                 writeC(48);
                 writeBit(pc.getNetConnection().getAccount().getEinDayBonus());


                 writeC(56);



                 if (blessOfAin < 1 && pc.hasSkillEffect(7116)) {
                   writeBit(3L);
                 } else {
                   writeBit(1L);
                 }
                 if (blessOfAin > 0 && blessOfAin < 80000000 && !pc._dragonbless) {
                   pc.sendPackets(new S_NewSkillIcon(8689, true, -1L));
                   pc._dragonbless = true;
                 }
                 break;
             }
             writeH(0);
           }

           public byte[] getContent() {
             return getBytes();
           }

           public String getType() {
             return "S_ACTION_UI";
           }
         }


