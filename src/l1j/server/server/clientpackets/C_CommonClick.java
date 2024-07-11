 package l1j.server.server.clientpackets;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.Timestamp;
 import java.util.Calendar;
 import l1j.server.Config;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJNetServer.MJClientEntranceService;
 import l1j.server.MJRankSystem.Business.MJRankBusiness;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.CharacterTable;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_CharAmount;
 import l1j.server.server.serverpackets.S_CharPacks;
 import l1j.server.server.serverpackets.S_CharPass;
 import l1j.server.server.serverpackets.S_NewCreateItem;
 import l1j.server.server.serverpackets.S_PssConfig;
 import l1j.server.server.serverpackets.S_Unknown2;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.SQLUtil;

 public class C_CommonClick {
   private static final String C_COMMON_CLICK = "[C] C_CommonClick";

   public C_CommonClick(final GameClient client) {
     if (client == null || client.getAccount() == null) {
       return;
     }
     deleteCharacter(client);

     if (!MJClientEntranceService.service().useWaitQueue()) {
       onCharListEnter(client);

       return;
     }
     MJClientEntranceService.service().offer(client, new Runnable()
         {
           public void run() {
             C_CommonClick.onCharListEnter(client);
           }
         });
   }

   private static void onCharListEnter(GameClient client) {
     try {
       int amountOfChars = client.getAccount().countCharacters();
       int slot = client.getAccount().getCharSlot();

       if (Config.Login.CharPassword && (client.getAccount().getCPW() == null || client.getAccount().getCPW().equalsIgnoreCase(""))) {
         client.sendPacket((ServerBasePacket)new S_CharPass(22));
       }
       client.sendPacket((ServerBasePacket)S_PssConfig.CONFIG);

       client.sendPacket((ServerBasePacket)new S_CharAmount(amountOfChars, slot));
       client.sendPacket((ServerBasePacket)new S_CharPass(10));
       if (amountOfChars > 0) {
         sendCharPacks(client);
       }
       client.sendPacket((ServerBasePacket)new S_CharPass(64));

       client.getAccount().탐포인트업데이트(client.getAccount());
       client.sendPacket((ServerBasePacket)new S_NewCreateItem(450, client));
       client.sendPacket((ServerBasePacket)new S_Unknown2(0));
     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   public static void sendCharPacks(GameClient client) {
     Connection conn = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

       try {
           conn = L1DatabaseFactory.getInstance().getConnection(); // 註解: 獲取數據庫連接
           pstm = conn.prepareStatement("SELECT * FROM characters WHERE account_name=? ORDER BY objid"); // 註解: 準備SQL查詢語句以根據賬戶名查找角色
           pstm.setString(1, client.getAccountName()); // 註解: 設置SQL查詢語句中的賬戶名參數
           rs = pstm.executeQuery(); // 註解: 執行查詢並獲取結果集
           S_CharPacks cpk = null; // 註解: 初始化角色包對象
           while (rs.next()) { // 註解: 遍歷結果集中的每一行
               int lvl;
               String name = rs.getString("char_name"); // 註解: 獲取角色名稱
               String clanname = rs.getString("Clanname"); // 註解: 獲取血盟名稱
               int type = rs.getInt("Type"); // 註解: 獲取角色類型
               byte sex = rs.getByte("Sex"); // 註解: 獲取角色性別
               int lawful = rs.getInt("Lawful"); // 註解: 獲取角色正義值

               int currenthp = rs.getInt("CurHp"); // 註解: 獲取角色當前HP
               int currentmp = rs.getInt("CurMp"); // 註解: 獲取角色當前MP

               if (Config.Login.CharacterConfigInServerSide) { // 註解: 如果伺服器側配置啟用
                   lvl = rs.getInt("level"); // 註解: 獲取角色等級
                   if (lvl < 1) { // 註解: 如果等級小於1
                       lvl = 1; // 註解: 將等級設置為1
                   } else if (lvl > 127) { // 註解: 如果等級大於127
                       lvl = 127; // 註解: 將等級設置為127
                   }
               } else { // 註解: 如果伺服器側配置未啟用
                   lvl = 1; // 註解: 將等級設置為1
               }

               int ac = rs.getInt("Ac"); // 註解: 獲取角色防禦值
               int str = rs.getByte("Str"); // 註解: 獲取角色力量
               int dex = rs.getByte("Dex"); // 註解: 獲取角色敏捷
               int con = rs.getByte("Con"); // 註解: 獲取角色體質
               int wis = rs.getByte("Wis"); // 註解: 獲取角色智慧
               int cha = rs.getByte("Cha"); // 註解: 獲取角色魅力
               int intel = rs.getByte("Intel"); // 註解: 獲取角色智力
               int accessLevel = rs.getShort("AccessLevel"); // 註解: 獲取角色訪問等級
               int birth = rs.getInt("BirthDay"); // 註解: 獲取角色生日
               Timestamp deleteStamp = rs.getTimestamp("DeleteTime"); // 註解: 獲取角色刪除時間戳

               MJRankBusiness.getInstance().noti(client, rs.getInt("objid")); // 註解: 通知排名業務模塊角色信息
           }


         cpk = new S_CharPacks(name, clanname, type, sex, lawful, currenthp, currentmp, ac, lvl, str, dex, con, wis, cha, intel, accessLevel, birth, (deleteStamp == null) ? 0 : (int)((deleteStamp.getTime() - System.currentTimeMillis()) / 1000L));
         client.sendPacket((ServerBasePacket)cpk);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(conn);
     }
   }

   private void deleteCharacter(GameClient client) {
     Connection conn = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       conn = L1DatabaseFactory.getInstance().getConnection();
       pstm = conn.prepareStatement("SELECT * FROM characters WHERE account_name=? ORDER BY objid");
       pstm.setString(1, client.getAccountName());
       rs = pstm.executeQuery();
       Timestamp deleteTime = null;
       Calendar cal = null;
       L1Clan clan = null;
       while (rs.next()) {
         String name = rs.getString("char_name");
         String clanname = rs.getString("Clanname");

         deleteTime = rs.getTimestamp("DeleteTime");
         if (deleteTime != null) {
           cal = Calendar.getInstance();
           long checkDeleteTime = (cal.getTimeInMillis() - deleteTime.getTime()) / 1000L / 3600L;
           if (checkDeleteTime >= 0L) {
             clan = L1World.getInstance().findClan(clanname);
             if (clan != null) {
               clan.removeClanMember(name);
             }
             CharacterTable.getInstance().deleteCharacter(client.getAccountName(), name);
           }
         }
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(conn);
     }
   }

   public String getType() {
     return "[C] C_CommonClick";
   }
 }


