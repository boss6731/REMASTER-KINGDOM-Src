 package l1j.server.server.clientpackets;

 import l1j.server.server.Account;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_Ship
   extends ClientBasePacket {
   private static final String C_SHIP = "[C] C_Ship";

   public C_Ship(byte[] abyte0, GameClient client) {
     super(abyte0);

     int shipMapId = readH();
     int locX = readH();
     int locY = readH();

     L1PcInstance pc = client.getActiveChar();
     if (pc == null)
       return;
     int mapId = pc.getMapId();

     switch (mapId) {
       case 5:
         pc.getInventory().consumeItem(40299, 1);
         break;
       case 6:
         pc.getInventory().consumeItem(40298, 1);
         break;
       case 83:
         pc.getInventory().consumeItem(40300, 1);
         break;
       case 84:
         pc.getInventory().consumeItem(40301, 1);
         break;
       case 446:
         pc.getInventory().consumeItem(40303, 1);
         break;
       case 447:
         pc.getInventory().consumeItem(40302, 1);
         break;
       default:
         Account.ban(pc.getAccountName(), 95);
         client.kick();
         try {
           client.close();
         } catch (Exception e) {
           e.printStackTrace();
         }

           // 向玩家發送系統訊息，告知其不要使用漏洞
           pc.sendPackets((ServerBasePacket)new S_SystemMessage("我们發現您使用了一個不正確的遊戲方式，請遵守遊戲規則和體驗公平的遊戲環境。"));

            // 打印出玩家名稱和其嘗試移動的地圖ID，記錄中繼器漏洞
           System.out.println("特定座標移動中繼器漏洞 > 玩家: " + pc.getName() + " 移動嘗試地圖ID: " + mapId);

           break;
     pc.start_teleport(locX, locY, shipMapId, 0, 18339, true, false);
     client.kick();
   }


   public String getType() {
     return "[C] C_Ship";
   }
 }


