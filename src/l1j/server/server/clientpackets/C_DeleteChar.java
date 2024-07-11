 package l1j.server.server.clientpackets;

 import MJShiftObject.MJShiftObjectManager;
 import MJShiftObject.Object.MJShiftObject;
 import java.sql.Timestamp;
 import l1j.server.Config;
 import l1j.server.MJBookQuestSystem.Loader.BQSCharacterDataLoader;
 import l1j.server.MJNetServer.Codec.MJNSHandler;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.CharacterTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_CommonNews;
 import l1j.server.server.serverpackets.S_DeleteCharOK;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_DeleteChar
   extends ClientBasePacket {
   private static final String C_DELETE_CHAR = "[C] RequestDeleteChar";
   private static final int DELETE_TYPE_DELETE = 0;
   private static final int DELETE_TYPE_RECOVERY = 1;

            // C_DeleteChar 類的構造函數
     public C_DeleteChar(byte[] decrypt, GameClient client) throws Exception {
         super(decrypt);
         String name = readS(); // 讀取角色名稱
         int type = readC(); // 讀取操作類型
         try {
             if (Config.Login.UseShiftServer) {
                    // 獲取與帳戶相關的轉移對象
                 MJShiftObject sobject = MJShiftObjectManager.getInstance().get_shift_sender_object_from_account(client.getAccountName());
                 if (sobject != null) {
                        // 如果轉移對象不為空，打印日誌並阻止角色刪除
                     System.out.println(String.format("【帳號】%s 【參與中】%s 【刪除】%s 【%s】【IP】%s 阻止參與大航戰的帳號進行角色刪除。", new Object[] { client.getAccountName(), sobject.get_source_character_name(), name,
                             MJNSHandler.getLocalTime(), client.getIp() }));
                     SC_CUSTOM_MSGBOX.do_kick(client, String.format("%s 正在參與大航戰，無法刪除角色。", new Object[] { sobject.get_source_character_name() }));

                     return;
                 }
             }

                // 從資料庫中恢復角色
             L1PcInstance pc = CharacterTable.getInstance().restoreCharacter(name);
             if (pc == null) {
                    // 如果角色不存在，發送系統消息並返回
                 client.sendPacket((ServerBasePacket)new S_CommonNews("不存在的角色。"));
                 return;
             }

            // 繼續處理角色刪除或恢復

         } catch (Exception e) {
                // 處理異常
             e.printStackTrace();
             client.close();
         }
     }
         try {
                     // 檢查是否存在與當前角色ID相同的在線玩家
             for (L1PcInstance target : L1World.getInstance().getAllPlayers3()) {
                 if (target.getId() == pc.getId()) {
                        // 如果角色在線，發送系統消息並返回
                     client.sendPacket((ServerBasePacket)new S_CommonNews("無法刪除正在連接的角色。"));
                     return;
                 }
             }

             if (type == 1) {
                    // 如果類型為1，進行角色恢復
                 onRecovery(pc, client);
             } else if (type == 0) {
                    // 如果類型為0，進行角色刪除
                 if (pc.getType() >= 32) {
                        // 如果角色類型無效，發送系統消息並返回
                     client.sendPacket((ServerBasePacket)new S_CommonNews("狀態錯誤。（刪除）"));
                     return;
                 }

                    // 恢復角色的物品欄
                 CharacterTable.getInstance().restoreInventory(pc);

                        // 檢查角色物品欄中的每一個物品
                 for (L1ItemInstance item : pc.getInventory().getItems()) {
                     if (item.getBless() >= 128) {
                                // 如果角色擁有被封印的物品，發送系統消息並返回
                         client.sendPacket((ServerBasePacket)new S_CommonNews("無法刪除擁有被封印物品的角色。"));
                         return;
                     }
                 }

                    // 如果角色等級大於30且設置了刪除角色延遲7天，則延遲刪除
                 if (pc.getLevel() > 30 && Config.ServerAdSetting.DELETECHARACTERAFTER7DAYS) {
                     onDelete7Days(pc, client);
                 } else {
                        // 否則立即刪除角色
                     onDeleteNow(pc, client);
                 }
             } else {
                     // 處理未知的刪除命令
                 String s = String.format("未知的刪除命令。 name:%s code:%d", new Object[] { name, Integer.valueOf(type) });
                 client.sendPacket((ServerBasePacket)new S_CommonNews(s));
                 System.out.println(s);
             }
         } catch (Exception e) {
                // 處理異常
             e.printStackTrace();
             client.close();
         }

             // 定義 onRecovery 方法，用於處理角色恢復
     private void onRecovery(L1PcInstance pc, GameClient client) {
                // 檢查角色類型是否小於32
         if (pc.getType() < 32) {
                // 如果角色類型無效，發送系統消息並返回
             client.sendPacket((ServerBasePacket)new S_CommonNews("狀態錯誤。（恢復）"));
             return;
         }

            // 檢查角色職業並設置對應的類型
         if (pc.isCrown()) {
             pc.setType(0);
         } else if (pc.isKnight()) {
             pc.setType(1);
         } else if (pc.isElf()) {
             pc.setType(2);
         } else if (pc.isWizard()) {
             pc.setType(3);
         } else if (pc.isDarkelf()) {
             pc.setType(4);
         } else if (pc.isDragonknight()) {
             pc.setType(5);
         } else if (pc.isBlackwizard()) {
             pc.setType(6);
         } else if (pc.is戰士()) { // 註解: 전사應該代表戰士
             pc.setType(7);
         } else if (pc.isFencer()) {
             pc.setType(8);
         } else if (pc.isLancer()) {
             pc.setType(9);
         }

            // 設置角色的刪除時間為空，表示角色不會被刪除
         pc.setDeleteTime(null);

            // 保存角色狀態
         pc.save();

        // 發送角色刪除成功的消息給客戶端
         client.sendPacket((ServerBasePacket)new S_DeleteCharOK(81));
     }

   private void onDelete7Days(L1PcInstance pc, GameClient client) {
     if (pc.isCrown()) {
       pc.setType(32);
     } else if (pc.isKnight()) {
       pc.setType(33);
     } else if (pc.isElf()) {
       pc.setType(34);
     } else if (pc.isWizard()) {
       pc.setType(35);
     } else if (pc.isDarkelf()) {
       pc.setType(36);
     } else if (pc.isDragonknight()) {
       pc.setType(37);
     } else if (pc.isBlackwizard()) {
       pc.setType(38);
     } else if (pc.is戰士()) {
       pc.setType(39);
     } else if (pc.isFencer()) {
       pc.setType(40);
     } else if (pc.isLancer()) {
       pc.setType(41);
     }
     Timestamp deleteTime = new Timestamp(System.currentTimeMillis() + 604800000L);
     pc.setDeleteTime(deleteTime);
     pc.save();
     client.sendPacket((ServerBasePacket)S_DeleteCharOK.deleteRemainSeconds(604800));
   }

   private void onDeleteNow(L1PcInstance pc, GameClient client) throws Exception {
     CharacterTable.getInstance().deleteCharacter(client.getAccountName(), pc.getName());
     BQSCharacterDataLoader.deleteCharacterBps(pc, true);
     client.sendPacket((ServerBasePacket)new S_DeleteCharOK(5));
   }


   public String getType() {
     return "[C] RequestDeleteChar";
   }
 }


