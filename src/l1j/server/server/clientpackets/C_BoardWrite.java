 package l1j.server.server.clientpackets;

 import java.util.logging.Logger;
 import l1j.server.Config;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1BoardInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1BoardPost;

 public class C_BoardWrite
   extends ClientBasePacket
 {
     private static final String C_BOARD_WRITE = "[C] C_BoardWrite"; // 註解: 定義靜態常量字符串，表示C_BoardWrite類型
     private static Logger _log = Logger.getLogger(C_BoardWrite.class.getName()); // 註解: 定義靜態日誌記錄器

     public C_BoardWrite(byte[] decrypt, GameClient client) { // 註解: C_BoardWrite的構造方法
         super(decrypt); // 註解: 調用父類構造方法
         int id = readD(); // 註解: 讀取一個整數，表示NPC的ID
         String title = readS(); // 註解: 讀取一個字符串，表示公告的標題
         String content = readS(); // 註解: 讀取一個字符串，表示公告的內容
         L1PcInstance pc = client.getActiveChar(); // 註解: 獲取當前客戶端活躍的角色實例
         if (pc == null) // 註解: 如果角色實例為空
             return; // 註解: 返回，停止後續執行

         L1Object tg = L1World.getInstance().findObject(id); // 註解: 根據ID在世界中查找對象
         if (tg == null) { // 註解: 如果未找到對象
             _log.warning("無效的NPC ID: " + id); // 註解: 日誌記錄無效的NPC ID
             System.out.println("無效的NPC ID: " + id); // 註解: 輸出無效的NPC ID信息
             return; // 註解: 返回，停止後續執行
         }
       if (title.length() > 16) { // 註解: 如果標題的長度超過16個字符
           pc.sendPackets((ServerBasePacket)new S_SystemMessage("公告標題字數超過限制。")); // 註解: 向玩家發送消息，通知標題字數超過限制
           return; // 註解: 返回，停止後續執行
       }
       if (tg instanceof L1BoardInstance) { // 註解: 如果目標對象是L1BoardInstance的實例
           L1BoardInstance board = (L1BoardInstance)tg; // 註解: 將目標對象轉換為L1BoardInstance
           if (board != null &&
                   pc.getLevel() < Config.ServerAdSetting.BOARDLEVEL && board.getNpcId() != 900006 && board.getNpcId() != 80006 && board.getNpcId() != 500002) { // 註解: 如果公告板不為空且玩家等級低於公告板要求，且公告板ID不在特定範圍內
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("等級未達到 " + Config.ServerAdSetting.BOARDLEVEL + " 或者這是GM專用公告板。")); // 註解: 向玩家發送消息，通知等級不足或這是GM專用公告板
               return; // 註解: 返回，停止後續執行
           }
           switch (board.getNpcId()) { // 註解: 根據公告板的NPC ID進行處理
               case 900006: // 註解: 如果公告板的NPC ID是900006
                   if (pc.getInventory().checkItem(410040, 1)) { // 註解: 如果玩家的背包中有1個ID為410040的物品（龍鑰匙）
                       L1BoardPost.createKey(pc.getName(), title, content); // 註解: 創建龍鑰匙的公告帖子
                       pc.sendPackets((ServerBasePacket)new S_SystemMessage("請使用村莊公告板進行龍鑰匙的銷售。")); // 註解: 向玩家發送消息，通知使用村莊公告板銷售龍鑰匙
                   } else { // 註解: 如果玩家的背包中沒有龍鑰匙
                       pc.sendPackets((ServerBasePacket)new S_SystemMessage("未持有龍鑰匙。")); // 註解: 向玩家發送消息，通知未持有龍鑰匙
                   }
                   return; // 註解: 返回，停止後續執行
               case 4200015: // 註解: 如果公告板的NPC ID是4200015
                   if (pc.getAccessLevel() == Config.ServerAdSetting.GMCODE) { // 註解: 如果玩家的訪問等級為GM
                       L1BoardPost.createGM(pc.getName(), title, content); // 註解: 創建GM的公告帖子
                   } else { // 註解: 如果玩家不是GM
                       pc.sendPackets((ServerBasePacket)new S_SystemMessage("此公告板僅限管理員使用。")); // 註解: 向玩家發送消息，通知此公告板僅限管理員使用
                       return; // 註解: 返回，停止後續執行
                   }
                   return; // 註解: 返回，停止後續執行
               case 4200020: // 註解: 如果公告板的NPC ID是4200020
                   if (pc.getAccessLevel() == Config.ServerAdSetting.GMCODE) { // 註解: 如果玩家的訪問等級為GM
                       L1BoardPost.createGM1(pc.getName(), title, content); // 註解: 創建GM1的公告帖子
                   } else { // 註解: 如果玩家不是GM
                       pc.sendPackets((ServerBasePacket)new S_SystemMessage("此公告板僅限管理員使用。")); // 註解: 向玩家發送消息，通知此公告板僅限管理員使用
                       return; // 註解: 返回，停止後續執行
                   }
             return; // 註解: 返回，停止後續執行

           case 4200021: // 註解: 處理操作碼為4200021的情況
               if (pc.getAccessLevel() == Config.ServerAdSetting.GMCODE) { // 註解: 如果玩家的訪問等級為GM（遊戲管理員）
                   L1BoardPost.createGM2(pc.getName(), title, content); // 註解: 創建管理員2的公告帖子
               } else { // 註解: 如果玩家不是GM
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("此公告板僅限管理員使用。")); // 註解: 向玩家發送消息，通知此公告板僅限管理員使用
                   return; // 註解: 返回，停止後續執行
               }
               return; // 註解: 返回，停止後續執行

           case 71008: // 註解: 處理操作碼為71008的情況
           case 4200022: // 註解: 處理操作碼為4200022的情況
               if (pc.getAccessLevel() == Config.ServerAdSetting.GMCODE) { // 註解: 如果玩家的訪問等級為GM
                   L1BoardPost.createGM3(pc.getName(), title, content); // 註解: 創建管理員3的公告帖子
               } else { // 註解: 如果玩家不是GM
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("此公告板僅限管理員使用。")); // 註解: 向玩家發送消息，通知此公告板僅限管理員使用
                   return; // 註解: 返回，停止後續執行
               }
               return; // 註解: 返回，停止後續執行

           case 500002: // 註解: 處理操作碼為500002的情況
               if (pc.getInventory().checkItem(40308, 500)) { // 註解: 如果玩家的背包中有500個ID為40308的物品（阿登納）
                   pc.getInventory().consumeItem(40308, 500); // 註解: 消耗500個阿登納
                   L1BoardPost.createPhone(pc.getName(), "[建議確認]", content); // 註解: 創建建議確認的公告帖子
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("公告已成功註冊。")); // 註解: 向玩家發送消息，通知公告已成功註冊
               } else { // 註解: 如果玩家的背包中沒有足夠的阿登納
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("金幣不足。")); // 註解: 向玩家發送消息，通知阿登納不足
               }
               return; // 註解: 返回，停止後續執行
       }

         if (pc.getInventory().checkItem(40308, 500)) { // 註解: 如果玩家的背包中有500個阿登納
             pc.getInventory().consumeItem(40308, 500); // 註解: 消耗500個阿登納
             L1BoardPost.create(pc.getName(), title, content); // 註解: 創建公告帖子
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("公告註冊完成。")); // 註解: 向玩家發送消息，通知公告註冊完成
         } else { // 註解: 如果玩家的背包中沒有足夠的阿登納
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("金幣不足。")); // 註解: 向玩家發送消息，通知阿登納不足
         }
     }
   }

   public String getType() {
     return "[C] C_BoardWrite";
   }
 }


