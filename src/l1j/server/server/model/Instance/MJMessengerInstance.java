 package l1j.server.server.model.Instance;
 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.serverpackets.S_CharTitle;
 import l1j.server.server.serverpackets.S_NpcChatPacket;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.types.Point;

 public class MJMessengerInstance extends L1NpcInstance {
   private static final long serialVersionUID = 1L;

   public MJMessengerInstance(L1Npc template) {
     super(template);
     this.m_is_ghost = false;
     this.m_current_message = "";
   }
   private String m_current_message; private boolean m_is_ghost;
   public String get_current_message() {
     return this.m_current_message;
   }
   public void set_current_message(String message) {
     this.m_current_message = message;
     setTitle(message);
   }
   public boolean is_ghost() {
     return this.m_is_ghost;
   }
   public void set_is_ghost(boolean is_ghost) {
     this.m_is_ghost = is_ghost;
   }

   private SC_WORLD_PUT_OBJECT_NOTI make_object_packet() {
     return SC_WORLD_PUT_OBJECT_NOTI.new_namechat_isntance(this, get_current_message(), is_ghost());
   }

   public void broadcast_message() {
     broadcastPacket((ServerBasePacket)new S_NpcChatPacket(this, get_current_message(), 4));

     broadcastPacket((ServerBasePacket)new S_ChangeName(getId(), get_current_message()));
     broadcastPacket((ServerBasePacket)new S_CharTitle(getId(), ""));
   }




   public void onPerceive(L1PcInstance perceivedFrom) {
     if (perceivedFrom == null) {
       return;
     }



     getMap().setPassable((Point)getLocation(), true);
     perceivedFrom.addKnownObject((L1Object)this);
     if (perceivedFrom.getAI() == null) {
       perceivedFrom.sendPackets((MJIProtoMessage)make_object_packet(), MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI, true);
     }
   }


   public void onAction(L1PcInstance pc) {
     L1Attack attack = new L1Attack(pc, this);
     attack.calcHit();
     attack.action();
   }


   public void onNpcAI() {
     if (isAiRunning()) {
       return;
     }
     setActived(false);
     startAI();
   }

   public void onTalkAction(L1PcInstance player) {}

   public void onFinalAction(L1PcInstance player, String action) {}

   public void doFinalAction(L1PcInstance player) {}
 }


