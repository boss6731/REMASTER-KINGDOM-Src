 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Broadcaster;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Teleport;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_Teleport extends ClientBasePacket {
   public C_Teleport(byte[] data, GameClient gc) throws Exception {
     super(data);
     try {
       if (gc == null) {
         return;
       }

       L1PcInstance pc = gc.getActiveChar();
       if (pc == null || pc.isPrivateShop() || pc.is무인상점()) {
         return;
       }
       ServerBasePacket pck = pc.getTemporaryEffect();
       if (pck != null) {
         pc.sendPackets(pck, false);
         Broadcaster.broadcastPacket((L1Character)pc, pck);
         pc.clearTemporaryEffect();
       }
       Runnable teleport = () -> L1Teleport.getInstance().doTeleportation(pc);




       GeneralThreadPool.getInstance().schedule(teleport, 100L);
     } catch (Exception e) {
       e.printStackTrace();
     }
   }


   public String getType() {
     return "[C] C_Teleport";
   }
 }


