 package l1j.server.server.model.Instance;

 import l1j.server.server.model.L1Object;
 import l1j.server.server.serverpackets.S_SignboardPack;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;



















 public class L1SignboardInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;

   public L1SignboardInstance(L1Npc template) {
     super(template);
   }



   public void onAction(L1PcInstance pc) {}


   public void onPerceive(L1PcInstance perceivedFrom) {
     if (perceivedFrom == null || this == null)
       return;
     perceivedFrom.addKnownObject((L1Object)this);
     if (perceivedFrom.getAI() == null)
       perceivedFrom.sendPackets((ServerBasePacket)new S_SignboardPack(this));
   }
 }


