 package l1j.server.server.model;

 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_Light;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class Light {
   private int _chaLightSize = 0;
   private int _ownLightSize = 0;
   private L1Character character = null;

   public void dispose() {
     this.character = null;
   }

   public Light(L1Character cha) {
     this.character = cha;
   }

   public int getChaLightSize() {
     if (this.character.isInvisble()) {
       return 0;
     }
     return this._chaLightSize;
   }

   public void setChaLightSize(int i) {
     this._chaLightSize = i;
   }

   public int getOwnLightSize() {
     return this._ownLightSize;
   }

   public void setOwnLightSize(int i) {
     this._ownLightSize = i;
   }

   public void turnOnOffLight() {
     if (this.character == null) {
       return;
     }
     int lightSize = 0;
     if (this.character instanceof L1NpcInstance) {
       L1NpcInstance npc = (L1NpcInstance)this.character;
       lightSize = npc.getLightSize();
     }

     if (this.character.hasSkillEffect(2)) {
       lightSize = 14;
     }
     int TYPE_ETC_ITEM = 0, TYPE_LIGHT = 2;

     for (L1ItemInstance item : this.character.getInventory().getItems()) {
       if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) {
         int itemlightSize = item.getItem().getLightRange();
         if (itemlightSize != 0 && item.isNowLighting() &&
           itemlightSize > lightSize) {
           lightSize = itemlightSize;
         }
       }
     }

     if (this.character instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this.character;
       pc.sendPackets((ServerBasePacket)new S_Light(pc.getId(), lightSize));
     }

     if (!this.character.isInvisble()) {
       this.character.broadcastPacket((ServerBasePacket)new S_Light(this.character.getId(), lightSize));
     }
     setOwnLightSize(lightSize);
     setChaLightSize(lightSize);
   }
 }


