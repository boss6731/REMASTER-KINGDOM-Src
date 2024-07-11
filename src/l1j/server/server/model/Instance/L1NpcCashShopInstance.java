 package l1j.server.server.model.Instance;

 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.serverpackets.S_DoActionShop;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;

 public class L1NpcCashShopInstance extends L1NpcInstance {
   private static final long serialVersionUID = 1L;
   private int _state = 0;


   private String _shopName;


   public L1NpcCashShopInstance(L1Npc template) {
     super(template);
   }


   public void onPerceive(L1PcInstance perceivedFrom) {
     perceivedFrom.addKnownObject((L1Object)this);
     if (perceivedFrom.getAI() != null)
       return;
     perceivedFrom.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));


     if (this._state == 1) {
       perceivedFrom.sendPackets((ServerBasePacket)new S_DoActionShop(getId(), 70, getShopName().getBytes()));
     }
   }


   public void onTalkAction(L1PcInstance player) {}

   public int getState() {
     return this._state;
   }

   public void setState(int i) {
     this._state = i;
   }

   public String getShopName() {
     return this._shopName;
   }

   public void setShopName(String name) {
     this._shopName = name;
   }
 }


