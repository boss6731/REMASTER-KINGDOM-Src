     package l1j.server.server.serverpackets;

     import java.sql.Connection;
     import java.sql.PreparedStatement;
     import java.sql.ResultSet;
     import java.util.Calendar;
     import java.util.Iterator;
     import java.util.List;
     import java.util.TimeZone;
     import l1j.server.Config;
     import l1j.server.L1DatabaseFactory;
     import l1j.server.MJPassiveSkill.MJPassiveID;
     import l1j.server.MJTemplate.MJEncoding;
     import l1j.server.server.Account;
     import l1j.server.server.datatables.PolyTable;
     import l1j.server.server.model.Instance.L1ItemInstance;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1PolyMorph;
     import l1j.server.server.model.L1World;
     import l1j.server.server.templates.L1BookMark;
     import l1j.server.server.utils.SQLUtil;


     public class S_PacketBox
       extends ServerBasePacket
     {
       private static final String S_PACKETBOX = "[S] S_PacketBox";
       public static final int MSG_WAR_BEGIN = 0;
       public static final int MSG_WAR_END = 1;
       public static final int MSG_WAR_GOING = 2;
       public static final int MSG_WAR_INITIATIVE = 3;
       public static final int MSG_WAR_OCCUPY = 4;
       public static final int MSG_DUEL = 5;
       public static final int MSG_SMS_SENT = 6;
       public static final int MSG_MARRIED = 9;
       public static final int WEIGHT = 10;
       public static final int FOOD = 11;
       public static final int MSG_LEVEL_OVER = 12;
       public static final int HTML_UB = 14;
       public static final int MSG_ELF = 15;
       public static final int SHOW_LIST_EXCLUDE = 17;
       public static final int ADD_EXCLUDE = 18;
       public static final int REM_EXCLUDE = 19;
       public static final int PC방버프 = 127;
       public static final int ICONS1 = 20;
       public static final int ICONS2 = 21;
       public static final int ICON_AURA = 22;
       public static final int MSG_TOWN_LEADER = 23;
       public static final int PLEDGE_TWO = 24;
       public static final int PLEDGE_REFRESH_PLUS = 25;
       public static final int PLEDGE_REFRESH_MINUS = 26;
       public static final int MSG_RANK_CHANGED = 27;
       public static final int MSG_WIN_LASTAVARD = 30;
       public static final int MSG_FEEL_GOOD = 31;
       public static final int SOMETHING1 = 33;
       public static final int ICON_BLUEPOTION = 34;
       public static final int ICON_POLYMORPH = 35;
       public static final int ICON_CHATBAN = 36;
       public static final int SOMETHING2 = 37;
       public static final int HTML_CLAN1 = 38;
       public static final int ICON_I2H = 40;
       public static final int CHARACTER_CONFIG = 41;
       public static final int LOGOUT = 42;
       public static final int MSG_CANT_LOGOUT = 43;
       public static final int CALL_SOMETHING = 45;
       public static final int MSG_COLOSSEUM = 49;
       public static final int HTML_CLAN2 = 51;
       public static final int COOK_WINDOW = 52;
       public static final int ICON_COOKING = 53;
       public static final int FISHING = 55;
       public static final int DEL_ICON = 59;
       public static final int DRAGON_PEARL = 60;
       public static final int ALLIANCE_LIST = 62;
       public static final int MINIGAME_START_COUNT = 64;
       public static final int MINIGAME_TIME2 = 65;
       public static final int MINIGAME_LIST = 66;
       public static final int MINIGAME_10SECOND_COUNT = 69;
       public static final int MINIGAME_END = 70;
       public static final int MINIGAME_TIME = 71;
       public static final int MINIGAME_TIME_CLEAR = 72;
       public static final int SPOT = 75;
       public static final int aaaa1 = 78;
       public static final int bbbb2 = 79;
       public static final int cccc3 = 80;
       public static final int EINHASAD = 82;
       public static final int HADIN_DISPLAY = 83;
       public static final int GREEN_MESSAGE = 84;
       public static final int YELLOW_MESSAGE = 61;
       public static final int RED_MESSAGE = 51;
       public static final int SCORE_MARK = 4;
       public static final int EMERALD_ICON = 86;
       public static final int KARMA = 87;
       public static final int INIT_DODGE = 88;
       public static final int DRAGONBLOOD = 100;
       public static final int DODGE = 101;
       public static final int DragonMenu = 102;
       public static final int MINI_MAP_SEND = 111;
       public static final int CLAN_WAREHOUSE_LIST = 117;
       public static final int BAPO = 114;
       public static final int ICON_SECURITY_SERVICES = 125;
       public static final int ICON_PC_BUFF = 127;
       public static final int ER_UpDate = 132;
       public static final int BOOKMARK_SIZE_PLUS_10 = 141;
       public static final int BOOKMARK = 142;
       public static final int UNLIMITED_ICON = 147;
       public static final int UNLIMITED_ICON1 = 180;
       public static final int NONE_TIME_ICON = 180;
       public static final int ITEM_STATUS = 149;
       public static final int MAP_TIMER = 153;
       public static final int BUFFICON = 154;
       public static final int ROUND = 156;
       public static final int ROUND1 = 156;
       public static final int DungeonTime = 159;
       public static final int POSION_ICON = 161;
       public static final int CLAN_BUFF_ICON = 165;
       public static final int HTML_PLEDGE_ANNOUNCE = 167;
       public static final int HTML_PLEDGE_REALEASE_ANNOUNCE = 168;
       public static final int HTML_PLEDGE_WRITE_NOTES = 169;
       public static final int HTML_PLEDGE_MEMBERS = 170;
       public static final int HTML_PLEDGE_ONLINE_MEMBERS = 171;
       public static final int ITEM_ENCHANT_UPDATE = 172;
       public static final int PLEDGE_EMBLEM_STATUS = 173;
       public static final int TOWN_TELEPORT = 176;
         public static final int 攻擊可能距離 = 160;  // 攻擊可能距離   공격가능거리
         public static final int 不知道2 = 184;         // 不知道2
         public static final int 不知道3 = 188;         // 不知道3
         public static final int 背包儲存 = 189;       // 背包儲存
         public static final int 戰鬥射擊 = 181;         // 戰鬥射擊
         public static final int 商店開設次數 = 198;  // 商店開設次數
         public static final int 玩家背刺 = 193;     // 玩家背刺
         public static final int EFFECT_DURATOR = 194; // 效果持續時間
         public static final int ICON_COMBO_BUFF = 204;// 圖示組合增益
         public static final int DRAGONRAID_BUFF = 179;// 龍襲增益
         public static final int a = 103;              // 不明
         public static final int LOGIN_UNKNOWN3 = 32;  // 登錄未知3

       public S_PacketBox(int subCode) {
         writeC(108);
         writeC(subCode);

         switch (subCode) {
           case 78:
           case 79:
             writeC(7);
             writeS("");
             writeS("");
             writeS("");
             writeS("");
             writeS("");
             writeS("");
             writeS("");
             writeH(0);
             break;
           case 188:
             writeD(0);
             writeD(0);
             break;
           case 189:
             writeD(0);
             break;
           case 184:
             writeH(0);
             break;

           case 45:
             callSomething();
             break;
           case 69:
             writeC(10);
             writeC(109);
             writeC(85);
             writeC(208);
             writeC(2);
             writeC(220);
             break;
           case 59:
             writeH(0);
             break;
           case 70:
             writeC(147);
             writeC(92);
             writeC(151);
             writeC(220);
             writeC(42);
             writeC(74);
             break;
           case 64:
             writeC(5);
             writeC(129);
             writeC(252);
             writeC(125);
             writeC(110);
             writeC(17);
             break;
           case 22:
             writeC(152);
             writeC(0);
             writeC(0);
             writeC(0);
             writeC(0);
             writeC(0);
             break;
         }
       }




       public S_PacketBox(int time, boolean ck, boolean ck2) {
         writeC(108);
         writeC(86);
         writeC(170);
         writeC(1);
         writeH(time / 16);
         writeH(0);
       }

       public S_PacketBox(int subCode, L1PcInstance pc, L1ItemInstance weapon) {
         writeC(108);
         writeC(160);


         int range = 1;
         int equipType = 0;
         int isTohand = 0;
         int charGfxId = pc.getCurrentSpriteId();
         int charBaseGfxId = pc.getClassId();
         if (weapon != null && weapon.isEquipped()) {
           boolean gfxCk; L1PolyMorph poly; isTohand = weapon.getItem().isTwohandedWeapon() ? 1 : 0;
           int weaponType = weapon.getItem().getType();
           switch (weaponType) { case 1: case 3:
               equipType = 1; break;
             case 6: case 15: equipType = 2; break;
             case 4: case 10: case 13:
               equipType = 3;
               range = 24;
               if (weaponType == 4) {
                 range = 24;
               }
               if (pc.hasSkillEffect(5158)) {
                 range = 10;
               }
               break;
             case 12:
               equipType = 4; break;
             case 5: case 14: case 18: equipType = 5; range = 2;
               if (weaponType == 18) equipType = 10;
               gfxCk = false;
               poly = PolyTable.getInstance().getTemplate(charGfxId);
               if (poly != null && poly.isSpearGfx()) {
                 gfxCk = true;
               }



               if (!gfxCk && charBaseGfxId != charGfxId)
                 range = 1;
               break;
             case 7:
             case 16:
               equipType = 6;
               break;
             case 2:
               equipType = 7;
               break;
             case 11:
               equipType = 8;
               break;
             case 17:
               equipType = 8;
               range = 4;
               break; }




         }
         if (pc.isSpearModeType()) {
           if (pc != null && pc.isPassive(MJPassiveID.INCREASE_RANGE.toInt())) {
             range = 8;
           } else {
             range = 4;
           }
         }

         pc.setAttackRang(range);
         writeC(range);
         writeC(equipType);
         writeC(isTohand);
       }



       public S_PacketBox(int subCode, int range, int type, boolean bow, L1PcInstance pc) {
         writeC(108);
         writeC(subCode);
         switch (subCode) {
           case 160:
             writeC(range);
             writeC(type);
             if (bow) {
               writeC(1);
             } else {
               writeC(0);
             }
             pc.setAutoRange(range);
             break;
         }
       }

       public S_PacketBox(int subCode, int time1, int time2, int time3, int time4) {
         writeC(108);
         writeC(subCode);
         switch (subCode) {
           case 159:
             writeD(7);
             writeD(1);
             writeS("$12125");
             writeD(time1);
             writeD(2);
             writeS("$6081");
             writeD(time2);
             writeD(15);
             writeS("$13527");
             writeD(time3);
             writeD(500);
             writeS("$19375");
             writeD(time4);
             writeD(49200);
             break;
         }
       }



       public S_PacketBox(int subCode, L1PcInstance pc) {
         writeC(108);
         writeC(subCode);
         switch (subCode) {
           case 176:
             writeC(1);
             writeH(pc.getX());
             writeH(pc.getY());
             break;
           case 193:
             writeH(pc.getX());
             writeH(pc.getY());
             break;
         }
       }

       public S_PacketBox(int subCode, int value) {
         writeC(108);
         writeC(subCode);
         switch (subCode) {
           case 32:
             writeC(16);
             writeD(0);
             writeD(0);
             writeD(0);
             writeD(0);
             break;
           case 179:
             writeC(1);
             writeC(39);
             writeC(14);
             writeD(value);
             writeH(25583);
             break;
           case 204:
             writeC(value);
             writeH(0);
             break;
           case 127:
             if (value == 1) {
               writeC(24); break;
             }
             writeC(0);
             break;

           case 198:
             writeD(value);
             writeD(40);
             writeD(0);
             break;
           case 34:
           case 36:
           case 40:
           case 71:
           case 88:
             writeH(value);
             break;
           case 153:
             writeD(value);
             break;
           case 181:
             writeD(value);
             break;
           case 0:
           case 1:
           case 2:
             writeC(value);
             writeH(0);
             break;
           case 6:
           case 10:
           case 11:
           case 101:
             writeC(value);
             break;
           case 15:
           case 49:
           case 75:
           case 132:
             writeC(value);
             break;
           case 12:
             writeC(0);
             writeC(value);
             break;
           case 52:
             writeC(219);
             writeC(49);
             writeC(223);
             writeC(2);
             writeC(1);
             writeC(value);
             break;
           case 66:
             writeH(0);
             writeH(0);
             break;
           case 82:
             value /= 10000;
             writeD(value);
             writeC(0);


             writeD(16);
             writeC(32);
             writeC(0);
             writeC(0);

             writeD(1);
             writeC(0);
             writeC(0);
             writeC(0);
             writeC(0);
             break;

           case 83:
             writeC(value);
             break;
           case 141:
             writeC(value);
             break;
           case 173:
             writeC(1);
             if (value == 0) {
               writeC(0);
             } else if (value == 1) {
               writeC(1);
             }
             writeD(0);
             break;
           case 156:
             writeD(value);
             writeD(12);
             break;
         }
       }



       public S_PacketBox(int subCode, int type, int time, boolean second, boolean temp) {
         writeC(108);
         writeC(subCode);
         switch (subCode) {
           case 154:
             writeH(time);
             writeH(type);
             writeH(0);
             writeH(second ? 1 : 0);
             break;
         }
       }

       public S_PacketBox(int subCode, int time, int gfxid, int type) {
         writeC(108);
         writeC(subCode);
         switch (subCode) {
           case 37:
             writeC(time);
             writeD(gfxid);
             writeH(type);
             break;
           case 154:
             writeH(time);
             writeD(gfxid);
             writeC(type);
             writeC(0);
             break;
           case 86:
             writeC(time);
             writeC(1);
             switch (time) {

               case 62:
                 writeH(gfxid);
                 writeC(20);
                 writeC(type);
                 break;
               case 112:
               case 129:
                 writeC(gfxid);
                 writeH(type);
                 break;
             }
             writeD(gfxid);
             break;
         }
       }


       public S_PacketBox(int subCode, int type, int time) {
         writeC(108);
         writeC(subCode);

         switch (subCode) {
           case 35:
             writeH(type);
             writeC(time);
             break;
           case 53:
             if (type != 7) {
               writeC(46);
               writeC(0);
               writeC(11);
               writeC(0);
               writeC(10);
               writeC(0);
               writeC(16);
               writeC(0);
               writeC(16);
               writeC(0);
               writeC(8);
               writeC(0);
               writeC(208);
               writeC(7);
               writeC(type);
               writeC(36);
               writeH(time);
               writeC(21);
               break;
             }
             writeC(12);
             writeC(12);
             writeC(12);
             writeC(18);
             writeC(12);
             writeC(9);
             writeC(200);
             writeC(0);
             writeC(type);
             writeC(38);
             writeH(time);
             writeC(62);
             writeC(135);
             break;

           case 22:
             writeC(221);
             writeH(time);
             writeC(type);
             break;
           case 5:
             writeD(type);
             writeD(time);
             break;
           case 154:
             writeH(time);
             writeH(type);
             writeH(0);
             break;
           case 100:
             writeC(type);
             writeD(time);
             break;
           case 156:
             writeD(type);
             writeD(time);
             break;
           case 60:
             writeC(time);
             writeC(type);
             break;


           case 86:
             writeC(112);
             writeC(1);
             writeC(type);
             writeH(time);
             break;
           case 180:
             writeC(type);
             writeD(time);

             writeD(3431);
             writeH(0);
             break;
         }
       }




       public S_PacketBox(int time, int val, boolean ck, boolean ck2) {
         writeC(108);
         writeC(86);
         writeC(129);
         writeC(1);
         writeC(val);
         writeH(time);
       }

       public S_PacketBox(int i, int time, boolean ck, boolean ck2, boolean ck3) {
         writeC(108);
         writeC(86);
         writeC(62);
         writeC(i);
         writeH(time);
         writeC(20);
         writeC(134);
       }

       public S_PacketBox(int subCode, String name) {
         writeC(108);
         writeC(subCode);

         switch (subCode) {
           case 168:
             writeByte(name.getBytes(MJEncoding.EUCKR));
             writeH(0);
             break;
           case 23:
             writeS(name);
             break;
           case 84:
             writeC(2);
             writeS(name);
             break;
         }
       }



       public S_PacketBox(int subCode, int id, String name, String clanName) {
         writeC(108);
         writeC(subCode);

         switch (subCode) {
           case 30:
             writeD(id);
             writeS(name);
             writeS(clanName);
             break;
         }
       }



       public S_PacketBox(int subCode, L1ItemInstance item, int type) {
         writeC(108);
         writeC(subCode);
         switch (subCode) {
           case 149:
             writeD(item.getId());
             writeH(type);
             break;
         }
       }

       public S_PacketBox(int subCode1, int subCode2, String name) {
         writeC(108);
         writeC(subCode1);
         switch (subCode2) {
           case 51:
           case 61:
             writeC(2);
             writeH(26204);
             writeC(subCode2);
             writeS(name);
             return;
           case 4:
             writeC(subCode2);
             writeS(name);
             return;
         }
         switch (subCode1) {
           case 27:
             writeC(subCode2);
             writeS(name);
             break;
           case 18:
           case 19:
             writeS(name);
             writeC(subCode2);
             break;
         }
       }


       public S_PacketBox(int subCode, int range, int type, boolean bow) {
         writeC(108);
         writeC(subCode);
         switch (subCode) {
           case 86:
             writeC(62);
             if (type == 0 || type == 2) {
               writeC(bow ? 1 : 2);
             } else {
               writeC(bow ? 1 : 3);
             }
             writeH(range);
             writeC(0);
             writeC(0);

             if (type == 0) {
               writeC(134); break;
             }
             writeC(73);
             break;

           case 194:
             writeD(range);
             writeD(type);
             writeD(bow ? 1 : 0);
             writeH(0);
             break;
         }
       }



       public S_PacketBox(int subCode, Object[] names) {
         writeC(108);
         writeC(subCode);

         switch (subCode) {
           case 171:
             writeH(names.length);
             for (Object name : names) {
               if (name != null) {

                 L1PcInstance pc = (L1PcInstance)name;
                 writeS(pc.getName());
                 writeC(0);
               }
             }
             break;
         }
       }


       public S_PacketBox(int subCode, String[] names, int type) {
         writeC(108);
         writeC(subCode);
         writeC(0);
         switch (subCode) {
           case 17:
             writeC(type);
             writeC(names.length);
             for (String name : names) {
               writeS(name);
             }
             writeH(0);
             break;
         }
       }

       public S_PacketBox(int subCode, L1ItemInstance item) {
         writeC(108);
         writeC(subCode);
         switch (subCode) {
           case 102:
             writeD(item.getId());
             writeC((item.getItemId() == 490012) ? 1 : 0);
             writeC((item.getItemId() == 490013) ? 1 : 0);
             writeC((item.getItemId() == 490014) ? 1 : 0);
             writeC(0);
             break;
           case 172:
             writeD(item.getId());
             writeC(24);
             writeC(0);
             writeH(0);
             writeH(0);
             writeC(item.getEnchantLevel());
             writeD(item.getId());
             writeD(0);
             writeD(0);
             writeD((item.getBless() >= 128) ? 3 : (item.getItem().isTradable() ? 7 : 2));
             writeC(0);
             writeC(item.getAttrEnchantBit(item.getAttrEnchantLevel()));
             writeH(0);
             break;
         }
       }



       public S_PacketBox(int subCode, L1PcInstance pc, int value1, int value2) {
         writeC(108);
         writeC(subCode);
         switch (subCode) {
           case 161:
             writeC(value1);
             writeD(value2);
             break;
         }
       } public S_PacketBox(L1PcInstance pc, int subCode) {
         int count;
         Connection con;
         PreparedStatement pstm, pstm2, pstm3;
         ResultSet rs, rs3;
         writeC(108);
         writeC(subCode);

         switch (subCode) {

           case 117:
             count = 0;
             con = null;
             pstm = null;
             pstm2 = null;
             pstm3 = null;
             rs = null;
             rs3 = null;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("SELECT id, time FROM clan_warehouse_log WHERE clan_name='" + pc.getClanname() + "'");
               rs = pstm.executeQuery();
               while (rs.next()) {
                 if (System.currentTimeMillis() - rs.getTimestamp(2).getTime() > 4320000L) {
                   pstm2 = con.prepareStatement("DELETE FROM clan_warehouse_log WHERE id='" + rs.getInt(1) + "'");
                   pstm2.execute(); continue;
                 }
                 count++;
               }
               writeD(count);

               pstm3 = con.prepareStatement("SELECT name, item_name, item_count, type, time FROM clan_warehouse_log WHERE clan_name='" + pc.getClanname() + "'");
               rs3 = pstm3.executeQuery();
               while (rs3.next()) {
                 writeS(rs3.getString(1));
                 writeC(rs3.getInt(4));
                 writeS(rs3.getString(2));
                 writeD(rs3.getInt(3));
                 writeD((System.currentTimeMillis() - rs3.getTimestamp(5).getTime()) / 60000L);
               }
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(rs3);
               SQLUtil.close(rs);
               SQLUtil.close(pstm3);
               SQLUtil.close(pstm2);
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
             break;


           case 26:
             writeS(pc.getName());
             writeC(pc.getClanRank());
             writeH(0);
             break;
           case 87:
             writeD(pc.getKarma());
             break;
         }
       }



       public S_PacketBox(int subCode, String name, int mapid, int x, int y, int Mid) {
         writeC(108);
         writeC(subCode);
         switch (subCode) {
           case 111:
             writeS(name);
             writeH(mapid);
             writeH(x);
             writeH(y);
             writeD(Mid);
             break;
         }
       }



       public S_PacketBox(int subCode, int value, boolean show) {
         writeC(108);
         writeC(subCode);
         switch (subCode) {
           case 114:
             writeD(value);
             writeD(show ? 1 : 0);
             break;
           case 147:
             writeC(show ? 1 : 0);
             writeC(value);
             break;
           case 180:
             writeC(show ? 1 : 0);
             writeD(value);
             writeD(0);
             writeH(0);
             break;
         }
       }



       public S_PacketBox(int subCode, boolean show) {
         writeC(108);
         writeC(subCode);
         switch (subCode) {
           case 165:
             writeC(show ? 1 : 0);
             break;
         }
       }

       public S_PacketBox(int subCode, int itemid, String name, List<L1BookMark> list) {
         writeC(108);
         writeC(subCode);
         switch (subCode) {
           case 142:
             writeC(0);
             writeD(itemid);
             writeS(name);
             writeC(list.size());
             for (L1BookMark book : list) {
               writeS(book.getName());
               writeD(book.getMapId());
               writeH(book.getLocX());
               writeH(book.getLocY());
             }
             break;
         }
       }


       private void callSomething() {
         Iterator<L1PcInstance> itr = L1World.getInstance().getAllPlayers().iterator();

         writeC(L1World.getInstance().getAllPlayers().size());
         L1PcInstance pc = null;
         Account acc = null;
         Calendar cal = null;
         while (itr.hasNext()) {
           pc = itr.next();
           acc = Account.load(pc.getAccountName());

           if (acc == null) {
             writeD(0);
           } else {
             cal = Calendar.getInstance(TimeZone.getTimeZone(Config.Synchronization.TimeZone));
             long lastactive = acc.getLastActive().getTime();
             cal.setTimeInMillis(lastactive);
             cal.set(1, 1970);
             int time = (int)(cal.getTimeInMillis() / 1000L);
             writeD(time);
           }


           writeS(pc.getName());
           writeS(pc.getClanname());
         }
       }

       public S_PacketBox(boolean run, int type, int time) {
         writeC(108);
         writeC(86);
         writeC(62);
         writeC(run ? 2 : 1);
         writeD(time);
         writeC(type);
       }

       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_PacketBox";
       }
     }


