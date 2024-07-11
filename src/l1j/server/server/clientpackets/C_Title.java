 package l1j.server.server.clientpackets;

 import l1j.server.Config;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Broadcaster;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_CharTitle;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;




 public class C_Title
   extends ClientBasePacket
 {
   public static final int CLAN_RANK_GUARDIAN = 9;
   public static final int CLAN_RANK_SUBLEADER = 3;
   private static final String C_TITLE = "[C] C_Title";

     public C_Title(byte[] abyte0, GameClient clientthread) {
         super(abyte0);

         // 獲取當前玩家實例
         L1PcInstance pc = clientthread.getActiveChar();
         if (pc == null) {
             return;
         }

         // 讀取角色名稱和稱號
         String charName = readS();
         String title = readS();

         // 檢查稱號的長度是否超過16個字符
         if (title.length() > 16) {
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("稱號的字數超過了允許的最大限制。"));
             return;
         }

         // 這裡可以繼續添加對其他條件的檢查和處理
         // 例如檢查角色是否存在、是否有權限更改稱號等
     }
     public C_Title(byte[] abyte0, GameClient clientthread) {
         super(abyte0);

// 獲取當前玩家實例
         L1PcInstance pc = clientthread.getActiveChar();
         if (pc == null) {
             return;
         }

// 讀取角色名稱和稱號
         String charName = readS();
         String title = readS();

// 檢查稱號長度是否超過16個字符
         if (title.length() > 16) {
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("稱號的字數超過了允許的最大限制。"));
             return;
         }

// 檢查角色名稱或稱號是否為空
         if (charName.isEmpty() || title.isEmpty()) {
             pc.sendPackets((ServerBasePacket)new S_ServerMessage(196));  // 196: 無效的名稱
             return;
         }

// 查找目標角色
         L1PcInstance target = L1World.getInstance().getPlayer(charName);
         if (target == null) {
             return;
         }

// 檢查目標角色是否有特定的技能效果（如被通緝）
         if (target.hasSkillEffect(32423423) || target.hasSkillEffect(32423424) || target.hasSkillEffect(32423425)) {
             pc.sendPackets("通緝期間無法更改稱號。");
             return;
         }

// 如果當前玩家是GM，則可以直接更改稱號
         if (pc.isGm()) {
             changeTitle(target, title);
             return;
         }

// 其他非GM玩家的操作
// 可以在這裡添加更多的邏輯，如檢查玩家是否有相應的權限等
     }

     private void changeTitle(L1PcInstance target, String title) {
// 更改目標角色的稱號
         target.setTitle(title);
// 保存更改並更新客戶端顯示
         target.save();
         target.sendPackets(new S_CharTitle(target.getId(), title));
         L1World.getInstance().broadcastPacketToAll(new S_CharTitle(target.getId(), title));
     }
     if (isClanLeader(pc) || (pc.getClanid() == target.getClanid() && pc.getClanRank() == 9) || pc.getClanRank() == 14) {
       if (pc.getId() == target.getId()) {
         if (pc.getLevel() < 10) {

           pc.sendPackets((ServerBasePacket)new S_ServerMessage(197));
           return;
         }
         changeTitle(pc, title);
       } else {
         if (pc.getClanid() != target.getClanid()) {

           pc.sendPackets((ServerBasePacket)new S_ServerMessage(199));
           return;
         }
         if (target.getLevel() < 10) {

           pc.sendPackets((ServerBasePacket)new S_ServerMessage(202, charName));
           return;
         }
         changeTitle(target, title);
         L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
         if (clan != null) {
           for (L1PcInstance clanPc : clan.getOnlineClanMember())
           {
             clanPc.sendPackets((ServerBasePacket)new S_ServerMessage(203, pc
                   .getName(), charName, title));
           }
         }
       }

     } else if (pc.getClanRank() == 6 || pc.getClanRank() == 3) {
       if (pc.getId() == target.getId()) {
         if (pc.getLevel() < 10) {

           pc.sendPackets((ServerBasePacket)new S_ServerMessage(197));
           return;
         }
         changeTitle(pc, title);
       } else {
         if (pc.getClanid() != target.getClanid()) {

           pc.sendPackets((ServerBasePacket)new S_ServerMessage(199));
           return;
         }
         if (target.getLevel() < 10) {

           pc.sendPackets((ServerBasePacket)new S_ServerMessage(202, charName));
           return;
         }
         changeTitle(target, title);
         L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
         if (clan != null) {
           for (L1PcInstance clanPc : clan.getOnlineClanMember())
           {
             clanPc.sendPackets((ServerBasePacket)new S_ServerMessage(203, pc.getName(), charName, title));
           }
         }
       }

     }
     public C_Title(byte[] abyte0, GameClient clientthread) {
         super(abyte0);

// 獲取當前玩家實例
         L1PcInstance pc = clientthread.getActiveChar();
         if (pc == null) {
             return;
         }

// 讀取角色名稱和稱號
         String charName = readS();
         String title = readS();

// 檢查稱號長度是否超過16個字符
         if (title.length() > 16) {
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("稱號的字數超過了允許的最大限制。"));
             return;
         }

