 package l1j.server.server.model.poison;

 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.SingleTask;
 import l1j.server.server.datatables.MonsterParalyzeDelay;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.serverpackets.S_Paralysis;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class L1ParalysisPoison extends L1Poison {
   private final L1Character _target;
   private SingleTask _timer;
   private final int _delay;
   private final int _time;

   private class ParalysisPoisonTimer extends SingleTask { public void execute() {
       L1ParalysisPoison.this._effectId = 2;
       L1ParalysisPoison.this._target.setPoisonEffect(2);

       if (isActive() &&
         L1ParalysisPoison.this._target instanceof L1PcInstance) {
         L1PcInstance player = (L1PcInstance)L1ParalysisPoison.this._target;

         if (!player.isDead()) {
           SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
           noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
           noti.set_spell_id(33);
           noti.set_duration(L1ParalysisPoison.this._time / 1000);
           noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
           noti.set_on_icon_id(5270);
           noti.set_off_icon_id(5270);
           noti.set_icon_priority(10);
           noti.set_tooltip_str_id(213);
           noti.set_end_str_id(0);
           noti.set_is_good(false);
           player.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
           player.sendPackets((ServerBasePacket)new S_Paralysis(4, true));
         }

         L1ParalysisPoison.this._timer = new L1ParalysisPoison.ParalysisTimer();
         L1ParalysisPoison.this._target.setSkillEffect(1009, 0L);
         L1ParalysisPoison.this._target.setParalyzed(true);

         GeneralThreadPool.getInstance().schedule((Runnable)L1ParalysisPoison.this._timer, L1ParalysisPoison.this._time);
       }
     }

     private ParalysisPoisonTimer() {} }

   private class ParalysisTimer extends SingleTask { private ParalysisTimer() {}

     public void execute() {
       L1ParalysisPoison.this._timer = null;
       L1ParalysisPoison.this.cure();
     } }






   private int _effectId = 1;

   private L1ParalysisPoison(L1Character cha, MonsterParalyzeDelay.MonsterParalyze paralyze) {
     this._target = cha;
     this._delay = paralyze.paralyze_delay;
     this._time = paralyze.paralyze_millis;
     doInfection();
   }

   public static boolean doInfection(L1Character cha, int skill_id) {
     MonsterParalyzeDelay.MonsterParalyze paralyze = MonsterParalyzeDelay.getInstance().get_paralyze(skill_id);
     return doInfection(cha, paralyze);
   }

   public static boolean doInfection(L1Character cha, MonsterParalyzeDelay.MonsterParalyze paralyze) {
     if (!L1Poison.isValidTarget(cha)) {
       return false;
     }

     cha.setPoison(new L1ParalysisPoison(cha, paralyze));
     return true;
   }

   private void doInfection() {
     sendMessageIfPlayer(this._target, 212);
     this._target.setPoisonEffect(1);

     if (this._target instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this._target;
       this._timer = new ParalysisPoisonTimer();
       this._target.setSkillEffect(1008, 0L);
       SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
       noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
       noti.set_spell_id(33);
       noti.set_duration(this._delay / 1000);
       noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
       noti.set_on_icon_id(5270);
       noti.set_off_icon_id(5270);
       noti.set_icon_priority(10);
       noti.set_tooltip_str_id(212);
       noti.set_end_str_id(0);
       noti.set_is_good(false);
       pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
       GeneralThreadPool.getInstance().schedule((Runnable)this._timer, this._delay);
     }
   }


   public int getEffectId() {
     return this._effectId;
   }


   public void cure() {
     if (this._timer != null) {
       this._timer.cancel();

       if (this._timer instanceof ParalysisTimer) {
         if (!this._timer.isExecuted()) {
           this._timer.execute();
         }
       } else {
         this._target.killSkillEffectTimer(1008);
       }

       this._timer = null;
     }

     if (this._target instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this._target;
       SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
       noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.END);
       noti.set_spell_id(33);
       noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
       noti.set_is_good(false);
       pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
       if (!pc.isDead()) {
         pc.sendPackets((ServerBasePacket)new S_Paralysis(4, false));
       }
     }

     this._target.setParalyzed(false);
     this._target.setPoisonEffect(0);
     this._target.setPoison(null);
   }
 }


