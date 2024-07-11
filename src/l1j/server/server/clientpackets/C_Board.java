 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.NPCTalkDataTable;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1NpcTalkData;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_NPCTalkReturn;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_Board
   extends ClientBasePacket
 {
   private static final String C_BOARD = "[C] C_Board";

   private boolean isBoardInstance(L1Object obj) {
     return (obj instanceof l1j.server.server.model.Instance.L1BoardInstance || obj instanceof l1j.server.server.model.Instance.L1AuctionBoardInstance);
   }
   private boolean isNewBoard(L1Object obj) {
     if (obj instanceof L1NpcInstance) {
       L1NpcInstance b = (L1NpcInstance)obj;
       if (b.getNpcTemplate().get_npcId() == 7310127) {
         return true;
       }
     }
     return false;
   }

   public C_Board(byte[] abyte0, GameClient clientthread) {
     super(abyte0);

     int objectId = readD();
     L1PcInstance pc = clientthread.getActiveChar();
     L1Object obj = L1World.getInstance().findObject(objectId);
     if (!isBoardInstance(obj)) {
       return;
     }

     if (isNewBoard(obj)) {
       if (obj != null && pc != null) {
         L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(7310127);
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objectId, 1));
       }
       return;
     }
     obj.onAction(clientthread.getActiveChar());
   }

   public String getType() {
     return "[C] C_Board";
   }
 }


