 package l1j.server.server.model.Instance;

 import l1j.server.server.model.L1Character;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;

 public class L1TowerFromLfcInstance
   extends L1TowerInstance {
   public L1TowerFromLfcInstance(L1Npc template) {
     super(template);
   }

   public void init() {
     getMap().setPassable(getX(), getY(), false);
   }


   public void receiveDamage(L1Character attacker, int damage) {
     if (!(attacker instanceof L1PcInstance) || attacker == null) {
       return;
     }
     L1PcInstance pc = (L1PcInstance)attacker;
     if (getCurrentHp() > 0 && !isDead()) {
       int newHp = getCurrentHp() - damage;
       if (newHp <= 0 && !isDead()) {
         this._crackStatus = 0;
         setDeath(attacker);
       } else if (newHp > 0) {
         setCurrentHp(newHp);
         if (getMaxHp() * 1 / 4 > getCurrentHp()) {
           if (this._crackStatus != 3) {
             sendView((ServerBasePacket)new S_DoActionGFX(getId(), 34));
             setStatus(34);
             this._crackStatus = 3;
           }
         } else if (getMaxHp() * 2 / 4 > getCurrentHp()) {
           if (this._crackStatus != 2) {
             sendView((ServerBasePacket)new S_DoActionGFX(getId(), 33));
             setStatus(33);
             this._crackStatus = 2;
           }
         } else if (getMaxHp() * 3 / 4 > getCurrentHp() &&
           this._crackStatus != 1) {
           sendView((ServerBasePacket)new S_DoActionGFX(getId(), 32));
           setStatus(32);
           this._crackStatus = 1;
         }

       }
     } else if (!isDead()) {
       setDeath(attacker);
     }
   }

   private void setDeath(L1Character attacker) {
     setDead(true);
     setStatus(35);
     getMap().setPassable(getX(), getY(), true);
     this._lastattacker = attacker;
     sendDead();
   }

   public void sendDead() {
     sendView((ServerBasePacket)new S_DoActionGFX(getId(), 35));
   }

   private void sendView(ServerBasePacket packet) {
     broadcastPacket(packet);
   }
 }


