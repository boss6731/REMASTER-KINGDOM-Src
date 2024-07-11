 package l1j.server.server.clientpackets;

 import java.util.logging.Logger;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.serverpackets.S_ChangeHeading;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_ChangeHeading extends ClientBasePacket { // 註解: C_ChangeHeading類，繼承自ClientBasePacket
     private static Logger _log = Logger.getLogger(C_ChangeHeading.class.getName()); // 註解: 定義靜態日誌記錄器
     private static final String C_CHANGE_HEADING = "[C] C_ChangeHeading"; // 註解: 定義靜態常量字符串，表示C_ChangeHeading類型

     public C_ChangeHeading(byte[] decrypt, GameClient client) { // 註解: C_ChangeHeading的構造方法
         super(decrypt); // 註解: 調用父類構造方法
         int heading = readC(); // 註解: 讀取一個字節，表示方向
         if (heading < 0 || heading > 7) { // 註解: 如果方向值不在0到7之間
             return; // 註解: 返回，停止後續執行
         }
         L1PcInstance pc = client.getActiveChar(); // 註解: 獲取當前客戶端活躍的角色實例
         if (pc == null) { // 註解: 如果角色實例為空
             return; // 註解: 返回，停止後續執行
         }
         pc.setHeading(heading); // 註解: 設置角色的方向

         _log.finest("改變方向 : " + pc.getHeading()); // 註解: 記錄改變方向的詳細日誌

         if (!pc.isGmInvis() && !pc.isGhost() && !pc.isInvisble()) { // 註解: 如果角色不是GM隱身狀態、幽靈狀態或隱身狀態
             pc.broadcastPacket((ServerBasePacket)new S_ChangeHeading((L1Character)pc)); // 註解: 廣播方向改變的封包
         }
     }

     public String getType() { // 註解: 獲取類型方法
         return "[C] C_ChangeHeading"; // 註解: 返回字符串 "[C] C_ChangeHeading"
     }
 }


