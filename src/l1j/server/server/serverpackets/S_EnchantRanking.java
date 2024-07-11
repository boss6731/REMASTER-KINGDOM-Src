     package l1j.server.server.serverpackets;

     import java.sql.Connection;
     import java.sql.PreparedStatement;
     import java.sql.ResultSet;
     import java.util.Calendar;
     import java.util.TimeZone;
     import l1j.server.Config;
     import l1j.server.L1DatabaseFactory;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.utils.SQLUtil;



     public class S_EnchantRanking
       extends ServerBasePacket
     {
       private static final String S_EnchantRanking = "[C] S_EnchantRanking";
       private byte[] _byte = null;
       private int j = 0;
       static String[] name;
       static String[] name1;
       static String[] castlename;
       static String[] clanname;
       static String[] leadername;
       static int[] enchantlvl;
       static int[] aden;
       static int[] armor;
       static int[] level;
       static int[] Ac;
       static int[] priaden;
       static int[] castleid;
       static int[] hascastle;
       static int[] taxrate;
       static int[] castleaden;
       static int[] MaxHp;
       static int[] MaxMp;

       public S_EnchantRanking(L1PcInstance pc, int number) {
         name = new String[10];
         name1 = new String[10];
         enchantlvl = new int[10];
         aden = new int[10];
         armor = new int[10];
         level = new int[10];
         Ac = new int[10];
         priaden = new int[10];
         castlename = new String[10];
         clanname = new String[10];
         leadername = new String[10];
         castleid = new int[10];
         hascastle = new int[10];
         taxrate = new int[10];
         castleaden = new int[10];
         MaxHp = new int[10];
         MaxMp = new int[10];
         buildPacket(pc, number);
       }

       private void buildPacket(L1PcInstance pc, int number) {
         // 獲取當前時間，並將其格式化為字符串
         String date = time();
         // 初始化類型和標題變數
         String type = null;
         String title = null;

         // 寫入操作碼 248
         writeC(248);
         // 寫入編號
         writeD(number);
         // 寫入字符串 "GM"
         writeS("GM");

         // 根據編號設置標題
         switch (number) {
           case 1:
             title = "強化排名"; // 強化排名 "인첸 랭킹"
             break;
           case 2:
             title = "防具排名"; // 防具排名  "방어구 랭킹"
             break;
           case 3:
             title = "金幣排名"; // 阿登排名 "아덴 랭킹"
             break;
           case 4:
             title = "等級排名"; // 等級排名 "레벨 랭킹"
             break;
           case 5:
             title = "神秘羽毛排名"; // 神秘羽毛排名 "신비깃털 랭킹"
             break;
           case 6:
             title = "倉庫金幣排名"; // 倉庫阿登排名  "창고아덴랭킹"
             break;
           case 7:
             title = "HP排名"; // HP排名 "HP 랭킹"
             break;
           case 8:
             title = "MP排名"; // MP排名 "MP 랭킹"
             break;
           default:
             title = "未知排名"; // 未知排名 "알 수 없는 랭킹"
             break;
         }
         // 將標題和日期寫入，這裡假設 writeS 是一個方法，用於寫入字符串數據
         writeS(title);
         writeS(date);

          // 根據角色類型設置變量 type 的值
         switch (pc.getType()) {
           case 0:
             type = "王族"; // 軍主
             break;
           case 1:
             type = "騎士"; // 騎士
             break;
           case 2:
             type = "妖精"; // 妖精
             break;
           case 3:
             type = "法師"; // 魔法師
             break;
           case 4:
             type = "黑暗精靈"; // 黑暗精靈
             break;
           case 5:
             type = "龍騎士"; // 龍騎士
             break;
           case 6:
             type = "幻術師"; // 幻術師
             break;
           case 7:
             type = "戰士"; // 戰士
           case 8:
             type = "劍士"; // 劍士
             break;
           case 9:
             type = "黃金槍騎"; // 黃金槍騎
             break;
           default:
             type = "未知類型"; // 未知類型
             break;
         }

          // 這段代碼缺少的部分可能包括變量 type 的聲明和初始化。可以考慮以下完整例子：

          // 假設 type 是一個字符串變量，已經聲明
         String type;

          // 將標題和日期寫入
         writeS(title);
         writeS(date);

        // 根據角色類型設置變量 type 的值
         switch (pc.getType()) {
           case 0:
             type = "王族"; // 軍主"군주"
             break;
           case 1:
             type = "騎士"; // 騎士"기사"
             break;
           case 2:
             type = "妖精"; // 妖精"요정"
             break;
           case 3:
             type = "法師"; // 魔法師"마법사"
             break;
           case 4:
             type = "黑暗精靈"; // 黑暗精靈"다크엘프"以下新增五個職業類型5-9
             break;
           case 5:
             type = "龍騎士"; // 龍騎士
             break;
           case 6:
             type = "幻術師"; // 幻術師
             break;
           case 7:
             type = "戰士"; // 戰士
           case 8:
             type = "劍士"; // 劍士
             break;
           case 9:
             type = "黃金槍騎"; // 黃金槍騎
             break;
           default:
             type = "未知類型"; // 未知類型"알 수 없는 타입"
             break;
         }

// 這段代碼會根據 pc.getType() 返回的數值，將 type 設置為相應的角色類型名稱。
         int p = Rank(pc, number);
         if (number == 1) {
           writeS("\n\r  第一名 +" + enchantlvl[0] + " " + name[0] + "\n\r  擁有者 : " + name1[0] + "\n\r  第二名 +" + enchantlvl[1] + " " + name[1] + "\n\r  擁有者 : " + name1[1] + "\n\r  第三名 +" + enchantlvl[2] + " " + name[2] + "\n\r  擁有者 : " + name1[2] + "\n\r  第四名 +" + enchantlvl[3] + " " + name[3] + "\n\r  擁有者 : " + name1[3] + "\n\r  第五名 +" + enchantlvl[4] + " " + name[4] + "\n\r  擁有者 : " + name1[4] + "\n\r  第六名 +" + enchantlvl[5] + " " + name[5] + "\n\r  擁有者 : " + name1[5] + "\n\r  第七名 +" + enchantlvl[6] + " " + name[6] + "\n\r  擁有者 : " + name1[6] + "\n\r  第八名 +" + enchantlvl[7] + " " + name[7] + "\n\r  擁有者 : " + name1[7] + "\n\r  第九名 +" + enchantlvl[8] + " " + name[8] + "\n\r  擁有者 : " + name1[8] + "\n\r 第十名 +" + enchantlvl[9] + " " + name[9] + "\n\r  擁有者 : " + name1[9] + "\n\r      ");





         }
         else if (number == 2) {
           writeS("\n\r  第一名 +" + armor[0] + " " + name[0] + "\n\r  擁有者 : " + name1[0] + "\n\r  第二名 +" + armor[1] + " " + name[1] + "\n\r  擁有者 : " + name1[1] + "\n\r  第三名 +" + armor[2] + " " + name[2] + "\n\r  擁有者 : " + name1[2] + "\n\r  第四名 +" + armor[3] + " " + name[3] + "\n\r  擁有者 : " + name1[3] + "\n\r  第五名 +" + armor[4] + " " + name[4] + "\n\r  擁有者 : " + name1[4] + "\n\r  第六名 +" + armor[5] + " " + name[5] + "\n\r  擁有者 : " + name1[5] + "\n\r  第七名 +" + armor[6] + " " + name[6] + "\n\r  擁有者 : " + name1[6] + "\n\r  第八名 +" + armor[7] + " " + name[7] + "\n\r  擁有者 : " + name1[7] + "\n\r  第九名 +" + armor[8] + " " + name[8] + "\n\r  擁有者 : " + name1[8] + "\n\r 第十名 +" + armor[9] + " " + name[9] + "\n\r  擁有者 : " + name1[9] + "\n\r      ");





         }
         else if (number == 3) {
           writeS("\n\r  第一名 $ " + aden[0] + " 金幣\n\r  擁有者 : " + name[0] + "\n\r  第二名 $ " + aden[1] + " 金幣\n\r  擁有者 : " + name[1] + "\n\r  第三名 $ " + aden[2] + " 金幣\n\r  擁有者 : " + name[2] + "\n\r  第四名 $ " + aden[3] + " 金幣\n\r  擁有者 : " + name[3] + "\n\r  第五名 $ " + aden[4] + " 金幣\n\r  擁有者 : " + name[4] + "\n\r  第六名 $ " + aden[5] + " 金幣\n\r  擁有者 : " + name[5] + "\n\r  第七名 $ " + aden[6] + " 金幣\n\r  擁有者 : " + name[6] + "\n\r  第八名 $ " + aden[7] + " 金幣\n\r  擁有者 : " + name[7] + "\n\r  第九名 $ " + aden[8] + " 金幣\n\r  擁有者 : " + name[8] + "\n\r 第十名 $ " + aden[9] + " 金幣\n\r  擁有者 : " + name[9] + "\n\r      ");




         }
         else if (number == 4) {
           writeS("\n\r  第一名 " + name[0] + " \n\r  當前等級 : " + level[0] + "\n\r  第二名 " + name[1] + " \n\r  當前等級 : " + level[1] + "\n\r  第三名 " + name[2] + " \n\r  當前等級 : " + level[2] + "\n\r  第四名 " + name[3] + " \n\r  當前等級 : " + level[3] + "\n\r  第五名 " + name[4] + " \n\r  當前等級 : " + level[4] + "\n\r  第六名 " + name[5] + " \n\r  當前等級 : " + level[5] + "\n\r  第七名 " + name[6] + " \n\r  當前等級 : " + level[6] + "\n\r  第八名 " + name[7] + " \n\r  當前等級 : " + level[7] + "\n\r  第九名 " + name[8] + " \n\r  當前等級 : " + level[8] + "\n\r 第十名 " + name[9] + " \n\r  當前等級 : " + level[9] + "\n\r      ");



         }
         else if (number == 5) {
           writeS("\n\r  第一名 " + priaden[0] + "羽毛\n\r  擁有者 : " + name[0] + "\n\r  第二名 " + priaden[1] + "羽毛\n\r  擁有者 : " + name[1] + "\n\r  第三名 " + priaden[2] + "羽毛\n\r  擁有者 : " + name[2] + "\n\r  第四名 " + priaden[3] + "羽毛\n\r  擁有者 : " + name[3] + "\n\r  第五名 " + priaden[4] + "羽毛\n\r  擁有者 : " + name[4] + "\n\r  第六名 " + priaden[5] + "羽毛\n\r  擁有者 : " + name[5] + "\n\r  第七名 " + priaden[6] + "羽毛\n\r  擁有者 : " + name[6] + "\n\r  第八名 " + priaden[7] + "羽毛\n\r  擁有者 : " + name[7] + "\n\r  第九名 " + priaden[8] + "羽毛\n\r  擁有者 : " + name[8] + "\n\r 第十名 " + priaden[9] + "羽毛\n\r  擁有者 : " + name[9] + "\n\r      ");




         }
         else if (number == 6) {
           writeS("\n\r  第一名 $ : " + priaden[0] + " 金幣\n\r  帳號名稱 : " + name[0] + "\n\r  第二名 $ : " + priaden[1] + " 金幣\n\r  帳號名稱 : " + name[1] + "\n\r  第三名 $ : " + priaden[2] + " 金幣\n\r  帳號名稱 : " + name[2] + "\n\r  第四名 $ : " + priaden[3] + " 金幣\n\r  帳號名稱 : " + name[3] + "\n\r  第五名 $ : " + priaden[4] + " 金幣\n\r  帳號名稱 : " + name[4] + "\n\r  第六名 $ : " + priaden[5] + " 金幣\n\r  帳號名稱 : " + name[5] + "\n\r  第七名 $ : " + priaden[6] + " 金幣\n\r  帳號名稱 : " + name[6] + "\n\r  第八名 $ : " + priaden[7] + " 金幣\n\r  帳號名稱 : " + name[7] + "\n\r  第九名 $ : " + priaden[8] + " 金幣\n\r  帳號名稱 : " + name[8] + "\n\r 第十名 $ : " + priaden[9] + " 金幣\n\r  帳號名稱 : " + name[9] + "\n\r      ");




         }
         else if (number == 7) {
           writeS("\n\r  第一名. " + name[0] + " " + MaxHp[0] + "\n\r  第二名. " + name[1] + " " + MaxHp[1] + "\n\r  第三名. " + name[2] + " " + MaxHp[2] + "\n\r  第四名. " + name[3] + " " + MaxHp[3] + "\n\r  第五名. " + name[4] + " " + MaxHp[4] + "\n\r  第六名. " + name[5] + " " + MaxHp[5] + "\n\r  第七名. " + name[6] + " " + MaxHp[6] + "\n\r  第八名. " + name[7] + " " + MaxHp[7] + "\n\r  第九名. " + name[8] + " " + MaxHp[8] + "\n\r 第十名. " + name[9] + " " + MaxHp[9] + "\n\r      ");


         }
         else if (number == 8) {
           writeS("\n\r  第一名. " + name[0] + " " + MaxMp[0] + "\n\r  第二名. " + name[1] + " " + MaxMp[1] + "\n\r  第三名. " + name[2] + " " + MaxMp[2] + "\n\r  第四名. " + name[3] + " " + MaxMp[3] + "\n\r  第五名. " + name[4] + " " + MaxMp[4] + "\n\r  第六名. " + name[5] + " " + MaxMp[5] + "\n\r  第七名. " + name[6] + " " + MaxMp[6] + "\n\r  第八名. " + name[7] + " " + MaxMp[7] + "\n\r  第九名. " + name[8] + " " + MaxMp[8] + "\n\r 第十名. " + name[9] + " " + MaxMp[9] + "\n\r      ");
         }
       }





       private int Rank(L1PcInstance pc, int number) {
         Connection con = null;
         PreparedStatement pstm = null;
         ResultSet rs = null;
         int objid = pc.getId();
         int i = 0;
         try {
           con = L1DatabaseFactory.getInstance().getConnection();
           switch (number) {
             case 1:
               pstm = con.prepareStatement("SELECT enchantlvl, weapon.name, characters.char_name  FROM character_items, weapon, characters WHERE character_items.item_id in(select item_id from weapon) And character_items.char_id in(select objid from characters where AccessLevel = 0) And character_items.item_id=weapon.item_id And character_items.char_id=characters.objid And count = 1 order by character_items.enchantlvl desc limit 10");
               break;
             case 2:
               pstm = con.prepareStatement("SELECT enchantlvl, armor.name, characters.char_name  FROM character_items, armor, characters WHERE character_items.item_id in(select item_id from armor) And character_items.char_id in(select objid from characters where AccessLevel = 0) And character_items.item_id=armor.item_id And character_items.char_id=characters.objid And count = 1 order by character_items.enchantlvl desc limit 10");
               break;
             case 3:
               pstm = con.prepareStatement("SELECT count, characters.char_name FROM character_items, characters WHERE item_id in(select item_id from etcitem) And char_id in(select objid from characters where AccessLevel = 0) And character_items.char_id=characters.objid And item_id = 40308 order by count desc limit 10");
               break;
             case 4:
               pstm = con.prepareStatement("SELECT level, char_name FROM characters WHERE AccessLevel = 0 order by level desc limit 10");
               break;
             case 5:
               pstm = con.prepareStatement("SELECT count, characters.char_name FROM character_items, characters WHERE item_id in(select item_id from etcitem) And char_id in(select objid from characters where AccessLevel = 0) And character_items.char_id=characters.objid And item_id = 41921 order by count desc limit 10");
               break;
             case 6:
               pstm = con.prepareStatement("SELECT count, accounts.login FROM character_warehouse, accounts WHERE  login in(select login from accounts where access_level = 0) And character_warehouse.account_name =accounts.login And item_id = 40308 order by count desc limit 10");
               break;
             case 7:
               pstm = con.prepareStatement("SELECT MaxHp, char_name FROM characters WHERE AccessLevel = 0 order by MaxHp desc limit 10");
               break;
             case 8:
               pstm = con.prepareStatement("SELECT MaxMp, char_name FROM characters WHERE AccessLevel = 0 order by MaxMp desc limit 10");
               break;
             default:
               pstm = con.prepareStatement("SELECT char_name FROM characters WHERE AccessLevel = 0 order by Exp desc limit 10");
               break;
           }

           rs = pstm.executeQuery();
           if (number == 1) {
             while (rs.next()) {
               enchantlvl[i] = rs.getInt(1);
               name[i] = rs.getString(2);
               name1[i] = rs.getString(3);
               i++;
             }
           } else if (number == 2) {
             while (rs.next()) {
               armor[i] = rs.getInt(1);
               name[i] = rs.getString(2);
               name1[i] = rs.getString(3);
               i++;
             }
           } else if (number == 3) {
             while (rs.next()) {
               aden[i] = rs.getInt(1);
               name[i] = rs.getString(2);
               i++;
             }
           } else if (number == 4) {
             while (rs.next()) {
               level[i] = rs.getInt(1);
               name[i] = rs.getString(2);
               i++;
             }
           } else if (number == 5) {
             while (rs.next()) {
               priaden[i] = rs.getInt(1);
               name[i] = rs.getString(2);
               i++;
             }
           } else if (number == 6) {
             while (rs.next()) {
               priaden[i] = rs.getInt(1);
               name[i] = rs.getString(2);
               i++;
             }

           }
           else if (number == 7) {
             while (rs.next()) {
               MaxHp[i] = rs.getInt(1);
               name[i] = rs.getString(2);
               i++;
             }
           } else if (number == 8) {
             while (rs.next()) {
               MaxMp[i] = rs.getInt(1);
               name[i] = rs.getString(2);
               i++;
             }
           } else {

             while (rs.next()) {
               name[i] = rs.getString(1);
               i++;
             }


             while (i < 10) {
               name[i] = "不存在.";
               i++;
             }
           }
         } catch (Exception e) {
           e.printStackTrace();
         } finally {

           SQLUtil.close(rs);
           SQLUtil.close(pstm);
           SQLUtil.close(con);
         }

         return i;
       }
       private static String time() {
         String year2;
         TimeZone tz = TimeZone.getTimeZone(Config.Synchronization.TimeZone);
         Calendar cal = Calendar.getInstance(tz);
         int year = cal.get(1) - 2000;

         if (year < 10) {
           year2 = "0" + year;
         } else {
           year2 = Integer.toString(year);
         }
         int Month = cal.get(2) + 1;
         String Month2 = null;
         if (Month < 10) {
           Month2 = "0" + Month;
         } else {
           Month2 = Integer.toString(Month);
         }
         int date = cal.get(5);
         String date2 = null;
         if (date < 10) {
           date2 = "0" + date;
         } else {
           date2 = Integer.toString(date);
         }
         return year2 + "/" + Month2 + "/" + date2;
       }


       public byte[] getContent() {
         if (this._byte == null) {
           this._byte = getBytes();
         }
         return this._byte;
       }

       public String getType() {
         return "[C] S_EnchantRanking";
       }
     }


