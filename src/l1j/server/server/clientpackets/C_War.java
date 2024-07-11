 package l1j.server.server.clientpackets;

 import l1j.server.Config;
 import l1j.server.MJWarSystem.MJCastleWar;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.MJWarSystem.MJWar;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1CastleLocation;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_Message_YN;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_War extends ClientBasePacket {
     public C_War(byte[] abyte0, GameClient clientthread) throws Exception {
         super(abyte0);
         try {
             int type = readC(); // 讀取請求類型
             String s = readS(); // 讀取字符串
             L1PcInstance player = clientthread.getActiveChar(); // 獲取當前玩家實例

             if (player == null) {
                 return; // 如果玩家為空，則返回
             }

             if (type == 0) {
                 if (s.equalsIgnoreCase("紅色騎士團")) { // 如果字符串為 "붉은 기사단"
                     int castleId = L1CastleLocation.getCastleIdByArea((L1Character)player); // 根據玩家所在地區獲取城堡ID
                     if (castleId == 0) {
                         player.sendPackets("目前沒有進行中的攻城戰。"); // 如果城堡ID為0，則表示沒有進行中的攻城戰，發送提示信息並返回
                         return;
                     }
                     MJCastleWar mJCastleWar1 = MJCastleWarBusiness.getInstance().get(castleId); // 獲取對應城堡的攻城戰实例
                     if (mJCastleWar1 == null) {
                         player.sendPackets("目前沒有進行中的攻城戰。"); // 如果找不到攻城戰实例，發送提示信息並返回
                         return;
                     }
                     MJCastleWarBusiness.getInstance().proclaim(player, castleId); // 宣佈攻城戰
                     return;
                 }
             }
         } catch (Exception e) {
             e.printStackTrace(); // 捕捉並打印異常
         }
     }
 }
     MJCastleWar mJCastleWar = MJCastleWarBusiness.getInstance().findWar(s); // 根據名稱查找攻城戰实例
     L1Clan c = L1World.getInstance().findClan(s); // 根據名稱查找血盟实例

if (mJCastleWar != null) {
        MJCastleWarBusiness.getInstance().proclaim(player, mJCastleWar.getCastleId()); // 宣佈攻城戰
        } else if (c != null) {
        MJCastleWarBusiness.getInstance().proclaim(player, c.getCastleId()); // 宣佈攻城戰
        } else {
        player.sendPackets((ServerBasePacket)new S_SystemMessage(String.format("無法找到目標血盟。 [%s]", new Object[] { s }))); // 發送提示信息：無法找到目標血盟
        }

        return;
        }
        String playerName = player.getName(); // 獲取玩家名稱
        String clanName = player.getClanname(); // 獲取玩家所屬血盟名稱
        int clanId = player.getClanid(); // 獲取玩家所屬血盟ID

        if (player.getRedKnightClanId() != 0) {
        L1Clan l1Clan = L1World.getInstance().getClan(player.getRedKnightClanId()); // 根據紅色騎士血盟ID獲取血盟实例
        if (l1Clan == null) {
        player.sendPackets((ServerBasePacket)new S_SystemMessage("未找到目標血盟。"), true); // 如果找不到血盟实例，發送提示信息並重置紅色騎士血盟ID
        player.setRedKnightClanId(0);
        return;
        }
        clanName = l1Clan.getClanName(); // 重新設定血盟名稱
        clanId = l1Clan.getClanId(); // 重新設定血盟ID
        }

        if (!player.isCrown() && player.getRedKnightClanId() == 0) {
        player.sendPackets((ServerBasePacket)new S_ServerMessage(478)); // 如果玩家不是王族且未加入紅色騎士團，發送錯誤信息
        return;
        }
        if (clanId == 0) {
        player.sendPackets((ServerBasePacket)new S_ServerMessage(272)); // 如果血盟ID為0，發送錯誤信息
        return;
        }
        L1Clan clan = L1World.getInstance().getClan(clanId); // 根據血盟ID獲取血盟实例
        if (clan == null) {
        S_SystemMessage sm = new S_SystemMessage("未找到目標血盟。"); // 如果根據血盟ID找不到血盟实例，發送錯誤信息
        player.sendPackets((ServerBasePacket)sm);
        sm = null;
        return;
        }
       if (player.getId() != clan.getLeaderId() && player.getRedKnightClanId() == 0) {
         player.sendPackets((ServerBasePacket)new S_ServerMessage(478));

         return;
       }
       if (clanName.toLowerCase().equals(s.toLowerCase())) {
         return;
       }

        L1Clan enemyClan = null; // 初始化敵對血盟為空
        String enemyClanName = null; // 初始化敵對血盟名稱為空

