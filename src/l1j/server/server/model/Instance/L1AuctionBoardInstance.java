 package l1j.server.server.model.Instance;

 import l1j.server.server.serverpackets.S_AuctionBoard;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;




















 public class L1AuctionBoardInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;

   public L1AuctionBoardInstance(L1Npc template) {
     super(template);
   }


   public void onAction(L1PcInstance pc) {
     if (pc != null && this != null)
       pc.sendPackets((ServerBasePacket)new S_AuctionBoard(this));
   }
 }


