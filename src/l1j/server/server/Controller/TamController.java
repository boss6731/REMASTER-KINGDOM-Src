 package l1j.server.server.Controller;

 import l1j.server.Config;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_NewCreateItem;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class TamController implements Runnable {
   public static final int SLEEP_TIME = Config.ServerRates.TamTime * 60000;

   private static TamController _instance;

   public static TamController getInstance() {
     if (_instance == null) {
       _instance = new TamController();
     }
     return _instance;
   }


   public void run() {
     try {
       PremiumTime();
     } catch (Exception exception) {}
   }


     private void PremiumTime() {
         for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) { // 註解: 遍歷所有玩家
             int premium1 = Config.ServerRates.TamNum1; // 註解: 獲取伺服器配置的第一種高級時間數值
             int premium2 = Config.ServerRates.TamNum2; // 註解: 獲取伺服器配置的第二種高級時間數值

             if (!pc.isPrivateShop() && !pc.noPlayerCK && !pc.noPlayerck2 && pc != null && !pc.isDead()) { // 註解: 檢查玩家是否符合條件
                 int tamcount = pc.tamcount(); // 註解: 獲取玩家的探索點數
                 if (tamcount > 0) { // 註解: 如果探索點數大於0
                     int addtam = Config.ServerRates.TamNum * tamcount; // 註解: 計算應增加的探索點數
                     pc.getNetConnection().getAccount().addTamPoint(addtam); // 註解: 增加玩家帳戶的探索點數
                     try {
                         pc.getNetConnection().getAccount().updateTam(); // 註解: 更新玩家帳戶的探索點數
                     } catch (Exception exception) {} // 註解: 捕捉並忽略更新過程中的異常

                     L1Clan clan = L1World.getInstance().getClan(pc.getClanid()); // 註解: 獲取玩家所在的公會
                     pc.sendPackets("\f2(成長之環:一般) " + tamcount + "階段 探索點數:" + addtam + "個獲得"); // 註解: 發送系統消息，告知玩家獲得了多少探索點數
                     if (clan != null) { // 註解: 如果玩家有公會
                         if (clan.getClanId() != 0) { // 註解: 如果公會ID不為0
                             pc.getAccount().addTamPoint(premium1); // 註解: 增加玩家帳戶的第一種高級時間數值
                             pc.sendPackets("\f2(成長之環:血盟) " + tamcount + "階段 探索點數:" + premium1 + "個獲得"); // 註解: 發送系統消息，告知玩家獲得了多少公會探索點數
                         } else if (clan.getCastleId() != 0) { // 註解: 如果公會擁有城堡
                             pc.getAccount().addTamPoint(premium2); // 註解: 增加玩家帳戶的第二種高級時間數值
                             pc.sendPackets("\f2(成長之環:城血) " + tamcount + "階段 探索點數:" + premium2 + "個獲得"); // 註解: 發送系統消息，告知玩家獲得了多少城堡探索點數
                         }
                     }
                 }
             }
         }
     }
           try {
             pc.sendPackets((ServerBasePacket)new S_NewCreateItem(450, pc.getNetConnection()), true);
           } catch (Exception exception) {}
         }
       }
     }
   }
 }


