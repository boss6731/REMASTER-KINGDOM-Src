 package l1j.server.server.model;

 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.gametime.GameTimeClock;
 import l1j.server.server.serverpackets.S_PinkName;
 import l1j.server.server.serverpackets.ServerBasePacket;


 public class L1PinkName
 {
     static class PinkNameTimer
             implements Runnable
     {
         private L1PcInstance _attacker = null;
         private int _nextPinkNameSendTime;

         public PinkNameTimer(L1PcInstance attacker) {
             this._attacker = attacker;
             this._nextPinkNameSendTime = GameTimeClock.getInstance().getGameTime().getSeconds() + this._attacker.getPinkNameTime() + 1;
         }



         public void run() {
             if (!this._attacker.isPinkName()) {
                 this._attacker.setPinkName(true);
                 this._attacker.send_pink_name(this._attacker.getPinkNameTime());
                 if (this._attacker.isInParty()) {
                     L1Party party = this._attacker.getParty();
                     for (L1PcInstance pc : party.getList()) {
                         pc.setPinkName(true);
                         pc.send_pink_name(this._attacker.getPinkNameTime());
                     }
                 }
             }

             if (this._attacker.isDead()) {
                 this._attacker.SetPinkNameTime(0);
             } else if (this._attacker.DecrementPinkNameTime() > 0) {
                 int currentTime = GameTimeClock.getInstance().getGameTime().getSeconds();

                 if (this._nextPinkNameSendTime < currentTime) {
                     this._attacker.send_pink_name(this._attacker.getPinkNameTime());
                     this._nextPinkNameSendTime = currentTime + this._attacker.getPinkNameTime() + 1;
                 }
                 GeneralThreadPool.getInstance().schedule(this, 1000L);
                 return;
             }
             stopPinkName(this._attacker);
         }

         private void stopPinkName(L1PcInstance attacker) {
             attacker.setPinkName(false);

             attacker.sendPackets((ServerBasePacket)new S_PinkName(attacker.getId(), 0));
             attacker.broadcastPacket((ServerBasePacket)new S_PinkName(attacker.getId(), 0));
         }
     }

     public static void onAction(L1PcInstance attacker) {
         if (attacker == null) {
             return;
         }
         boolean isNowWar = false;
         int castleId = L1CastleLocation.getCastleIdByArea((L1Character)attacker);
         if (castleId != 0) {
             isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
         }

         if (attacker.getZoneType() == 0 && !isNowWar &&
                 attacker.SetPinkNameTime(20) == 0) {
             attacker.setPinkName(true);
             attacker.send_pink_name(20);
             PinkNameTimer pink = new PinkNameTimer(attacker);
             GeneralThreadPool.getInstance().execute(pink);
         }
     }


     public static void onAction(L1PcInstance pc, L1PcInstance attacker) {
         if (pc == null || attacker == null) {
             return;
         }

         if (pc.getId() == attacker.getId()) {
             return;
         }
         if (attacker.getFightId() == pc.getId()) {
             return;
         }

         boolean isNowWar = false;
         int castleId = L1CastleLocation.getCastleIdByArea((L1Character)pc);
         if (castleId != 0) {
             isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
         }

         if (pc.getLawful() >= 0 &&
                 pc.getZoneType() == 0 && attacker.getZoneType() == 0 && !isNowWar)
         {
             if (attacker.SetPinkNameTime(20) == 0) {
                 attacker.setPinkName(true);
                 attacker.send_pink_name(20);
                 PinkNameTimer pink = new PinkNameTimer(attacker);
                 GeneralThreadPool.getInstance().execute(pink);
             }
         }
     }


     public static void onAction(L1PcInstance target, L1Character attacker) {
         if (attacker.instanceOf(4))
             onAction(target, (L1PcInstance)attacker);
     }

     public static void onAction(L1Character target, L1PcInstance attacker) {
         if (target.instanceOf(4))
             onAction((L1PcInstance)target, attacker);
     }

     public static void onAction(L1Character target, L1Character attacker) {
         if (target.instanceOf(4) && attacker.instanceOf(4))
             onAction((L1PcInstance)target, (L1PcInstance)attacker);
     }

     public static void onHelp(L1Character target, L1Character helper) {
         if (target == null || helper == null) {
             return;
         }
         if (!(helper instanceof L1PcInstance)) {
             return;
         }
         if (!(target instanceof L1PcInstance)) {
             return;
         }

         L1PcInstance helperPc = (L1PcInstance)helper;
         L1PcInstance targetPc = (L1PcInstance)target;

         if (!targetPc.isPinkName()) {
             return;
         }

         if (helperPc.getId() == targetPc.getId()) {
             return;
         }

         boolean isNowWar = false;
         int castleId = L1CastleLocation.getCastleIdByArea((L1Character)helperPc);

         if (castleId != 0) {
             isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
         }

         if (targetPc.getZoneType() == 0 && helperPc.getZoneType() == 0 && !isNowWar)
         {
             if (helperPc.SetPinkNameTime(20) == 0) {
                 helperPc.setPinkName(true);
                 helperPc.send_pink_name(20);
                 PinkNameTimer pink = new PinkNameTimer(helperPc);
                 GeneralThreadPool.getInstance().execute(pink);
             }
         }
     }
 }