// 遍歷所有血盟，查找名稱匹配的血盟
        for (L1Clan checkClan : L1World.getInstance().getAllClans()) {
        if (checkClan.getClanName().toLowerCase().equals(s.toLowerCase())) {
        enemyClan = checkClan; // 找到匹配的血盟
        enemyClanName = checkClan.getClanName(); // 設定敵對血盟名稱
        break; // 跳出循環
        }
        }

// 如果未找到匹配的血盟
        if (enemyClan == null) {
        S_SystemMessage sm = new S_SystemMessage("未找到目標血盟。"); // 發送提示信息：未找到目標血盟
        player.sendPackets((ServerBasePacket)sm);
        sm = null;
        return;
        }

        boolean inWar = false; // 初始化是否在戰爭狀態標誌為false
        MJWar war = clan.getCurrentWar(); // 獲取玩家血盟當前的戰爭实例

// 如果玩家血盟當前在戰爭中
        if (war != null) {
        if (type == 0) {
        player.sendPackets((ServerBasePacket)new S_ServerMessage(234)); // 發送提示信息：無法在戰爭中
        return;
        }
        inWar = true; // 設定為戰爭狀態
        }

       if (!inWar && (type == 2 || type == 3)) {
         return;
       }

       if (clan.getCastleId() != 0) {
         if (type == 0) {
           player.sendPackets((ServerBasePacket)new S_ServerMessage(474)); return;
         }
         if (type == 2 || type == 3) {
           return;
         }
       }


        // 如果敵方血盟沒有城堡，且玩家等級小於52
        if (enemyClan.getCastleId() == 0 && player.getLevel() < 52) {
         // 向玩家發送訊息包，告知他們不能進行操作
         player.sendPackets((ServerBasePacket)new S_ServerMessage(232));
         return;
         }

// 如果敵方血盟有城堡，但玩家等級小於設定的最低戰爭等級
         if (enemyClan.getCastleId() != 0 && player.getLevel() < Config.ServerAdSetting.WARMINLEVEL) {
         // 向玩家發送系統訊息，告知需要達到最低戰爭等級才能宣戰
         player.sendPackets((ServerBasePacket)new S_SystemMessage("\aA王子/公主 等級\aG[" + Config.ServerAdSetting.WARMINLEVEL + "]\aA 才能宣戰。"));
         return;
         }

// 如果玩家的紅騎士血盟ID為0，且血盟在線成員數量少於設定的最低人數
         if (player.getRedKnightClanId() == 0 && (clan.getOnlineClanMember()).length <= Config.ServerAdSetting.WARPLAYER) {
         // 向玩家發送系統訊息，告知需要在線的血盟成員數量達到設定的最低人數才能宣戰
         player.sendPackets((ServerBasePacket)new S_SystemMessage("在線的血盟成員數量需要達到 " + Config.ServerAdSetting.WARPLAYER + " 人以上才能宣戰。"));
         return;
         }

       MJWar enemyWar = enemyClan.getCurrentWar();
       if (enemyClan.getCastleId() == 0) {

         boolean enemyInWar = false;
         if (enemyClan.getCurrentWar() != null) {
           if (type == 0) {
             player.sendPackets((ServerBasePacket)new S_ServerMessage(236, enemyClanName)); return;
           }
           if ((type == 2 || type == 3) &&
             war.getId() != enemyWar.getId()) {
             return;
           }

           enemyInWar = true;
         }

         if (!enemyInWar && (type == 2 || type == 3)) {
           return;
         }


         L1PcInstance enemyLeader = L1World.getInstance().getPlayer(enemyClan.getLeaderName());

         if (enemyLeader == null) {
           player.sendPackets((ServerBasePacket)new S_ServerMessage(218, enemyClanName));

           return;
         }
         if (type == 0) {
           enemyLeader.setTempID(player.getId());
           enemyLeader.sendPackets((ServerBasePacket)new S_Message_YN(217, clanName, playerName));
         } else if (type == 2) {
           enemyLeader.setTempID(player.getId());
           enemyLeader.sendPackets((ServerBasePacket)new S_Message_YN(221, clanName));
         } else if (type == 3) {
           enemyLeader.setTempID(player.getId());
           enemyLeader.sendPackets((ServerBasePacket)new S_Message_YN(222, clanName));
         }
       }
     } catch (Exception exception) {

     } finally {
       clear();
     }
   }
   private static final String C_WAR = "[C] C_War";

   public String getType() {
     return "[C] C_War";
   }
 }


