 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1BoardInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1BoardPost;

 public class C_BoardDelete
   extends ClientBasePacket {
   private static final String C_BOARD_DELETE = "[C] C_BoardDelete";

   public C_BoardDelete(byte[] decrypt, GameClient client) {
     super(decrypt);
     int objId = readD();
     int topicId = readD();
     L1Object obj = L1World.getInstance().findObject(objId);
     L1BoardInstance board = (L1BoardInstance)obj;
     L1PcInstance pc = client.getActiveChar();
     if (pc == null || board == null || obj == null) {
       return;
     }
     L1BoardPost topic = L1BoardPost.findById(topicId);
     if (topic == null) {
       return;
     }
       String name = client.getActiveChar().getName(); // 註解: 獲取當前活躍角色的名稱
       if (!name.equals(topic.getName())) { // 註解: 如果角色名稱與主題名稱不匹配
           return; // 註解: 返回，停止後續執行
       }
       if (!pc.isGm()) { // 註解: 如果玩家不是GM（遊戲管理員）
           pc.sendPackets((ServerBasePacket)new S_SystemMessage("無法刪除公告板上的文章。")); // 註解: 向玩家發送消息，通知無法刪除公告
           return; // 註解: 返回，停止後續執行
       }
       if (board.getNpcId() == 4200015) { // 註解: 如果公告板的NPC ID是4200015
           topic.伺服器信息(); // 註解: 呼叫方法 '서버정보' (伺服器信息)
       } else if (board.getNpcId() == 4200020) { // 註解: 如果公告板的NPC ID是4200020
           topic.管理員1(); // 註解: 呼叫方法 '운영자1' (管理員1)
       } else if (board.getNpcId() == 4200021) { // 註解: 如果公告板的NPC ID是4200021
           topic.管理員2(); // 註解: 呼叫方法 '운영자2' (管理員2)
       } else if (board.getNpcId() == 4200022 || board.getNpcId() == 71008) { // 註解: 如果公告板的NPC ID是4200022或71008
           topic.管理員3(); // 註解: 呼叫方法 '운영자3' (管理員3)
       } else if (board.getNpcId() == 500002) { // 註解: 如果公告板的NPC ID是500002
           topic.建議事項(); // 註解: 呼叫方法 '건의사항' (建議事項)
       } else { // 註解: 如果公告板的NPC ID不符合以上條件
           topic.自由公告板(); // 註解: 呼叫方法 '자유게시판' (自由公告板)
       }
   }

     public String getType() { // 註解: 獲取類型方法
         return "[C] C_BoardDelete"; // 註解: 返回字符串 "[C] C_BoardDelete"
     }
 }