// 檢查角色名稱或稱號是否為空
         if (charName.isEmpty() || title.isEmpty()) {
             pc.sendPackets((ServerBasePacket)new S_ServerMessage(196));  // 196: 無效的名稱
             return;
         }

// 查找目標角色
         L1PcInstance target = L1World.getInstance().getPlayer(charName);
         if (target == null) {
             return;
         }

// 檢查目標角色是否有特定的技能效果（如被通緝）
         if (target.hasSkillEffect(32423423) || target.hasSkillEffect(32423424) || target.hasSkillEffect(32423425)) {
             pc.sendPackets("通緝期間無法更改稱號。");
             return;
         }

// 如果當前玩家是GM，則可以直接更改稱號
         if (pc.isGm()) {
             changeTitle(target, title);
             return;
         }

// 檢查當前玩家是否試圖更改自己的稱號
         else if (pc.getId() == target.getId()) {
             // 檢查玩家是否屬於某個血盟且服務器配置不允許更改稱號
             if (pc.getClanid() != 0 && !Config.ServerAdSetting.CLANPCTITLESETTING) {
                 pc.sendPackets((ServerBasePacket)new S_ServerMessage(198)); // 198: 無效的操作
                 return;
             }
             // 檢查目標玩家等級是否小於40
             if (target.getLevel() < 40) {
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("不是新手並且稱號需要至少40級。"));
                 return;
             }
             // 更改稱號
             changeTitle(pc, title);
         }

// 檢查當前玩家是否為王族且與目標玩家屬於同一血盟
         else if (pc.isCrown() && pc.getClanid() == target.getClanid()) {
             // 發送系統提示信息
             pc.sendPackets((ServerBasePacket)new S_ServerMessage(201, pc.getClanname())); // 201: 王族操作失敗
             return;
         }

// 其他非GM玩家的操作
// 可以在這裡添加更多的邏輯，如檢查玩家是否有相應的權限等
     }

     private void changeTitle(L1PcInstance target, String title) {
// 更改目標角色的稱號
         target.setTitle(title);
// 保存更改並更新客戶端顯示
         target.save();
         target.sendPackets(new S_CharTitle(target.getId(), title));
         L1World.getInstance().broadcastPacketToAll(new S_CharTitle(target.getId(), title));
     }




     private void changeTitle(L1PcInstance pc, String title) {
         int objectId = pc.getId();  // 獲取玩家的ID
         pc.setTitle(title);  // 設置新稱號

         String broadcastTitle = title;  // 準備廣播的稱號
         // 根據技能效果設定特殊稱號
         if (pc.hasSkillEffect(32423423)) {
             broadcastTitle = "\fe[緊急通緝中][1級]";
         } else if (pc.hasSkillEffect(32423424)) {
             broadcastTitle = "\fe[緊急通緝中][2級]";
         } else if (pc.hasSkillEffect(32423425)) {
             broadcastTitle = "\fe[緊急通緝中][3級]";
         }

         // 發送稱號更改信息給玩家和其他玩家
         pc.sendPackets((ServerBasePacket)new S_CharTitle(objectId, broadcastTitle));
         Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)new S_CharTitle(objectId, broadcastTitle));

         try {
             pc.save();  // 保存玩家數據
         } catch (Exception e) {
             e.printStackTrace();  // 捕獲並打印異常信息
         }
     }

   private boolean isClanLeader(L1PcInstance pc) {
     boolean isClanLeader = false;
     if (pc.getClanid() != 0) {
       L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
       if (clan != null &&
         pc.isCrown() && pc.getId() == clan.getLeaderId())
       {

         isClanLeader = true;
       }
     }

     return isClanLeader;
   }



   public String getType() {
     return "[C] C_Title";
   }
 }


